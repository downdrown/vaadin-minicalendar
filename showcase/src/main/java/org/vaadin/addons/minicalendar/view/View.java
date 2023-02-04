package org.vaadin.addons.minicalendar.view;

import com.vaadin.flow.component.Component;

public enum View {

    Overview(OverviewView.class, "nav.overview"),
    EnabledProvider(EnabledProviderView.class, "nav.enabled-provider"),
    StyleProvider(StyleProviderview.class, "nav.style-provider"),
    OtherExamples(OtherExamplesView.class, "nav.other-examples");

    private final Class<? extends Component> navigationTarget;
    private final String i18nKey;

    View(Class<? extends Component> navigationTarget, String i18nKey) {
        this.navigationTarget = navigationTarget;
        this.i18nKey = i18nKey;
    }

    public Class<? extends Component> getNavigationTarget() {
        return navigationTarget;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}
