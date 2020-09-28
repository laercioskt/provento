package br.com.laercioskt.views.protocol;

import br.com.laercioskt.authentication.AccessControl;
import br.com.laercioskt.authentication.AccessControlFactory;
import br.com.laercioskt.backend.data.Customer;
import br.com.laercioskt.backend.data.Protocol;
import br.com.laercioskt.backend.service.CustomerService;
import br.com.laercioskt.backend.service.ProtocolService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.provider.Query;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public class ProtocolViewLogic implements Serializable {

    private final ProtocolView view;
    private final ProtocolService protocolService;
    private final CustomerService customerService;

    public ProtocolViewLogic(ProtocolView view, ProtocolService protocolService, CustomerService customerService) {
        this.view = view;
        this.protocolService = protocolService;
        this.customerService = customerService;
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
        view.showNotification("Protocol " + protocol.getCode() + (newProtocol ? " created" : " updated"));
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

    public Stream<Customer> customers(String filter, int offset, int limit) {
        Query<Customer, Void> query
                = new Query<>(offset, limit, emptyList(), null, null);
        return customerService.find(query, filter).stream();
    }

    public Integer customersCount(String filter) {
        return Math.toIntExact(customerService.count(filter));
    }
}
