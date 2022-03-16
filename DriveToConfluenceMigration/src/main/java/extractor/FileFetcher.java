package extractor;

import FetchDataFromGoogleDrive.GetDataFromDrive;
import UploadDataIntoConfluence.DecisionEngine;

import java.util.List;
import java.util.stream.Collectors;

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
        List<GFile> preList = postgresConnector.getAllFiles();
        List<GFile> fileList = GetDataFromDrive.getAllFiles(preList.stream().map(GFile::getId).collect(Collectors.toList()));
        System.out.println("Files fetched full ==> \n\n "+fileList.size()+"\n\n\n");
        fileList.removeAll(preList);
        System.out.println("Files newly fetched ==> \n\n "+fileList.size()+"\n\n\n");
        if(!fileList.isEmpty()){
            postgresConnector.upsertFiles(fileList);
            DecisionEngine.CreateOrUpdate(fileList);
        }
        System.out.println("=========================");
    }
}
