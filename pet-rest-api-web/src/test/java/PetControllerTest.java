import fr.pet.rest.app.Application;
import fr.pet.rest.core.dao.CategoryRepository;
import fr.pet.rest.core.dao.PetRepository;
import fr.pet.rest.core.model.Category;
import fr.pet.rest.core.model.Pet;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by TDERVILY on 02/03/2017.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class PetControllerTest extends BaseTest {

    private Pet pet;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    PetRepository petRepository;

    @BeforeEach
    public void setUp() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
        Category dogCategory = new Category(1L, "dog");
        categoryRepository.save(dogCategory);
        petRepository.save(new Pet("Labrador chocolate", 2, dogCategory));
        petRepository.save(new Pet("Golden retriever", 2, dogCategory));
        pet = petRepository.findById(0L).get();
    }

    @Test
    public void testCRUD() throws Exception {
        test1GetPetDetail();
        test2AddPet();
        test3UpdatePet();
        test4getAll();
        test5DeletePet();
    }

    private void test1GetPetDetail() throws Exception {
        mockMvc.perform(get("/pet/" + pet.getId())
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(pet.getId().intValue())))
                .andExpect(jsonPath("$.name", is(pet.getName())))
                .andExpect(jsonPath("$.quantity", is(pet.getQuantity())))
                .andExpect(jsonPath("$.category.id", is(pet.getCategory().getId().intValue())));
    }

    private void test2AddPet() throws Exception {
        mockMvc.perform(post("/pet")
                .content(json(new Pet("red dog", 10, new Category(1L))))
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("red dog")))
                .andExpect(jsonPath("$.quantity", is(10)));
    }

    private void test3UpdatePet() throws Exception {
        Pet pet = petRepository.findById(0L).get();
        Pet petUpd = new Pet(pet.getName() + "updated", pet.getQuantity(), new Category(pet.getId()));
        petUpd.setId(pet.getId());

        mockMvc.perform(put("/pet")
                .content(json(petUpd))
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(pet.getName() + "updated")))
                .andExpect(jsonPath("$.quantity", is(pet.getQuantity())));
    }

    private void test4getAll() throws Exception {
        mockMvc.perform(get("/pet/list?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", isA(JSONArray.class)));
    }

    private void test5DeletePet() throws Exception {
        mockMvc.perform(delete("/pet/" + pet.getId())
                .contentType(contentType))
                .andExpect(status().isOk());
    }

}
