package fr.univtln.bruno.i311.simplers.generic.ws;

import fr.univtln.bruno.i311.simplers.generic.dao.IdentifiableEntity;
import fr.univtln.bruno.i311.simplers.generic.dao.exception.DAOException;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Created by bruno on 05/12/14.
 */
public interface DAOResource<T extends IdentifiableEntity> {
    T find(Long id) throws DAOException;

    void delete(Long id) throws DAOException;

    Response metadata() throws DAOException;

    Response findAll(boolean reverse, int pageNumber, int perPage, int limit, UriInfo uriInfo) throws DAOException;

    List<Long> getIds(boolean reverse) throws DAOException;

    int create(List<T> list) throws DAOException;

    T update(@Valid T t) throws DAOException;

    int delete() throws DAOException;

    public int getMaxPageSize();

    public void setMaxPageSize(int pageSize);
}
