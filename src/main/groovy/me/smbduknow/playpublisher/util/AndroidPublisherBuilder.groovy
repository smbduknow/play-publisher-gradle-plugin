package me.smbduknow.playpublisher.util

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.androidpublisher.AndroidPublisher
import com.google.api.services.androidpublisher.AndroidPublisherScopes

import java.nio.file.Files
import java.nio.file.Paths
import java.security.GeneralSecurityException

class AndroidPublisherBuilder {

    private static final APPLICATION_NAME = 'smbduknow/PlayStore-Gradle-Plugin'

    private static final JSON_FACTORY = JacksonFactory.getDefaultInstance()
    private static HttpTransport httpTransport

    private File jsonFile

    def AndroidPublisher build() throws IOException, GeneralSecurityException {
        if (httpTransport == null) {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        }

        // Build service account credential.
        def credential = buildCredential(jsonFile)

        // Set up and return API client.
        return new AndroidPublisher.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    def AndroidPublisherBuilder setJsonFile(File jsonFile) {
        this.jsonFile = jsonFile
        return this
    }


    private static Credential buildCredential(File jsonFile) throws IOException {
        def path = Paths.get(jsonFile.absolutePath)
        def serviceAccountStream = new ByteArrayInputStream(Files.readAllBytes(path))
        return GoogleCredential
                .fromStream(serviceAccountStream, httpTransport, JSON_FACTORY)
                .createScoped(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER))
    }
}
