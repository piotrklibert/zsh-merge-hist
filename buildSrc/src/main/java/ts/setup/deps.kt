@file:Suppress("KDocMissingDocumentation", "PublicApiImplicitType", "ClassName", "unused", "SpellCheckingInspection")

package ts.setup

import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.add

annotation class Unpinned

object deps {
	private object versions {
		const val android_gradle_plugin = "7.3.1"
		const val androidx_annotation = "1.5.0"
		const val androidx_appcompat = "1.5.1"
		const val androidx_cardview = "1.0.0"
		const val androidx_constraintlayout = "2.1.4"
		const val androidx_core_ktx = "1.9.0"
		const val androidx_lifecycle = "2.5.1"
		const val androidx_lifecycle_extension = "2.2.0"
		const val androidx_material = "1.7.0"
		const val androidx_navigation = "2.5.3"
		const val androidx_recycler_view = "1.2.1"
		const val androidx_test_core = "1.2.0"
		const val androidx_test_ext_junit = "1.1.3"
		const val androidx_test_monitor = "1.4.0"
		const val androidx_work_manager = "2.6.0"
		const val androidx_work_testing = "2.6.0"
		const val arch = "2.1.0"
		const val bouncycastle = "1.65"
		const val camera_camera2 = "1.0.2"
		const val camera_core = "1.0.2"
		const val camera_extensions = "1.0.0-alpha28"
		const val camera_lifecycle = "1.0.2"
		const val camera_view = "1.0.0-alpha28"
		const val camerax = "1.1.0"
		const val commons_math3 = "3.6.1"
		const val concurrent_futures = "1.1.0-alpha01"
		const val dagger = "2.44"
		const val encrypted_preferences = "1.0.0-rc04"
		const val espresso = "3.1.0"
		const val faker = "1.12.0"
		const val fbjni = "0.0.3"
		const val fernet_java8 = "1.4.2"
		const val firebase_analytics = "17.4.4"
		const val firebase_bom = "31.0.2"
		const val firebase_crashlytics = "18.2.7"
		const val firebase_crashlytics_gradle = "2.9.2"
		const val firebase_messaging = "23.1.0"
		const val fragment_ktx = "1.5.4"
		const val google_services = "4.3.14"
		const val hilt = "2.44"
		const val hilt_work = "1.0.0"
		const val icepick = "3.2.0"
		const val javax = "1"
		const val jose4j = "0.8.0"
		const val junit = "4.13.2"
		const val junit_ext = "1.1.5"
		const val junit_ktx = "1.1.3"
		const val jwt = "0.11.5"
		const val kotlin = "1.7.0"
		const val kotlin_collections_immutable = "0.3.5"
		const val kotlin_coroutines = "1.6.4"
		const val kotlin_logging = "2.0.11"
		const val kotlin_test = "1.7.20"
		const val kotlinx_coroutines_test = "1.3.9"
		const val leak_canary = "2.8.1"
		const val lifecycle_livedata = "2.5.1"
		const val ml_kit_vision = "16.1.4"
		const val mockito_inline = "5.1.1"
		const val mockito_kotlin = "2.2.0"
		const val mockk = "1.12.7"
		const val mockwebserver = "4.10.0"
		const val moshi = "1.14.0"
		const val moshi_adapter = "1.13.0"
		const val moshi_converter = "2.9.0"
		const val nearex = "1.0"
		const val okhttp = "4.10.0"
		const val okhttp_logging_interceptor = "4.10.0"
		const val open_cv = "4.5.4"
		const val play_services = "21.0.1"
		const val play_services_ads = "20.5.0"
		const val play_services_basement = "18.0.0"
		const val play_services_integrity = "1.0.2"
		const val play_services_nearby = "18.0.2"
		const val play_services_safetynet = "18.0.1"
		const val protobuf = "0.8.12"
		const val protobuf_lite = "3.0.0"
		const val protoc = "3.0.0"
		const val protoc_gen_javalite = "3.0.0"
		const val pytorch = "1.0.3"
		const val pytorch_torchvision = "1.0.3"
		const val qr_code = "4.3.0"
		const val r8 = "3.1.42"
		const val redrock = "3.3"
		const val retrofit = "2.9.0"
		const val robolectric = "4.9"
		const val room = "2.4.3"
		const val room_encryption = "4.4.2"
		const val runner = "1.0.2"
		const val rx_android = "2.1.1"
		const val rx_java = "2.2.11"
		const val rx_kotlin = "2.4.0"
		const val rx_observers = "1.0.0"
		const val rx_relay = "2.1.0"
		const val rx_work = "1.0.0"
		const val sdp = "1.0.6"
		const val security_crypto = "1.0.0"
        const val slf4j = "1.7.30"
		const val ssp = "1.0.6"
		const val support_design = "26.1.0"
		const val truststamp = "1.4.5"
		const val work = "2.7.1"
		const val zxing = "3.4.0"
	}

