import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.springframework.boot") version "2.7.7"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.springdoc.openapi-gradle-plugin") version "1.5.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("org.flywaydb.flyway") version "9.10.1"
    id("co.uzzu.dotenv.gradle") version "2.0.0"
    val kotlinVersion = "1.8.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.noarg") version kotlinVersion
    kotlin("plugin.lombok") version kotlinVersion
    id("io.freefair.lombok") version "5.3.0"
}

group = "code.seat"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

val spekVersion = "2.0.19"
val springDotEnvVersion = "2.5.4"
val axonVersion = "4.6.2"
val axonKotlinVersion = "4.6.0"
val springDocOpenApiVersion = "1.6.13"
val springBootVersion = "2.7.7"
val mysqlConnectorJavaVersion = "8.0.31"
val flywayVersion = "9.10.1"
val springShellVersion = "2.1.4"
val lombokPluginVersion = "1.18.24"

ext["springShellVersion"] = springShellVersion

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-graphql:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-hateoas:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.shell:spring-shell-starter:$springShellVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springdoc:springdoc-openapi-ui:$springDocOpenApiVersion")
    implementation("org.springdoc:springdoc-openapi-webmvc-core:$springDocOpenApiVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.axonframework:axon-spring-boot-starter:$axonVersion") {
        exclude("org.axonframework", "axon-server-connector")
    }
    implementation("org.axonframework:axon-metrics:$axonVersion")
    implementation("org.axonframework.extensions.kotlin:axon-kotlin:$axonKotlinVersion")
    implementation("me.paulschwarz:spring-dotenv:$springDotEnvVersion")
    implementation("org.springdoc:springdoc-openapi-ui:$springDocOpenApiVersion")
    implementation("org.springdoc:springdoc-openapi-webmvc-core:$springDocOpenApiVersion")
    runtimeOnly("com.mysql:mysql-connector-j:$mysqlConnectorJavaVersion")
    compileOnly("org.projectlombok:lombok:$lombokPluginVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokPluginVersion")

    // Dev
    compileOnly("org.flywaydb:flyway-core:$flywayVersion")
    compileOnly("org.flywaydb:flyway-mysql:$flywayVersion")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
    testImplementation("org.axonframework:axon-test:$axonVersion")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
    testCompileOnly("org.projectlombok:lombok:$lombokPluginVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokPluginVersion")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.shell:spring-shell-dependencies:${property("springShellVersion")}")
        mavenBom("org.axonframework:axon-bom:$axonVersion")
    }
}

noArg {
    annotation("org.axonframework.spring.stereotype.Aggregate")
    annotation("javax.persistence.Entity")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform() {
        includeEngines = mutableSetOf("spek2")
    }
}

flyway {
    url = env.FLYWAY_URL.value
    user = env.FLYWAY_USERNAME.value
    password = env.FLYWAY_PASSWORD.value
    driver = "com.mysql.cj.jdbc.Driver"
}

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-mysql:9.10.1")
    }
}
