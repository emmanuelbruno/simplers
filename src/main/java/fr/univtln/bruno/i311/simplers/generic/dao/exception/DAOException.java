package fr.univtln.bruno.i311.simplers.generic.dao.exception;



import fr.univtln.bruno.i311.simplers.AppConstants;

import javax.ws.rs.core.Response;

/**
 * Created by bruno on 04/12/14.
 */
public class DAOException extends BusinessException {
    public DAOException(Throwable e) {
        super(e);
    }

    public DAOException(Response.Status status, AppConstants.ErrorCode code, String message, String developerMessage, String link) {
        super(status, code, message, developerMessage, link);
    }
}
