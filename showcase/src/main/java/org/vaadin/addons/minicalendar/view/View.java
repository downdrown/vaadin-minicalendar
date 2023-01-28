package org.vaadin.addons.minicalendar.view;

import com.vaadin.flow.component.Component;

public enum View {

    Overview(OverviewView.class),
    EnabledProvider(EnabledProviderView.class),
    StyleProvider(StyleProviderview.class);

    private final Class<? extends Component> navigationTarget;

    View(Class<? extends Component> navigationTarget) {
        this.navigationTarget = navigationTarget;
    }

    public Class<? extends Component> getNavigationTarget() {
        return navigationTarget;
    }
}
