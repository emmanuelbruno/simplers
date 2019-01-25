package fr.univtln.bruno.i311.simplers.generic.dao;

import fr.univtln.bruno.i311.simplers.AppConstants;
import fr.univtln.bruno.i311.simplers.generic.dao.exception.DAOException;
import fr.univtln.bruno.i311.simplers.generic.dao.exception.PageNotFoundException;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.Response;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by bruno on 01/12/14.
 * From http://www.adam-bien.com/roller/abien/entry/generic_crud_service_aka_dao
 * and http://theelitegentleman.blogspot.fr/2014/04/daos-as-ejbs-you-are-doing-it-wrong.html
 */
public class AbstractDAO<D extends IdentifiableEntity> implements DAO<D> {
    private static List<DAOEventListener> daoEventListeners = new ArrayList<>();
    private Class<D> entityBeanType;
    private final String idname;
    protected EntityManager entityManager;
    private int batchSize = 1000;

    public AbstractDAO(EntityManager entityManager) throws DAOException {
        this.entityManager = entityManager;

        Class c = getClass();
        //In case of use with implementation class using raw types (mandatory with EJBs).
        while (!(c.getGenericSuperclass() instanceof ParameterizedType)) {
            System.out.println(c);
            c = c.getSuperclass();
        }
        for (Type t : (((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments()))
            entityBeanType = ((Class) ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments()[0]);


        idname = getInheritedFields(entityBeanType).stream().filter(f -> f.isAnnotationPresent(Id.class)).map(Field::getName).findFirst()
                .orElseGet(getInheritedMethods(entityBeanType).stream().filter(f -> f.isAnnotationPresent(Id.class)).map(Method::getName).findFirst()::get);
        if (idname == null)
            throw new DAOException(Response.Status.INTERNAL_SERVER_ERROR,
                    AppConstants.ErrorCode.DAO_EXCEPTION,
                    "No field or method annotated with @Id for class: " + entityBeanType.getName(),
                    "", "");
    }

    public static void addEventListener(DAOEventListener eventListener) {
        daoEventListeners.add(eventListener);
    }

    private static void notifyDAOEvent(DAOEvent daoEvent) {
        daoEventListeners.forEach(l -> l.onDAOEvent(daoEvent));
    }

    public static List<Field> getInheritedFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    public static List<Method> getInheritedMethods(Class<?> type) {
        List<Method> methods = new ArrayList<Method>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        }
        return methods;
    }

    private Class[] getParametersClasses() {
        Class c = getClass();
        //In case of use with implementation class using raw types (mandatory with EJBs).
        while (!(c.getGenericSuperclass() instanceof ParameterizedType)) {
            c = c.getSuperclass();
        }

        Type[] types = ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments();
        Class[] classes = new Class[types.length];
        for (int i = 0; i < types.length; i++) {
            if (types[i] instanceof ParameterizedType)
                classes[i] = (Class) ((ParameterizedType) types[i]).getRawType();
            else
                classes[i] = (Class) types[i];
        }
        return classes;
    }

    @Override
    public void consume(Query query, Consumer consumer) throws DAOException {
        query.getResultStream().forEach(consumer);
    }

    @Override
    public void consumeAll(boolean reverse, int first, int limit, Consumer<D> consumer) throws DAOException {
        consume(findAllQuery(reverse, first, limit), consumer);
    }

    @Override
    public int create(Stream<D> stream) throws DAOException {
        final int[] nbAdded = {0};
        FlushModeType flushmode = entityManager.getFlushMode();
        entityManager.setFlushMode(FlushModeType.COMMIT);
        stream.forEachOrdered(d -> {
                    entityManager.persist(d);
                    if (nbAdded[0]++ % batchSize == 0) {
                        //Push data to the database in the same transaction
                        entityManager.flush();
                        entityManager.clear();
                    }
                }
        );
        entityManager.flush();
        entityManager.clear();
        entityManager.setFlushMode(flushmode);
        return nbAdded[0];
    }

    @Override
    public D create(D t) throws DAOException {
        entityManager.persist(t);
        entityManager.flush();
        entityManager.refresh(t);

        notifyDAOEvent(new DAOEvent(DAOEvent.Type.CREATE, t));
        return t;
    }

    @Override
    public void delete(Long id) throws DAOException {
        entityManager.remove(find(id));
    }

    @Override
    public int deleteAll() throws DAOException {
        CriteriaDelete<D> cdelete = entityManager.getCriteriaBuilder().createCriteriaDelete(entityBeanType);
//        Root<R> t = cdelete.from(entityBeanType);
        return entityManager.createQuery(cdelete).executeUpdate();
    }

    @Override
    public D find(Long id) {
        return entityManager.find(entityBeanType, id);
    }

    @Override
    public List<D> findAll(boolean reverse, int first, int limit) throws DAOException {
        return findAllQuery(reverse, first, limit).getResultList();
    }

    @Override
    public Stream<D> findAllAsStream(boolean reverse, int first, int limit) throws DAOException {
        return findAllQuery(reverse, first, limit).getResultStream();
    }

    @Override
    public List findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int first, int limit) {
        return getNamedQuery(namedQueryName, parameters, first, limit).getResultList();
    }

