package com.gfabrego.gradle_dropbox_plugin

import com.dropbox.core.DbxException
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import org.gradle.api.Plugin
import org.gradle.api.Project

class DropboxPlugin implements Plugin<Project> {

    private static final String EXTENSION_KEY = "dropboxUpload"
    private static final String TASK_GROUP = "dropbox"
    private static final String BASE_TASK_BASE_NAME = "package"
    private static final String NEW_TASK_BASE_NAME = "dropboxUpload"
    private static final String ANDROID_PROPERTY = "android"

    @Override
    void apply(Project project) {
        def extension = project.extensions.create(EXTENSION_KEY, DropboxPluginExtension, project)

        project.configure(project) {
            if (it.hasProperty("${ANDROID_PROPERTY}")) {
                tasks.whenTaskAdded {
                    task ->
                        project.("${ANDROID_PROPERTY}").applicationVariants.all {
                            variant ->
                                def expectingTask = "${BASE_TASK_BASE_NAME}${variant.name.capitalize()}".toString()
                                if (expectingTask.equals(task.name)) {
                                    def newTaskName = "${NEW_TASK_BASE_NAME}${variant.name.capitalize()}"
                                    project.task(newTaskName) << {
                                        String apkFile = task.outputFile.toString()
                                        uploadApk(apkFile, project, extension)
                                    }

                                    project.(newTaskName.toString()).dependsOn(expectingTask)
                                    project.(newTaskName.toString()).group = TASK_GROUP
                                    project.(newTaskName.toString()).description =
                                            "Uploads ${variant.name.capitalize()} build to Dropbox"
                                }
                        }
                }
            }
        }
    }

    /**
     * Uploads an apk to google drive
     * @param apkFile url of the apk file
     * @param extension
     */
    private void uploadApk(String apkFile, Project project, DropboxPluginExtension extension) {
        project.logger.debug("Uploading ${apkFile} file to Dropbox")
        DbxRequestConfig config = new DbxRequestConfig("GradlePlugin/v0.1.0", Locale.getDefault().toLanguageTag())
        DbxClientV2 client = new DbxClientV2(config, extension.accessToken)
        String fileName = new File(apkFile).getName()

        try {
            InputStream stream = new FileInputStream(apkFile)
            client.files()
                    .uploadBuilder("/${extension.appFolder}/${fileName}")
                    .uploadAndFinish(stream)

        } catch (DbxException ex) {
            project.logger.error("Error making API call: ${ex.message}")
            System.exit(-1)
        }
    }
}