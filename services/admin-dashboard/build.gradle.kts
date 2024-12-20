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
    implementation(kotlin("stdlib-jdk8"))
}

highloadApp {
    serviceName.set("admin-dashboard")
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(17)
}