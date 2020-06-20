package br.com.laercioskt.backend.data;

import br.com.laercioskt.backend.data.base.BaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Audited
public class Parameter extends BaseEntity implements Serializable {

    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String valueString;
    @Column
    private Long valueLong;
    @Column
    private LocalDateTime valueLocalDateTime;
    @Column
    private Boolean valueBoolean;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public Long getValueLong() {
        return valueLong;
    }

    public void setValueLong(Long valueLong) {
        this.valueLong = valueLong;
    }

    public LocalDateTime getValueLocalDateTime() {
        return valueLocalDateTime;
    }

    public void setValueLocalDateTime(LocalDateTime valueLocalDateTime) {
        this.valueLocalDateTime = valueLocalDateTime;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public void setValueBoolean(Boolean valueBoolean) {
        this.valueBoolean = valueBoolean;
    }

    public static final class ParameterBuilder {

        private String name;
        private String description;
        private LocalDateTime value;

        public ParameterBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ParameterBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ParameterBuilder withValue(LocalDateTime value) {
            this.value = value;
            return this;
        }

        public Parameter build() {
            Parameter parameter = new Parameter();
            parameter.setName(name);
            parameter.setDescription(description);
            parameter.setValueLocalDateTime(value);
            return parameter;
        }

    }

}
