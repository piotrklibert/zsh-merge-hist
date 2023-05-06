import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.task



plugins {
    id("application")
    id("scala")
    id("com.github.maiflai.scalatest") version "0.25"
    // id("java-toolchain")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

javaToolchains {
    compilerFor {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
    launcherFor {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}
application {
    applicationName = "zsh-merge-history"
    mainClass.set("zsh.history.Main")
}


scala {
    version = "2.13.10"
}

sourceSets {
    named("test") {
        scala.setSrcDirs(listOf("src/scala", "src/test/scala"))
    }
    named("main") {
        scala.setSrcDirs(listOf("src/scala"))
    }
}

repositories {
    google()
    mavenCentral()
    maven { setUrl("https://storage.googleapis.com/r8-releases/raw") }
    maven { setUrl("https://www.jitpack.io") }
    maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { setUrl("https://s3.amazonaws.com/repo.commonsware.com") }
}

dependencies {
    implementation("org.scala-lang:scala-library:2.13.10")
    implementation("com.chuusai:shapeless_2.13:2.3.10")
    implementation("com.fasterxml.jackson.module:jackson-module-scala_2.13:2.13.4")
    implementation("com.github.pathikrit:better-files_2.13:3.9.1")
    implementation("com.lihaoyi:ammonite-ops_2.13:2.4.1")
    implementation("com.lihaoyi:requests_2.13:0.7.1")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("org.scala-lang.modules:scala-parser-combinators_2.13:2.1.1")
    implementation("org.scala-lang.modules:scala-parallel-collections_2.13:1.0.4")
    implementation("org.scala-lang:scala-dist:2.13.10")
    implementation("org.scalaz:scalaz-core_2.13:7.3.6")
    implementation("org.slf4j:slf4j-simple:2.0.3")
    implementation("org.yaml:snakeyaml:1.33")


    // https://mvnrepository.com/artifact/io.github.java-diff-utils/java-diff-utils
    implementation("io.github.java-diff-utils:java-diff-utils:4.12")
    implementation("com.github.nscala-time:nscala-time_2.13:2.32.0")
    // Unit tests
    implementation("org.pegdown:pegdown:1.6.0")
    implementation("org.scalatest:scalatest_2.13:3.2.14")
    implementation("org.scalactic:scalactic_2.13:3.2.14")
    implementation("org.scalacheck:scalacheck_2.13:1.17.0")

    testImplementation("org.pegdown:pegdown:1.6.0")
    testImplementation("org.scalatest:scalatest_2.13:3.2.14")
    testImplementation("org.scalactic:scalactic_2.13:3.2.14")
    testImplementation("org.scalacheck:scalacheck_2.13:1.17.0")

}

tasks.withType<ScalaCompile> {
    scalaCompileOptions.additionalParameters = listOf(
            "-Xlint:adapted-args,inaccessible,infer-any,missing-interpolator,doc-detached,private-shadow,type-parameter-shadow,poly-implicit-overload,option-implicit,delayedinit-select,package-object-classes,stars-align,constant,nonlocal-return,implicit-not-found,serial,valpattern,eta-zero,eta-sam,deprecation", // warns about unsed imports among other things
            "-feature",
            "-language:dynamics",
            "-language:higherKinds",
            "-language:implicitConversions",
            "-language:reflectiveCalls",
            "-language:existentials",
            "-Wunused:imports,privates,locals"
    )
}



// test {
//     maxParallelForks = 1
// }

tasks.getByName("build").dependsOn("makeScripts")

// ////////////////////////////////////////////////////////////////////////////////


val mkfile = { obj: Any -> if (obj is File) obj else File(obj.toString()) }

task<DefaultTask>("makeScripts") {
    val shebang = "#! /usr/bin/env sh\n"
    val root = projectDir.absolutePath
    var pre = ""
    var outFile: File

    val classpath = sourceSets["main"].runtimeClasspath.toList().joinToString(":")
    try {
        val opts = mkfile("$root/.java").readText().replace("\n", " ")
        pre += "\nexport JAVA_OPTS='$opts'\n\n"
    } catch (_: Exception) {}

    outFile = mkfile("-scala")
    outFile.writeText(shebang + pre + "scala -classpath ${classpath}\n")
    outFile.setExecutable(true)

    outFile = mkfile("-amm")
    outFile.writeText(shebang + pre + "java -cp ${classpath}:/usr/local/bin/amm ammonite.Main\n")
    outFile.setExecutable(true)

    val jshellCp = classpath.split(":").filterNot {
        // TODO: filter out all nonexistent dirs, not just the blacklisted ones
        it.contains("build/classes/java/main") || it.contains("build/resources/main")
    }
    // jshellCp.forEach { println(it) }
    outFile = mkfile("-jshell")
    outFile.writeText(shebang + pre + "jshell --class-path ${jshellCp.joinToString(":")}\n")
    outFile.setExecutable(true)
}
