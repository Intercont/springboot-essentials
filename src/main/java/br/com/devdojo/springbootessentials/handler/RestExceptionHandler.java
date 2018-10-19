package br.com.devdojo.springbootessentials.handler;

import br.com.devdojo.springbootessentials.error.ResourceNotFoundDetails;
import br.com.devdojo.springbootessentials.error.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

/**
 * Classe para customização das exceptions retornadas as chamadas na API
 */

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class) //sempre que cair nesta excessão, o Spring vai lançar este Handler
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rfnException){
        ResourceNotFoundDetails rnfDetails = ResourceNotFoundDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Resouce Not Found")
                .detail(rfnException.getMessage())
                .developerMessage(rfnException.getClass().getName())
                .build();

        return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
    }


}
