package org.vaadin.addons.minicalendar;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;

@Route("/")
public class ShowCaseLayout extends AppLayout {

    public ShowCaseLayout() {
        setContent(new Label("it works"));
    }
}
