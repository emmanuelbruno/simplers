package fr.univtln.bruno.i311.simplers.generic.ws.exceptions;

import fr.univtln.bruno.i311.simplers.generic.exceptions.ErrorMessage;
import fr.univtln.bruno.i311.simplers.generic.ws.exceptions.AppException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class AppExceptionMapper implements ExceptionMapper<AppException> {

    public Response toResponse(AppException ex) {
        return Response.status(ex.getStatus())
                .entity(new ErrorMessage(ex))
                .type(MediaType.APPLICATION_JSON).
                        build();
    }
}
