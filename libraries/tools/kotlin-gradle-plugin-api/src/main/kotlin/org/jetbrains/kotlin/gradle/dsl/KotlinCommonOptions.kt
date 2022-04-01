// DO NOT EDIT MANUALLY!
// Generated by org/jetbrains/kotlin/generators/arguments/GenerateGradleOptions.kt
// To regenerate run 'generateGradleOptions' task
@file:Suppress("RemoveRedundantQualifierName", "Deprecation", "DuplicatedCode")

package org.jetbrains.kotlin.gradle.dsl

interface KotlinCommonOptions : org.jetbrains.kotlin.gradle.dsl.KotlinCommonToolOptions {

    /**
     * Allow using declarations only from the specified version of bundled libraries
     * Possible values: "1.3 (deprecated)", "1.4 (deprecated)", "1.5", "1.6", "1.7", "1.8 (experimental)", "1.9 (experimental)"
     * Default value: null
     */
    @get:org.gradle.api.tasks.Optional
    @get:org.gradle.api.tasks.Input
    val apiVersion: org.gradle.api.provider.Property<kotlin.String>

    /**
     * Provide source compatibility with the specified version of Kotlin
     * Possible values: "1.3 (deprecated)", "1.4 (deprecated)", "1.5", "1.6", "1.7", "1.8 (experimental)", "1.9 (experimental)"
     * Default value: null
     */
    @get:org.gradle.api.tasks.Optional
    @get:org.gradle.api.tasks.Input
    val languageVersion: org.gradle.api.provider.Property<kotlin.String>

    /**
     * Compile using Front-end IR. Warning: this feature is far from being production-ready
     * Default value: false
     */
    @get:org.gradle.api.tasks.Input
    val useFir: org.gradle.api.provider.Property<kotlin.Boolean>
}
