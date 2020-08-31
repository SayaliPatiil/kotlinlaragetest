plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    compile(project(":core:util.runtime"))
    compile(project(":compiler:config"))
    compile(project(":compiler:config.jvm"))
    compile(project(":js:js.config"))
    compile(project(":native:kotlin-native-utils"))
    compile(project(":compiler:cli-messages"))
    compile(project(":compiler:util"))
    compileOnly(project(":kotlin-reflect-api"))
    compileOnly(intellijCoreDep()) { includeJars("intellij-core") }
    compileOnly(intellijDep()) { includeIntellijCoreJarDependencies(project) }
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
