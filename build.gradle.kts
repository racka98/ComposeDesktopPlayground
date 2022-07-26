import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val kotlinVersion = extra["kotlin.version"] as String
val koinVersion = extra["koin.version"] as String
val kotlinDateTimeVersion = extra["kotlin.datetime.version"] as String
val coroutinesVersion = extra["coroutines.version"] as String
val serializationVersion = extra["kotlin.serialization.version"] as String
val sqldelightVersion = extra["sqldelight.version"] as String
val ktorVersion = extra["ktor.version"] as String
val slf4jVersion = extra["slf4j.version"] as String
val kermitVersion = extra["kermit.version"] as String
val logbackVersion = extra["logback.version"] as String
val decomposeVersion = extra["decompose.version"] as String
val multiplatformSettingsVersion = extra["multiplatform.settings.version"] as String

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization").version("1.7.0")
}

group = "work.racka"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    mingwX64("windows") {
        binaries { executable() }
    }

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_11.majorVersion
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                // Compose
                implementation(compose.desktop.currentOs)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.desktop.components.splitPane)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.desktop.components.animatedImage)

                // Kotlin - Date/Time
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinDateTimeVersion")

                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$coroutinesVersion")

                // Decompose - Navigation
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")

                // Multiplatform Settings
                implementation("com.russhwolf:multiplatform-settings:$multiplatformSettingsVersion")
                implementation("com.russhwolf:multiplatform-settings-no-arg:$multiplatformSettingsVersion")

                // SQL Delight
                implementation("com.squareup.sqldelight:runtime:$sqldelightVersion")
                implementation("com.squareup.sqldelight:coroutines-extensions:$sqldelightVersion")
                implementation("com.squareup.sqldelight:sqlite-driver:$sqldelightVersion")

                // Logs
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("co.touchlab:kermit:$kermitVersion")

                implementation("net.java.dev.jna:jna:5.12.1")
                implementation("net.java.dev.jna:jna-platform:5.12.1")
            }
        }
        val jvmTest by getting

        val windowsMain by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "Compose Desktop Playground"
            packageVersion = "1.0.0"
            description = "A collection of Experiments with Compose Desktop"
            copyright = "© 2022 RackaApps. All rights reserved."
            vendor = "RackaApps"

            windows {
                shortcut = true
                menuGroup = packageName
                // Wondering what the heck is this?
                // See : https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                upgradeUuid = "B5B8365D-7442-4D9C-A451-AAFBC411E08C"
            }
        }
    }
}
