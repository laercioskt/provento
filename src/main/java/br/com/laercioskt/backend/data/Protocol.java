package br.com.laercioskt.backend.data;

import br.com.laercioskt.backend.data.base.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Audited
public class Protocol extends BaseEntity implements Serializable {

    @NotNull
    private String note = "";

    private String code;

    @ManyToOne
    private Customer customer;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return """ 
                Protocol {
                    note = %s
                    code = %s
                    customer = %s
                } """.formatted(note, StringUtils.defaultString(code, ""), customer);
    }

    public static class ProtocolBuilder {

        private String code;
        private String note;
        private Long id;

        public ProtocolBuilder() {
        }

        public ProtocolBuilder withNote(String note) {
            this.note = note;
            return this;
        }

        public ProtocolBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public ProtocolBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public Protocol build() {
            Protocol protocol = new Protocol();
            if (id != null)
                protocol.setId(id);
            protocol.setNote(this.note);
            protocol.setCode(this.code);
            return protocol;
        }

    }


}
