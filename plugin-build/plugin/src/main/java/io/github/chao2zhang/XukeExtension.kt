package io.github.chao2zhang

import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

abstract class XukeExtension @Inject constructor(
    objects: ObjectFactory
) {

    var configurations: List<String> = emptyList()
}
