package highload

import java.util.*

plugins {
    id("highload.common")
    id("com.google.cloud.tools.jib")
    id("org.springframework.boot")
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor:reactor-core")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.3.2")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.6.0")

    testImplementation("io.projectreactor:reactor-test")
    testImplementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.mockk:mockk:1.12.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

interface HighloadAppExtension {
    val serviceName: Property<String>
}

val applicationExtension = project.extensions.create<HighloadAppExtension>("highloadApp")

val jdkVersion = 21
val hostArchitecture = System.getProperty("os.arch").lowercase(Locale.getDefault()).let {
    when {
        it.contains("aarch64") -> "arm64"
        else -> it
    }
}

gradle.projectsEvaluated {
    jib {
        from {
            image = "eclipse-temurin:21-jre"
            platforms {
                platform {
                    architecture = hostArchitecture
                    os = "linux"
                }
            }
        }
        to {
            image = "magenta/highload/${applicationExtension.serviceName.get()}:dev"
        }
    }
}