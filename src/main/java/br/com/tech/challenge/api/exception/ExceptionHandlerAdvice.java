package br.com.tech.challenge.api.exception;

import br.com.tech.challenge.domain.dto.ResponseExceptionDTO;
import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Generated
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseExceptionDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var errors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        return ResponseEntity.badRequest().body(
                ResponseExceptionDTO.builder()
                        .exceptionMessage(exception.getMessage())
                        .messages(errors)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ResponseExceptionDTO> handleObjectNotFoundException(ObjectNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseExceptionDTO.builder()
                                .exceptionMessage(exception.getMessage())
                                .messages(null)
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .build()
        );
    }


    @ExceptionHandler(StatusPedidoInvalidoException.class)
    public ResponseEntity<ResponseExceptionDTO> handleStatusPedidoInvalidoException(StatusPedidoInvalidoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseExceptionDTO.builder()
                        .exceptionMessage(exception.getMessage())
                        .messages(null)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build());
    }

    @ExceptionHandler(MercadoPagoAPIException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ResponseExceptionDTO> handleMercadoPagoAPIException(MercadoPagoAPIException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseExceptionDTO.builder()
                        .exceptionMessage(exception.getMessage())
                        .messages(null)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

}
