package bean;

import java.util.ArrayList;
import java.util.List;

public class Directory {

    private String name;
    private String path;
    private int count;
    private final List<File> fileList;
    private final List<Directory> directoryList;

    public Directory() {
        this.fileList = new ArrayList<>();
        this.directoryList = new ArrayList<>();
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public List<Directory> getDirectoryList() {
        return directoryList;
    }

    @Override
    public String toString() {
        return "Directory{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", count=" + count +
                ", fileList=" + fileList +
                ", directoryList=" + directoryList +
                '}';
    }
}
