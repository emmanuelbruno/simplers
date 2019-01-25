package fr.univtln.bruno.i311.simplers.persons.ressources;

import fr.univtln.bruno.i311.simplers.generic.ws.AbstractDAOResource;
import fr.univtln.bruno.i311.simplers.persons.business.PersonBean;
import fr.univtln.bruno.i311.simplers.persons.dao.Person;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("persons")
public class PersonResource extends AbstractDAOResource<Person> {
    @Inject
    public PersonResource(PersonBean personManagerBean) {
        super(personManagerBean);
    }
}
