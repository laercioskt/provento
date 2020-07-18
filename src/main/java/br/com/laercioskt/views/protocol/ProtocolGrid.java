package br.com.laercioskt.views.protocol;

import br.com.laercioskt.backend.data.Protocol;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;

import static br.com.laercioskt.backend.data.Protocol_.CODE;
import static br.com.laercioskt.backend.data.Protocol_.NOTE;

public class ProtocolGrid extends Grid<Protocol> {

    public ProtocolGrid() {
        setSizeFull();

        addColumn(Protocol::getCode).setHeader("Protocol code")
                .setFlexGrow(20).setSortable(true)
                .setKey(CODE);

        addColumn(Protocol::getNote).setHeader("Protocol note")
                .setFlexGrow(20).setSortable(true)
                .setKey(NOTE);

        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        // TODO we'll add more fields soon
        if (width > 550) {
            getColumnByKey(CODE).setVisible(true);
            getColumnByKey(NOTE).setVisible(true);
        } else {
            getColumnByKey(CODE).setVisible(true);
            getColumnByKey(NOTE).setVisible(true);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // fetch browser width
        UI.getCurrent().getInternals().setExtendedClientDetails(null);
        UI.getCurrent().getPage().retrieveExtendedClientDetails(e -> setColumnVisibility(e.getBodyClientWidth()));
    }

    public void refresh(Protocol protocol) {
        getDataCommunicator().refresh(protocol);
    }

}
