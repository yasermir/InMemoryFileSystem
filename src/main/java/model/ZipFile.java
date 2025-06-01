package model;

public class ZipFile extends ContainerEntity{


    public ZipFile(String name, FileSystemEntity parent) {
        super(name, parent);
    }

    @Override
    public String getType() {
        return EntityType.ZIP_FILE;
    }
}
