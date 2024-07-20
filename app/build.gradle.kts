plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    // Complemento Gradle de servicios de Google
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.firebasedemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.firebasedemo"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    //Se habilitara la vinculacion de vistas (elementos de la interfaz de usuarios) para ya no usar findViewById
    buildFeatures{
        viewBinding = true
    }
}



dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Importar la lista de materiales de Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

    //Dependencias del servicio de authenticacion de firebase
    implementation("com.google.firebase:firebase-auth")

    //Dependencia de la biblioteca de servicios de Google Play
    implementation("com.google.android.gms:play-services-auth:21.2.0")
}