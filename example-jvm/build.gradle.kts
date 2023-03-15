import io.github.chao2zhang.XukeExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.github.chao2zhang.xuke")
}

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java.srcDir("build/generated")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
compileKotlin.dependsOn(tasks.xuke)

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

configure<XukeExtension> {
    configurations.set(listOf("runtimeClasspath"))
    outputPackage.set("io.github.chao2zhang.example")
}
