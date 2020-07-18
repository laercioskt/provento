package br.com.laercioskt.views.protocol;

import br.com.laercioskt.backend.data.Protocol;
import br.com.laercioskt.backend.data.base.ContextLookup;
import br.com.laercioskt.backend.service.ProtocolService;
import br.com.laercioskt.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import static java.util.Objects.requireNonNull;

@Route(value = "Protocols", layout = MainLayout.class)
public class ProtocolView extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Protocols";
    public static final String CUSTOMER_VIEW_FILTER_ID = "ProtocolViewFilterID";

    private final ProtocolGrid grid;
    private final ProtocolForm form;

    private TextField filter;

    private final ProtocolViewLogic viewLogic;

    private Button newProtocol;

    private final ProtocolDataProvider dataProvider;

    public ProtocolView() {
        ProtocolService protocolService = ContextLookup.getBean(ProtocolService.class);
        requireNonNull(protocolService,
                "it's not expected use of ProtocolView without protocolService instance.");

        setSizeFull();

        final HorizontalLayout topLayout = createTopBar();
        grid = new ProtocolGrid();
        dataProvider = new ProtocolDataProvider(protocolService,
                query -> protocolService.find(query, filter.getValue()).stream(),
                query -> (int) protocolService.count(filter.getValue()));
        grid.setDataProvider(dataProvider);
        viewLogic = new ProtocolViewLogic(this, protocolService);
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        form = new ProtocolForm(viewLogic);

        final VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);
        add(form);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setId(CUSTOMER_VIEW_FILTER_ID);
        filter.setPlaceholder("Filter code or note");
        filter.addValueChangeListener(event -> dataProvider.refreshAll());
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        newProtocol = new Button("New protocol");
        newProtocol.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newProtocol.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newProtocol.addClickListener(click -> viewLogic.newProtocol());
        newProtocol.addClickShortcut(Key.KEY_N, KeyModifier.ALT);

        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newProtocol);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);

        return topLayout;
    }

    public void showNotification(String msg) {
        Notification.show(msg);
    }

    public void setNewProtocolEnabled(boolean enabled) {
        newProtocol.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Protocol row) {
        grid.getSelectionModel().select(row);
    }

    public void updateProtocol(Protocol protocol) {
        dataProvider.save(protocol);
    }

    public void removeProtocol(Protocol protocol) {
        dataProvider.delete(protocol);
    }

    public void editProtocol(Protocol protocol) {
        showForm(protocol != null);
        form.editProtocol(protocol);
    }

    public void showForm(boolean show) {
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }

}
