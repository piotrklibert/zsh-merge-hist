import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//
val kotlinVersion = "1.7.0"
//
plugins {
	`kotlin-dsl`
	`java-library`
	kotlin("jvm") version "1.7.0"
	id("org.jetbrains.kotlin.kapt") version "1.7.0"
}

//
repositories {
	mavenCentral()
	google()
	maven { setUrl("https://storage.googleapis.com/r8-releases/raw") }
	maven { setUrl("https://www.jitpack.io") }
	maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }
	maven { setUrl("https://s3.amazonaws.com/repo.commonsware.com") }

}

gradlePlugin {
	plugins {
		create("addRepos") {
			id = "ts.setup.plugins.addRepos"
			implementationClass = "ts.setup.plugins.Repositories"
		}
		create("mavenPush") {
			id = "ts.setup.plugins.mavenPush"
			implementationClass = "ts.setup.plugins.MavenPush"
		}
		create("android-library") {
			id = "ts.setup.plugins.android-library"
			implementationClass = "ts.setup.plugins.AndroidLibraryDefaults"
		}
	}
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

buildscript {
    dependencies {
        // classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")
    }
    repositories {
        mavenCentral()
        google()
        maven { setUrl("https://storage.googleapis.com/r8-releases/raw") }
        maven { setUrl("https://www.jitpack.io") }
        maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }
        maven { setUrl("https://s3.amazonaws.com/repo.commonsware.com") }

    }
}

dependencies {
	implementation(kotlin("stdlib-jdk8", kotlinVersion))
	implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
	implementation("com.android.tools.build:gradle:7.3.1")

	testImplementation(platform("org.junit:junit-bom:5.9.1"))
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
	implementation("com.squareup:javapoet:1.13.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "1.8"
	}
}
