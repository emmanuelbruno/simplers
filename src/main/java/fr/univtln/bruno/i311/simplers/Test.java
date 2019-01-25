package fr.univtln.bruno.i311.simplers;

import fr.univtln.bruno.i311.simplers.generic.dao.exception.DAOException;
import fr.univtln.bruno.i311.simplers.persons.business.PersonBean;
import fr.univtln.bruno.i311.simplers.persons.dao.Person;
import lombok.extern.java.Log;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Log
@Startup
@Singleton
public class Test {
    private static List<Person> people = Arrays.asList(
            Person.builder()
                    .email("marc.boeuf@ici.fr")
                    .firstname("Marc")
                    .lastname("Boeuf")
                    .build(),
            Person.builder()
                    .email("jeanne.mouton@labas.fr")
                    .firstname("Jeanne")
                    .lastname("Mouton")
                    .build()
    );

    @Inject
    private PersonBean personBean;

    @PostConstruct
    public void init() {
        try {
            personBean.create(people);
            log.info("### FindAll ### "+personBean.findAll());
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
