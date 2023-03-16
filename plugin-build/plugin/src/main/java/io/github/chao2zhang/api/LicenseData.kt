package io.github.chao2zhang.api

data class License(
    val name: String,
    val url: String,
    val distribution: String,
    val comments: String,
)

data class Dependency(
    val group: String,
    val name: String,
    val version: String,
)

typealias LicenseData = Map<Dependency, List<License>>
