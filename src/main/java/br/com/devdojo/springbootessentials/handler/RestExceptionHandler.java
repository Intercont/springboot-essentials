package br.com.devdojo.springbootessentials.handler;

import br.com.devdojo.springbootessentials.error.ErrorDetails;
import br.com.devdojo.springbootessentials.error.ResourceNotFoundDetails;
import br.com.devdojo.springbootessentials.error.ResourceNotFoundException;
import br.com.devdojo.springbootessentials.error.ValidationErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe para customização das exceptions retornadas as chamadas na API
 */

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    //sempre que cair nesta excessão, o Spring vai lançar este Handler
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rfnException) {
        ResourceNotFoundDetails rnfDetails = ResourceNotFoundDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Resouce Not Found Custom by Igor")
                .detail(rfnException.getMessage())
                .developerMessage(rfnException.getClass().getName())
                .build();

        return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
    }

    //sempre que cair nesta excessão, o Spring vai lançar este Handler
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException manvException){
    /*quando estavamos sem extender de ResponseEntityExceptionHandler marcavamos o handler com a assinatura acima e
    a anotação de @ExceptionHandler. Agora que estamos sobrescrevendo o método do pai, precisamos especificar
    o Override e não usar o @ExceptionHandler, do contrário teremos um erro de Ambiguedade (duplicidade)*/
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manvException, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<FieldError> fieldErrors = manvException.getBindingResult().getFieldErrors();

        //vou coletar o campo Field aonde o erro aconteceu e o campo DefaultMessage que indica o que deve ser feito para a chamada ser válida
        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
        String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));

        ValidationErrorDetails manvDetails = ValidationErrorDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Invalid Field Value")
//                .detail(manvException.getMessage())
                .detail("Field Validation Error")
                .developerMessage(manvException.getClass().getName())
                .field(fields)
                .fieldMessage(fieldMessages)
                .build();

        return new ResponseEntity<>(manvDetails, HttpStatus.BAD_REQUEST);
    }

//    @Override
//    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException hmnrException, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        ErrorDetails errorDetails = ErrorDetails.Builder
//                .newBuilder()
//                .timestamp(new Date().getTime())
//                .status(HttpStatus.BAD_REQUEST.value())
//                .title("Message not Readable, wrong format, custom by Igor")
//                .detail(hmnrException.getMessage())
//                .developerMessage(hmnrException.getClass().getUsername())
//                .build();
//
//        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
//    }

    //handler de erro genérico, para todos os demais erros lançados
    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                          @Nullable Object body,
                                                          HttpHeaders headers,
                                                          HttpStatus status,
                                                          WebRequest request) {

        ErrorDetails errorDetails = ErrorDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(status.value()) //inserimos o status diretamente da requisição
                .title("Custom Internal Exception")
                .detail(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .build();

        return new ResponseEntity<>(errorDetails, headers, status);
    }
}