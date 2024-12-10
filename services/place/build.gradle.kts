@file:Suppress("UnstableApiUsage")


plugins {
    id("highload.web")
    id("highload.application")
    id("highload.security")
    id("highload.reactive-db")
    id("highload.e2e-test")
    id("highload.common")

    id("io.spring.dependency-management")
    id("org.springframework.boot")
}

 testing {
     suites {
         val integrationTest by getting(JvmTestSuite::class) {
             dependencies {
                 implementation("org.springframework.cloud:spring-cloud-contract-wiremock:4.1.4")
             }
         }
     }
 }

dependencies {
    implementation(project(":shared:api"))
    implementation(project(":shared:security"))

    implementation("org.springframework.cloud:spring-cloud-starter-config:4.1.3")
    @Suppress("VulnerableDependency")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.1.3")

    implementation("org.springframework.boot:spring-boot-starter-logging")
    @Suppress("VulnerableDependency")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.3")
    @Suppress("VulnerableDependency")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-hystrix:2.2.10.RELEASE")
    implementation("com.playtika.reactivefeign:feign-reactor-spring-cloud-starter:4.2.1")

    implementation("org.springframework:spring-websocket:6.1.14")
    implementation("org.springframework:spring-messaging:6.1.14")
    // https://mvnrepository.com/artifact/jakarta.websocket/jakarta.websocket-api
    compileOnly("jakarta.websocket:jakarta.websocket-api:2.1.1")
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.5")
}

highloadApp {
    serviceName.set("place")
}
