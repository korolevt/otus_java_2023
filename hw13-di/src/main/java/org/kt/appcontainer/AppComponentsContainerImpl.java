package org.kt.appcontainer;

import org.kt.appcontainer.api.AppComponent;
import org.kt.appcontainer.api.AppComponentsContainer;
import org.kt.appcontainer.api.AppComponentsContainerConfig;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        processConfigs(List.of(initialConfigClasses));
    }

    public AppComponentsContainerImpl(String basePackage) {
        final Reflections reflections = new Reflections(basePackage);

        var configClasses = reflections
                .getTypesAnnotatedWith(AppComponentsContainerConfig.class)
                .stream()
                .toList();

        if (configClasses.isEmpty()) {
            throw new IllegalArgumentException(String.format("Not found config file for package %s", basePackage));
        }

        processConfigs(configClasses);
    }

    private void processConfigs(List<Class<?>> configClasses) {
        configClasses.stream()
                .peek(this::checkConfigClass)
                .sorted(Comparator.comparingInt(c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        try {
            var clazz = configClass.getConstructor().newInstance();

            var methods = Arrays.stream(configClass.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(AppComponent.class))
                    .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                    .toList();

            for (Method method : methods) {
                var annotation = method.getAnnotation(AppComponent.class);
                checkTheSameNameComponent(annotation.name());

                var params = Arrays.stream(method.getParameterTypes())
                        .map(this::getAppComponent)
                        .toArray();

                var appComponent = method.invoke(clazz, params);

                appComponents.add(appComponent);
                appComponentsByName.put(annotation.name(), appComponent);
            }
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private void checkTheSameNameComponent(String name) {
        if (appComponentsByName.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Is present the same name %s", name));
        }
    }

    private void checkDuplicateComponent(Class<?> componentClass) {
        if (appComponents.stream()
                .filter(c -> componentClass.isAssignableFrom(c.getClass()))
                .count() > 1) {
            throw new RuntimeException(String.format("Is present the duplicate component %s", componentClass.getSimpleName()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        checkDuplicateComponent(componentClass);

        return (C) appComponents.stream()
                .filter(c -> componentClass.isAssignableFrom(c.getClass()))
                .findFirst()
                .orElseThrow(()->new RuntimeException(String.format("Not found component: " + componentClass.getSimpleName())));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        } else {
            throw new RuntimeException(String.format("Not found component: " + componentName));
        }
    }
}
