package org.vaadin.addons.minicalendar;

import com.vaadin.flow.component.ComponentEvent;

class ReadOnlyStateChangeEvent extends ComponentEvent<MiniCalendar> {
    public ReadOnlyStateChangeEvent(MiniCalendar source, boolean fromClient) {
        super(source, fromClient);
    }

    boolean isReadOnly() {
        return getSource().isReadOnly();
    }
}
