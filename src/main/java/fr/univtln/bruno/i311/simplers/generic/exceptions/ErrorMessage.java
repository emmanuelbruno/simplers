package fr.univtln.bruno.i311.simplers.generic.exceptions;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univtln.bruno.i311.simplers.AppConstants;
import fr.univtln.bruno.i311.simplers.generic.ws.exceptions.AppException;
import lombok.*;
import org.apache.commons.beanutils.BeanUtils;

@Builder
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)

public class ErrorMessage {

    private String link;
    /** contains the same HTTP Status code returned by the server */
    private int status;

    /** application specific error code */
    private AppConstants.ErrorCode code;

    /** message describing the error*/
    private String message;

    private Map<String,String> messageDetail;

    public ErrorMessage(AppException ex){
        try {
            BeanUtils.copyProperties(this, ex);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ErrorMessage(NotFoundException ex){
        this.status = Response.Status.NOT_FOUND.getStatusCode();
        this.message = ex.getMessage();
        this.link = "https://jersey.java.net/apidocs/2.8/jersey/javax/ws/rs/NotFoundException.html";
    }
}
