package org.kt.asm;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                if (className.equals("org/kt/share/TestLogging")) {
                    return addProxyMethod(classfileBuffer);
                }
                return classfileBuffer;
            }
        });
    }

    private static byte[] addProxyMethod(byte[] originalClass) {
        var cr = new ClassReader(originalClass);
        var cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                var methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                class ChangeMethodVisitor extends AdviceAdapter {
                    ChangeMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor) {
                        super(Opcodes.ASM7, methodVisitor, access, name, descriptor);
                    }

                    @Override
                    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                        System.out.println(descriptor);
                        if (desc.equals("Lorg/kt/share/annotations/Log;")) {
                            System.out.println("visitAnnotation: name=" + name + " visible=" + visible);
                            Matcher matcher = Pattern.compile("(I|Ljava/lang/String;)", Pattern.DOTALL).matcher(descriptor);
                            List<String> paramTypes = new ArrayList<>();
                            while (matcher.find()) {
                                paramTypes.add(matcher.group());
                            }
                            System.out.println("parameterTypes: " + paramTypes);
                            var handle = new Handle(
                                    H_INVOKESTATIC,
                                    Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                                    "makeConcatWithConstants",
                                    MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                                    false);
                            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

                            if (paramTypes.size() > 0) {
                                int index = 1;
                                String printArgs = "execution method: " + name + ", params: \u0001";
                                for (String type : paramTypes) {
                                    if (type.equals("I"))
                                        mv.visitVarInsn(Opcodes.ILOAD, index);
                                    else
                                        mv.visitVarInsn(Opcodes.ALOAD, index);
                                    if (index > 1)
                                        printArgs += ", \u0001";
                                    index++;
                                }
                                mv.visitInvokeDynamicInsn("makeConcatWithConstants", descriptor.replace("V", "") + "Ljava/lang/String;", handle, printArgs);
                            } else {
                                mv.visitLdcInsn("execution method: " + name + ", no parameters");
                            }
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                            mv.visitEnd();
                        }
                        return super.visitAnnotation(desc, visible);
                    }

                }
                return new ChangeMethodVisitor(methodVisitor, access, name, descriptor);
            }
        };
        cr.accept(cv, Opcodes.ASM5);

        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("agentASM.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalClass;
    }
}
