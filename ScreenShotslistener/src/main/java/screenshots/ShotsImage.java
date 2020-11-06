package screenshots;

public class ShotsImage {

    private String name;
    private String path;
    private long size;
    private long date;


    public ShotsImage(String name, String path, long size, long date) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
