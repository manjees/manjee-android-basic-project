rootProject.name = "ManjeeBasicApp"
include(":app")
include(":data")
include(":data-resource")
include(":domain")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        jcenter()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
