package br.com.laercioskt.backend.data;

import br.com.laercioskt.backend.data.base.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Audited
public class Protocol extends BaseEntity implements Serializable {

    private String code;

    private String note;

    @ManyToOne
    @NotNull(message = "Customer should be informed")
    private Customer customer;

    @OneToMany(cascade = ALL, orphanRemoval = true, fetch = LAZY, targetEntity = Document.class)
//    @Size(min = 2, message = "Protocol should have at least 2 itens")
    private List<Document> documents = new ArrayList<>();

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

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
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
