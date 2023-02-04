package org.vaadin.addons.minicalendar.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.vaadin.addons.minicalendar.Constants;
import org.vaadin.addons.minicalendar.MiniCalendar;
import org.vaadin.addons.minicalendar.MiniCalendarVariant;
import org.vaadin.addons.minicalendar.layout.ShowCaseLayout;
import org.vaadin.addons.minicalendar.util.ComponentUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.Set;

import static org.vaadin.addons.minicalendar.MiniCalendarVariant.HIGHLIGHT_WEEKEND;
import static org.vaadin.addons.minicalendar.MiniCalendarVariant.HOVER_DAYS;
import static org.vaadin.addons.minicalendar.MiniCalendarVariant.ROUNDED;
import static org.vaadin.addons.minicalendar.i18n.I18NUtils.i18n;

@Route(value = "/", layout = ShowCaseLayout.class)
@RouteAlias(value = "/overview", layout = ShowCaseLayout.class)
public class OverviewView extends Div implements HasDynamicTitle {

    /* State Vars */
    private boolean syncValueChanges = false;
    private boolean syncYearMonthChanges = false;

    private final Set<MiniCalendar> miniCalendars = new HashSet<>();
    private final HasValue.ValueChangeListener<HasValue.ValueChangeEvent<LocalDate>> onDateSelection = event -> {
        if (event.isFromClient()) {

            final var notification = i18n(
                "overview.notification.value-change",
                event.getValue().format(
                    DateTimeFormatter
                        .ofLocalizedDate(FormatStyle.FULL)
                        .localizedBy(UI.getCurrent().getLocale())
                )
            );
            Notification.show(notification, Constants.DEFAULT_NOTIFICATION_DURATION, Notification.Position.TOP_END);
            if (syncValueChanges) {
                miniCalendars.forEach(cal -> cal.setValue(event.getValue()));
            }
        }
    };

    private final HasValue.ValueChangeListener<HasValue.ValueChangeEvent<YearMonth>> onYearMonthSelection = event -> {
        if (event.isFromClient()) {
            final var notification = i18n(
                "overview.notification.month-year-change",
                event.getValue().getMonth().getDisplayName(TextStyle.FULL, getLocale()),
                event.getValue().getYear()
            );
            Notification.show(notification, Constants.DEFAULT_NOTIFICATION_DURATION, Notification.Position.TOP_END);
            if (syncYearMonthChanges) {
                miniCalendars.forEach(cal -> cal.setYearMonth(event.getValue()));
            }
        }
    };

    public OverviewView() {
        setSizeFull();
        renderView();
        miniCalendars.forEach(miniCalendar -> miniCalendar.setValue(LocalDate.now()));
    }

    @Override
    public String getPageTitle() {
        return i18n("overview.title");
    }

