package model;

public class TextFile extends FileSystemEntity {
    private String content;

    public TextFile(String name, FileSystemEntity parent) {
        super(name, parent);
        this.content = "";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getType() {
        return EntityType.TEXT_FILE;
    }
}
