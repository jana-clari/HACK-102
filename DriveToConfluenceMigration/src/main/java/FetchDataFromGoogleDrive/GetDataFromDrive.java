package FetchDataFromGoogleDrive;

import UploadDataIntoConfluence.ConfluenceRestAPI;
import com.google.api.services.drive.model.File;
import extractor.FileStatus;
import extractor.GFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import static FetchDataFromGoogleDrive.googleDrive.getAllFilesInDrive;
import static FetchDataFromGoogleDrive.googleDrive.getStringDataFromGoogleDoc;
import com.google.api.services.docs.v1.model.Document;

public class GetDataFromDrive {


    public static void main(String... args) throws IOException, GeneralSecurityException {

        List<File> files = getAllFilesInDrive().getFiles();
        int count = 0;
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            for (File file : files) {
                if (file.getMimeType().contains("document")) {
                    System.out.printf("%s (%s)\n", file.getName(), file.getId());
                    System.out.printf("%s (%s)\n", file.getCreatedTime(),file.getVersion());
                    count = count + 1;
                }
            }
        }
        System.out.println("Total number of documents available: " + count + "\n");

        Document doc=getStringDataFromGoogleDoc("1AxrgxrHBFukzV7bdD-OTi5HEtmovUga4YBVCo0UkiDg");
        ConfluenceRestAPI confluenceRestAPI=new ConfluenceRestAPI();
        String str = Helper.readStructuralElements(doc.getBody().getContent()).replaceAll("[^a-zA-Z0-9]", " ");
        confluenceRestAPI.setPageAtrributes(doc.getTitle(),str);
        confluenceRestAPI.publishConfluencePage();
    }

    public static List<GFile> getAllFiles(List<String> preList)
    {
        try {
            List<GFile> gFiles = new ArrayList<>();
            List<File> files = getAllFilesInDrive().getFiles();
            int count = 0;
            if (files == null || files.isEmpty()) {
//                System.out.println("No files found.");
            }
            else {
                for (File file : files) {
                    if (file.getMimeType().contains("document")) {
                        gFiles.add(new GFile(file.getId(), file.getVersion(), preList.contains(file.getId()) ? FileStatus.MODIFIED : FileStatus.CREATED));
//                        System.out.printf("%s (%s)\n", file.getName(), file.getId());
//                        System.out.printf("%s (%s)\n", file.getCreatedTime(), file.getVersion());
                        count = count + 1;
                    }
                }
            }
            return gFiles;
        } catch (Exception e){
            return new ArrayList<>();
        }
    }


}
