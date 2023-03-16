package io.github.chao2zhang

import io.github.chao2zhang.api.Dependency
import io.github.chao2zhang.api.License
import io.github.chao2zhang.api.LicenseData
import io.github.chao2zhang.format.FormatOptions
import io.github.chao2zhang.format.FormatterFactory
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.component.ComponentIdentifier
import org.gradle.api.artifacts.result.ResolvedArtifactResult
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.maven.MavenModule
import org.gradle.maven.MavenPomArtifact
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

abstract class XukeTask : DefaultTask() {

    init {
        description = "Collect software licenses from dependencies"
    }

    @get:Input
    abstract val buildConfigurationsProp: ListProperty<String>

    @get:Input
    abstract val outputPackage: Property<String>

    @get:OutputFile
    abstract val outputFileProp: RegularFileProperty

    private val documentFactory = DocumentBuilderFactory.newInstance()

    @TaskAction
    fun collect() {
        val artifacts = getArtifacts()
        val licenseData = extractLicenses(artifacts)
        writeLicenses(licenseData)
    }

    private fun getArtifacts(): List<ResolvedArtifact> = project
        .configurations
        .filter { configuration ->
            val buildConfigurations = buildConfigurationsProp.getOrElse(emptyList<String>())
            if (buildConfigurations.isEmpty()) true else configuration.name in buildConfigurations
        }
        .filter { it.isCanBeResolved }
        .flatMap { configuration ->
            configuration.resolvedConfiguration.resolvedArtifacts
        }

    private fun extractLicenses(artifacts: List<ResolvedArtifact>): LicenseData = artifacts
        .associate { resolvedArtifact ->
            val componentIdentifier = resolvedArtifact.id.componentIdentifier
            val pom = queryPom(project, componentIdentifier)
            Dependency(
                group = resolvedArtifact.moduleVersion.id.group,
                name = resolvedArtifact.moduleVersion.id.name,
                version = resolvedArtifact.moduleVersion.id.version
            ) to (pom?.let(::extractLicenses) ?: emptyList())
        }

    private fun writeLicenses(licenseData: LicenseData) {
        val outputFile = outputFileProp.get().asFile
        outputFile.writeText(
            FormatterFactory
                .fromFileExtension(outputFile.extension)
                .format(
                    licenseData,
                    FormatOptions(
                        packagePath = outputPackage.getOrElse("")
                    )
                )
        )
        logger.info(licenseData.toList().joinToString(separator = "\n"))
    }

    private fun queryPom(project: Project, componentIdentifier: ComponentIdentifier): File? =
        project.dependencies
            .createArtifactResolutionQuery()
            .forComponents(componentIdentifier)
            .withArtifacts(MavenModule::class.java, MavenPomArtifact::class.java)
            .execute()
            .resolvedComponents
            .singleOrNull()
            ?.getArtifacts(MavenPomArtifact::class.java)
            ?.singleOrNull()
            ?.safeAs<ResolvedArtifactResult>()
            ?.file

    private fun extractLicenses(pomFile: File): List<License> = buildList {
        val pomDoc = documentFactory.newDocumentBuilder().parse(pomFile).documentElement
        val licenseNodes = pomDoc.getElementsByTagName("licenses").singleOrNull()?.childNodes
        licenseNodes?.forEach { licenseNode ->
            if (licenseNode.nodeType == Node.ELEMENT_NODE) {
                add(
                    License(
                        name = licenseNode.namedChildTextContentOrEmpty("name"),
                        url = licenseNode.namedChildTextContentOrEmpty("url"),
                        distribution = licenseNode.namedChildTextContentOrEmpty("distribution"),
                        comments = licenseNode.namedChildTextContentOrEmpty("comments")
                    )
                )
            }
        }
    }
}
