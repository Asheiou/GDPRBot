plugins {
  kotlin("jvm") version "2.2.0"
  id("com.gradleup.shadow") version "8.3.0"
  id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "cymru.asheiou"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/") {
    name = "papermc-repo"
  }
  maven("https://oss.sonatype.org/content/groups/public/") {
    name = "sonatype"
  }

  maven("https://repo.asheiou.cymru/snapshots") {
    name = "asheiou-snapshots"
  }
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
  compileOnly("xyz.aeolia:lib:2.0-dev6")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.10.2")
}

tasks {
  runServer {
    // Configure the Minecraft version for our task.
    // This is the only required configuration besides applying the plugin.
    // Your plugin's jar (or shadowJar if present) will be used automatically.
    minecraftVersion("1.21")
  }
}

val targetJavaVersion = 21
kotlin {
  jvmToolchain(targetJavaVersion)
}

tasks.processResources {
  val props = mapOf("version" to version)
  inputs.properties(props)
  filteringCharset = "UTF-8"
  filesMatching("plugin.yml") {
    expand(props)
  }
}

tasks.shadowJar {
  dependencies {
    include(dependency("org.jetbrains.kotlin:kotlin-stdlib.*"))
    include(dependency(("org.jetbrains.kotlinx:kotlinx-coroutines-core.*")))
  }
  relocate("kotlin", "cymru.asheiou.gdprbot.shade.kotlin")
  relocate("kotlinx", "cymru.asheiou.gdprbot.shade.kotlinx")

  minimize()
}
