package io.github.chao2zhang

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

abstract class XukeExtension @Inject constructor(
    project: Project,
    objects: ObjectFactory,
) {

    /**
     * The Gradle build configurations from which we collect dependencies
     */
    val configurations: ListProperty<String> = objects.listProperty(String::class.java).convention(emptyList())

    /**
     * The output file of the licenses collected - Only supporting .kt file as of now
     */
    val outputFile: RegularFileProperty = objects.fileProperty()
        .convention(project.layout.buildDirectory.file("generated/license/SoftwareLicense.kt"))

    // The output package name of the output file
    val outputPackage: Property<String> = objects.property(String::class.java).convention("")
}
