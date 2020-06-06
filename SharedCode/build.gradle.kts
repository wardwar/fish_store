import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
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

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
    }

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
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
