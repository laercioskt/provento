package br.com.laercioskt.backend.data;

import br.com.laercioskt.backend.data.base.BaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Audited
public class User extends BaseEntity implements Serializable {

    @NotNull
    @Size(min = 2, message = "User name must have at least two characters")
    private String userName = "";
    @NotNull
    @Size(min = 8, message = "User password must have at least eight characters")
    private String password = "";
    @NotNull
    private UserStatus status = UserStatus.ACTIVE;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Category> category = new HashSet<>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<Category> getCategory() {
        return category;
    }

    public void setCategory(Set<Category> category) {
        this.category = category;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return """ 
                User {
                    userName = %s
                    status = %s
                    category = %s
                } """.formatted(userName, status, category);
    }

    public static class UserBuilder {

        private String userName;
        private String password;
        private UserStatus status = UserStatus.ACTIVE;
        private final Set<Category> categories = new HashSet<>();
        private Long id;

        public UserBuilder() {
        }

        public UserBuilder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withStatus(UserStatus status) {
            this.status = status;
            return this;
        }

        public UserBuilder withCategory(Category category) {
            this.categories.add(category);
            return this;
        }

        public UserBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public User build() {
            User user = new User();
            if (id != null)
                user.setId(id);
            user.setUserName(this.userName);
            user.setPassword(this.password);
            user.setStatus(this.status);
            user.setCategory(this.categories);
            return user;
        }

    }


}
