package me.smbduknow.playpublisher.util

import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import com.google.api.client.http.FileContent
import com.google.api.services.androidpublisher.AndroidPublisher
import com.google.api.services.androidpublisher.model.Apk
import com.google.api.services.androidpublisher.model.Track

class ApkPublisherUtil {

    private static final MIME_TYPE_APK = 'application/vnd.android.package-archive'
    private static final MIME_TYPE_MAPPING = 'application/octet-stream'

    static publishVariant(AndroidPublisher publisher, ApplicationVariant variant, String extTrack) {
        def packageName = variant.applicationId
        def edits = publisher.edits()

        def editId = edits.insert(packageName, null).execute().getId()

        def outputs = variant.outputs
                .findAll { it instanceof ApkVariantOutput }
                .collect { it as ApkVariantOutput }

        def versionCodes = outputs
                .collect { publishApk(edits, editId, packageName, it.outputFile, variant.mappingFile) }
                .collect { apk -> apk.getVersionCode() }

        def track = new Track().setVersionCodes(versionCodes)

        edits.tracks()
                .update(packageName, editId, extTrack, track)
                .execute()

        edits.commit(packageName, editId).execute()
    }

    static Apk publishApk(
            AndroidPublisher.Edits edits,
            String editId,
            String packageName,
            File apkFile,
            File mappingFile
    ) {

        // Upload apk file
        def apkStream = new FileContent(MIME_TYPE_APK, apkFile)
        def apk = edits.apks()
                .upload(packageName, editId, apkStream)
                .execute()

        // Upload Proguard mapping.txt if available
        if (mappingFile?.exists()) {
            def fileStream = new FileContent(MIME_TYPE_MAPPING, mappingFile)
            edits.deobfuscationfiles()
                    .upload(packageName, editId, apk.getVersionCode(), 'proguard', fileStream)
                    .execute()
        }

        return apk
    }
}
