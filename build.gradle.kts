plugins {
    java
    id("io.izzel.taboolib") version "1.31"
    id("org.jetbrains.kotlin.jvm") version "1.5.30"
}

taboolib {
    description {
        dependencies {
            name("Multiverse-Core").optional(true)
        }
        load("STARTUP")
        contributors {
            name("咸蛋")
        }
    }
    install("common")
    install("common-5")
    install("module-configuration")
    install("module-chat")
    install("module-nms", "module-nms-util")
    install("platform-bukkit")
    install("expansion-command-helper")
    classifier = null
    version = "6.0.6-14"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v11200:11200:all@jar")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}