plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.eleconomico"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.eleconomico"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Habilita MultiDex si es necesario
        multiDexEnabled = true
    }

    signingConfigs {
        create("release") {
            storeFile = file("eleconomico.jks")          // Ruta a tu keystore
            storePassword = "tuStorePassword"           // Cambia por tu contraseña
            keyAlias = "eleconomico_key"                // Alias de tu clave
            keyPassword = "tuKeyPassword"               // Contraseña de la clave
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true                     // Activa ProGuard/R8
            isShrinkResources = true                   // Reduce recursos no usados
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            multiDexEnabled = true                     // Evita problemas de límite de métodos
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            multiDexEnabled = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Librerías externas
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.google.firebase:firebase-messaging:23.0.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("androidx.multidex:multidex:2.0.1")  // Corregido para Kotlin DSL
}
