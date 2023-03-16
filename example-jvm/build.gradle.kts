plugins {
    kotlin("jvm") version "1.8.10"
    id("io.github.chao2zhang.xuke")
}

sourceSets {
    main {
        java.srcDir("build/generated")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.compileKotlin.configure {
    dependsOn(tasks.xuke)
}

xuke {
    configurations.set(listOf("runtimeClasspath"))
    outputPackage.set("io.github.chao2zhang.example")
}
