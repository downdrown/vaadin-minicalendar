package org.vaadin.addons.minicalendar.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.minicalendar.MiniCalendar;
import org.vaadin.addons.minicalendar.layout.ShowCaseLayout;

import java.time.format.TextStyle;

import static org.vaadin.addons.minicalendar.i18n.I18NUtils.i18n;
import static org.vaadin.addons.minicalendar.util.ComponentUtils.fromDefinition;
import static org.vaadin.addons.minicalendar.util.ComponentUtils.header;

@CssImport("css/other.css")
@Route(value = "/other", layout = ShowCaseLayout.class)
public class OtherExamplesView extends Div implements HasDynamicTitle {

    public OtherExamplesView() {
        setSizeFull();
        renderView();
    }

    private void renderView() {

        var viewLayout = new VerticalLayout(
            calendarWithDayTextStyle()
        );
        viewLayout.setWidth(null);
        viewLayout.setPadding(true);

        var viewLayoutWrapper = new VerticalLayout(viewLayout);
        viewLayoutWrapper.setWidthFull();
        viewLayoutWrapper.setPadding(false);
        viewLayoutWrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        viewLayoutWrapper.setAlignItems(FlexComponent.Alignment.CENTER);

        var scroller = new Scroller(viewLayoutWrapper);
        scroller.setSizeFull();

        add(scroller);
    }

    private static Component calendarWithDayTextStyle() {
        return new VerticalLayout(
            header(i18n("otherexamples.daytextstyle.header")),
            fromDefinition(() -> {
                var miniCalendar = new MiniCalendar();
                miniCalendar.setDayTextStyle(TextStyle.SHORT_STANDALONE);
                miniCalendar.addClassName("rotated-header");
                return miniCalendar;
            })
        );
    }

    @Override
    public String getPageTitle() {
        return i18n("otherexamples.title");
    }
}
