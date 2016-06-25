package com.gfabrego.gradle_dropbox_plugin

import org.gradle.api.Project

class DropboxPluginExtension {
    private String accessToken
    private String appFolder

    DropboxPluginExtension(Project project) {}

    String getAccessToken() {
        return accessToken
    }

    void setAccessToken(String accessToken) {
        this.accessToken = accessToken
    }

    String getAppFolder() {
        return appFolder
    }

    void setAppFolder(String appFolder) {
        this.appFolder = appFolder
    }
}