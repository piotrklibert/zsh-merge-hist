package ts.setup.utils

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import java.net.URI


internal fun Project.getNexusUrl(): URI {
    val url = this.lookupEnvKeys(
        "TRUSTSTAMP_NEXUS_PROD_URL",
        "TRUSTSTAMP_NEXUS_DEV_URL",
        "TRUSTSTAMP_NEXUS_URL"
    )
        ?: throw Exception("TS Nexus URL not set, please see the README.md")
    return URI(url)
}

internal fun Project.getNexusUsername(): String {
    return lookupEnvKeys("BITRISE_USERNAME", "TRUSTSTAMP_NEXUS_USERNAME")
        ?: throw Exception("TS Nexus username not set, please see the README.md")
}

internal fun Project.getNexusPassword(): String {
    return lookupEnvKeys(
        "BITRISE_PROD_PASSWORD",
        "BITRISE_DEV_PASSWORD",
        "TRUSTSTAMP_NEXUS_PASSWORD"
    )
        ?: throw Exception("TS Nexus password not set, please see the README.md")
}

internal fun RepositoryHandler.tsNexus(project: Project) {
    maven {
        url = project.getNexusUrl()
        credentials {
            username = project.getNexusUsername()
            password = project.getNexusPassword()
        }
    }
}
