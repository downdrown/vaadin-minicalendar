package org.vaadin.addons.minicalendar.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.minicalendar.MiniCalendar;
import org.vaadin.addons.minicalendar.MiniCalendarVariant;
import org.vaadin.addons.minicalendar.layout.ShowCaseLayout;

import java.time.format.TextStyle;

import static org.vaadin.addons.minicalendar.i18n.I18NUtils.i18n;
import static org.vaadin.addons.minicalendar.util.ComponentUtils.fromDefinition;
import static org.vaadin.addons.minicalendar.util.ComponentUtils.header;

@CssImport("./css/other.css")
@Route(value = "/other", layout = ShowCaseLayout.class)
public class OtherExamplesView extends Div implements HasDynamicTitle {

    public OtherExamplesView() {
        setSizeFull();
        renderView();
    }

    private void renderView() {

        var viewLayout = new HorizontalLayout(
            calendarWithDayTextStyle(),
            calendarWithDeselectionDisallowed(),
            calendarWithWeekDisplay()
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

    private static Component calendarWithDeselectionDisallowed() {
        return new VerticalLayout(
            header(i18n("otherexamples.deselectiondisallowed.header")),
            fromDefinition(() -> {
                var miniCalendar = new MiniCalendar();
                miniCalendar.setAllowDeselection(false);
                return miniCalendar;
            })
        );
    }

    private static Component calendarWithWeekDisplay() {
        return new VerticalLayout(
            header(i18n("otherexamples.weekdisplay.header")),
            fromDefinition(() -> {
                var miniCalendar = new MiniCalendar();
                miniCalendar.addThemeVariants(MiniCalendarVariant.HIGHLIGHT_CURRENT_DAY);
                miniCalendar.setShowWeekNumbers(true);
                miniCalendar.setWeekNumberPrefix("KW");
                return miniCalendar;
            })
        );
    }

    @Override
    public String getPageTitle() {
        return i18n("otherexamples.title");
    }
}
