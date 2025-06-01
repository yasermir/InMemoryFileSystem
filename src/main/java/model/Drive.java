package model;

 public class Drive extends ContainerEntity{

    public Drive(String name){
        super(name,null);

    }
    @Override
    public String getType() {
        return EntityType.DRIVE;
    }
}
