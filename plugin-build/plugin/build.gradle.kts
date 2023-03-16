plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    alias(libs.plugins.pluginPublish)
}

dependencies {
    compileOnly(gradleApi())

    testImplementation(libs.junit4)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

group = "io.github.chao2zhang"
version = "0.0.11"

val pluginId = "io.github.chao2zhang.xuke"

gradlePlugin {
    website.set("https://github.com/chao2zhang/Xuke")
    vcsUrl.set("https://github.com/chao2zhang/Xuke")

    plugins {
        create(pluginId) {
            id = pluginId
            implementationClass = "io.github.chao2zhang.XukePlugin"
            displayName = "A Gradle plugin to collect software licenses from dependencies"
            description = "A Gradle plugin to collect software licenses from dependencies"
            tags.set(
                listOf(
                    "plugin",
                    "gradle",
                    "license",
                    "oss",
                ),
            )
        }
    }
}

tasks.register("setupPluginUploadFromEnvironment") {
    doLast {
        val key = System.getenv("GRADLE_PUBLISH_KEY")
        val secret = System.getenv("GRADLE_PUBLISH_SECRET")

        if (key == null || secret == null) {
            throw GradleException("gradlePublishKey and/or gradlePublishSecret are not defined environment variables")
        }

        System.setProperty("gradle.publish.key", key)
        System.setProperty("gradle.publish.secret", secret)
    }
}
