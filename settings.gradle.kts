rootProject.name = "otusJava2023"

include ("hw01-gradle")
include ("hw02-generics")
include ("hw03-annotations")
include ("hw04-gc")
include ("hw05-aop")

pluginManagement {
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings


    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
    }
}
include("hw06-atm")
