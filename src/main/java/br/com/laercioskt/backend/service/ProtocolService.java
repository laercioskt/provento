package br.com.laercioskt.backend.service;

import br.com.laercioskt.backend.data.Protocol;
import br.com.laercioskt.backend.repository.ProtocolRepository;
import com.vaadin.flow.data.provider.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.vaadin.flow.data.provider.SortDirection.ASCENDING;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class ProtocolService {

    final ProtocolRepository protocolRepository;

    public ProtocolService(ProtocolRepository protocolRepository) {
        this.protocolRepository = protocolRepository;
    }

    public List<Protocol> find(Query<Protocol, Void> query, String filterText) {
        List<Order> sortOrders = query.getSortOrders().stream()
                .map(s -> new Order(s.getDirection().equals(ASCENDING) ? ASC : DESC, s.getSorted())).collect(toList());
        return protocolRepository.find(filterText, of(query.getOffset(), query.getLimit()), sortOrders);
    }

    public long count() {
        return protocolRepository.count();
    }

    public Protocol save(Protocol protocol) {
        return protocolRepository.save(protocol);
    }

    public void deleteProtocol(long id) {
        protocolRepository.deleteById(id);
    }

    public Optional<Protocol> protocolById(int protocolId) {
        return protocolRepository.findById((long) protocolId);
    }

    public long count(String filterText) {
        return protocolRepository.count(filterText);
    }

}
