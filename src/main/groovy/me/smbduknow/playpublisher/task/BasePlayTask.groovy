package me.smbduknow.playpublisher.task

import com.google.api.services.androidpublisher.AndroidPublisher
import me.smbduknow.playpublisher.PlayPublishExtension
import me.smbduknow.playpublisher.util.AndroidPublisherBuilder
import org.gradle.api.DefaultTask

class BasePlayTask extends DefaultTask {

    PlayPublishExtension extension

    AndroidPublisher publisher

    def init() {

        // Init extension
        PlayPublishExtension extension = PlayPublishExtension.from(project)

        // Init publisher
        if (publisher == null) {
            publisher = new AndroidPublisherBuilder()
                    .setJsonFile(extension.jsonKeyFile)
                    .build()
        }

    }
}
