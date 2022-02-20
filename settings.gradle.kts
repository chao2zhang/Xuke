pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = ("xuke")

include(":example")
includeBuild("plugin-build")
