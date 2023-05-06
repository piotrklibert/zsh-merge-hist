package ts.setup.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.kotlin.dsl.closureOf
import ts.setup.utils.*

class Repositories : Plugin<Project> {
	override fun apply(project: Project) {
		project.task("getNexusConfiguration") {
			doLast {
				println("Nexus URL:      ${project.getNexusUrl()}")
				println("Nexus username: ${project.getNexusUsername()}")
				println("Nexus password: ${project.getNexusPassword()}")
			}
		}

		project.repositories(
			closureOf<RepositoryHandler> {
				tsNexus(project)
				google()
				mavenCentral()
				maven { setUrl("https://storage.googleapis.com/r8-releases/raw") }
				maven { setUrl("https://www.jitpack.io") }
				maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }
				maven { setUrl("https://s3.amazonaws.com/repo.commonsware.com") }
			},
		)

		project.configurations.all {
			resolutionStrategy {
				force("org.xerial:sqlite-jdbc:3.34.0")
			}
		}

		project.tasks.withType(PublishToMavenRepository::class.java) {
			val match =
				"^publish(.*)(ProdRelease|StagingReleaseDebuggable)AarPublicationTo(.*)$"
					.toRegex()
					.find(this.name)
			if (match != null) {
				assert(match.groupValues.size == 4)
				this.dependsOn("assemble${match.groupValues[2]}")
			}
		}
	}
}
