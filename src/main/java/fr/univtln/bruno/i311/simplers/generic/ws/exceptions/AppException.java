package fr.univtln.bruno.i311.simplers.generic.ws.exceptions;

import lombok.Data;

import javax.json.bind.annotation.JsonbTransient;
import java.net.URL;

@Data
public class AppException extends Exception {

    /**
     * contains redundantly the HTTP status of the response sent back to the client in case of error, so that
     * the developer does not have to look into the response headers. If null a default
     */
    private int status;

    /** application specific error code */
    private int code;

    /** link documenting the exception */
    private URL link;

    @Override
    @JsonbTransient
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    @Override
    @JsonbTransient
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    @JsonbTransient
    public synchronized Throwable getCause() {
        return super.getCause();
    }


}
