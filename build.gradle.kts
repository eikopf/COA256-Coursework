import java.util.*

// this section handles the high-level plugin groups
plugins {
    // enables java for gradle
    id("java")

    // configurations for application usage
    id("application")

    // adds tasks to create distributions
    id("distribution")

    // GUI plugin for java
    id("org.openjfx.javafxplugin") version "0.0.13"

    // adds tasks for producing fat .jar files
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "org.example"
version = "0.1-SNAPSHOT"

// check OS version
// source: https://www.techiedelight.com/determine-current-operating-system-kotlin/
enum class OS {
    WINDOWS, LINUX, MAC
}

fun getOS(): OS? {
    val os = System.getProperty("os.name").lowercase(Locale.getDefault())
    return when {
        os.contains("win") -> {
            OS.WINDOWS
        }
        os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
            OS.LINUX
        }
        os.contains("mac") -> {
            OS.MAC
        }
        else -> null
    }
}

val os: OS = getOS()!! // double bang asserts that the value is not null


// this will basically only ever say mavenCentral()
repositories {
    mavenCentral()
}

// this section provides specific locations for the build tool to search
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    // https://mvnrepository.com/artifact/org.openjfx/javafx
    implementation("org.openjfx:javafx:19.0.2.1")
    // https://mvnrepository.com/artifact/org.openjfx/javafx-controls
    implementation("org.openjfx:javafx-controls:19.0.2.1")
    // https://github.com/iAmGio/animated
    implementation("eu.iamgio:animated:0.7.0")
    // https://central.sonatype.com/artifact/org.kordamp.ikonli/ikonli-javafx/12.3.1
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    //https://central.sonatype.com/artifact/org.kordamp.ikonli/ikonli-bootstrapicons-pack/12.3.1
    implementation("org.kordamp.ikonli:ikonli-bootstrapicons-pack:12.3.1")
    // https://mvnrepository.com/artifact/org.openjfx/javafx-fxml
    implementation("org.openjfx:javafx-fxml:19.0.2.1")
}

// enables JUnit testing
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

// javafx configurations
javafx {
    version = "19"
    modules("javafx.controls", "javafx.fxml")
}

// application configurations
application {
    mainClass.set("Main")
}

// distribution configurations
distributions {
    main {
        distributionBaseName.set("F214180-Coursework")

        contents {
            from("{$projectDir()}")
        }
    }
}

// configurations for the shadowJar task
tasks.shadowJar {
    archiveBaseName.set("F214180-Coursework")
}


// TODO: fix zipping behaviour on windows
// i think it can be fixed with powershell functions?
// some quick googling says that it was added in 2016
// so an important question: what version of windows does hossein have?

// custom task to finalize build
tasks.register("buildSubmission") {

    // local distribution configuration
    distributions {
        main {
            distributionBaseName.set("F214180-Coursework-Submission")

            contents {
                from("{$projectDir()}")
            }
        }
    }

    dependsOn("shadowJar")
    dependsOn("test")
    dependsOn("distZip")
    tasks.findByName("shadowJar")?.mustRunAfter("test")
    tasks.findByName("distZip")!!.mustRunAfter("shadowJar")

    doLast {
        if (os == OS.LINUX || os == OS.MAC) {
            // add the source files into the zip
            exec {
                workingDir("$projectDir")
                commandLine(
                    "zip",
                    "-ur",
                    "./build/distributions/F214180-Coursework-Submission-$version.zip",
                    "./src",
                    "./gradle" // necessary for ./gradlew build
                )
            }

            // add other project files into the zip
            exec {
                workingDir("$projectDir")
                commandLine(
                    "zip",
                    "-u",
                    "./build/distributions/F214180-Coursework-Submission-$version.zip",
                    ".project",
                    "build.gradle.kts",
                    "gradlew",
                    "gradlew.bat",
                    "README.md",
                    "settings.gradle.kts"
                )
            }

            // add the fat jar into the zip
            exec {
                workingDir("$projectDir")
                commandLine(
                    "zip",
                    "-uj",
                    "./build/distributions/F214180-Coursework-Submission-$version.zip",
                    "./build/libs/F214180-Coursework-$version-all.jar"
                )
            }
        }

        // compression commands are piped through powershell to use Powershell.Archive
        // docs: https://learn.microsoft.com/en-us/powershell/module/microsoft.powershell.archive/compress-archive?view=powershell-7.3
        else if (os == OS.WINDOWS) {

            // add source to zip
            exec {
                workingDir("$projectDir")
                commandLine(
                    "cmd",
                    "/c",
                    "powershell " +
                            "-Command \"Compress-Archive " +
                            "-Path $projectDir\\src " +
                            "-DestinationPath " +
                            "$projectDir\\build\\distributions\\F214180-Coursework-Submission-$version.zip" +
                            "\""
                )
            }

            // add gradle folder to zip
            exec {
                workingDir("$projectDir")
                commandLine(
                    "cmd",
                    "/c",
                    "powershell " +
                            "-Command \"Compress-Archive " +
                            "-Path $projectDir\\gradle " +
                            "-Update " +
                            "-DestinationPath " +
                            "$projectDir\\build\\distributions\\F214180-Coursework-Submission-$version.zip" +
                            "\""
                )
            }

            // add other project files and fat jar to zip
            exec {
                workingDir("$projectDir")
                commandLine(
                    "cmd",
                    "/c",
                    "powershell -Command \"Get-ChildItem -Path " +
                            "$projectDir\\.project, " +
                            "$projectDir\\build.gradle.kts, " +
                            "$projectDir\\gradlew, " +
                            "$projectDir\\gradlew.bat, " +
                            "$projectDir\\README.md, " +
                            "$projectDir\\settings.gradle.kts, " +
                            "$projectDir\\build\\libs\\F214180-Coursework-$version-all.jar" +
                            "| " +
                            "Compress-Archive " +
                            "-Update " +
                            "-DestinationPath " +
                            "$projectDir\\build\\distributions\\F214180-Coursework-Submission-$version.zip\""
                )
            }
        }
    }
}

