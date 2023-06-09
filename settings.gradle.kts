rootProject.name = "otusJava2023"

include ("hw01-gradle")
include ("hw02-generics")
include ("hw03-annotations")
include ("hw04-gc")
include ("hw05-aop")
include("hw06-atm")
include("hw07-patterns")
include("hw08-json")
include("hw09-jdbc")
include("hw10-hibernate")
include("hw11-cache")
include("hw12-web")

pluginManagement {
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val protobufVer: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.protobuf") version protobufVer
    }
}

