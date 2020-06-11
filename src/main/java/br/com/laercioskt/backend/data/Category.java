package br.com.laercioskt.backend.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private long id = -1;
    @Size(min = 2, message = "Category name must be at least two characters")
    private String name;

    public long getId() {
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

    @Override
    public String toString() {
        return getName();
    }

    
    public boolean isNewCAtegory() {
        return getId() == -1;
    }
    /*
     * Vaadin DataProviders rely on properly implemented equals and hashcode
     * methods.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || id == -1) {
            return false;
        }
        if (obj instanceof Category) {
            return id == ((Category) obj).id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (id == -1) {
            return super.hashCode();
        }

        return Objects.hash(id);
    }

    public static class CategoryBuilder {

        private String name;

        public CategoryBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public Category build() {
            Category category = new Category();
            category.setName(name);
            return category;
        }
    }

//    new CategoryBuilder().withName("Category " + i).build()
    
    public class CategoryFilter{
    	
    }

}
