@file:Suppress("UnstableApiUsage")



plugins {
    id("highload.webmvc")
    id("io.spring.dependency-management")
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":shared:api"))
    implementation(project(":shared:security"))

    implementation("org.springframework.cloud:spring-cloud-starter-config:4.1.3")
    @Suppress("VulnerableDependency")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.1.3")

    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.springframework.boot:spring-boot-starter-integration")
    implementation("software.amazon.awssdk:s3:2.29.20")
    implementation("org.springframework.kafka:spring-kafka:3.3.0")
    implementation("org.springframework:spring-websocket:6.1.14")
    implementation("org.springframework:spring-messaging:6.1.14")
    // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

}

highloadApp {
    serviceName.set("files")
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(17)
}