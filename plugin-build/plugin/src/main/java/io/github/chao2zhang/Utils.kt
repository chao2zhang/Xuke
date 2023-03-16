package io.github.chao2zhang

internal inline fun <reified T> Any.safeAs(): T? = this as? T
