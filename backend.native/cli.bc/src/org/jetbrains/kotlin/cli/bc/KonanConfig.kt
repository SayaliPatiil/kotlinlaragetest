package org.jetbrains.kotlin.cli.bc

import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.LanguageVersionSettingsImpl
import org.jetbrains.kotlin.descriptors.PackageFragmentProvider
import org.jetbrains.kotlin.descriptors.impl.ModuleDescriptorImpl
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.CompilerDeserializationConfiguration
import org.jetbrains.kotlin.serialization.js.JsModuleDescriptor
import org.jetbrains.kotlin.serialization.js.KotlinJavascriptSerializationUtil
import org.jetbrains.kotlin.serialization.js.ModuleKind
import org.jetbrains.kotlin.storage.LockBasedStorageManager

import org.jetbrains.kotlin.backend.konan.llvm.KotlinKonanMetadata
import org.jetbrains.kotlin.backend.konan.llvm.KotlinKonanMetadataUtils

/**
 * Base class representing a configuration of translator.
 */
class KonanConfig(val project: Project, val configuration: CompilerConfiguration) {
    private val storageManager = LockBasedStorageManager()

    private val sourceFilesFromLibraries = listOf<KtFile>()
    private var metadata = listOf<KotlinKonanMetadata>()
    internal var moduleDescriptors: MutableList<JsModuleDescriptor<ModuleDescriptorImpl>>? = null

    init {
        val libraries = configuration.getList(KonanConfigurationKeys.LIBRARY_FILES)
        metadata = KotlinKonanMetadataUtils.loadLibMetadata(libraries)
    }

    val moduleId: String
        get() = configuration.getNotNull(CommonConfigurationKeys.MODULE_NAME)

    val moduleKind: ModuleKind
        get() = configuration.get(KonanConfigurationKeys.MODULE_KIND)!!

    fun getModuleDescriptors(): MutableList<JsModuleDescriptor<ModuleDescriptorImpl>> {
        if (moduleDescriptors != null) return moduleDescriptors!!

        moduleDescriptors = mutableListOf<JsModuleDescriptor<ModuleDescriptorImpl>>()
        val kotlinModuleDescriptors = mutableListOf<ModuleDescriptorImpl>()
        for (metadataEntry in metadata) {
            val descriptor = createModuleDescriptor(metadataEntry)
            moduleDescriptors!!.add(descriptor)
            kotlinModuleDescriptors.add(descriptor.data)
        }

        for (module in moduleDescriptors!!) {
            setDependencies(module.data, kotlinModuleDescriptors)
        }

        return moduleDescriptors!!
    }


    // We reuse JsModuleDescriptor for serialization for now, as we haven't got one for Konan yet.
    private fun createModuleDescriptor(metadata: KotlinKonanMetadata): JsModuleDescriptor<ModuleDescriptorImpl> {

        val moduleDescriptor = ModuleDescriptorImpl(
                Name.special("<" + metadata.moduleName + ">"), storageManager, KonanPlatform.builtIns)

        val rawDescriptor = KotlinJavascriptSerializationUtil.readModule(
                metadata.body, storageManager, moduleDescriptor, CompilerDeserializationConfiguration(
                configuration.get(CommonConfigurationKeys.LANGUAGE_VERSION_SETTINGS, LanguageVersionSettingsImpl.DEFAULT)))

        val provider = rawDescriptor.data
        moduleDescriptor.initialize(provider ?: PackageFragmentProvider.Empty)

        return rawDescriptor.copy(moduleDescriptor)
    }

    companion object {
        private fun setDependencies(module: ModuleDescriptorImpl, modules: List<ModuleDescriptorImpl>) {
            module.setDependencies(modules.plus(KonanPlatform.builtIns.builtInsModule))
        }

        fun withJsLibAdded(files: Collection<KtFile>, config: KonanConfig): Collection<KtFile> {
            val allFiles = mutableListOf<KtFile>()
            allFiles.addAll(files)
            // We don't store source files in the bitcode for Konan, dont't we?
            //allFiles.addAll(config.getSourceFilesFromLibraries());
            return allFiles
        }
    }

}
