package me.smbduknow.playpublisher

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.builder.model.BuildType
import me.smbduknow.playpublisher.task.PlayPublishAllTask
import me.smbduknow.playpublisher.task.PlayPublishTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class PlayPublishPlugin implements Plugin<Project> {

    private final static String GROUP_NAME = 'play-publish'

    @Override
    void apply(Project project) {
        def android = project.property('android') as AppExtension
        if (!android) {
            throw new IllegalStateException("The 'com.android.application' plugin is required.")
        }
        project.extensions.create(PlayPublishExtension.NAME, PlayPublishExtension)

        android.applicationVariants.all { ApplicationVariant variant ->
            if (variant.buildType.isDebuggable()) {
                return
            }
            project.tasks.create("publish${variant.name.capitalize()}", PlayPublishTask, { PlayPublishTask task ->
                task.group = GROUP_NAME
                task.description = "Deply APK to the Google Play Store for the ${variant.name.capitalize()} build"
                task.variant = variant
                task.outputs.upToDateWhen { false }
                task.dependsOn variant.assemble
            })
        }

        android.buildTypes.all { BuildType buildType ->
            if (buildType.isDebuggable()) {
                return
            }
            def variants = android.applicationVariants.findAll { it.buildType == buildType }

            project.tasks.create("publish${buildType.name.capitalize()}", PlayPublishAllTask, { PlayPublishAllTask task ->
                task.group = GROUP_NAME
                task.description = "Deply all flavors APKs to the Google Play Store for the ${buildType.name.capitalize()} build"
                task.variants = variants
                task.outputs.upToDateWhen { false }
                task.dependsOn "assemble${buildType.name.capitalize()}"
            })
        }

    }
}
