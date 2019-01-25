package fr.univtln.bruno.i311.simplers.generic.ws.exceptions;

import fr.univtln.bruno.i311.simplers.generic.exceptions.ErrorMessage;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    public Response toResponse(javax.validation.ConstraintViolationException cex) {
        ErrorMessage error = new ErrorMessage();
        error.setMessage("Constraint violation.");
        error.setMessageDetail(cex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(p->p.getPropertyPath().toString(), ConstraintViolation::getMessage)));

        return Response.status(400)
                .entity(error)
                .type(MediaType.APPLICATION_JSON).
                        build();

    }
}
