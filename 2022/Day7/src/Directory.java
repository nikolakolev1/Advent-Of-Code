import java.util.ArrayList;
import java.util.HashMap;

public class Directory {
    private String dirName;
    private Directory parentDir;
    private HashMap<String, Integer> files;
    private ArrayList<Directory> directories;
    private int filesSize;

    public Directory(String thisDirName, Directory parentDir) {
        dirName = thisDirName;
        this.parentDir = parentDir;

        files = new HashMap<>();
        directories = new ArrayList<>();
    }

    public void putFileInDir(String fileName, Integer thisFileSize) {
        files.put(fileName, thisFileSize);
        filesSize += thisFileSize;
    }

    public void putDirInDir(String childDirName) {
        directories.add(new Directory(childDirName, this));
    }

    public int getSize() {
        int directoriesSize = 0;
        for (Directory childDir : directories) {
            directoriesSize += childDir.getSize();
        }
        return (directoriesSize + filesSize);
    }

    public String getDirName() {
        return dirName;
    }

    public Directory getParentDir() {
        return parentDir;
    }

    public ArrayList<Directory> getDirectories() {
        return directories;
    }
}