pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = ("OssLicenseAttribution")

include(":example")
includeBuild("plugin-build")
