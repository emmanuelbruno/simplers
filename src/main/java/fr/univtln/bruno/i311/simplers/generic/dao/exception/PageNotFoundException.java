package fr.univtln.bruno.i311.simplers.generic.dao.exception;

import fr.univtln.bruno.i311.simplers.AppConstants;

import javax.ws.rs.core.Response;

/**
 * Created by bruno on 04/12/14.
 */
public class PageNotFoundException extends DAOException {
    public PageNotFoundException() {
        super(Response.Status.NOT_FOUND, AppConstants.ErrorCode.DAO_EXCEPTION, "Page doesn't exist", "Page doesn't exist", null);
    }
}
