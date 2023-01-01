package pe.todotic.mitiendaapi_s3.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.todotic.mitiendaapi_s3.exception.StorageException;
import pe.todotic.mitiendaapi_s3.web.dto.ErrorDetail;
import pe.todotic.mitiendaapi_s3.web.dto.ValidationError;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
public class RestExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ErrorDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException manve){
        ErrorDetail errorDetail = new ErrorDetail();
        // llenar los detalles del error
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle("Error de validación");
        errorDetail.setDetail("El formulario tiene algunos errores de validación");
        // crear y agregar los errores de validación
                List<FieldError> fieldErrors =manve.getBindingResult().getFieldErrors();
        for (FieldError fe : fieldErrors) {
            List<ValidationError> validationErrorList =
                    errorDetail.getErrors().get(fe.getField());
            if (validationErrorList == null) {
                validationErrorList = new ArrayList<>();
                errorDetail.getErrors().put(fe.getField(),
                        validationErrorList);
            }
            ValidationError validationError = new ValidationError();
            validationError.setCode(fe.getCode());
            validationError.setMessage(messageSource.getMessage(fe, Locale.getDefault()));
            validationErrorList.add(validationError);
        }
        return errorDetail;

    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    ErrorDetail handleEntityNotFoundException(){
        ErrorDetail errorDetail = new ErrorDetail();
        // llenar los detalles del error
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetail.setTitle("El recurso no existe");
        errorDetail.setDetail("El recurso al cual estas intentado acceder no ha sido encontrado");
        return errorDetail;
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(StorageException.class)
    ErrorDetail handleStorageException(){
        ErrorDetail errorDetail = new ErrorDetail();
        // llenar los detalles del error
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetail.setTitle("EROR INTERNO DEL SERVIDOR");
        return errorDetail;
    }
}
