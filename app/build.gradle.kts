plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}


android {
    namespace = "com.csi.irite"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        applicationId = "com.csi.irite"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.compose.material3:material3-android:1.2.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.github.bumptech.glide:glide:3.7.0")
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.ernestoyaquello.stepperform:vertical-stepper-form:2.7.0")
    implementation("com.balysv:material-ripple:1.0.2")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("com.wdullaer:materialdatetimepicker:4.2.3")// date & time picker
    //implementation("org.mapsforge:mapsforge-map-android:0.21.0")
    //implementation("org.mapsforge:mapsforge-themes:0.8.0")
    implementation("io.github.g0dkar:qrcode-kotlin:4.1.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.zxing:core:3.3.0")
    implementation ("com.github.yukuku:ambilwarna:2.0")
    implementation("commons-codec:commons-codec:1.15")

    // Paging 3.0
    implementation("androidx.paging:paging-compose:1.0.0-alpha15")
    //implementation("org.mapsforge:mapsforge-themes:0.18.0")
    //implementation("org.mapsforge:mapsforge-map-android:0.18.0")
    //implementation("org.mapsforge:mapsforge-poi-android:0.18.0")

    implementation ("org.mapsforge:mapsforge-map-android:0.18.0")
    implementation ("org.mapsforge:mapsforge-map:0.18.0")
    implementation ("org.mapsforge:mapsforge-core:0.18.0")
    implementation ("org.mapsforge:mapsforge-map-reader:0.18.0")
    implementation ("org.mapsforge:mapsforge-themes:0.18.0")
    
    implementation ("com.itextpdf:itextg:5.5.10")
    implementation ("androidx.print:print:1.0.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
}