	object debug {
		const val firebase_analytics = "com.google.firebase:firebase-analytics-ktx:21.2.0" // :${versions.firebase_analytics}"
		const val firebase_crashlytics = "com.google.firebase:firebase-crashlytics-ktx:18.3.5" // ""com.crashlytics.sdk.android:crashlytics:${versions.firebase_crashlytics}"
	}

	object test {
		const val arch = "androidx.arch.core:core-testing:${versions.arch}"
		const val core = "androidx.test:core:${versions.androidx_test_core}"
		const val core_ktx = "androidx.test:core-ktx:${versions.androidx_test_core}"
		const val espresso_core = "androidx.test.espresso:espresso-core:${versions.espresso}"
		const val espresso_intents = "androidx.test.espresso:espresso-intents:${versions.espresso}"
		const val faker = "io.github.serpro69:kotlin-faker:${versions.faker}"
		const val junit = "junit:junit:${versions.junit}"
		const val junit_ext = "androidx.test.ext:junit:${versions.junit_ext}"
		const val junit_ktx = "androidx.test.ext:junit-ktx:${versions.junit_ktx}"
		const val kotlin_test = "org.jetbrains.kotlin:kotlin-test:${versions.kotlin_test}"
		const val kotlinx_coroutines_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${versions.kotlinx_coroutines_test}"
		const val mockito_inline = "org.mockito:mockito-inline:${versions.mockito_inline}"
		const val mockito_kotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${versions.mockito_kotlin}"
		const val mockk = "io.mockk:mockk:${versions.mockk}"
		const val mockwebserver = "com.squareup.okhttp3:mockwebserver:${versions.mockwebserver}"
		const val robolectric = "org.robolectric:robolectric:${versions.robolectric}"
		const val runner = "com.android.support.test:runner:${versions.runner}"
	}

	object dagger {
		const val android = "com.google.dagger:dagger-android:${versions.dagger}"
		const val android_processor = "com.google.dagger:dagger-android-processor:${versions.dagger}"
		const val android_support = "com.google.dagger:dagger-android-support:${versions.dagger}"
		const val annotation = "javax.inject:javax.inject:${versions.javax}"
		const val compiler = "com.google.dagger:dagger-compiler:${versions.dagger}"
		const val runtime = "com.google.dagger:dagger:${versions.dagger}"
	}

	object ui {
		const val sdp = "com.intuit.sdp:sdp-android:${versions.sdp}"
		const val ssp = "com.intuit.ssp:ssp-android:${versions.ssp}"
	}

	object database {
		const val compiler = "androidx.room:room-compiler:${versions.room}"
		const val encryption = "net.zetetic:android-database-sqlcipher:${versions.room_encryption}"
		const val runtime = "androidx.room:room-runtime:${versions.room}"
		const val rxjava2 = "androidx.room:room-rxjava2:${versions.room}"
        const val ktx = "androidx.room:room-ktx:${versions.room}"
	}

