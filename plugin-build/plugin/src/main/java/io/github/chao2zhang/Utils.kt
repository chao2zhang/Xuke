package io.github.chao2zhang

inline fun <reified T> Any.safeAs(): T? = this as? T