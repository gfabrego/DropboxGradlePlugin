# DropboxGradlePlugin
Sample repository with a Gradle Plugin which allows to upload apk files to Dropbox http://blog.gfabrego.com/2016/06/creating-gradle-plugins-for-android.html

If you want to try, you should: 

1. Create a Dropbox application https://www.dropbox.com/developers

2. Run publish to local maven task in gradle-dropbox-plugin module. You will need to initially comment plugin's use in app's build.gradle and in main build.gradle (classpath) so the gradle sync can be successfully executed.

3. Set the access token in app's build.gradle file. Also app's folder may be configured. 

4. Run one of the generated DropboxUpload tasks

