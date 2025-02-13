package fr.pet.rest.core.dao;

import fr.pet.rest.core.model.Pet;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by TDERVILY on 02/03/2017.
 */
public interface PetRepository extends CrudRepository<Pet, Long> {
    Pet findByName(String name);
}
