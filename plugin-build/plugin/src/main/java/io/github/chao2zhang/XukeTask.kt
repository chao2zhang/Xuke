package io.github.chao2zhang

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.component.ComponentIdentifier
import org.gradle.api.artifacts.result.ResolvedArtifactResult
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
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
    abstract val configurationsProperty: ListProperty<String>

    private val documentFactory = DocumentBuilderFactory.newInstance()

    @TaskAction
    fun collect() {
        val artifacts = getArtifacts()
        val dependencyWithLicense = artifacts.mapNotNull { resolvedArtifact ->
            val componentIdentifier = resolvedArtifact.id.componentIdentifier
            val pom = queryPom(project, componentIdentifier)
            pom?.let {
                DependencyData(
                    group = resolvedArtifact.moduleVersion.id.group,
                    name = resolvedArtifact.moduleVersion.id.name,
                    version = resolvedArtifact.moduleVersion.id.version,
                    licenseData = extractLicenseData(pom)
                )
            }
        }
        dependencyWithLicense.forEach {
            logger.lifecycle(it.toString())
        }
    }

    private fun getArtifacts(): List<ResolvedArtifact> {
        return project
            .configurations
            .filter { configuration ->
                val gradleConfigurations = configurationsProperty.getOrElse(emptyList<String>())
                if (gradleConfigurations.isEmpty()) true else configuration.name in gradleConfigurations
            }
            .filter { it.isCanBeResolved }
            .flatMap { configuration ->
                configuration.resolvedConfiguration.resolvedArtifacts
            }
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

    @OptIn(ExperimentalStdlibApi::class)
    private fun extractLicenseData(pomFile: File): List<LicenseData> = buildList<LicenseData> {
        val pomDoc = documentFactory.newDocumentBuilder().parse(pomFile).documentElement
        val licenseNodes = pomDoc.getElementsByTagName("licenses").singleOrNull()?.childNodes
        licenseNodes?.forEach { licenseNode ->
            if (licenseNode.nodeType == Node.ELEMENT_NODE) {
                add(
                    LicenseData(
                        name = licenseNode.namedChildTextContentOrEmpty("name"),
                        url = licenseNode.namedChildTextContentOrEmpty("url"),
                        distribution = licenseNode.namedChildTextContentOrEmpty("distribution"),
                        comments = licenseNode.namedChildTextContentOrEmpty("comments"),
                    )
                )
            }
        }
    }
}
