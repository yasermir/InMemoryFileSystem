package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class ContainerEntity extends FileSystemEntity {
    protected Map<String, FileSystemEntity> children = new HashMap<>();

    public ContainerEntity(String name, FileSystemEntity parent) {
        super(name, parent);
    }

    public void addChild(FileSystemEntity entity) {
        if (children.containsKey(entity.getName()))
            throw new IllegalArgumentException("Name already exists in parent.");
        children.put(entity.getName(), entity);
    }

    public void removeChild(String name) {
        children.remove(name);
    }

    public FileSystemEntity getChild(String name) {
        return children.get(name);
    }

    public Collection<FileSystemEntity> getChildren() {
        return children.values();
    }
}
