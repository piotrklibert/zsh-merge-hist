@file:Suppress("KDocMissingDocumentation", "PublicApiImplicitType", "ClassName", "unused", "SpellCheckingInspection")

package ts.setup

import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.add

annotation class Unpinned

object deps {
	private object versions {}

	object test {}

}
