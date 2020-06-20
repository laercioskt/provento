package br.com.laercioskt.backend.data;

import br.com.laercioskt.backend.data.base.BaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Audited
public class Category extends BaseEntity implements Serializable {

    @Size(min = 2, message = "Category name must be at least two characters")
    private String name;

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

    
    public boolean isNewCategory() {
        return getId() == -1;
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

}
