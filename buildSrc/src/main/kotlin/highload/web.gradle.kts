package highload

plugins {
    id("io.spring.dependency-management")
    id("org.springframework.boot")
    id("highload.common")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.slf4j:slf4j-api")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.kafka:spring-kafka:3.3.0")

    implementation("org.springframework:spring-websocket:6.1.14")
    implementation("org.springframework:spring-messaging:6.1.14")
}
