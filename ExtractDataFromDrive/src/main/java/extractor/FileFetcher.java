package extractor;

import FetchDataFromGoogleDrive.GetDataFromDrive;

import java.util.List;

public class FileFetcher implements Runnable
{
    private final PostgresConnector postgresConnector;

    public FileFetcher(PostgresConnector postgresConnector)
    {
        this.postgresConnector = postgresConnector;
    }

    @Override
    public void run()
    {
        List<GFile> fileList = GetDataFromDrive.getAllFiles();
        System.out.println("Files fetched full ==> \n\n "+fileList.size()+"\n\n\n");
        fileList.removeAll(postgresConnector.getAllFiles());
        System.out.println("Files newly fetched ==> \n\n "+fileList.size()+"\n\n\n");
        if(!fileList.isEmpty()){
            postgresConnector.upsertFiles(fileList);
            //TODO trigger Hasshals code
        }
        System.out.println("=========================");
    }
}
