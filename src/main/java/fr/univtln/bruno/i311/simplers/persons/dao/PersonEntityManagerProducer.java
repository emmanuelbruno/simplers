package fr.univtln.bruno.i311.simplers.persons.dao;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class PersonEntityManagerProducer {
    @Produces
    @Dependent
    @PersistenceContext(unitName = "persons")
    @PersonsDatabase
    private static EntityManager entityManager;


}
