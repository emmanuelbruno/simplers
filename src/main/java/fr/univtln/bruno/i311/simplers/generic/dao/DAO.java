package fr.univtln.bruno.i311.simplers.generic.dao;


import fr.univtln.bruno.i311.simplers.generic.dao.exception.DAOException;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by bruno on 01/12/14.
 * From http://www.adam-bien.com/roller/abien/entry/generic_crud_service_aka_dao
 * and http://theelitegentleman.blogspot.fr/2014/04/daos-as-ejbs-you-are-doing-it-wrong.html
 */

public interface DAO<T extends IdentifiableEntity> {
    void consume(Query query, Consumer consumer) throws DAOException;

    default void consumeAll(Consumer<T> consumer) throws DAOException {
        consumeAll(false, consumer);
    }

    default void consumeAll(boolean reverse, Consumer<T> consumer) throws DAOException {
        consumeAll(reverse, 0, consumer);
    }

    default void consumeAll(boolean reverse, int first, Consumer<T> consumer) throws DAOException {
        consumeAll(reverse, first, -1, consumer);
    }

    void consumeAll(boolean reverse, int first, int limit, Consumer<T> consumer) throws DAOException;

    /**
     * Add the collection of items to the underlaying datasource.
     *
     * @param collection : the collection of items to create
     * @return the number created items.
     * @throws DAOException
     */
    default int create(Collection<T> collection) throws DAOException {
        return create(collection.stream());
    }

    int create(Stream<T> stream) throws DAOException;

    /**
     * Add the items given by the iterator to the underlaying datasource.
     *
     * @param iterator : the iterator on the items to create.
     * @return the number of created items.
     * @throws DAOException
     */
    default int create(Iterator<T> iterator) throws DAOException {
        return create(StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.DISTINCT | Spliterator.NONNULL), false));
    }

    /**
     * Add a item of type T to the underlaying datasource.
     * Datasource managed properties (id, ...) are updated.
     *
     * @param t
     * @return the added item.
     * @throws DAOException
     */
    T create(T t) throws DAOException;

    /**
     * Deletes the Item with given id from the underlaying datasource.
     *
     * @param id The Id of the item to be remove.
     * @throws DAOException
     */
    void delete(Long id) throws DAOException;

    /**
     * Deletes all the item in the underlaying datasource.
     *
     * @return the number of deleted items.
     * @throws DAOException
     */
    int deleteAll() throws DAOException;

    T find(Long id) throws DAOException;

    default List<T> findAll() throws DAOException {
        return findAll(false);
    }

    default List<T> findAll(boolean reverse) throws DAOException {
        return findAll(reverse, 0);
    }

    default List<T> findAll(boolean reverse, int first) throws DAOException {
        return findAll(reverse, first, -1);
    }

    List<T> findAll(boolean reverse, int first, int limit) throws DAOException;

    default List<T> findAll(int first, int limit) throws DAOException {
        return findAll(false, first, limit);
    }

    default List<T> findAll(int first) throws DAOException {
        return findAll(false, first, -1);
    }

    default Stream<T> findAllAsStream() throws DAOException {
        return findAllAsStream(false, 0);
    }

    default Stream<T> findAllAsStream(boolean reverse, int first) throws DAOException {
        return findAllAsStream(false, 0, -1);
    }

    Stream<T> findAllAsStream(boolean reverse, int first, int limit) throws DAOException;

    default List findWithNamedQuery(String queryName) throws DAOException {
        return findWithNamedQuery(queryName, -1, 0);
    }

    default List findWithNamedQuery(String queryName, int first, int resultLimit) throws DAOException {
        return findWithNamedQuery(queryName, null, first, resultLimit);
    }

    List findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int first, int limit)
            throws DAOException;

    default List findWithNamedQuery(String queryName, int resultLimit) throws DAOException {
        return findWithNamedQuery(queryName, null, 0, resultLimit);
    }

    default List findWithNamedQuery(String namedQueryName, Map parameters) throws DAOException {
        return findWithNamedQuery(namedQueryName, parameters, -1, 0);
    }

    List findWithNativeQuery(String nativeQuery, Class type) throws DAOException;

    int getBatchSize();

    void setBatchSize(int i);

    default List<Long> getIds() throws DAOException {
        return getIds(false);
    }

    default List<Long> getIds(boolean reverse) throws DAOException {
        return getIds(reverse, 0);
    }

    default List<Long> getIds(boolean reverse, int first) throws DAOException {
        return getIds(reverse, first, -1);
    }

    List<Long> getIds(boolean reverse, int first, int limit) throws DAOException;


    default Stream<Long> getIdsAsStream() throws DAOException {
        return getIdsAsStream(false);
    }

    default Stream<Long> getIdsAsStream(boolean reverse) throws DAOException {
        return getIdsAsStream(reverse, 0, -1);
    }

    Stream<Long> getIdsAsStream(boolean reverse, int first, int limit) throws DAOException;

    Page<T> queryByPage(int pagenumber, int pagesize, int resultsize, Query contentQuery);

    default Page findAllByPage(int pagenumber, int pagesize) throws DAOException {
        return (findAllByPage(false, pagenumber, pagesize, -1));
    }

    default Page findAllByPage(int pagesize) throws DAOException {
        return (findAllByPage(false, 0, pagesize, -1));
    }

    Page<T> findAllByPage(boolean reverse, int pagenumber, int pagesize, int limit) throws DAOException;

    long getSize();

    /**
     * @return true if the underlaying datasource is open.
     */
    boolean isOpen();

    Stream<T> namedQueryAsStream(String namedQueryName, Map<String, Object> parameters, boolean reverse, int first, int limit) throws DAOException;

    Object singleWithNamedQuery(String namedQueryName) throws DAOException;

    Stream<T> typedQueryAsStream(TypedQuery typedQuery, Map<String, Object> parameters, boolean reverse, int first, int limit) throws DAOException;

    T update(T t) throws DAOException;

    default int updateWithNamedQuery(String namedQueryName) throws DAOException {
        return updateWithNamedQuery(namedQueryName, null);
    }

    int updateWithNamedQuery(String namedQueryName, Map<String, Object> parameters) throws DAOException;

    class QueryParameter {

        private Map parameters = null;

        private QueryParameter(String name, Object value) {
            parameters = new HashMap();
            parameters.put(name, value);
        }

        public static QueryParameter with(String name, Object value) {
            return new QueryParameter(name, value);
        }

        public QueryParameter and(String name, Object value) {
            parameters.put(name, value);
            return this;
        }

        public Map parameters() {
            return parameters;
        }


    }

}
