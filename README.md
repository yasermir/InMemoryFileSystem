**The assignment is to design and implement an in-memory file system. This file-system consists of 4 types of entities: Drives, Folders, Text files, Zip files.**

These entities, very much like their “real” file-system counterparts, obey the following relations.
a.	A folder, a drive or a zip file, may contain zero to many other folders, or files (text or zip).
b.	A text file does not contain any other entity.
c.	A drive is not contained in any entity.
d.	Any non-drive entity must be contained in another entity.
If entity A contains entity B then we say that A is the parent of B.

Every entity has the following properties:
•	Type – The type of the entity (one of the 4 type above).
•	Name - An alphanumeric string. Two entities with the same parent cannot have the same name. Similarly, two drives cannot have the same name.
•	Path – The concatenation of the names of the containing entities, from the drive down to and including the entity. The names are separated by ‘\’.
•	A text file has a property called Content which is a string. 

The system should be capable of supporting file-system like operations

1)	Create – Creates a new entity.
Arguments: Type, Name, Path of parent.
Exceptions: Path not found; Path already exists; Illegal File System Operation (if any of the rules a-d above is violated).
2)	Delete – Deletes an existing entity (and all the entities it contains).
Arguments: Path
Exceptions: Path not found.
3)	Move – Changing the parent of an entity.
Arguments: Source Path, Destination Path. 
Exceptions: Path not found; Path already exists, Illegal File System Operation.
4)	WriteToFile – Changes the content of a text file.
Arguments: Path, Content
Exceptions: Path not found; Not a text file.

*HIGH LEVEL DESIGN *

CLASS DIAGRAM

![image](https://github.com/user-attachments/assets/89021f97-fb12-492a-89a8-d3a716abfff9)


CORE ENTITIES
1. FileSystemEntity (Abstract Base Class)

Properties:
  name: String
  parent: ContainerEntity (null for Drives)
Abstract Methods:
getType(): Returns an  value (DRIVE, FOLDER, TEXT_FILE, ZIP_FILE)

2. ContainerEntity (Abstract Class, inherits from FileSystemEntity)

This class can be a parent for Drives, Folders, and ZipFiles as they can contain other entities.
Properties:
  children: A  map where the key is the child's name (String) and the value is the FileSystemEntity object. (e.g., Map<String, FileSystemEntity>)
Methods:
 addChild(entity: FileSystemEntity)
 removeChild(name: String)
 getChild(name: String): FileSystemEntity

3. Drive (Inherits from ContainerEntity)
Properties:
(Inherits name, children from ContainerEntity. parent will be null.)
Methods:
 getType(): Returns DRIVE.
 getPath(): Returns name (since it's the root).
 
4. Folder (Inherits from ContainerEntity)
Properties:
  (Inherits name, parent, children)
Methods:
  getType(): Returns FOLDER.

5. TextFile (Inherits from FileSystemEntity)
Properties:
  (Inherits name, parent)
  content: String
Methods:
  getType(): Returns TEXT_FILE.

6. ZipFile (Inherits from ContainerEntity)
Properties:
  (Inherits name, parent, children)
Methods:
  getType(): Returns ZIP_FILE.

File System Manager Class
You'll need a main class to manage the drives and orchestrate operations.

InMemoryFileSystem - Main class to manage the drive and orchestrate operations
  
Properties:

drives: A dictionary or map where the key is the drive name (String) and the value is the Drive object (e.g., Map<String, Drive>).

Key operations:
1. create(type: EntityType, name: String, parentPath: String):
   If type is DRIVE:
  parentPath should ideally be empty/null.
  Check if a drive with name already exists in this.drives. If yes, throw "Path already exists" (as drive names must be unique).
  Create the new Drive object.
  Add it to this.drives.
  If type is not DRIVE:
  Use resolvePath(parentPath) to find the parentEntity.
  If parentEntity is not found, throw "Path not found".
  If parentEntity is a TextFile, throw "Illegal File System Operation" (Rule b violation: Text file cannot contain other entities).
  Check if parentEntity.children already contains an entity with name. If yes, throw "Path already exists".
2. move(sourcePath: String, destinationParentPath: String):
3. delete(path: String):
   
4. writeToFile(path: String, content: String):
   Use resolvePath(path) to find targetEntity.
  If not found, throw "Path not found".
  If targetEntity.getType() is not TEXT_FILE, throw "Not a text file".
  Cast targetEntity to TextFile.
  Set targetEntity.content = content.
