package fr.pet.rest.core.dao;

import fr.pet.rest.core.model.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by TDERVILY on 02/03/2017.
 */
public interface PetRepository extends PagingAndSortingRepository<Pet, Long>, CrudRepository<Pet, Long> {
    Pet findByName(String name);
}
