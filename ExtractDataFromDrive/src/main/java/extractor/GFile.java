package extractor;

import java.util.Comparator;

public class GFile
{
    private final String id;
    private final long version;
    
    public GFile(String id, long version)
    {
        this.id = id;
        this.version = version;
    }

    @Override
    public String toString()
    {
        return "GFile{" +
                "id='" + id + '\'' +
                ", version=" + version +
                '}';
    }

    public String getId()
    {
        return id;
    }

    public long getVersion()
    {
        return version;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof GFile){
            GFile gFile = (GFile) obj;
            return this.getId().equals(gFile.getId()) && this.getVersion() == gFile.getVersion();
        }
        return false;
    }
}
