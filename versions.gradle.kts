import java.io.FileReader
import java.util.Properties
import java.nio.file.Path

fun locatePropertiesFile(): Pair<File, Boolean> {
    rootProject.findProject(":kotlin-ultimate")?.let { kotlinUltimateProject ->
        // if in multi-project build, just take the file from ":kotlin-ultimate" sub-project
        return kotlinUltimateProject.file("versions.properties") to false
    }

    if (rootProject.findProject(":cidr-native") != null) {
        // if in standalone build, then take the file from the root project
        return rootProject.file("versions.properties") to true
    }
    else {
        // otherwise (compiling buildSrc), take the file one directory below the root project
        return rootProject.file("../versions.properties") to true
    }
}

val (propertiesFile: File, isStandaloneBuild: Boolean) = locatePropertiesFile()

FileReader(propertiesFile).use {
    val properties = Properties()
    properties.load(it)
    properties.forEach { (k, v) ->
        rootProject.extra[k.toString()] = v
    }
}

val prepareDepsPath: Path = propertiesFile.toPath().resolve("../buildSrc/prepare-deps").toRealPath()

fun externalDepsDir(depsProjectName: String, suffix: String): File =
        prepareDepsPath.resolve(depsProjectName).resolve("build/external-deps").resolve(suffix).toFile()

val clionVersion: String = rootProject.extra["versions.clion"] as String
rootProject.extra["clionVersion"] = clionVersion
rootProject.extra["clionRepo"] = rootProject.extra["versions.clion.repo"] as String
rootProject.extra["clionVersionStrict"] = (rootProject.extra["versions.clion.strict"] as String).toBoolean()
rootProject.extra["clionPlatformDepsDir"] = externalDepsDir("platform-deps", "clion-platform-deps-$clionVersion")
rootProject.extra["clionUnscrambledJarDir"] = externalDepsDir("platform-deps", "clion-unscrambled-$clionVersion")

val appcodeVersion: String = rootProject.extra["versions.appcode"] as String
rootProject.extra["appcodeVersion"] = appcodeVersion
rootProject.extra["appcodeRepo"] = rootProject.extra["versions.appcode.repo"] as String
rootProject.extra["appcodeVersionStrict"] = (rootProject.extra["versions.appcode.strict"] as String).toBoolean()
rootProject.extra["appcodePlatformDepsDir"] = externalDepsDir("platform-deps", "appcode-platform-deps-$appcodeVersion")
rootProject.extra["appcodeUnscrambledJarDir"] = externalDepsDir("platform-deps", "appcode-unscrambled-$appcodeVersion")

val artifactsForCidrDir: File = rootProject.rootDir.resolve("dist/artifacts")
rootProject.extra["artifactsForCidrDir"] = artifactsForCidrDir
rootProject.extra["clionPluginDir"] = artifactsForCidrDir.resolve("clionPlugin/Kotlin")
rootProject.extra["appcodePluginDir"] = artifactsForCidrDir.resolve("appcodePlugin/Kotlin")

if (isStandaloneBuild) {
    // setup additional properties that are required only when running in standalone mode:
    rootProject.extra["clionDir"] = externalDepsDir("cidr", "clion-$clionVersion")

    val ideaPluginForCidrVersion: String = rootProject.extra["versions.ideaPluginForCidr"] as String
    val ideaPluginForCidrBuildNumber: String = ideaPluginForCidrVersion
            .split("-release", limit = 2)
            .takeIf { it.size == 2 }
            ?.let { "${it[0]}-release" } ?: ideaPluginForCidrVersion
    val ideaPluginForCidrIde: String = rootProject.extra["versions.ideaPluginForCidr.ide"] as String

    rootProject.extra["ideaPluginForCidrVersion"] = ideaPluginForCidrVersion
    rootProject.extra["ideaPluginForCidrBuildNumber"] = ideaPluginForCidrBuildNumber
    rootProject.extra["ideaPluginForCidrIde"] = ideaPluginForCidrIde
    rootProject.extra["ideaPluginForCidrRepo"] = rootProject.extra["versions.ideaPluginForCidr.repo"]

    rootProject.extra["ideaPluginForCidrDir"] = externalDepsDir("idea-plugin", "ideaPlugin-$ideaPluginForCidrBuildNumber-$ideaPluginForCidrIde")
    rootProject.extra["kotlinForCidrVersion"] = ideaPluginForCidrVersion.substringBefore('-')
}
