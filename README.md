# Xuke

[![Pre Merge Checks](https://github.com/cortinico/kotlin-gradle-plugin-template/workflows/Pre%20Merge%20Checks/badge.svg)](https://github.com/cortinico/kotlin-gradle-plugin-template/actions?query=workflow%3A%22Pre+Merge+Checks%22)
[![License](https://img.shields.io/github/license/cortinico/kotlin-android-template.svg)](LICENSE)
![Language](https://img.shields.io/github/languages/top/cortinico/kotlin-android-template?color=blue&logo=kotlin)

## Goal
Displaying used open source libraries and their licenses not only shows appreciations for the effort of library authors, but they could be also license requirements of those libraries. Apps are responsible for appropriately displaying the notices for the open source libraries, and this Gradle plugin automates this process by collecting the licenses from libraries and generates the source code for apps to display.

## Background
Xuke comes from 许可 (License in English)

## Tutorial
Add the following scripts to your Gradle scripts:

```
plugins {
    id("io.github.chao2zhang.xuke")
}

configure<XukeExtension> {

    // The Gradle build configurations from which we collect dependencies
    configurations.set(listOf("runtimeClasspath"))

    // The output file of the licenses collected - Only supporting .kt file as of now
    outputFile.set(project.layout.buildDirectory.file("generated/license/SoftwareLicense.kt"))
    
    // The output package name of the output file
    outputPackage.set("io.github.chao2zhang.example")
}
```
