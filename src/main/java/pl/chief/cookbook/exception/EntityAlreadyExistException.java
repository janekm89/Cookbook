package pl.chief.cookbook.exception;

public class EntityAlreadyExistException extends RuntimeException {
    public EntityAlreadyExistException(){
        super("Entity already exists in DB");
    }

    public EntityAlreadyExistException(String name){
        super("Entity with name " + name + "already exists in DB");
    }
}
