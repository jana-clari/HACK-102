package extractor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostgresConnector
{
    private Connection c = null;
    {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/clarius_bootstrap_dev",
                                   "datamart", "datamart");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void createTable() throws SQLException
    {
        c.createStatement().execute("CREATE TABLE IF NOT EXISTS FILES (ID VARCHAR PRIMARY KEY, VERSION INT NOT NULL, STATUS VARCHAR NOT NULL)");
    }
    
    List<GFile> getAllFiles()
    {
        try {
            ResultSet resultSet = c.createStatement().executeQuery("SELECT * FROM FILES");
            return extractFiles(resultSet);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private List<GFile> extractFiles(ResultSet resultSet) throws SQLException
    {
        List<GFile> result = new ArrayList<>();
        while(resultSet.next()){
            result.add(new GFile(resultSet.getString("ID"), resultSet.getInt("VERSION"), FileStatus.valueOf(resultSet.getString("STATUS"))));
        }
        return result;
    }
    
    public int[] upsertFiles(List<GFile> files)
    {
        try {
            Statement statement = c.createStatement();
            for (GFile file : files) {
                String sql = "INSERT INTO FILES VALUES ('" + file.getId() + "', " + file.getVersion() + ", '"+FileStatus.CREATED.name()+"') ON CONFLICT (ID) DO UPDATE SET VERSION = " + file.getVersion() +", STATUS = '"+FileStatus.MODIFIED.name()+"'";
                statement.addBatch(sql);
            }
            return statement.executeBatch();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
