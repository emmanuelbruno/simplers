package fr.univtln.bruno.i311.simplers.generic.business;

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
 * A standard and generic DAO interface for java bean.
 *
 * @param <T> An entity with and id.
 */
public interface DAOBean<T extends IdentifiableEntity> {

    /**
     * "Consumes" every entities.
     *
     * @param consumer the consumer applied to each entity.
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    default void consumeAll(Consumer<T> consumer) throws DAOException {
        consumeAll(false, consumer);
    }

    /**
     * "Consumes" every entities.
     *
     * @param reverse  has to be set to true to retrieve entities in reverse order.
     * @param consumer
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    default void consumeAll(boolean reverse, Consumer<T> consumer) throws DAOException {
        consumeAll(reverse, 0, consumer);
    }

    /**
     * "Consumes" every entities.
     *
     * @param reverse  has to be set to true to retrieve entities in reverse order.
     * @param first    is the first rank of the entity to be consider.
     * @param consumer
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    default void consumeAll(boolean reverse, int first, Consumer<T> consumer) throws DAOException {
        consumeAll(reverse, first, -1, consumer);
    }

    /**
     * "Consumes" every entities.
     *
     * @param reverse  has to be set to true to retrieve entities in reverse order.
     * @param first
     * @param limit
     * @param consumer
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    void consumeAll(boolean reverse, int first, int limit, Consumer<T> consumer) throws DAOException;

    /**
     * persists a new entity.
     *
     * @param t
     * @return
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    T create(@Valid T t) throws DAOException;

    /**
     * persists each entity.
     *
     * @param collection
     * @return
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    int create(@Valid Collection<T> collection) throws DAOException;

    /**
     * persists each entity.
     *
     * @param iterator
     * @return
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    int create(Iterator<T> iterator) throws DAOException;

    /**
     * persists each entity.
     *
     * @param stream
     * @return
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    int create(Stream<T> stream) throws DAOException;

    /**
     * remove an entity from the persistence.
     *
     * @param id of the entity.
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    void delete(Long id) throws DAOException;

    /**
     * remove each entity from the persistence.
     *
     * @return the number of deletes entities.
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    int deleteAll() throws DAOException;

    /**
     * Finds an entity by id.
     *
     * @param id of the entity.
     * @return the entity whose id is <it>id</it>.
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    T find(Long id) throws DAOException;

    /**
     * @return The list of entities.
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    default List<T> findAll() throws DAOException {
        return findAll(false);
    }

    /**
     * @param reverse has to be set to true to retrieve entities in reverse order.
     * @return The list of entities.
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    default List<T> findAll(boolean reverse) throws DAOException {
        return findAll(reverse, 0);
    }

    /**
     * @param reverse has to be set to true to retrieve entities in reverse order.
     * @param first
     * @return
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    default List<T> findAll(boolean reverse, int first) throws DAOException {
        return findAll(reverse, first, -1);
    }

    /**
     * @param reverse has to be set to true to retrieve entities in reverse order.
     * @param first
     * @param limit
     * @return
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    List<T> findAll(boolean reverse, int first, int limit) throws DAOException;

    /**
     * @param first
     * @param limit
     * @return
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    default List<T> findAll(int first, int limit) throws DAOException {
        return findAll(false, first, limit);
    }

    /**
     * @param first
     * @return
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    default List<T> findAll(int first) throws DAOException {
        return findAll(false, first, -1);
    }

    /**
     * @return the list of entity ids.
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    default List<Long> getIds() throws DAOException {
        return getIds(false);
    }

    /**
     * @param reverse has to be set to true to retrieve entities in reverse order.
     * @return
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    default List<Long> getIds(boolean reverse) throws DAOException {
        return getIds(reverse, 0);
    }

    /**
     * @param reverse has to be set to true to retrieve entities in reverse order.
     * @param first
     * @return
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    default List<Long> getIds(boolean reverse, int first) throws DAOException {
        return getIds(reverse, first, -1);
    }

    /**
     * @param reverse has to be set to true to retrieve entities in reverse order.
     * @param first
     * @param limit
     * @return
     * @throws DAOException thrown is there is an exception in DAO operation.
     */
    List<Long> getIds(boolean reverse, int first, int limit) throws DAOException;

    /**
     * @return the number of  entities.
     */
    long getSize();

    /**
     * @param reverse    has to be set to true to retrieve entities in reverse order.
     * @param pagenumber
     * @param pagesize
     * @return A page of entities.
     * @throws DAOException
     */
    default Page findAllByPage(boolean reverse, int pagenumber, int pagesize) throws DAOException {
        return findAllByPage(reverse, pagenumber, pagesize, -1);
    }

    /**
     * @param reverse    has to be set to true to retrieve entities in reverse order.
     * @param pagenumber
     * @param pagesize
     * @param limit
     * @return
     * @throws DAOException
     */
    Page findAllByPage(boolean reverse, int pagenumber, int pagesize, int limit) throws DAOException;

    /**
     * @param t
     * @return
     * @throws DAOException
     */
    T update(@Valid T t) throws DAOException;
}
