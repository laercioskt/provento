package br.com.laercioskt.backend.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private long id = -1;
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

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public boolean isNewUser() {
        return getId() == -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || id == -1) {
            return false;
        }
        if (obj instanceof User) {
            return id == ((User) obj).id;
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

    public static class UserBuilder {

        private String userName;
        private String password;
        private UserStatus status = UserStatus.ACTIVE;
        private final Set<Category> categories = new HashSet<>();

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

        public User build() {
            User user = new User();
            user.setUserName(this.userName);
            user.setPassword(this.password);
            user.setStatus(this.status);
            user.setCategory(this.categories);
            return user;
        }
    }


}
