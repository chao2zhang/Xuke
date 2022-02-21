package io.github.chao2zhang

import org.gradle.api.Plugin
import org.gradle.api.Project

const val EXTENSION_NAME = "xuke"
const val TASK_NAME = "xuke"

abstract class XukePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(EXTENSION_NAME, XukeExtension::class.java)

        project.tasks.register(TASK_NAME, XukeTask::class.java) { task ->
            task.buildConfigurationsProp.convention(extension.configurations)
            task.outputFileProp.convention(extension.outputFile)
            task.outputPackage.convention(extension.outputPackage)
        }
    }
}
