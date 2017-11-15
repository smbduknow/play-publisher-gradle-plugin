package me.smbduknow.playpublisher

import org.gradle.api.Project

class PlayPublishExtension {

    public static final String NAME = "playConfig"

    File jsonKeyFile

    private String track = 'alpha'

    void setTrack(String track) {
        if (!(track in ['alpha', 'beta', 'production'])) {
            throw new IllegalArgumentException('Track has to be one of \'alpha\', \'beta\' or \'production\'.')
        }
        this.track = track
    }

    def getTrack() {
        return track
    }

    static PlayPublishExtension from(Project project) {
        return project.property(NAME) as PlayPublishExtension
    }

}
