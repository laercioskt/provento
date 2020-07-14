package br.com.laercioskt.backend.data;

import br.com.laercioskt.backend.data.base.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Audited
public class Customer extends BaseEntity implements Serializable {

    @NotNull
    @Size(min = 2, message = "Customer name must have at least two characters")
    private String name = "";

    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return """ 
                Customer {
                    name = %s
                    code = %s
                } """.formatted(name, StringUtils.defaultString(code, ""));
    }

    public static class CustomerBuilder {

        private String name;
        private String code;
        private Long id;

        public CustomerBuilder() {
        }

        public CustomerBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CustomerBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public CustomerBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();
            if (id != null)
                customer.setId(id);
            customer.setName(this.name);
            customer.setCode(this.code);
            return customer;
        }

    }


}
