import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget


plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")

}

sqldelight {
    database("EfisheryStore") {
        packageName = "app.by.wildan.model"
        sourceFolders = listOf("db")
        schemaOutputDirectory = file("build/dbs")
//        dependency(project(":SharedCode"))
    }
    linkSqlite = false
}


kotlin {

    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) ::iosArm64
        else ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "SharedCode"
            }
        }
    }

    jvm("android")

    val ktorVersion = "1.3.2"
    val coroutinesVersion = "1.3.0"
    val sqldelight_version = "1.3.0"


    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("io.ktor:ktor-client-core:$ktorVersion")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutinesVersion")
        implementation("io.ktor:ktor-client-json:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization:$ktorVersion")

}

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation ("io.ktor:ktor-client-android:$ktorVersion")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
        implementation("io.ktor:ktor-client-json-jvm:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
        implementation( "com.squareup.sqldelight:android-driver:$sqldelight_version")
    }
    sourceSets["iosMain"].dependencies {
        implementation("io.ktor:ktor-client-ios:$ktorVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutinesVersion")
        implementation("io.ktor:ktor-client-json-native:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization-native:$ktorVersion")
        implementation( "com.squareup.sqldelight:native-driver:1.2.2:$sqldelight_version")


    }

}


    val packForXcode by tasks.creating(Sync::class){
        val targetDir = File(buildDir, "xcode-frameworks")

        val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
        val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios")
            .binaries.getFramework(mode)

        inputs.property("mode",mode)
        dependsOn(framework.linkTask)
        from({framework.outputDirectory})
        into(targetDir)

        doLast{
            val gradlew = File(targetDir,"gradlew")
            gradlew.writeText("#!/bin/bash\n"
                    + "export 'JAVA_HOME=${System.getProperty("java.home")}'\n"
                    + "cd '${rootProject.rootDir}'\n"
                    + "./gradlew \$@\n")
            gradlew.setExecutable(true)
        }
    }

tasks.getByName("build").dependsOn(packForXcode)