	object androidx {
		const val annotation = "androidx.annotation:annotation:${versions.androidx_annotation}"
		const val appcompat = "androidx.appcompat:appcompat:${versions.androidx_appcompat}"
		const val camera_camera2 = "androidx.camera:camera-camera2:${versions.camerax}"
		const val camera_core = "androidx.camera:camera-core:${versions.camerax}"
		const val camera_extensions = "androidx.camera:camera-extensions:1.0.0-alpha05"
		const val camera_lifecycle = "androidx.camera:camera-lifecycle:1.0.0-alpha02"
		const val camera_view = "androidx.camera:camera-view:1.0.0-alpha05"
		const val concurrent_futures_ktx = "androidx.concurrent:concurrent-futures-ktx:${versions.concurrent_futures}"
		const val constraintlayout = "androidx.constraintlayout:constraintlayout:${versions.androidx_constraintlayout}"
		const val core_ktx = "androidx.core:core-ktx:${versions.androidx_core_ktx}"
		const val fragment_ktx = "androidx.fragment:fragment-ktx:${versions.fragment_ktx}"
        const val lifecycle_core = "androidx.lifecycle:lifecycle-livedata-core-ktx:${versions.androidx_lifecycle}"
		const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${versions.androidx_lifecycle}"
		const val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:${versions.androidx_lifecycle}"
		const val lifecycle_extensions_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${versions.androidx_lifecycle}"
		const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${versions.lifecycle_livedata}"
		const val lifecycle_process = "androidx.lifecycle:lifecycle-process:${versions.androidx_lifecycle}"
		const val material = "com.google.android.material:material:${versions.androidx_material}"
		const val navigation_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${versions.androidx_navigation}"
		const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${versions.androidx_navigation}"
		const val recyclerview = "androidx.recyclerview:recyclerview:${versions.androidx_recycler_view}"
		const val work_runtime_ktx = "androidx.work:work-runtime-ktx:${versions.work}"
		const val work_rxjava2 = "androidx.work:work-rxjava2:${versions.work}"
	}

	object misc {
		const val bouncycastle = "org.bouncycastle:bcprov-jdk15on:${versions.bouncycastle}"
		const val bouncycastle_bcprov = "org.bouncycastle:bcprov-jdk15on:${versions.bouncycastle}"
		const val bouncycastle_bcpkix = "org.bouncycastle:bcpkix-jdk15on:${versions.bouncycastle}"
		const val bouncycastle_pg = "org.bouncycastle:bcpg-jdk15on:${versions.bouncycastle}"
		const val bouncycastle_util = "org.bouncycastle:bcutil-jdk15on:${versions.bouncycastle}"
		const val bouncycastle_tls = "org.bouncycastle:bctls-jdk15on:${versions.bouncycastle}"

		// const val bouncycastle = "org.bouncycastle:bcprov-jdk18on:${versions.bouncycastle}"
		// const val bouncycastle_bcprov = "org.bouncycastle:bcprov-jdk18on:${versions.bouncycastle}"
		// const val bouncycastle_bcpkix = "org.bouncycastle:bcpkix-jdk18on:${versions.bouncycastle}"
		// const val bouncycastle_pg = "org.bouncycastle:bcpg-jdk18on:${versions.bouncycastle}"
		// const val bouncycastle_util = "org.bouncycastle:bcutil-jdk18on:${versions.bouncycastle}"
		// const val bouncycastle_tls = "org.bouncycastle:bctls-jdk18on:${versions.bouncycastle}"

		const val firebase_analytics = "com.google.firebase:firebase-analytics:${versions.firebase_analytics}"
		const val firebase_analytics_ktx = "com.google.firebase:firebase-analytics-ktx:${versions.firebase_analytics}"
		const val firebase_bom = "com.google.firebase:firebase-bom:${versions.firebase_bom}"
		const val firebase_crashlytics = "com.google.firebase:firebase-crashlytics:${versions.firebase_crashlytics}"
		const val firebase_crashlytics_ktx = "com.google.firebase:firebase-crashlytics-ktx:${versions.firebase_crashlytics}"
		const val firebase_messaging = "com.google.firebase:firebase-messaging:${versions.firebase_messaging}"
		const val icepick = "frankiesardo:icepick:${versions.icepick}"
		const val icepick_processor = "frankiesardo:icepick-processor:${versions.icepick}"
		const val moshi = "com.squareup.moshi:moshi-kotlin:${versions.moshi}"
		const val moshi_adapter = "com.squareup.moshi:moshi-adapters:${versions.moshi}"
		const val moshi_codegen = "com.squareup.moshi:moshi-kotlin-codegen:${versions.moshi}"
		const val play_services_location = "com.google.android.gms:play-services-location:${versions.play_services}"
		const val qr_code = "com.journeyapps:zxing-android-embedded:${versions.qr_code}"
		const val security_crypto = "androidx.security:security-crypto:${versions.security_crypto}"
		const val zxing = "com.google.zxing:core:${versions.zxing}"
	}

