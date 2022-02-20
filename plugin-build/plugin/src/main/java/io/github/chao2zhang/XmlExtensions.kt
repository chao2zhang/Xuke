package io.github.chao2zhang

import org.w3c.dom.Node
import org.w3c.dom.NodeList

internal fun NodeList.singleOrNull(): Node? =
    if (this.length == 1) this.item(0) as Node else null

internal inline fun NodeList.forEach(action: (Node) -> Unit) =
    (0 until length).forEach { action(item(it)) }

internal fun NodeList.namedChild(name: String): Node? {
    (0 until length).forEach { index ->
        val item = item(index)
        if (item.nodeName == name) {
            return item
        }
    }
    return null
}

internal fun Node.namedChildTextContentOrEmpty(name: String): String =
    childNodes.namedChild(name)?.textContent.orEmpty()
