pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        jcenter()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "ManjeeBasicApp"
include(
    ":app",
    ":data",
    ":domain"
)