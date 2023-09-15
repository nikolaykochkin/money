pluginManagement {
    val quarkusPluginVersion: String by settings
    val quarkusPluginId: String by settings

    val nodePluginId: String by settings
    val nodePluginVersion: String by settings

    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }

    plugins {
        id(quarkusPluginId) version quarkusPluginVersion
        id(nodePluginId) version nodePluginVersion
    }
}

rootProject.name = "money"
include("back-end")
include("front-end")
