pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral() // Thay thế jcenter()
        gradlePluginPortal()
    }
}
rootProject.name = "ReadingApplication"
include(":app")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral() // Thay thế jcenter()
        maven { url = uri("https://jitpack.io") }
    }

}
