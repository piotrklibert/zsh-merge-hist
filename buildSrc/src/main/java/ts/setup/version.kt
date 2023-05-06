@file:Suppress("unused", "ClassName", "MemberVisibilityCanBePrivate")

package ts.setup

internal interface VersionProvider {
    fun getVersionName(): String
    fun getBuildNumber(): Int
    fun getVersion(): String = getVersionName() + "." + getBuildNumber().toString()
}

object version {
    // All the SDKs are treated as a single library and share the same version number.
    object Lib : VersionProvider {
        override fun getVersionName() = "1.0.0"
        override fun getBuildNumber() =
            (System.getenv("BITRISE_BUILD_NUMBER") ?: "1").toInt()
    }

    object ClientApp : VersionProvider {
        override fun getVersionName() = "1.2.0"
        override fun getBuildNumber() =
            (System.getenv("BITRISE_BUILD_NUMBER") ?: "2").toInt()
    }

    object OfficerApp : VersionProvider {
        override fun getVersionName() = "1.2.0"
        override fun getBuildNumber() =
            (System.getenv("BITRISE_BUILD_NUMBER") ?: "2").toInt()
    }

    object DemoApp : VersionProvider {
        override fun getVersionName() = "0.0.1"
        override fun getBuildNumber() =
            (System.getenv("BITRISE_BUILD_NUMBER") ?: "2").toInt()
    }
}
