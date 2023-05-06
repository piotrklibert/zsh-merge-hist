package ts.setup.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import ts.setup.utils.tsNexus

interface MavenPushExtension {
	var names: Map<String, String>
	var kind: String?
}

class MavenPush : Plugin<Project> {
	override fun apply(project: Project) {
		project.pluginManager.apply(MavenPublishPlugin::class.java)
		val extension = project.extensions.create("mavenPush", MavenPushExtension::class.java)

		project.afterEvaluate {
			project.extensions.configure(PublishingExtension::class.java) {
				// NOTE: this `repositories` is for publishing artifacts, not installing dependencies
				repositories {
					tsNexus(project)
				}

				publications {
					extension.names.forEach { (name, component) ->
						create(name, MavenPublication::class.java) {
							try {
								from(project.components.getByName(component))
							} catch (_: Exception) {
								return@create
							}
							groupId = project.properties["GROUP_ID"] as String
							artifactId = project.properties["POM_ARTIFACT_ID"] as String
							version =
								if (extension.kind == "app")
									ts.setup.version.ClientApp.getVersion()
								else
									ts.setup.version.Lib.getVersion()
						}
					}
				}
			}
		}
	}
}