    private void renderView() {

        var highlightWeekend = new MiniCalendar();
        highlightWeekend.setFirstDayOfWeek(DayOfWeek.SUNDAY);
        highlightWeekend.addThemeVariants(HIGHLIGHT_WEEKEND);

        var shiftedWeekStart = new MiniCalendar();
        shiftedWeekStart.setFirstDayOfWeek(DayOfWeek.MONDAY);
        shiftedWeekStart.addThemeVariants(HIGHLIGHT_WEEKEND);

        var readOnlyHighlightWeekend = new MiniCalendar();
        readOnlyHighlightWeekend.setFirstDayOfWeek(DayOfWeek.SUNDAY);
        readOnlyHighlightWeekend.addThemeVariants(HIGHLIGHT_WEEKEND);
        readOnlyHighlightWeekend.setReadOnly(true);

        var readOnlyShiftedWeekStart = new MiniCalendar();
        readOnlyShiftedWeekStart.setFirstDayOfWeek(DayOfWeek.MONDAY);
        readOnlyShiftedWeekStart.addThemeVariants(HIGHLIGHT_WEEKEND);
        readOnlyShiftedWeekStart.setReadOnly(true);

        var disabledHighlightWeekend = new MiniCalendar();
        disabledHighlightWeekend.setFirstDayOfWeek(DayOfWeek.SUNDAY);
        disabledHighlightWeekend.addThemeVariants(HIGHLIGHT_WEEKEND);
        disabledHighlightWeekend.setEnabled(false);

        var disabledShiftedWeekStart = new MiniCalendar();
        disabledShiftedWeekStart.setFirstDayOfWeek(DayOfWeek.MONDAY);
        disabledShiftedWeekStart.addThemeVariants(HIGHLIGHT_WEEKEND);
        disabledShiftedWeekStart.setEnabled(false);

        var disabledReadOnlyHighlightWeekend = new MiniCalendar();
        disabledReadOnlyHighlightWeekend.setFirstDayOfWeek(DayOfWeek.SUNDAY);
        disabledReadOnlyHighlightWeekend.addThemeVariants(HIGHLIGHT_WEEKEND);
        disabledReadOnlyHighlightWeekend.setEnabled(false);
        disabledReadOnlyHighlightWeekend.setReadOnly(true);

        var disabledReadOnlyShiftedWeekStart = new MiniCalendar();
        disabledReadOnlyShiftedWeekStart.setFirstDayOfWeek(DayOfWeek.MONDAY);
        disabledReadOnlyShiftedWeekStart.addThemeVariants(HIGHLIGHT_WEEKEND);
        disabledReadOnlyShiftedWeekStart.setEnabled(false);
        disabledReadOnlyShiftedWeekStart.setReadOnly(true);

        final var standard =  i18n("overview.variant.standard");
        final var highlightWeekends =  i18n("overview.variant.highlight-weekends");
        final var shiftedBeginningOfWeek =  i18n("overview.variant.shifted-beginning-of-week");
        final var hoverDays =  i18n("overview.variant.hover-days");
        final var rounded =  i18n("overview.variant.rounded");
        final var roundedHighlightWeekends =  i18n("overview.variant.rounded-highlight-weekends");

        var viewLayout = new VerticalLayout(
            ComponentUtils.header(i18n("overview.style.default")),
            row(miniCalendar(standard),
                miniCalendar(highlightWeekends, highlightWeekend),
                miniCalendar(shiftedBeginningOfWeek, shiftedWeekStart),
                miniCalendar(hoverDays, HOVER_DAYS),
                miniCalendar(rounded, ROUNDED),
                miniCalendar(roundedHighlightWeekends, ROUNDED, HIGHLIGHT_WEEKEND)
            ),

            ComponentUtils.header(i18n("overview.style.readonly")),
            row(miniCalendar(standard, true, true),
                miniCalendar(highlightWeekends, readOnlyHighlightWeekend),
                miniCalendar(shiftedBeginningOfWeek, readOnlyShiftedWeekStart),
                miniCalendar(hoverDays, true, true, HOVER_DAYS),
                miniCalendar(rounded, true, true, ROUNDED),
                miniCalendar(roundedHighlightWeekends, true, true, ROUNDED, HIGHLIGHT_WEEKEND)
            ),

            ComponentUtils.header(i18n("overview.style.disabled")),
            row(miniCalendar(standard, false),
                miniCalendar(highlightWeekends, disabledHighlightWeekend),
                miniCalendar(shiftedBeginningOfWeek, disabledShiftedWeekStart),
                miniCalendar(hoverDays, false, HOVER_DAYS),
                miniCalendar(rounded, false, ROUNDED),
                miniCalendar(roundedHighlightWeekends, false, ROUNDED, HIGHLIGHT_WEEKEND)),

            ComponentUtils.header(i18n("overview.style.disabled-readonly")),
            row(miniCalendar(standard, false, true),
                miniCalendar(highlightWeekends, disabledReadOnlyHighlightWeekend),
                miniCalendar(shiftedBeginningOfWeek, disabledReadOnlyShiftedWeekStart),
                miniCalendar(hoverDays, false, true, HOVER_DAYS),
                miniCalendar(rounded, false, true, ROUNDED),
                miniCalendar(roundedHighlightWeekends, false, true, ROUNDED, HIGHLIGHT_WEEKEND)
            )
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

        var splitLayout = new SplitLayout(controlBar(), scroller);
        splitLayout.setSizeFull();
        splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);

        add(splitLayout);
    }

    private Component controlBar() {

        var todayAsValueButton = new Button(i18n("overview.controls.today-as-value"));
        todayAsValueButton.addClickListener(e -> miniCalendars.forEach(miniCalendar -> miniCalendar.setValue(LocalDate.now())));
        todayAsValueButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        var clearValuesButton = new Button(i18n("overview.controls.clear-values"));
        clearValuesButton.addClickListener(e -> miniCalendars.forEach(miniCalendar -> miniCalendar.setValue(null)));
        clearValuesButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        var syncValueChangeCheckbox = new Checkbox(i18n("overview.controls.sync-value-changes"), syncValueChanges);
        syncValueChangeCheckbox.addValueChangeListener(e -> syncValueChanges = e.getValue());
        var syncYearMonthChangeCheckbox = new Checkbox(i18n("overview.controls.sync-month-year-changes"), syncYearMonthChanges);
        syncYearMonthChangeCheckbox.addValueChangeListener(e -> syncYearMonthChanges = e.getValue());

        var layout = new HorizontalLayout(
            todayAsValueButton,
            clearValuesButton,
            syncValueChangeCheckbox,
            syncYearMonthChangeCheckbox
        );
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setPadding(true);
        layout.setSpacing(true);

        return layout;
    }

    private static Component row(Component... items) {
        var layout = new HorizontalLayout(items);
        layout.setMargin(false);
        layout.setSpacing(true);
        return layout;
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
        miniCalendar.addYearMonthChangeListener(onYearMonthSelection);
        miniCalendar.setTooltipText(description);
        miniCalendars.add(miniCalendar);

        return miniCalendar;
    }
}
