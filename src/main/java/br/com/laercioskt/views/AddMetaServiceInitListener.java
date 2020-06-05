package br.com.laercioskt.views;

import br.com.laercioskt.authentication.AccessControl;
import br.com.laercioskt.authentication.AccessControlFactory;
import br.com.laercioskt.views.login.LoginScreen;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class AddMetaServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent initEvent) {
        System.out.println("salkdjfhsldkjfhaskldf");
        System.out.println("salkdjfhsldkjfhaskldf");
        System.out.println("salkdjfhsldkjfhaskldf");
        System.out.println("salkdjfhsldkjfhaskldf");
        System.out.println("salkdjfhsldkjfhaskldf");
        System.out.println("salkdjfhsldkjfhaskldf");
        System.out.println("salkdjfhsldkjfhaskldf");

        final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();

        initEvent.getSource().addUIInitListener(uiInitEvent -> {
            uiInitEvent.getUI().addBeforeEnterListener(enterEvent -> {
                if (!accessControl.isUserSignedIn() && !LoginScreen.class.equals(enterEvent.getNavigationTarget()))
                    enterEvent.rerouteTo(LoginScreen.class);
            });
        });

    }

}
