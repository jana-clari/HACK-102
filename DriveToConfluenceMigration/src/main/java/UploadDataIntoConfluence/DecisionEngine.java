package UploadDataIntoConfluence;

import FetchDataFromGoogleDrive.Helper;
import FetchDataFromGoogleDrive.googleDrive;
import com.google.api.services.docs.v1.model.Document;
import extractor.FileStatus;
import extractor.GFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class DecisionEngine {
    public static void CreateOrUpdate(List<GFile> Files){
        try {
            for (int i = 0; i < (long) Files.size(); i++) {
                GFile file = Files.get(i);
                Document doc = googleDrive.getStringDataFromGoogleDoc(file.getId());
                if (file.getStatus() == FileStatus.CREATED) {
                    Create(doc.getTitle(), Helper.htmlToXhtml(googleDrive.getContentAsHTML(file.getId()).toString()));
                } else {
                    Update(doc.getDocumentId(), doc.getTitle(), Helper.htmlToXhtml(googleDrive.getContentAsHTML(file.getId()).toString()));
                }

            }
        }catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    private static void Create(String pageTitle, String pageBody) {
        ConfluenceRestAPI confluenceRestAPI = new ConfluenceRestAPI();
        confluenceRestAPI.setPageAtrributes(pageTitle, pageBody);
        confluenceRestAPI.publishConfluencePage();
    }

    private static void Update(String Id, String pageTitle, String pageBody){

    }
}

