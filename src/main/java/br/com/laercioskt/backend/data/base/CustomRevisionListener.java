package br.com.laercioskt.backend.data.base;

import br.com.laercioskt.authentication.CurrentUser;
import br.com.laercioskt.backend.service.DateService;
import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CustomRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object o) {
        String userLogged = CurrentUser.get();
        LocalDateTime now = ContextLookup.getApplicationContext().getBean(DateService.class).systemDate();

        CustomRevision revisionEntity = (CustomRevision) o;
        revisionEntity.setUserName(userLogged);
        revisionEntity.setDate(now);

//  TODO change after store logged user correctly
//        revisionEntity.setUserId(userLogged.getId());
//        revisionEntity.setUserName(userLogged.getUserName());
    }

}
