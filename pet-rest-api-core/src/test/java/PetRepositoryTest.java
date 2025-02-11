import fr.pet.rest.core.app.TestApplication;
import fr.pet.rest.core.dao.CategoryRepository;
import fr.pet.rest.core.dao.PetRepository;
import fr.pet.rest.core.model.Category;
import fr.pet.rest.core.model.Pet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by TDERVILY on 02/03/2017.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
@DataJpaTest
public class PetRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    PetRepository petRepository;
    @Autowired
    CategoryRepository categoryRepository;

    public static final String DOLPHIN = "dolphin";

    @Test
    public void getOnePet() throws Exception {
        Optional<Pet> one = petRepository.findById(0L);
        assertNotNull(one);
    }

    @Test
    public void createPet() throws Exception {
        Optional<Category> fish =
                StreamSupport.stream(categoryRepository.findAll().spliterator(), false).
                        filter(category -> category.getName().equals("Fish")).findFirst();
        assertNotNull(fish.get());
        entityManager.persist(new Pet(DOLPHIN, 3, fish.get()));
        assertNotNull(petRepository.findByName(DOLPHIN));
    }

    @Test
    public void updatePet() throws Exception {
        Pet one = petRepository.findById(0L).get();
        assertNotNull(one);
        one.setName(one.getName() + "updated");
        petRepository.save(one);
        Pet updated = petRepository.findById(0L).get();
        assertEquals(one.getName(), updated.getName());
    }

    @Test
    public void deletePet() throws Exception {
        petRepository.deleteById(2L);
        assertNull(petRepository.findById(2L));
    }
}