tasks.register("buildRelease") {

    // local distribution configuration
    distributions {
        main {
            distributionBaseName.set("F214180-Coursework-Release")

            contents {
                from("{$projectDir()}")
            }
        }
    }

    dependsOn("shadowJar")
    dependsOn("test")
    dependsOn("distZip")
    tasks.findByName("shadowJar")?.mustRunAfter("test")
    tasks.findByName("distZip")!!.mustRunAfter("shadowJar")

    doLast {
        if (os == OS.MAC || os == OS.LINUX) {
            // add the source files into the zip
            exec {
                workingDir("$projectDir")
                commandLine(
                    "zip",
                    "-ur",
                    "./build/distributions/F214180-Coursework-Release-$version.zip",
                    "./gradle" // necessary for ./gradlew build
                )
            }

            // add other project files into the zip
            exec {
                workingDir("$projectDir")
                commandLine(
                    "zip",
                    "-u",
                    "./build/distributions/F214180-Coursework-Release-$version.zip",
                    "build.gradle.kts",
                    "gradlew",
                    "gradlew.bat",
                    "README.md",
                    "settings.gradle.kts"
                )
            }

            // add the fat jar into the zip
            exec {
                workingDir("$projectDir")
                commandLine(
                    "zip",
                    "-uj",
                    "./build/distributions/F214180-Coursework-Release-$version.zip",
                    "./build/libs/F214180-Coursework-$version-all.jar"
                )
            }
        }

        else if (os == OS.WINDOWS) {

            // add gradle folder to zip
            exec {
                workingDir("$projectDir")
                commandLine(
                    "cmd",
                    "/c",
                    "powershell " +
                            "-Command \"Compress-Archive " +
                            "-Path $projectDir\\gradle " +
                            "-Update " +
                            "-DestinationPath " +
                            "$projectDir\\build\\distributions\\F214180-Coursework-Release-$version.zip" +
                            "\""
                )
            }

            // add other project files and fat jar to zip
            exec {
                workingDir("$projectDir")
                commandLine(
                    "cmd",
                    "/c",
                    "powershell -Command \"Get-ChildItem -Path " +
                            "$projectDir\\.project, " +
                            "$projectDir\\build.gradle.kts, " +
                            "$projectDir\\gradlew, " +
                            "$projectDir\\gradlew.bat, " +
                            "$projectDir\\README.md, " +
                            "$projectDir\\settings.gradle.kts, " +
                            "$projectDir\\build\\libs\\F214180-Coursework-$version-all.jar" +
                            "| " +
                            "Compress-Archive " +
                            "-Update " +
                            "-DestinationPath " +
                            "$projectDir\\build\\distributions\\F214180-Coursework-Release-$version.zip\""
                )
            }
        }
    }
}

// settings for the gradle wrapper task
tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}