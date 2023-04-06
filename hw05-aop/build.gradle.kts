import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id ("com.github.johnrengelman.shadow")
}

dependencies {
    implementation("org.ow2.asm:asm-commons")
}

tasks {
    create<ShadowJar>("agentDemoJar") {
        archiveBaseName.set("agentDemo")
        archiveVersion.set("")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "org.kt.asm.AgentDemo",
                "Premain-Class" to "org.kt.asm.Agent"))
        }
        from(sourceSets.main.get().output)
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }


    build {
        dependsOn("agentDemoJar")
    }
}