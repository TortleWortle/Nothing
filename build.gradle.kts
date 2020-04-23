import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
plugins {
    kotlin("jvm") version "1.3.71"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "com.github.tortlewortle"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("https://jitpack.io")
    maven("https://oss.jfrog.org/artifactory/libs-release")
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("net.dv8tion:JDA:4.1.1_136")

    implementation("org.koin:koin-core:2.1.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")

    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("com.github.uchuhimo.konf:konf:0.22.1")

    implementation("com.github.twitch4j:twitch4j:1.0.0-alpha.19")

    testImplementation("junit:junit:4.11")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.3.71")
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "org.notcascade.MainKt"))
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}