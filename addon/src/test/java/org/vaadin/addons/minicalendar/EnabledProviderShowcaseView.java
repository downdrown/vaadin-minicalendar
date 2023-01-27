package org.vaadin.addons.minicalendar;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;

@Route("/enabledprovider")
@PageTitle("MiniCalendar Showcase")
public class EnabledProviderShowcaseView extends VerticalLayout {

    public EnabledProviderShowcaseView() {

        var disabledDays = getDisabledDays();

        var miniCalendar = new MiniCalendar();
        miniCalendar.setValue(disabledDays.get(0));
        miniCalendar.addThemeVariants(MiniCalendarVariant.HOVER_DAYS, MiniCalendarVariant.HIGHLIGHT_WEEKEND);
        miniCalendar.setDayEnabledProvider(value -> !disabledDays.contains(value));

        add(miniCalendar);
    }

    private List<LocalDate> getDisabledDays() {
        var now = LocalDate.now();
        return List.of(
            now.plusDays(2),
            now.plusDays(3),
            now.plusDays(4),
            now.plusDays(7),
            now.plusDays(8),
            now.plusDays(9),
            now.plusDays(10)
        );
    }
}
