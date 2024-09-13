import org.gradle.internal.classpath.Instrumented.systemProperty

plugins {
    java
    application
}

application {
	mainClass.set("suite.Main")
}

run {
    systemProperty("java.library.path", "build/resources/main/native/windows")
}