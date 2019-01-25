package fr.univtln.bruno.i311.simplers.generic.business;


import fr.univtln.bruno.i311.simplers.generic.dao.DAO;
import fr.univtln.bruno.i311.simplers.generic.dao.IdentifiableEntity;
import fr.univtln.bruno.i311.simplers.generic.dao.Page;
import fr.univtln.bruno.i311.simplers.generic.dao.exception.DAOException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by bruno on 13/03/15.
 */
public abstract class AbstractDAOBean<T extends IdentifiableEntity> implements DAOBean<T> {

    public abstract DAO<T> getDAO();

    @Override
    public T find(Long id) throws DAOException {
        return getDAO().find(id);
    }

    @Override
    public void delete(Long id) throws DAOException {
        getDAO().delete(id);
    }

    @Override
    public List<T> findAll(boolean reverse, int first, int limit) throws DAOException {
        return getDAO().findAll(reverse, first, limit);
    }

    @Override
    public long getSize() {
        return getDAO().getSize();
    }

    @Override
    public Page findAllByPage(boolean reverse, int pagenumber, int pagesize, int limit) throws DAOException {
        return getDAO().findAllByPage(reverse, pagenumber, pagesize, limit);
    }

    @Override
    public List<Long> getIds(boolean reverse, int first, int limit) throws DAOException {
        return getDAO().getIds(reverse, first, limit);
    }

    @Override
    public void consumeAll(boolean reverse, int first, int limit, Consumer<T> consumer) throws DAOException {
        getDAO().consumeAll(reverse, first, limit, consumer);
    }

    private int complexeTransaction(DAOIntUpdateSupplier supplier) throws DAOException {
        return supplier.getAsInt();
    }

    protected T simpleTransaction(DAOSimpleUpdateSupplier<T> supplier) throws DAOException {
        return supplier.get();
    }

    @Override
    public T create(@Valid T t) throws DAOException {
        return simpleTransaction(() -> getDAO().create(t));
    }

    @Override
    public int create(@Valid Collection<T> collection) throws DAOException {
        return complexeTransaction(() -> getDAO().create(collection));
    }

    @Override
    public int create(Iterator<T> iterator) throws DAOException {
        return complexeTransaction(() -> getDAO().create(iterator));
    }

    @Override
    public int create(Stream<T> stream) throws DAOException {
        return complexeTransaction(() -> getDAO().create(stream));
    }

    @Override
    public T update(@Valid T t) throws DAOException {
        return simpleTransaction(() -> getDAO().update(t));
    }

    @Override
    public int deleteAll() throws DAOException {
        return complexeTransaction(() -> getDAO().deleteAll());
    }

    protected interface DAOIntUpdateSupplier {
        int getAsInt() throws DAOException;
    }

    protected interface DAOSimpleUpdateSupplier<T> {
        T get() throws DAOException;
    }

}
