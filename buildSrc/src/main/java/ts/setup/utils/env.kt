package ts.setup.utils

import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import java.util.*


/** Use [Project.lookupEnv] to find a value corresponding to the given key. Throw an
 *  informative exception if the value is not found. */
fun Project.requireEnv(key: String): String {
    val msg by lazy {
        """
            The following environment variable is not set: $key 
            Please set it in your CI environment or in the checkin.properties file. See 
            the README.md for more details.
        """.trimIndent()
    }
    return lookupEnv(key) ?: throw Exception(msg)
}

/** Use [Project.lookupEnvKeys] to find a value corresponding to one of the given keys.
 * Throw an informative exception if the value is not found. */
fun Project.requireEnvKeys(vararg keys: String): String {
    val msg by lazy {
        """
            None of the following environment variables are set: ${keys.joinToString(", ")}
            Please set it in your CI environment or in the checkin.properties file. See 
            the README.md for more details.
        """.trimIndent()
    }
    return lookupEnvKeys(*keys) ?: throw Exception(msg)
}

/** Return the value corresponding to the first key that is found in the environment.
 * Return null if none of the keys was found. */
fun Project.lookupEnvKeys(vararg keys: String): String? {
    return keys.find { lookupEnv(it) != null }?.let { lookupEnv(it) }
}

/** Search for a value corresponding to the given key in the project environment.
 * This checks:
 * 1. The environment variables
 * 2. The subproject properties
 * 3. The root project properties
 * 4. The gradle.properties file
 * 5. The checkin.properties file
 * Return null if the value is not found. */
fun Project.lookupEnv(key: String): String? {
    val value =
        // First check the environment variables
        System.getenv().lookupValue(key)
        // Then check the subproject properties
            ?: this.properties.lookupValue(key) as? String
            // Then check the root project properties, which also includes values from the
            // gradle.properties file.
            ?: this.rootProject.extra.properties.lookupValue(key) as? String
            // Finally check the checkin.properties file
            ?: this.checkProjectPropertiesFile(key)
            // If none of that worked, just give up
            ?: null
    return value
}

private fun Project.checkProjectPropertiesFile(key: String): String? {
    try {
        return this.rootProject.file("checkin.properties").inputStream().use {
            Properties().apply { load(it) }.lookupValue(key) as? String
        }
    } catch (e: Exception) {
        return null
    }
}

internal fun <K, V> Map<K, V>.lookupValue(vararg keys: K): V? {
    keys.forEach { key ->
        // NOTE: don't change it to simple lookup unless you're sure we don't want regex support
        entries.find { it.key == key }?.let { return it.value }
    }
    return null
}
