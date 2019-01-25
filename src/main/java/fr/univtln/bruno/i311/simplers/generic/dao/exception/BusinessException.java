package fr.univtln.bruno.i311.simplers.generic.dao.exception;

import fr.univtln.bruno.i311.simplers.AppConstants;

import javax.json.bind.annotation.JsonbTransient;
import javax.ws.rs.core.Response;

/**
 * Created by bruno on 15/02/15.
 */
public class BusinessException extends Exception {
    /**
     * contains redundantly the HTTP status of the response sent back to the client in case of error, so that
     * the developer does not have to look into the response headers. If null a default
     */
    private Response.Status status;

    /**
     * application specific error businessErrorCode
     */
    private AppConstants.ErrorCode businessErrorCode;

    /**
     * link documenting the exception
     */
    private String link;

    /**
     * detailed error description for developers
     */
    private String developerMessage;

    /**
     * @param status
     * @param businessErrorCode
     * @param message
     * @param developerMessage
     * @param link
     */
    public BusinessException(Response.Status status, AppConstants.ErrorCode businessErrorCode, String message,
                             String developerMessage, String link) {
        super(message);
        this.status = status;
        this.businessErrorCode = businessErrorCode;
        this.developerMessage = developerMessage;
        this.link = link;
    }

    public BusinessException(Throwable e) {
        this(Response.Status.INTERNAL_SERVER_ERROR, AppConstants.ErrorCode.GENERIC_EXCEPTION, e.getMessage(), null, null);
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

    public AppConstants.ErrorCode getBusinessErrorCode() {
        return businessErrorCode;
    }

    public void setBusinessErrorCode(AppConstants.ErrorCode businessErrorCode) {
        this.businessErrorCode = businessErrorCode;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    @Override
    @JsonbTransient
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    @Override
    @JsonbTransient
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

}
