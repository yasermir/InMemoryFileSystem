package core;

import exceptions.NotATextFileException;
import exceptions.PathAlreadyExistsException;
import exceptions.PathNotFoundException;
import model.EntityType;

public class TestClass {
    public static void main(String[] args) {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        fs.create(EntityType.DRIVE,"C",null);
        fs.create(EntityType.DRIVE,"D",null);
        fs.create(EntityType.FOLDER,"Software","C");
        fs.create(EntityType.TEXT_FILE,"notes.txt","C\\Software");
        fs.create(EntityType.TEXT_FILE,"notes2.txt","C\\Software");

        fs.writeToFile("C\\Software\\notes.txt", "Hello, FS!");
        fs.writeToFile("C\\Software\\notes2.txt", "Hello, FS2!");

        // move operation
        fs.move("C\\Software\\notes2.txt","D");

        fs.delete("C\\Software");

        System.out.println("Test1 : Attempting to create file in non-existent path");
        try {
            fs.create(EntityType.TEXT_FILE, "error.txt", "C\\NonExistentFolder");
        } catch (PathNotFoundException e) {
            System.err.println("Caught expected exception: " + e.getMessage());
        }

        System.out.println("Test1 : Attempting to create duplicate name ");
        try {
            fs.create(EntityType.FOLDER, "Software", "C");
        } catch (PathAlreadyExistsException e) {
            System.err.println("Caught expected exception: " + e.getMessage());
        }
        System.out.println("Test3 : Attempting to write to a folder");
        try {
            fs.writeToFile("C\\Software", "some content");
        } catch (NotATextFileException e) {
            System.err.println("Caught expected exception: " + e.getMessage());
        }
    }
}
