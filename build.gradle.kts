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

// this will basically only ever say mavenCentral()
repositories {
    mavenCentral()
}

// this section provides specific locations for the build tool to search
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    // https://mvnrepository.com/artifact/org.openjfx/javafx
    implementation("org.openjfx:javafx:21-ea+5")
    // https://mvnrepository.com/artifact/org.openjfx/javafx-controls
    implementation("org.openjfx:javafx-controls:21-ea+5")
    // https://github.com/iAmGio/animated
    implementation("eu.iamgio:animated:0.7.0")
    // https://central.sonatype.com/artifact/org.kordamp.ikonli/ikonli-javafx/12.3.1
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    //https://central.sonatype.com/artifact/org.kordamp.ikonli/ikonli-bootstrapicons-pack/12.3.1
    implementation("org.kordamp.ikonli:ikonli-bootstrapicons-pack:12.3.1")
}

// enables JUnit testing
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

// javafx configurations
javafx {
     modules("javafx.controls")
}

// shadow configurations
shadow {

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
}

// settings for the gradle wrapper task
tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}