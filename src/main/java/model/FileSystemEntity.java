package model;

public abstract class FileSystemEntity {
    protected String name;
    protected String path;
    protected FileSystemEntity parent;
    public FileSystemEntity(String name, FileSystemEntity parent) {
        this.name = name;
        //this.path = path;
        this.parent = parent;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        if (parent == null) return name; // Drive
        return parent.getPath() + "\\" + name;
    }
    public abstract String getType();

    public FileSystemEntity getParent() {
        return parent;
    }
    public void setParent(FileSystemEntity parent) {
        this.parent = parent;
    }
}
