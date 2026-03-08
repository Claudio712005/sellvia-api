plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.flywaydb.flyway") version "10.17.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

ktlint {
    version.set("1.2.1")
    verbose.set(true)
    outputToConsole.set(true)

    filter {
        exclude { it.file.path.contains("scripts/") }
        include("src/**/*.kt")
    }
}

group = "br.com.claus"
version = "0.0.1-SNAPSHOT"
description = "Sellvia"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("software.amazon.awssdk:bom:2.20.0")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-h2console")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.mockk:mockk:1.14.9")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.auth0:java-jwt:4.5.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.sksamuel.scrimage:scrimage-core:4.1.3")
    implementation("com.sksamuel.scrimage:scrimage-webp:4.1.3")
    implementation("org.springframework.boot:spring-boot-starter-validation:4.0.2")
    implementation("software.amazon.awssdk:s3")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}