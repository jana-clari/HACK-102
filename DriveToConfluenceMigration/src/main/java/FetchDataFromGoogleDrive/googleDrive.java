package FetchDataFromGoogleDrive;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.Document;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static FetchDataFromGoogleDrive.Authentication.JSON_FACTORY;
import static FetchDataFromGoogleDrive.Authentication.getCredentials;

public class googleDrive {
    private static final String APPLICATION_NAME = "FetchDataFromDrive";

    public static Document getStringDataFromGoogleDoc(String fileId) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Docs service1 =
                new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();

        Document doc = service1.documents().get(fileId).execute();
        System.out.println(doc.getTitle());
        System.out.println(Helper.readStructuralElements(doc.getBody().getContent()));
        return doc;
    }

    public static FileList getAllFilesInDrive() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();


        FileList result = service.files().list()
                .setPageSize(1000)
                .setFields("nextPageToken, files(id, name, mimeType,createdTime,fileExtension,exportLinks,driveId,description,version)")
                .execute();
        return result;
    }
}
