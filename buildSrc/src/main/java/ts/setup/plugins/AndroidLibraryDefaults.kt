@file:Suppress("UnstableApiUsage")

package ts.setup.plugins

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import ts.setup.config
import java.io.File
import com.android.build.gradle.LibraryExtension as AndroidLibraryExtension

class AndroidLibraryDefaults : Plugin<Project> {
	override fun apply(project: Project) {
		project.plugins.apply("com.android.library")
		project.plugins.apply("org.jetbrains.kotlin.android")
		project.plugins.apply("org.jetbrains.kotlin.kapt")

		with(project) {
			project.afterEvaluate {
				project.extensions.configure(AndroidLibraryExtension::class.java) {
					libraryVariants.forEach { variant ->
						if (variant.buildType.isMinifyEnabled) {
							variant.assembleProvider.get().doLast {
								val mappingFiles = variant.mappingFileProvider.get().files
								mappingFiles.filter { it.exists() }.forEach { file ->
									val newMappingFile = File(file.parent, "mapping_${mappingName()}.txt")
									newMappingFile.delete()
									file.renameTo(newMappingFile)
								}
							}
						}
					}
				}
			}

			project.extensions.configure(AndroidLibraryExtension::class.java) {
				defaultConfig {
					minSdkVersion(config.minSdk)
					targetSdkVersion(config.targetSdk)
					compileSdkVersion(config.compileSdk)
					// versionCode = getBuildNumber()
					// versionName = getVersionName()
					vectorDrawables.useSupportLibrary = true
					testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

					ndk {
						abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
					}
				}
				signingConfigs {

				}
				buildTypes {
					named("release") {
						isMinifyEnabled = true
						setProguardFiles(fileTree("proguard"))
						consumerProguardFiles("consumer-rules.pro")
					}
					create("releaseDebuggable") {
						initWith(getByName("release"))
//						isDebuggable = true
					}
					named("debug") {
//						isDebuggable = true
						isMinifyEnabled = false
					}
				}

				compileOptions {
					sourceCompatibility = JavaVersion.VERSION_1_8
					targetCompatibility = JavaVersion.VERSION_1_8
				}

				lintOptions {
					isAbortOnError = false
				}

				testOptions {
					unitTests {
						isIncludeAndroidResources = true
					}
				}

				buildFeatures {
					buildConfig = true
//					viewBinding = true
//					dataBinding = true
				}

				externalNativeBuild {
					cmake {
						if (project.file("src/main/cpp/CMakeLists.txt").exists())
							path("src/main/cpp/CMakeLists.txt")
					}
				}
			}
		}
	}

	private fun Project.mappingName(): String =
		this.properties["MAPPING_NAME"] as? String
			?: project.name.toUpperCase().replace("_", "-")
}