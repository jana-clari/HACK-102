package extractor;


public class GFile
{
    
    private final String id;
    private final long version;
    private final FileStatus status;
    
    public GFile(String id, long version, FileStatus status)
    {
        this.id = id;
        this.version = version;
        this.status = status;
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

    public FileStatus getStatus()
    {
        return status;
    }
}


