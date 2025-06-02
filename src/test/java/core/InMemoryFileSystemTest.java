package core;

import exceptions.NotATextFileException;
import exceptions.PathAlreadyExistsException;
import exceptions.PathNotFoundException;
import model.EntityType;
import model.FileSystemEntity;
import model.TextFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryFileSystemTest {
    private InMemoryFileSystem inMemoryFileSystem;

    @BeforeEach
    void setUp() {
        inMemoryFileSystem = new InMemoryFileSystem();
        inMemoryFileSystem.create("DRIVE", "C", null);
    }

    @Test
    public void testCreateFolder() {
        inMemoryFileSystem.create("FOLDER", "Documents", "C");
        FileSystemEntity fileSystemEntity = inMemoryFileSystem.resolvePath("C\\Documents");
        assertNotNull(fileSystemEntity);
        assertEquals(EntityType.FOLDER, fileSystemEntity.getType());
        assertEquals("Documents", fileSystemEntity.getName());
    }

    @Test
    public void testCreateFile() {
        inMemoryFileSystem.create("FOLDER", "Documents", "C");
        inMemoryFileSystem.create("TEXT_FILE", "notes.txt", "C\\Documents");
        FileSystemEntity fileSystemEntity = inMemoryFileSystem.resolvePath("C\\Documents\\notes.txt");
        assertNotNull(fileSystemEntity);
        assertEquals(EntityType.TEXT_FILE, fileSystemEntity.getType());
        assertEquals("notes.txt", fileSystemEntity.getName());
    }

    @Test
    public void testWriteToFile() {
        inMemoryFileSystem.create("FOLDER", "Documents", "C");
        inMemoryFileSystem.create("TEXT_FILE", "notes.txt", "C\\Documents");
        inMemoryFileSystem.writeToFile("C\\Documents\\notes.txt", "Dummy Note");
        TextFile testFileEntity = (TextFile) inMemoryFileSystem.resolvePath("C\\Documents\\notes.txt");
        assertNotNull(testFileEntity);
        assertEquals(EntityType.TEXT_FILE, testFileEntity.getType());
        assertEquals("notes.txt", testFileEntity.getName());
        assertEquals("Dummy Note", testFileEntity.getContent());
    }

    @Test
    public void testDeleteFile() {
        inMemoryFileSystem.create("FOLDER", "Documents", "C");
        inMemoryFileSystem.create("TEXT_FILE", "notes.txt", "C\\Documents");
        inMemoryFileSystem.delete("C\\Documents\\notes.txt");
        assertThrows(PathNotFoundException.class, () -> {
            inMemoryFileSystem.resolvePath("C\\Documents\\notes.txt");
        });
    }
    @Test
    public void testMoveFile() {
        inMemoryFileSystem.create("DRIVE", "D", null);
        inMemoryFileSystem.create("FOLDER", "Documents", "C");
        inMemoryFileSystem.create("FOLDER", "Files", "D");
        inMemoryFileSystem.create("TEXT_FILE", "notes.txt", "C\\Documents");
        inMemoryFileSystem.move("C\\Documents\\notes.txt", "D\\Files");
        // if move is success we should be able to get file from destination path
        FileSystemEntity entity = inMemoryFileSystem.resolvePath("D\\Files\\notes.txt");
        assertNotNull(entity);
        assertEquals(EntityType.TEXT_FILE, entity.getType());
        assertEquals("Files", entity.getParent().getName());
    }

    @Test
    public void testPathNotFound() {
        inMemoryFileSystem.create("FOLDER", "Documents", "C");
        assertThrows(PathNotFoundException.class, () -> {
            inMemoryFileSystem.resolvePath("C\\FalseDocumemntPath");
        });
    }

    @Test
    public void testPathAlreadyExists() {
        inMemoryFileSystem.create("FOLDER", "Documents", "C");
        assertThrows(PathAlreadyExistsException.class,() -> {inMemoryFileSystem.create("FOLDER", "Documents", "C");
    });
    }

    @Test
    public void testNotATestFile(){
        inMemoryFileSystem.create("FOLDER", "Documents", "C");
        assertThrows(NotATextFileException.class, () -> {inMemoryFileSystem.writeToFile("C\\Documents", "Dummy Note");
    });
    }
}
