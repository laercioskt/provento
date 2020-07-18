package br.com.laercioskt.views.protocol;

import br.com.laercioskt.backend.data.Protocol;
import br.com.laercioskt.backend.service.ProtocolService;
import com.vaadin.flow.data.provider.CallbackDataProvider;

import java.util.Locale;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class ProtocolDataProvider extends CallbackDataProvider<Protocol, Void> {

    private final ProtocolService service;

    private String filterText = "";

    public ProtocolDataProvider(ProtocolService service, FetchCallback<Protocol, Void> fetchCallback,
                                CountCallback<Protocol, Void> countCallback) {
        super(fetchCallback, countCallback);
        this.service = service;
    }

    public void save(Protocol protocol) {
        final boolean newProtocol = protocol.isNew();

        service.save(protocol);
        if (newProtocol) {
            refreshAll();
        } else {
            refreshItem(protocol);
        }
    }

    public void delete(Protocol protocol) {
        service.deleteProtocol(protocol.getId());
        refreshAll();
    }

    public void setFilter(String filterText) {
        requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public Integer getId(Protocol protocol) {
        requireNonNull(protocol, "Cannot provide an id for a null protocol.");

        return Math.toIntExact(protocol.getId());
    }

}