	object di {
		const val hilt = "com.google.dagger:hilt-android:${versions.hilt}"
		const val hilt_codegen = "com.google.dagger:hilt-android-compiler:${versions.hilt}"
		const val hilt_work = "androidx.hilt:hilt-work:${versions.hilt_work}"
	}

	object truststamp {
		const val face_recognition = "ai.checkin:mastercard-face-recognition:${versions.truststamp}"
		const val it2 = "ai.checkin:mastercard-it2:${versions.truststamp}"
		const val similarity_search = "ai.checkin:mastercard-similarity-search:${versions.truststamp}"
	}

	object network {
		const val moshi = "com.squareup.retrofit2:converter-moshi:${versions.moshi_converter}"
		const val jwt = "io.jsonwebtoken:jjwt-api:${versions.jwt}"
		const val jwt_impl = "io.jsonwebtoken:jjwt-impl:${versions.jwt}"
		const val jwt_orgjson = "io.jsonwebtoken:jjwt-orgjson:${versions.jwt}"
		const val logging = "com.squareup.okhttp3:logging-interceptor:${versions.okhttp_logging_interceptor}"
		const val runtime = "com.squareup.retrofit2:retrofit:${versions.retrofit}"
		const val rxjava2 = "com.squareup.retrofit2:adapter-rxjava2:${versions.retrofit}"
	}

	object play_services {
		const val basement = "com.google.android.gms:play-services-basement:${versions.play_services_basement}"
		const val integrity = "com.google.android.play:integrity:${versions.play_services_integrity}"
	}

	object rx {
		const val android = "io.reactivex.rxjava2:rxandroid:${versions.rx_android}"
		const val java = "io.reactivex.rxjava2:rxjava:${versions.rx_java}"
		const val kotlin = "io.reactivex.rxjava2:rxkotlin:${versions.rx_kotlin}"
		const val relay = "com.jakewharton.rxrelay2:rxrelay:${versions.rx_relay}"
		const val work = "com.github.PaulinaSadowska:RxWorkManagerObservers:${versions.rx_work}"
	}

