pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")}
    }
}

rootProject.name = "journeydex"
include(":app")
include(":core:data")
include(":core:auth")
include(":feature:login")
include(":feature:home")
include(":feature:dex")
include(":feature:reward")
include(":feature:map")
include(":core:designsystem")
