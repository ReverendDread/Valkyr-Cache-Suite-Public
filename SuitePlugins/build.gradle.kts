subprojects {

    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

	group = "Plugins"
    version = "1.0.0"

    dependencies {
        implementation(project(":ValkyrSuite"))
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.20")
    }

    tasks.create("buildAndMove") {
        group = "plugin"
        dependsOn("compileJava", "jar")
        doLast {
            copy {
                from(tasks.withType<Jar>().getByName("jar").archiveFile.get().asFile)
                into(file("$rootDir/ValkyrSuite/plugins/"))
            }
        }
    }
    
}
