plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("plugin.allopen") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.flywaydb.flyway") version "10.17.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"

    jacoco
}

flyway {
    url = "jdbc:postgresql://${System.getenv("DB_HOST")}:${System.getenv("DB_PORT")}/${System.getenv("DB_NAME")}"
    user = System.getenv("FLYWAY_USER")
    password = System.getenv("FLYWAY_PASSWORD")
    locations = arrayOf("classpath:db/migration")
    baselineOnMigrate = true
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

jacoco {
    toolVersion = "0.8.12"
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
        mavenBom("software.amazon.awssdk:bom:2.27.0")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("software.amazon.awssdk:s3")

    implementation("com.auth0:java-jwt:4.5.0")
    implementation("com.sksamuel.scrimage:scrimage-core:4.1.3")
    implementation("com.sksamuel.scrimage:scrimage-webp:4.1.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    testImplementation("io.mockk:mockk:1.14.9")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.postgresql:postgresql")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

apply(from = "jacoco-report.gradle.kts")

tasks.withType<Test> {
    useJUnitPlatform()
}