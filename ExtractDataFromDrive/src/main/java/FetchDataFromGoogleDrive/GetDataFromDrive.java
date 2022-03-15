package FetchDataFromGoogleDrive;

import com.google.api.services.drive.model.File;
import extractor.GFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import static FetchDataFromGoogleDrive.googleDrive.getAllFilesInDrive;
import static FetchDataFromGoogleDrive.googleDrive.getStringDataFromGoogleDoc;

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

        getStringDataFromGoogleDoc("1AxrgxrHBFukzV7bdD-OTi5HEtmovUga4YBVCo0UkiDg");
    }

    public static List<GFile> getAllFiles()
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
                        gFiles.add(new GFile(file.getId(), file.getVersion()));
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
