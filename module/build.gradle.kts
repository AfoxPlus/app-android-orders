plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.jetbrains.kotlin.plugin.parcelize)
    alias(libs.plugins.kotlin.compose)
    id("org.sonarqube") version "3.3"
    id("jacoco")
}

apply {
    from(ConfigureApp.Gradle.sonarqube)
    from(ConfigureApp.Gradle.jacoco)
    from(ConfigureApp.Gradle.uploadArtifact)
    from("graph.gradle.kts")
}

android {
    namespace = "com.afoxplus.orders"
    compileSdk = ConfigureApp.Versions.compileSdkVersion

    defaultConfig {
        minSdk = ConfigureApp.Versions.minSdkVersion
        testInstrumentationRunner = ConfigureApp.Versions.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
        renderscriptSupportModeEnabled = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("staging") {
            initWith(getByName("debug"))
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin { jvmToolchain(ConfigureApp.Versions.jdkVersion) }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        compose = true
    }

    lint {
        disable.addAll(
            listOf(
                "TypographyFractions",
                "TypographyQuotes",
                "JvmStaticProvidesInObjectDetector",
                "FieldSiteTargetOnQualifierAnnotation",
                "ModuleCompanionObjects",
                "ModuleCompanionObjectsNotInModuleParent"
            )
        )
        checkDependencies = true
        abortOnError = false
        ignoreWarnings = false
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    //Jetpack
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.fragment.ktx)

    //Jetpack UI
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)


    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.androidx.compose)

    //Image Async
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //Lifecycle Scope
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.androidx.hilt.compiler)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //Scan
    implementation(libs.zxing.android.embedded) { isTransitive = false}
    implementation(libs.zxing.core)

    // Test
    testImplementation(libs.bundles.unit.test)

    // Business Dependencies
    implementation(project(ModuleDependency.Core.DESIGN_SYSTEM))
    implementation(project(ModuleDependency.Integration.NETWORK))
    implementation(project(ModuleDependency.Feature.PRODUCTS))


    //Compose Debug
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.tooling.preview)
}