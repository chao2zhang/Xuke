package io.github.chao2zhang.format

import io.github.chao2zhang.api.Dependency
import io.github.chao2zhang.api.License
import io.github.chao2zhang.api.LicenseData

object KotlinFactory : FormatterFactory {

    override fun format(licenseData: LicenseData, options: FormatOptions): String = """
        ${options.formatPackagePath()}
        
        data class License(
            val name: String,
            val url: String,
            val distribution: String,
            val comments: String
        )

        data class Dependency(
            val group: String,
            val name: String,
            val version: String
        )

        object SoftwareLicense {
            val ALL = mapOf(
                ${licenseData.entries.joinToString(separator = ",\n                ", transform = { it.format() })}
            )
        }
    """.trimIndent()

    private fun FormatOptions.formatPackagePath() =
        if (packagePath.isEmpty()) "" else "package $packagePath"

    private fun Map.Entry<Dependency, List<License>>.format() =
        "${key.format()} to ${value.format()}"

    private fun Dependency.format() = "Dependency(group = \"$group\", name = \"$name\", version = \"$version\")"

    private fun License.format() = "License(name = \"$name\", url = \"$url\", distribution = \"${distribution}\", comments = \"${comments}\")"

    private fun List<License>.format() = "listOf(" + joinToString(separator = ",", transform = {it.format()}) + ")"
}