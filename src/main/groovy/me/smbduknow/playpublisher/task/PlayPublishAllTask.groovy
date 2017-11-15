package me.smbduknow.playpublisher.task

import com.android.build.gradle.api.ApplicationVariant
import me.smbduknow.playpublisher.util.ApkPublisherUtil
import org.gradle.api.tasks.TaskAction

class PlayPublishAllTask extends BasePlayTask {

    Set<ApplicationVariant> variants

    @TaskAction
    def publish() {
        init()

        variants.each { variant ->
            ApkPublisherUtil.publishVariant(publisher, variant, extension.track)
        }
    }

}
