plugins {
    id("java")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.apache.commons:commons-lang3:3.13.0")
    implementation("org.springframework.integration:spring-integration-websocket:4.1.0.RELEASE")
}
