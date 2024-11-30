import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GameCategoryStartupConfig {

    @Bean
    CommandLineRunner initializeCategories(GameCategoryRepository categoryRepository) {
        return args -> {
            List<String> predefinedCategories = Arrays.asList("Action", "Adventure", "RPG", "Strategy", "Sports");

            predefinedCategories.forEach(categoryName -> {
                if (categoryRepository.findGameCategoryByName(categoryName) == null) {
                    GameCategory category = new GameCategory();
                    category.setName(categoryName);
                    categoryRepository.save(category);
                    System.out.println("Added category: " + categoryName);
                }
            });

            System.out.println("Predefined categories initialization complete.");
        };
    }
}
