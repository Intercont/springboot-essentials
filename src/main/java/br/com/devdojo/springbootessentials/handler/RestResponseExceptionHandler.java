package br.com.devdojo.springbootessentials.handler;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

/**
 * Classe para tratamento de exceções quando chamados os Endpoints da API programáticamente
 */
@ControllerAdvice
public class RestResponseExceptionHandler extends DefaultResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        System.out.println("Entrou hasError");
        return super.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        System.out.println("Entrou handleError, Status Code: " + response.getStatusCode());
        System.out.println("Entrou handleError, Body: " + IOUtils.toString(response.getBody(), "UTF-8"));
        super.handleError(response);
    }
}
