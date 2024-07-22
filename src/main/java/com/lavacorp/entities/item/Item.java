package com.lavacorp.entities.item;

import com.lavacorp.entities.item.category.Category;
import com.lavacorp.entities.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.measure.Unit;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Item implements Serializable {
    private int id;
    private String name;
    @Nullable private String description;
    private double basePrice;

    @Nullable private LocalDateTime creationDate;
    @Nullable private LocalDateTime updatedDate;
    @Nullable private Category category;
    @Nullable private Unit<?> unit;
    @Nullable private List<Tag> tags;

    public Item() {}

    public Item(@NotNull String name, @Nullable String description, double basePrice) {
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
    }

    public Item(int id, @NotNull String name, @Nullable String description, double basePrice) {
        this(name, description, basePrice);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @Nullable String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", basePrice=" + basePrice +
                '}';
    }
}
