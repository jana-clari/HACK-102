package extractor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Extractor
{
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws SQLException
    {
        PostgresConnector postgresConnector = new PostgresConnector();
        postgresConnector.createTable();
        FileFetcher fileFetcher = new FileFetcher(postgresConnector);
//        fileFetcher.run();
        scheduler.scheduleAtFixedRate(fileFetcher, 0, 15, TimeUnit.SECONDS);
    }
}
