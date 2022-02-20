import io.github.chao2zhang.XukeExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("io.github.chao2zhang.xuke")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

configure<XukeExtension> {
    configurations = listOf("runtimeClasspath")
}
