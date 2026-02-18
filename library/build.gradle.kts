fun gitVersion(): String {
    fun git(vararg args: String): String? {
        val process = ProcessBuilder(*args)
            .directory(rootDir)
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readLine()?.trim()
        return if (process.waitFor() == 0) output else null
    }

    val exactTag = git("git", "describe", "--tags", "--exact-match")

    return if (exactTag != null) {
        exactTag.removePrefix("v")
    } else {
        val latestTag = git("git", "describe", "--tags", "--abbrev=0")?.removePrefix("v") ?: "0.0.0"
        "$latestTag-SNAPSHOT"
    }
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    id("maven-publish")
    id("signing")
}

android {
    namespace = "dev.switchkraft"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "dev.switchkraft"
            artifactId = "switchkraft"
            version = gitVersion()

            pom {
                name.set("Switchkraft")
                description.set("A Jetpack Compose library for building forms that switch between View and Edit modes.")
                url.set("https://github.com/shaunburch/switchkraft")
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        id.set("shaunburch")
                        name.set("shaunburch")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/shaunburch/switchkraft.git")
                    developerConnection.set("scm:git:ssh://github.com/shaunburch/switchkraft.git")
                    url.set("https://github.com/shaunburch/switchkraft")
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/shaunburch/switchkraft")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

// AGP components aren't available until after evaluation, so wire them up here
afterEvaluate {
    (publishing.publications["release"] as MavenPublication).from(components["release"])

    signing {
        val signingKey = System.getenv("GPG_SIGNING_KEY")
        val signingPassword = System.getenv("GPG_SIGNING_PASSWORD")
        if (signingKey != null && signingPassword != null) {
            useInMemoryPgpKeys(signingKey, signingPassword)
            sign(publishing.publications)
        }
    }
}

dependencies {
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
