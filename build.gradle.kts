import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.undsf.mc"
version = "1.0.0-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/repository/public/")
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.squareup.okhttp3:okhttp:4.10.0")
                implementation("com.google.code.gson:gson:2.9.1")
            }
        }
        val jvmTest by getting {
            dependencies {
                // implementation("org.jetbrains.kotlin:kotlin-test-junit5:1.6.10")
                implementation(kotlin("test"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "mods-manager"
            packageVersion = "1.0.0"
        }
    }
}
