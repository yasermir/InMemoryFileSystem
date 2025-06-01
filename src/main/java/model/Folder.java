package model;

public class Folder extends ContainerEntity{


    public Folder(String name, FileSystemEntity parent) {
        super(name, parent);
    }

    @Override
    public String getType() {
        return EntityType.FOLDER;
    }

}
