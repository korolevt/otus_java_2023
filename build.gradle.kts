import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    idea
    `java-library`
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(17)
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}


allprojects {
    plugins.apply(JavaLibraryPlugin::class.java)

    group = "org.kt"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    val guava: String by project

    dependencies {
        implementation("com.google.guava:guava:$guava")
    }
}

subprojects {
    plugins.apply(JavaPlugin::class.java)
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing", "-Werror"))
    }
}