    @Override
    public List findWithNativeQuery(String sql, Class type) {
        return entityManager.createNativeQuery(sql, type).getResultList();
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

    @Override
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public List<Long> getIds(boolean reverse, int first, int limit) throws DAOException {
        return getIDsQuery(reverse, first, limit).getResultList();
    }

    private TypedQuery<Long> getIDsQuery(boolean reverse, int first, int limit) throws DAOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(long.class);
        Root<D> t = cq.from(entityBeanType);
        cq.select(t.get(idname));
        cq.orderBy(reverse ? cb.desc(t.get(idname)) : cb.asc(t.get(idname)));
        TypedQuery<Long> q = entityManager.createQuery(cq);
        if (first > -1) q.setFirstResult(first);
        if (limit > -1) q.setMaxResults(limit);
        return q;
    }

    @Override
    public Stream<Long> getIdsAsStream(boolean reverse, int first, int limit) throws DAOException {
        return getIDsQuery(reverse, first, limit).getResultStream();
    }

    private TypedQuery<Long> getSizeQuery() {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(entityBeanType)));
        return entityManager.createQuery(cq);
    }

    @Override
    public long getSize() {
        return getSizeQuery().getSingleResult();
    }

    @Override
    public Page queryByPage(int pagenumber, int pagesize, int totalItems, Query contentQuery) {
        return new Page(pagenumber, pagesize, totalItems, (int) Math.ceil(totalItems / (float) pagesize), contentQuery.getResultList());
    }

    @Override
    public Page findAllByPage(boolean reverse, int pagenumber, int pagesize, int limit) throws DAOException {
        int size = getSizeQuery().getSingleResult().intValue();
        if (limit > 0 && limit < size) size = limit;
        int first = pagenumber * pagesize;
        if (first >= size) throw new PageNotFoundException();
        return queryByPage(pagenumber, pagesize, size, findAllQuery(reverse, first, Integer.min(pagesize, size)));
    }

    @Override
    public boolean isOpen() {
        return entityManager.isOpen();
    }

    @Override
    public Stream<D> namedQueryAsStream(String namedQueryName, Map<String, Object> parameters, boolean reverse, int first, int limit) throws DAOException {
        return getNamedQuery(namedQueryName, parameters, first, limit).getResultStream();
    }

    private Query getNamedQuery(String namedQueryName, Map<String, Object> parameters, int first, int limit) {
        Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
        Query query = entityManager.createNamedQuery(namedQueryName);
        if (limit > 0)
            query.setMaxResults(limit);

        if (first > 0)
            query.setFirstResult(first);

        for (Map.Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query;
    }

    @Override
    public Object singleWithNamedQuery(String namedQueryName) {
        return entityManager.createNamedQuery(namedQueryName).getSingleResult();
    }

    @Override
    public Stream<D> typedQueryAsStream(TypedQuery typedQuery, Map<String, Object> parameters, boolean reverse, int first, int limit) throws DAOException {
        return typedQuery.getResultStream();
    }

    @Override
    public D update(D t) {
        return entityManager.merge(t);
    }

    @Override
    public int updateWithNamedQuery(String namedQueryName, Map<String, Object> parameters) {
        Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
        Query query = entityManager.createNamedQuery(namedQueryName);
        for (Map.Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.executeUpdate();
    }

    private TypedQuery<D> findAllQuery(boolean reverse, int first, int limit) throws DAOException {
        String queryName = entityBeanType.getSimpleName().toLowerCase() + ".findAll";
        TypedQuery<D> result = entityManager.createNamedQuery(queryName, entityBeanType);
        if (result == null) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<D> cq = cb.createQuery(entityBeanType);
            Root<D> t = cq.from(entityBeanType);
            cq.select(t);
            cq.orderBy(reverse ? cb.desc(t.get(idname)) : cb.asc(t.get(idname)));
            result = entityManager.createQuery(cq);
        } else if (first > -1) result.setFirstResult(first);
        if (limit > -1) result.setMaxResults(limit);

        return result;
    }

}
