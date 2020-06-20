package br.com.laercioskt.backend.data.base;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "REVINFO")
@RevisionEntity(CustomRevisionListener.class)
public class CustomRevision extends DefaultRevisionEntity {

    @Column
    private String userName;

    @Column
    private Long userId;

    @Column
    private LocalDateTime dateChanged;

    public CustomRevision() {
        super();
    }

    public CustomRevision(String userName, Long userId, LocalDateTime dateChanged) {
        this.userName = userName;
        this.userId = userId;
        this.dateChanged = dateChanged;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getDateChanged() {
        return dateChanged;
    }

    public void setDate(LocalDateTime dateChanged) {
        this.dateChanged = dateChanged;
    }
}
