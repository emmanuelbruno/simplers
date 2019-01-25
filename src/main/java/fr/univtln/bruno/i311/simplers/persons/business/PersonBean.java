package fr.univtln.bruno.i311.simplers.persons.business;

import fr.univtln.bruno.i311.simplers.generic.business.AbstractDAOBean;
import fr.univtln.bruno.i311.simplers.generic.dao.DAO;
import fr.univtln.bruno.i311.simplers.persons.dao.Person;
import fr.univtln.bruno.i311.simplers.persons.dao.PersonDAO;
import lombok.extern.java.Log;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@Log
public class PersonBean extends AbstractDAOBean<Person> {
    @Inject
    PersonDAO personDAO;

    @Override
    public DAO<Person> getDAO() {
        return personDAO;
    }
}
