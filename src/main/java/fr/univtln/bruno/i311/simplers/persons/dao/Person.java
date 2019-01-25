package fr.univtln.bruno.i311.simplers.persons.dao;

import fr.univtln.bruno.i311.simplers.generic.dao.IdentifiableEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.*;

@Builder
@Data
@Entity
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = "person.findAll", query = "SELECT p FROM Person p")
})
public class Person implements IdentifiableEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(nullable = false, unique = true)
    private Long id;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

}
