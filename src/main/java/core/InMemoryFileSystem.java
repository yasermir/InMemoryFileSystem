package core;

import exceptions.IllegalFileSystemOperationException;
import exceptions.NotATextFileException;
import exceptions.PathAlreadyExistsException;
import exceptions.PathNotFoundException;
import model.*;

import java.util.HashMap;
import java.util.Map;

import static model.EntityType.*;

public class InMemoryFileSystem {
    private Map<String, Drive> driveMap = new HashMap<>();

    /**+
     *
     * @param entityType - can be any of four types DRIVE, FOLDER, TEXT_FILE, ZIP_FILE
     * @param name - user entered custom name
     * @param parentPath - will be null for drive, for other entities will be container entity
     */
    public void create(String entityType, String name,String parentPath){

        if(entityType.equals(EntityType.DRIVE)) {
            if (driveMap.containsKey(entityType)) {
                throw new PathAlreadyExistsException("Drive with name " + name + " already exists");
            }
            driveMap.put(name, new Drive(name));
            return;
        }

        // For non-drive entities, parentPath must be valid.
        FileSystemEntity parent = resolvePath(parentPath);
        if (!(parent instanceof ContainerEntity))
            throw new IllegalFileSystemOperationException("File cannot contain children.");

        ContainerEntity container = (ContainerEntity) parent;
        if (container.getChild(name) != null)
            throw new PathAlreadyExistsException("Entity already exists.");

        FileSystemEntity newEntity;
        switch (entityType) {
            case FOLDER -> newEntity = new Folder(name, parent);
            case ZIP_FILE -> newEntity = new ZipFile(name, parent);
            case TEXT_FILE -> newEntity = new TextFile(name, parent);
            default -> throw new IllegalArgumentException("Unsupported entity type for creation: " + entityType);
        }

        container.addChild(newEntity);

    }

    /**+
     *
     * @param path - path of file entity to be deleted
     */
    public void delete(String path) {
        FileSystemEntity entity = resolvePath(path);
        if (entity instanceof Drive) {
            driveMap.remove(entity.getName());
        } else {
            ContainerEntity parent = (ContainerEntity) entity.getParent();
            parent.removeChild(entity.getName());
        }
    }

    /**
     *
     * @param path - path to text file
     * @param content - actual text to be written to file
     */
    public void writeToFile(String path, String content) {
        var entity = resolvePath(path);
        if (!(entity instanceof TextFile)) {
            throw new NotATextFileException(entity.getType() + " is Not a text file.");
        }
         ((TextFile) entity).setContent(content);
    }

    /**
     *
     * @param sourcePath - path of entity which needs to be moved
     * @param destPath - destination path where source entity will be copied
     */
    public void move(String sourcePath, String destPath) {
        FileSystemEntity sourceEntity = resolvePath(sourcePath);
        if (sourceEntity.getType().equals(EntityType.DRIVE)) {
            throw new IllegalFileSystemOperationException("A Drive cannot be moved: " + sourcePath);
        }
        FileSystemEntity destEntity = resolvePath(destPath);

        if (!(destEntity instanceof ContainerEntity))
            throw new IllegalFileSystemOperationException("File cannot contain children.");

        ContainerEntity sourceParent = (ContainerEntity) sourceEntity.getParent();
        ContainerEntity destination = (ContainerEntity) destEntity;

        if (destination.getChild(sourceEntity.getName()) != null)
            throw new PathAlreadyExistsException("Entity with the same name already exists at destination.");

        sourceParent.removeChild(sourceEntity.getName());
        sourceEntity.setParent(destination);
        destination.addChild(sourceEntity);
    }

    /**+
     *
     * @param path - full path that will be used to drive current entity in context
     * @return - entity in context from full path
     */
    public FileSystemEntity resolvePath(String path) {
        String[] parts = path.split("\\\\");
        if (parts.length == 0) throw new IllegalArgumentException("Invalid path.");

        Drive drive = driveMap.get(parts[0]);
        if (drive == null) throw new PathNotFoundException("Drive not found.");

        FileSystemEntity current = drive;
        for (int i = 1; i < parts.length; i++) {
            if (!(current instanceof ContainerEntity))
                throw new IllegalArgumentException("Path resolution failed.");
            current = ((ContainerEntity) current).getChild(parts[i]);
            if (current == null)
                throw new PathNotFoundException("Path not found.");
        }

        return current;
    }
}
