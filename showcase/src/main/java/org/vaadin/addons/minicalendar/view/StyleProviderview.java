package org.vaadin.addons.minicalendar.view;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.minicalendar.MiniCalendar;
import org.vaadin.addons.minicalendar.MiniCalendarVariant;
import org.vaadin.addons.minicalendar.layout.ShowCaseLayout;

import java.time.LocalDate;
import java.util.List;

import static org.vaadin.addons.minicalendar.i18n.I18NUtils.i18n;

@CssImport("css/funky.css")
@Route(value = "/styleprovider", layout = ShowCaseLayout.class)
public class StyleProviderview extends VerticalLayout implements HasDynamicTitle {

    public StyleProviderview() {

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

    @Override
    public String getPageTitle() {
        return i18n("styleprovider.title");
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
