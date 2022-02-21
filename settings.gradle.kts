pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = ("xuke")

include(":example-jvm")
includeBuild("plugin-build")
