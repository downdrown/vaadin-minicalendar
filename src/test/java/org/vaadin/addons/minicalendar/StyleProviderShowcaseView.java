package org.vaadin.addons.minicalendar;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;

@Route("/styleprovider")
@PageTitle("MiniCalendar Showcase")
@CssImport("css/funky.css")
public class StyleProviderShowcaseView extends VerticalLayout {

    public StyleProviderShowcaseView() {

        var funkyDays = getFunkyDays();

        var miniCalendar = new MiniCalendar();
        miniCalendar.setValue(funkyDays.get(0));
        miniCalendar.addThemeVariants(MiniCalendarVariant.HOVER_DAYS, MiniCalendarVariant.HIGHLIGHT_WEEKEND);
        miniCalendar.setDayStyleProvider(day -> {
            if (funkyDays.contains(day)) {
                return List.of("funky", "bounce");
            }
            return null;
        });

        add(miniCalendar);
    }

    private List<LocalDate> getFunkyDays() {
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
