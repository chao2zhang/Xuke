package io.github.chao2zhang.format

import io.github.chao2zhang.api.LicenseData

interface FormatterFactory {

    fun format(licenseData: LicenseData, options: FormatOptions): String

    companion object {
        fun fromFileExtension(extension: String): FormatterFactory = when (extension) {
            "kt" -> KotlinFactory
            else -> TODO("File extension $extension is yet to be supported")
        }
    }
}
