package fr.univtln.bruno.i311.simplers.persons.dao;

import fr.univtln.bruno.i311.simplers.generic.dao.AbstractDAO;
import fr.univtln.bruno.i311.simplers.generic.dao.exception.DAOException;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.persistence.EntityManager;

@Log
public class PersonDAO extends AbstractDAO<Person> {

    @Inject
    public PersonDAO(@PersonsDatabase EntityManager entityManager) throws DAOException {
        super(entityManager);
    }

}
