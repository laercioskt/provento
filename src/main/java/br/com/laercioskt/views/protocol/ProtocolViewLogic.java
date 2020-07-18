package br.com.laercioskt.views.protocol;

import br.com.laercioskt.authentication.AccessControl;
import br.com.laercioskt.authentication.AccessControlFactory;
import br.com.laercioskt.backend.data.Protocol;
import br.com.laercioskt.backend.service.ProtocolService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;

import java.io.Serializable;
import java.util.Optional;

public class ProtocolViewLogic implements Serializable {

    private final ProtocolView view;
    private final ProtocolService protocolService;

    public ProtocolViewLogic(ProtocolView view, ProtocolService protocolService) {
        this.view = view;
        this.protocolService = protocolService;
    }

    public void init() {
        if (!AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            view.setNewProtocolEnabled(false);
        }
    }

    public void cancelProtocol() {
        setFragmentParameter("");
        view.clearSelection();
    }

    private void setFragmentParameter(String protocolId) {
        String fragmentParameter;
        if (protocolId == null || protocolId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = protocolId;
        }

        UI.getCurrent().navigate(ProtocolView.class, fragmentParameter);
    }

    public void enter(String protocolId) {
        if (protocolId != null && !protocolId.isEmpty()) {
            if (protocolId.equals("new")) {
                newProtocol();
            } else {
                try {
                    final int pid = Integer.parseInt(protocolId);
                    final Optional<Protocol> protocol = findProtocol(pid);
                    if (protocol.isPresent())
                        view.selectRow(protocol.get());
                    else {
                        Notification.show("Cliente não cadastrado");
                        view.showForm(false);
                    }

                } catch (final NumberFormatException ignored) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Optional<Protocol> findProtocol(int protocolId) {
        return protocolService.protocolById(protocolId);
    }

    public void saveProtocol(Protocol protocol) {
        final boolean newProtocol = protocol.isNew();
        view.clearSelection();
        view.updateProtocol(protocol);
        setFragmentParameter("");
        view.showNotification(protocol.getNote() + (newProtocol ? " created" : " updated"));
    }

    public void deleteProtocol(Protocol protocol) {
        view.clearSelection();
        view.removeProtocol(protocol);
        setFragmentParameter("");
        view.showNotification(protocol.getNote() + " removed");
    }

    public void editProtocol(Protocol protocol) {
        if (protocol == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(protocol.getId() + "");
        }
        view.editProtocol(protocol);
    }

    public void newProtocol() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editProtocol(new Protocol());
    }

    public void rowSelected(Protocol protocol) {
        if (AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editProtocol(protocol);
        }
    }
}