	object classpath {
		const val android_gradle_plugin = "com.android.tools.build:gradle:${versions.android_gradle_plugin}"
		const val androidx_navigation_safe_args_plugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${versions.androidx_navigation}"
		const val firebase_crashlytics_gradle = "com.google.firebase:firebase-crashlytics-gradle:${versions.firebase_crashlytics_gradle}"
		const val google_services = "com.google.gms:google-services:${versions.google_services}"
		const val hilt_gradle_plugin = "com.google.dagger:hilt-android-gradle-plugin:${versions.hilt}"
		const val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}"
	}

	object kotlin {
		const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${versions.kotlin}"
		const val stdlib_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${versions.kotlin}"
		const val collections_immutable = "org.jetbrains.kotlinx:kotlinx-collections-immutable:${versions.kotlin_collections_immutable}"
		const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${versions.kotlin_coroutines}"
		const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${versions.kotlin_coroutines}"
		const val coroutines_rx2 = ("org.jetbrains.kotlinx:kotlinx-coroutines-rx2:${versions.kotlin_coroutines}")

		const val logging = "io.github.oshai:kotlin-logging-jvm:4.0.0-beta-22"
		const val slf4j = "org.slf4j:slf4j-api:${versions.slf4j}"
		const val slf4j_simple = "org.slf4j:slf4j-simple:${versions.slf4j}"
		const val slf4j_android = "uk.uuid.slf4j:slf4j-android:${versions.slf4j}-0"
	}

	object support {
		const val design = "com.android.support:design:${versions.support_design}"
	}

    /** Groups of dependencies commonly used/needed together */

    fun DependencyHandlerScope.groupKotlin() {
        add("implementation", kotlin.stdlib_jdk8)
        add("implementation", kotlin.collections_immutable)
        add("implementation", kotlin.reflect)
    }

    fun DependencyHandlerScope.groupCoroutines() {
        add("implementation", kotlin.coroutines_core)
        add("implementation", kotlin.coroutines_android)
        add("implementation", kotlin.coroutines_rx2)
    }

    fun DependencyHandlerScope.groupLogging() {
        add("implementation", kotlin.logging)
        add("implementation", kotlin.slf4j)
        add("implementation", kotlin.slf4j_android)
        add("testImplementation", kotlin.logging)
        add("testImplementation", kotlin.slf4j) {
            // Otherwise it tries to use 2.0 version, which is not compatible with 1.7.30
            version { strictly(versions.slf4j) }
        }
        add("testImplementation", kotlin.slf4j_simple)
    }

    fun DependencyHandlerScope.groupRx() {
        add("implementation", rx.android)
        add("implementation", rx.java)
        add("implementation", rx.kotlin)
        add("implementation", rx.relay)
        add("implementation", rx.work)
    }

    fun DependencyHandlerScope.groupRetrofit() {
        add("implementation", network.runtime)
        add("implementation", network.moshi)
        add("implementation", network.logging)
        add("implementation", network.rxjava2)
    }

    fun DependencyHandlerScope.groupTestUtils() {
        add("testImplementation", test.core)
        add("testImplementation", test.core_ktx)
        add("testImplementation", test.junit)
        add("testImplementation", test.junit_ext)
        add("testImplementation", test.junit_ktx)
        add("testImplementation", test.kotlin_test)
        add("testImplementation", test.mockito_inline)
        add("testImplementation", test.mockito_kotlin)
        add("testImplementation", test.kotlinx_coroutines_test)
        add("testImplementation", test.mockk)
        add("testImplementation", test.mockwebserver)
        add("testImplementation", test.robolectric)
        add("testImplementation", test.runner)
        add("testImplementation", test.faker)

        add("androidTestImplementation", test.espresso_core)
        add("androidTestImplementation", test.junit_ext)
    }

    fun DependencyHandlerScope.groupDatabase() {
        add("implementation", database.runtime)
        add("implementation", database.ktx)
        add("implementation", database.rxjava2)
        add("implementation", database.encryption)
        add("kapt", database.compiler)
    }

    fun DependencyHandlerScope.groupCrypto() {
        add("implementation", misc.bouncycastle)
        add("implementation", misc.bouncycastle_bcprov)
        add("implementation", misc.bouncycastle_bcpkix)
        add("implementation", misc.bouncycastle_pg)
        // add("implementation", misc.bouncycastle_util)
        add("implementation", misc.bouncycastle_tls)
        add("implementation", misc.security_crypto)
    }

    fun DependencyHandlerScope.groupFirebase() {
        add("implementation", platform(misc.firebase_bom))
        add("implementation", misc.firebase_analytics)
        add("implementation", misc.firebase_analytics_ktx)
        add("implementation", misc.firebase_crashlytics)
        add("implementation", misc.firebase_crashlytics_ktx)
        add("implementation", misc.firebase_messaging)
    }

    fun DependencyHandlerScope.groupMoshi() {
        add("implementation", misc.moshi)
        add("implementation", misc.moshi_adapter)
        add("kapt", misc.moshi_codegen)
        add("implementation", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    }

    fun DependencyHandlerScope.groupDependencyInjection() {
        add("implementation", di.hilt)
        add("kapt", di.hilt_codegen)
        add("implementation", di.hilt_work)
    }

    fun DependencyHandlerScope.groupArrow() {
        add("implementation", platform("io.arrow-kt:arrow-stack:1.1.2"))
        add("implementation", "io.arrow-kt:arrow-core")
        add("implementation", "io.arrow-kt:arrow-fx-coroutines")
        add("implementation", "io.arrow-kt:arrow-fx-stm")
    }

    fun DependencyHandlerScope.groupDagger() {
        add("implementation", dagger.android)
        add("implementation", dagger.android_processor)
        add("implementation", dagger.android_support)
        add("implementation", dagger.runtime)
        add("kapt", dagger.android_processor)
        add("kapt", dagger.compiler)
        add("kaptTest", dagger.compiler)
    }
}
