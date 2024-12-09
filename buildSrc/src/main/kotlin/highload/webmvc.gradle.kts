package highload

import gradle.kotlin.dsl.accessors._145876b8194ecaabd4ca6fa21333d0e2.implementation
import gradle.kotlin.dsl.accessors._145876b8194ecaabd4ca6fa21333d0e2.jib
import gradle.kotlin.dsl.accessors._145876b8194ecaabd4ca6fa21333d0e2.testImplementation
import gradle.kotlin.dsl.accessors._145876b8194ecaabd4ca6fa21333d0e2.testRuntimeOnly
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import java.util.*

plugins {
    id("highload.common")
    id("com.google.cloud.tools.jib")
    id("org.springframework.boot")
}

dependencies {
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.5")

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