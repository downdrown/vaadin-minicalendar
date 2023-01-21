package org.vaadin.addons.minicalendar;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.select.SelectVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Route("/")
@PageTitle("MiniCalendar Showcase")
public class ShowcaseView extends Div implements BeforeEnterListener {

    private final Set<MiniCalendar> miniCalendars = new HashSet<>();

    private final HasValue.ValueChangeListener<HasValue.ValueChangeEvent<LocalDate>> onDateSelection = event -> {
        if (event.isFromClient()) {
            Notification.show("🗓️ Value changed to " + event.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).localizedBy(UI.getCurrent().getLocale())));
            miniCalendars.forEach(cal -> cal.setValue(event.getValue()));
        }
    };

    public ShowcaseView() {
        setSizeFull();
        renderView();
    }

    private void renderView() {

        var highlightWeekend = new MiniCalendar();
        highlightWeekend.setFirstDayOfWeek(DayOfWeek.SUNDAY);
        highlightWeekend.addThemeVariants(MiniCalendarVariant.HIGHLIGHT_WEEKEND);

        var shiftedWeekStart = new MiniCalendar();
        shiftedWeekStart.setFirstDayOfWeek(DayOfWeek.MONDAY);
        shiftedWeekStart.addThemeVariants(MiniCalendarVariant.HIGHLIGHT_WEEKEND);

        var readOnlyHighlightWeekend = new MiniCalendar();
        readOnlyHighlightWeekend.setFirstDayOfWeek(DayOfWeek.SUNDAY);
        readOnlyHighlightWeekend.addThemeVariants(MiniCalendarVariant.HIGHLIGHT_WEEKEND);
        readOnlyHighlightWeekend.setReadOnly(true);

        var readOnlyShiftedWeekStart = new MiniCalendar();
        readOnlyShiftedWeekStart.setFirstDayOfWeek(DayOfWeek.MONDAY);
        readOnlyShiftedWeekStart.addThemeVariants(MiniCalendarVariant.HIGHLIGHT_WEEKEND);
        readOnlyShiftedWeekStart.setReadOnly(true);

        var disabledHighlightWeekend = new MiniCalendar();
        disabledHighlightWeekend.setFirstDayOfWeek(DayOfWeek.SUNDAY);
        disabledHighlightWeekend.addThemeVariants(MiniCalendarVariant.HIGHLIGHT_WEEKEND);
        disabledHighlightWeekend.setEnabled(false);

        var disabledShiftedWeekStart = new MiniCalendar();
        disabledShiftedWeekStart.setFirstDayOfWeek(DayOfWeek.MONDAY);
        disabledShiftedWeekStart.addThemeVariants(MiniCalendarVariant.HIGHLIGHT_WEEKEND);
        disabledShiftedWeekStart.setEnabled(false);

        var disabledReadOnlyHighlightWeekend = new MiniCalendar();
        disabledReadOnlyHighlightWeekend.setFirstDayOfWeek(DayOfWeek.SUNDAY);
        disabledReadOnlyHighlightWeekend.addThemeVariants(MiniCalendarVariant.HIGHLIGHT_WEEKEND);
        disabledReadOnlyHighlightWeekend.setEnabled(false);
        disabledReadOnlyHighlightWeekend.setReadOnly(true);

        var disabledReadOnlyShiftedWeekStart = new MiniCalendar();
        disabledReadOnlyShiftedWeekStart.setFirstDayOfWeek(DayOfWeek.MONDAY);
        disabledReadOnlyShiftedWeekStart.addThemeVariants(MiniCalendarVariant.HIGHLIGHT_WEEKEND);
        disabledReadOnlyShiftedWeekStart.setEnabled(false);
        disabledReadOnlyShiftedWeekStart.setReadOnly(true);

        var viewLayout = new VerticalLayout(
            controlBar(),
            header("Default"),
            row(miniCalendar("Standard"),
                miniCalendar("Highlight Weekends",
                    highlightWeekend),
                miniCalendar("Shifted beginning of week",
                    shiftedWeekStart),
                miniCalendar("Hover Days",
                    MiniCalendarVariant.HOVER_DAYS),
                miniCalendar("Rounded",
                    MiniCalendarVariant.ROUNDED),
                miniCalendar("Rounded & Highlight Weekends",
                    MiniCalendarVariant.ROUNDED,
                    MiniCalendarVariant.HIGHLIGHT_WEEKEND)),
            header("Read-Only"),
            row(miniCalendar("Standard",
                    true,
                    true),
                miniCalendar("Highlight Weekends",
                    readOnlyHighlightWeekend),
                miniCalendar("Shifted beginning of week",
                    readOnlyShiftedWeekStart),
                miniCalendar("Hover Days",
                    true,
                    true,
                    MiniCalendarVariant.HOVER_DAYS),
                miniCalendar("Rounded",
                    true,
                    true,
                    MiniCalendarVariant.ROUNDED),
                miniCalendar("Rounded & Highlight Weekends",
                    true,
                    true,
                    MiniCalendarVariant.ROUNDED,
                    MiniCalendarVariant.HIGHLIGHT_WEEKEND)),
            header("Disabled"),
            row(miniCalendar("Standard",
                    false),
                miniCalendar("Highlight Weekends",
                    disabledHighlightWeekend),
                miniCalendar("Shifted beginning of week",
                    disabledShiftedWeekStart),
                miniCalendar("Hover Days",
                    false,
                    MiniCalendarVariant.HOVER_DAYS),
                miniCalendar("Rounded",
                    false,
                    MiniCalendarVariant.ROUNDED),
                miniCalendar("Rounded & Highlight Weekends",
                    false,
                    MiniCalendarVariant.ROUNDED,
                    MiniCalendarVariant.HIGHLIGHT_WEEKEND)),
            header("Disabled & Read-Only"),
            row(miniCalendar("Standard",
                    false,
                    true),
                miniCalendar("Highlight Weekends",
                    disabledReadOnlyHighlightWeekend),
                miniCalendar("Shifted beginning of week",
                    disabledReadOnlyShiftedWeekStart),
                miniCalendar("Hover Days",
                    false,
                    true,
                    MiniCalendarVariant.HOVER_DAYS),
                miniCalendar("Rounded",
                    false,
                    true,
                    MiniCalendarVariant.ROUNDED),
                miniCalendar("Rounded & Highlight Weekends",
                    false,
                    true,
                    MiniCalendarVariant.ROUNDED,
                    MiniCalendarVariant.HIGHLIGHT_WEEKEND))
        );

        viewLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        viewLayout.setAlignItems(FlexComponent.Alignment.START);
        viewLayout.setSpacing(true);
        viewLayout.setMargin(false);
        viewLayout.setPadding(true);

        var scroller = new Scroller(viewLayout);
        scroller.setSizeFull();
        add(scroller);
    }

    private static Component controlBar() {

        var localeDataProvider = new ListDataProvider<>(List.of(Locale.getAvailableLocales()));
        localeDataProvider.setSortOrder(Locale::getDisplayName, SortDirection.ASCENDING);

        var localeSelect = new Select<Locale>();
        localeSelect.addThemeVariants(SelectVariant.LUMO_SMALL);
        localeSelect.setItems(localeDataProvider);
        localeSelect.setItemLabelGenerator(Locale::getDisplayName);
        localeSelect.setValue(UI.getCurrent().getLocale());
        localeSelect.addValueChangeListener(e -> UI.getCurrent().setLocale(e.getValue()));

        var themeToggleButton = new Button("Toggle Theme", e -> toggleTheme());
        themeToggleButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        var layout = new HorizontalLayout(themeToggleButton, localeSelect);
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(true);

        return layout;
    }

    private static Component row(Component... items) {
        var layout = new HorizontalLayout(items);
        layout.setMargin(false);
        layout.setSpacing(true);
        return layout;
    }

    private static Component header(String header) {
        var label = new Label(header);
        label.getStyle().set("font-size", "var(--lumo-font-size-l)").set("color", "var(--lumo-primary-text-color)");
        return label;
    }

    private Component miniCalendar(String description, MiniCalendarVariant... variants) {
        return miniCalendar(description, true, false, variants);
    }

    private Component miniCalendar(String description, boolean isEnabled, MiniCalendarVariant... variants) {
        return miniCalendar(description, isEnabled, false, variants);
    }

    private Component miniCalendar(String description, boolean isEnabled, boolean isReadOnly, MiniCalendarVariant... variants) {

        var miniCalendar = new MiniCalendar();
        miniCalendar.addThemeVariants(variants);
        miniCalendar.setEnabled(isEnabled);
        miniCalendar.setReadOnly(isReadOnly);

        return miniCalendar(description, miniCalendar);
    }

    private Component miniCalendar(String description, MiniCalendar miniCalendar) {

        miniCalendar.addValueChangeListener(onDateSelection);
        miniCalendars.add(miniCalendar);

        var label = new Label(description);
        label.getStyle().set("font-size", "var(--lumo-font-size-xs)").set("color", "var(--lumo-secondary-text-color)");

        var layout = new VerticalLayout(label, miniCalendar);
        layout.setMargin(false);
        layout.setPadding(false);

        return layout;
    }

    private static void toggleTheme() {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();
        if (themeList.contains(Lumo.DARK)) {
            themeList.remove(Lumo.DARK);
        } else {
            themeList.add(Lumo.DARK);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        miniCalendars.clear();
        renderView();
    }
}
