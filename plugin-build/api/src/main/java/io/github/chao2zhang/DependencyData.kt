package io.github.chao2zhang

data class DependencyData(
    val group: String,
    val name: String,
    val version: String,
    val licenseData: List<LicenseData>
)
