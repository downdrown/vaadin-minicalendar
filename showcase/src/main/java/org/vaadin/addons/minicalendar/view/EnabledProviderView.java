package org.vaadin.addons.minicalendar.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.minicalendar.MiniCalendar;
import org.vaadin.addons.minicalendar.MiniCalendarVariant;
import org.vaadin.addons.minicalendar.layout.ShowCaseLayout;

import java.time.LocalDate;
import java.util.List;

import static org.vaadin.addons.minicalendar.i18n.I18NUtils.i18n;

@Route(value = "/enabledprovider", layout = ShowCaseLayout.class)
public class EnabledProviderView extends VerticalLayout implements HasDynamicTitle {

    public EnabledProviderView() {

        var disabledDays = getDisabledDays();

        var miniCalendar = new MiniCalendar();
        miniCalendar.setValue(disabledDays.get(0));
        miniCalendar.addThemeVariants(MiniCalendarVariant.HOVER_DAYS, MiniCalendarVariant.HIGHLIGHT_WEEKEND);
        miniCalendar.setDayEnabledProvider(value -> !disabledDays.contains(value));

        add(miniCalendar);
    }

    @Override
    public String getPageTitle() {
        return i18n("enabledprovider.title");
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
