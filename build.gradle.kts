plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Buradaki '2.0.0' olan yeri '2.0.21' yapıyoruz
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
}
android {
    namespace = "com.example.filmkesifuygulamasi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.filmkesifuygulamasi"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    // 2. ADIM: Buradaki composeOptions (kotlinCompilerExtensionVersion) kısmını SİLDİK.
    // Çünkü artık yukarıdaki plugin bu işi otomatik hallediyor.
}

dependencies {
    // Temel Compose Kütüphaneleri
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2024.02.00")) // BOM versiyonunu güncelledik
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // --- FİLM UYGULAMASI İÇİN GEREKLİ KÜTÜPHANELER ---
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}