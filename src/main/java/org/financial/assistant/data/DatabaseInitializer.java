package org.financial.assistant.data;

import org.financial.assistant.models.User;
import org.financial.assistant.models.Category;
import org.financial.assistant.models.Transaction;
import org.financial.assistant.repositories.UserRepository;
import org.financial.assistant.repositories.CategoryRepository;
import org.financial.assistant.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class DatabaseInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseInitializer(UserRepository userRepository, CategoryRepository categoryRepository,
                               TransactionRepository transactionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initializeUsers();
        initializeCategories();
        initializeTransactions();
    }

    private void initializeUsers() {
        User user1 = new User();
        user1.setEmail("user@example.com");
        user1.setUsername("123123");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setPassword(passwordEncoder.encode("123"));
        user1.setRole("ROLE_USER");
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("admin@example.com");
        user2.setUsername("Admin");
        user2.setFirstName("Admin");
        user2.setLastName("User");
        user2.setPassword(passwordEncoder.encode("123"));
        user2.setRole("ROLE_ADMIN");
        userRepository.save(user2);

        User user3 = new User();
        user3.setEmail("123@example.com");
        user3.setUsername("123");
        user3.setFirstName("123");
        user3.setLastName("123");
        user3.setPassword(passwordEncoder.encode("123"));
        user3.setRole("ROLE_USER");
        userRepository.save(user3);
    }

    private void initializeCategories() {
        User user = userRepository.findById(3L).orElse(null); //important, user id = Long

        Category foodCategory = new Category();
        foodCategory.setName("Food");
        foodCategory.setUser(user);
        foodCategory.setType("expenses");
        categoryRepository.save(foodCategory);

        Category rentCategory = new Category();
        rentCategory.setName("Rent");
        rentCategory.setUser(user);
        rentCategory.setType("expenses");
        categoryRepository.save(rentCategory);

        Category transportationCategory = new Category();
        transportationCategory.setName("Transportation");
        transportationCategory.setUser(user);
        transportationCategory.setType("expenses");
        categoryRepository.save(transportationCategory);

        Category utilitiesCategory = new Category();
        utilitiesCategory.setName("Utilities");
        utilitiesCategory.setUser(user);
        utilitiesCategory.setType("expenses");
        categoryRepository.save(utilitiesCategory);

        Category entertainmentCategory = new Category();
        entertainmentCategory.setName("Entertainment");
        entertainmentCategory.setUser(user);
        entertainmentCategory.setType("expenses");
        categoryRepository.save(entertainmentCategory);

        Category salaryCategory = new Category();
        salaryCategory.setName("Salary");
        salaryCategory.setUser(user);
        salaryCategory.setType("income");
        categoryRepository.save(salaryCategory);

        Category investmentsCategory = new Category();
        investmentsCategory.setName("Investments");
        investmentsCategory.setUser(user);
        investmentsCategory.setType("income");
        categoryRepository.save(investmentsCategory);

        Category freelancingCategory = new Category();
        freelancingCategory.setName("Freelancing");
        freelancingCategory.setUser(user);
        freelancingCategory.setType("income");
        categoryRepository.save(freelancingCategory);

        Category giftsCategory = new Category();
        giftsCategory.setName("Gifts");
        giftsCategory.setUser(user);
        giftsCategory.setType("income");
        categoryRepository.save(giftsCategory);

        Category refundsCategory = new Category();
        refundsCategory.setName("Refunds");
        refundsCategory.setUser(user);
        refundsCategory.setType("income");
        categoryRepository.save(refundsCategory);
    }

    private void initializeTransactions() {
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        List<User> users = userRepository.findAll();

        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 100; i++) {
            //randomly select category and user
            Category category = categories.get(random.nextInt(categories.size()));
            User user = users.get(random.nextInt(users.size()));

            Transaction transaction = new Transaction();
            transaction.setCategory(category);
            transaction.setUser(user);
            transaction.setName("Transaction " + i);
            transaction.setAmount(BigDecimal.valueOf(random.nextInt(1000)));
            transaction.setIsAccounting(random.nextBoolean());

            //random timestamp the last 31 days
            int randomDaysOffset = random.nextInt(31);
            LocalDateTime createdAt = now.minusDays(randomDaysOffset);
            transaction.setCreatedAt(createdAt);

            transactionRepository.save(transaction);
        }
    }

}
