// DO NOT EDIT MANUALLY!
// Generated by org/jetbrains/kotlin/generators/arguments/GenerateGradleOptions.kt
// To regenerate run 'generateGradleOptions' task
@file:Suppress("RemoveRedundantQualifierName", "Deprecation", "DuplicatedCode")

package org.jetbrains.kotlin.gradle.dsl

internal class KotlinMultiplatformCommonOptionsBase @javax.inject.Inject constructor(
    objectFactory: org.gradle.api.model.ObjectFactory
) : org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformCommonOptions {

    override val allWarningsAsErrors: org.gradle.api.provider.Property<kotlin.Boolean> =
        objectFactory.property(kotlin.Boolean::class.java).convention(false)

    override val suppressWarnings: org.gradle.api.provider.Property<kotlin.Boolean> =
        objectFactory.property(kotlin.Boolean::class.java).convention(false)

    override val verbose: org.gradle.api.provider.Property<kotlin.Boolean> =
        objectFactory.property(kotlin.Boolean::class.java).convention(false)

    override val freeCompilerArgs: org.gradle.api.provider.ListProperty<kotlin.String> =
        objectFactory.listProperty(kotlin.String::class.java).convention(emptyList())

    override val apiVersion: org.gradle.api.provider.Property<kotlin.String> =
        objectFactory.property(kotlin.String::class.java)

    override val languageVersion: org.gradle.api.provider.Property<kotlin.String> =
        objectFactory.property(kotlin.String::class.java)

    override val useFir: org.gradle.api.provider.Property<kotlin.Boolean> =
        objectFactory.property(kotlin.Boolean::class.java).convention(false)

    override val moduleName: org.gradle.api.provider.Property<kotlin.String> =
        objectFactory.property(kotlin.String::class.java)

    internal fun toCompilerArguments(args: org.jetbrains.kotlin.cli.common.arguments.K2MetadataCompilerArguments) {
        args.allWarningsAsErrors = allWarningsAsErrors.get()
        args.suppressWarnings = suppressWarnings.get()
        args.verbose = verbose.get()
        args.freeArgs += freeCompilerArgs.get()
        args.apiVersion = apiVersion.orNull
        args.languageVersion = languageVersion.orNull
        args.useFir = useFir.get()
        args.moduleName = moduleName.orNull
    }

    internal fun fillDefaultValues(args: org.jetbrains.kotlin.cli.common.arguments.K2MetadataCompilerArguments) {
        args.allWarningsAsErrors = false
        args.suppressWarnings = false
        args.verbose = false
        args.apiVersion = null
        args.languageVersion = null
        args.useFir = false
        args.moduleName = null
    }
}
