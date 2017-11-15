package me.smbduknow.playpublisher.task

import com.android.build.gradle.api.ApplicationVariant
import me.smbduknow.playpublisher.util.ApkPublisherUtil
import org.gradle.api.tasks.TaskAction

class PlayPublishTask extends BasePlayTask {

    ApplicationVariant variant

    @TaskAction
    def publish() {
        init()

        ApkPublisherUtil.publishVariant(publisher, variant, extension.track)
    }

}
