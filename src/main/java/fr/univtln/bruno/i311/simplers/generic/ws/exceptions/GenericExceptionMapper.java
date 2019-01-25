package fr.univtln.bruno.i311.simplers.generic.ws.exceptions;

import fr.univtln.bruno.i311.simplers.AppConstants;
import fr.univtln.bruno.i311.simplers.generic.exceptions.ErrorMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    public Response toResponse(Exception ex) {

        ErrorMessage errorMessage = new ErrorMessage();
        setHttpStatus(ex, errorMessage);
        errorMessage.setCode(AppConstants.ErrorCode.GENERIC_EXCEPTION);
        errorMessage.setMessage(ex.getMessage());

        return Response.status(errorMessage.getStatus())
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private void setHttpStatus(Throwable ex, ErrorMessage errorMessage) {
        if(ex instanceof WebApplicationException ) { //NICE way to combine both of methods, say it in the blog
            errorMessage.setStatus(((WebApplicationException)ex).getResponse().getStatus());
        } else {
            errorMessage.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()); //defaults to internal server error 500
        }
    }
}
