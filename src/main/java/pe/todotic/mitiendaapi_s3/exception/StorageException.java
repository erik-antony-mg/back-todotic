package pe.todotic.mitiendaapi_s3.exception;


public class StorageException extends RuntimeException{
    public StorageException(String message) {
        super(message);
    }
}
