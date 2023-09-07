fun properties(key: String) = project.findProperty(key).toString()

plugins {
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("dev.clojurephant.clojure") version "0.7.0"
    id("org.jetbrains.intellij") version "1.15.0"
}

group = properties("group")
version = properties("version")

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.ericdallo"
            artifactId = "clj4intellij"
            version = "0.1.0"

            from(components["java"])
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "Clojars"
        url = uri("https://repo.clojars.org")
    }
}

dependencies {
    implementation ("org.clojure:clojure:1.11.1")
    implementation ("com.rpl:proxy-plus:0.0.9")
    implementation ("nrepl:nrepl:1.0.0")
}

sourceSets {
    main {
        java.srcDirs("src/main")
        resources.srcDirs("resources")
    }
    test {
        java.srcDirs("tests")
    }
}

// Useful to override another IC platforms from env
val platformVersion = System.getenv("PLATFORM_VERSION") ?: properties("platformVersion")
val platformPlugins = System.getenv("PLATFORM_PLUGINS") ?: properties("platformPlugins")

intellij {
    pluginName.set(properties("name"))
    version.set(platformVersion)
    type.set(properties("platformType"))
    updateSinceUntilBuild.set(false)
}

java {
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
            apiVersion = "1.5"
            languageVersion = "1.5"
            freeCompilerArgs = listOf("-Xjvm-default=all")
        }
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    buildSearchableOptions {
        enabled = false
    }
}

clojure.builds.named("main") {
    classpath.from(sourceSets.main.get().runtimeClasspath.asPath)
    checkAll()
    aotAll()
    reflection.set("fail")
}
