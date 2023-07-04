package org.financial.assistant.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private int amount;

    @Column(name = "is_accounting")
    private boolean isAccounting;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Transaction() {
        this.createdAt = LocalDateTime.now();
    }

    public Transaction(Category category, String description, User user, String name, int amount, boolean isAccounting) {
        this.category = category;
        this.description = description;
        this.user = user;
        this.name = name;
        this.amount = amount;
        this.isAccounting = isAccounting;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String formatCreatedAt(LocalDateTime createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");

        return createdAt.format(formatter);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return new BigDecimal(amount).divide(new BigDecimal(100));
    }

    public static String formatAmount(BigDecimal amount) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);

        return numberFormat.format(amount);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.multiply(new BigDecimal(100)).intValue();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean getIsAccounting() {
        return isAccounting;
    }

    public void setIsAccounting(boolean accounting) {
        isAccounting = accounting;
    }

}
