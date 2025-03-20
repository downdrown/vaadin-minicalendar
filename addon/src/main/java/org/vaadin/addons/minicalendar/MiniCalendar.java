package org.vaadin.addons.minicalendar;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.HasThemeVariant;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoIcon;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A small calendar component that can be used to let users select {@link LocalDate} values.
 * The component also supports read-only and disabled states and can be customized in various ways.
 *
 * @author Manfred Huber
 */
@CssImport("./minicalendar.css")
public class MiniCalendar extends CustomField<LocalDate> implements HasThemeVariant<MiniCalendarVariant>, LocaleChangeObserver {

    private final VerticalLayout content = new VerticalLayout();
    private final List<MiniCalendarVariant> appliedVariants = new ArrayList<>(MiniCalendarVariant.values().length);
    private final YearMonthHolder yearMonthHolder = new YearMonthHolder();
    private final MiniCalendarConfiguration configuration = new MiniCalendarConfiguration();
    private DayComponent selectedComponent = null;

    /* External Handlers */

    private SerializablePredicate<LocalDate> dayEnabledProvider = null;
    private SerializableFunction<LocalDate, List<String>> dayStyleProvider = null;


    /* Constructors */

    public MiniCalendar() {
        this(LocalDate.now());
    }

    public MiniCalendar(LocalDate localDate) {
        this(YearMonth.from(localDate));
    }

    public MiniCalendar(YearMonth yearMonth) {

        this.yearMonthHolder.setValue(yearMonth);
        yearMonthHolder.addValueChangeListener(e -> redraw());

        content.addClassName(Styles.BASE);
        content.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        content.setSpacing(false);
        content.setPadding(false);
        content.setMargin(false);
        add(content);

        renderComponent();
    }


    /* Overrides */

    @Override
    public void setValue(LocalDate newValue) {
        final var redrawRequired = !isToday(newValue);
        super.setValue(newValue);
        if (newValue != null) {
            yearMonthHolder.setValue(YearMonth.from(newValue));
        }
        if (redrawRequired) {
            redraw();
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        fireEvent(new ReadOnlyStateChangeEvent(this, false));
    }

    @Override
    public void addThemeVariants(MiniCalendarVariant... variants) {
        HasThemeVariant.super.addThemeVariants(variants);
        appliedVariants.addAll(Set.of(variants));
        redraw();
    }

    @Override
    public void removeThemeVariants(MiniCalendarVariant... variants) {
        HasThemeVariant.super.removeThemeVariants(variants);
        appliedVariants.removeAll(Set.of(variants));
        redraw();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        redraw();
    }

    @Override
    protected LocalDate generateModelValue() {
        return getValue();
    }

    @Override
    protected void setPresentationValue(LocalDate localDate) {
        setModelValue(localDate, false);
    }


    /* Public API */

    public void setFirstDayOfWeek(DayOfWeek firstDayOfWeek) {
        configuration.setFirstDayOfWeek(firstDayOfWeek);
        redraw();
    }

    public void setYearMonth(YearMonth yearMonth) {
        yearMonthHolder.setValue(yearMonth);
    }

    public Registration addYearMonthChangeListener(ValueChangeListener<ValueChangeEvent<YearMonth>> listener) {
        return yearMonthHolder.addValueChangeListener(listener);
    }

    public void setDayTextStyle(TextStyle dayTextStyle) {
        configuration.setDayTextStyle(dayTextStyle);
        redraw();
    }

    public void setMonthTextStyle(TextStyle monthTextStyle) {
        configuration.setMonthTextStyle(monthTextStyle);
        redraw();
    }

    public void setAllowDeselection(boolean allowDeselection) {
        configuration.setAllowDeselection(allowDeselection);
        redraw();
    }

    public Registration setDayEnabledProvider(SerializablePredicate<LocalDate> dayEnabledProvider) {
        this.dayEnabledProvider = dayEnabledProvider;
        redraw();
        return () -> {
            this.dayEnabledProvider = null;
            redraw();
        };
    }

    public Registration setDayStyleProvider(SerializableFunction<LocalDate, List<String>> dayStyleProvider) {
        this.dayStyleProvider = dayStyleProvider;
        redraw();
        return () -> {
            this.dayStyleProvider = null;
            redraw();
        };
    }


    /* Internal API */

    private void redraw() {
        resetComponent();
        renderComponent();
    }

    private void resetComponent() {
        content.removeAll();
        selectedComponent = null;
    }

    private void renderComponent() {
        renderTitle();
        renderHeaderRow();
        renderDayRows();
    }

    private void renderTitle() {
        content.add(makeTitleLayout());
    }

    private void renderHeaderRow() {

        var weekDays = new ArrayList<Span>(7);
        var day = configuration.getFirstDayOfWeek();

        do {
            Span weekDay = span(day.getDisplayName(configuration.getDayTextStyle(), getLocale()));
            weekDay.addClassName(Styles.WEEKDAY);
            weekDays.add(weekDay);
            day = day.plus(1);
        } while (day != configuration.getFirstDayOfWeek());

        addRow(weekDays);
    }

    private void renderDayRows() {

        var dayComponents = new ArrayList<Component>(7);
        var dayOfWeekOfFirstDayInMonth = yearMonthHolder.getValue().atDay(1).getDayOfWeek();
        var dayIterator = configuration.getFirstDayOfWeek();

        // Fill empty days before first day of month
        while (dayIterator != dayOfWeekOfFirstDayInMonth) {
            dayComponents.add(emptySpan());
            dayIterator = dayIterator.plus(1);
        }

        // Add actual days to the calendar view
        for (int dayOfMonth = 1; dayOfMonth <= getLastDayOfMonth(yearMonthHolder.getValue()); dayOfMonth++) {

            if (dayComponents.size() == 7) {
                addRow(dayComponents);
                dayComponents.clear();
            }

            var day = yearMonthHolder.getValue().atDay(dayOfMonth);
            var dayComponent = makeDayComponent(day);
            dayComponents.add(dayComponent);
        }

        // Fill empty days after last day of month
        while (dayComponents.size() < 7) {
            dayComponents.add(emptySpan());
        }

        addRow(dayComponents);
    }


    /* Component factory API */

    private Button makeButton(Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
        final var button = new Button(icon, clickListener);
        button.addThemeVariants(ButtonVariant.LUMO_SMALL);
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return button;
    }

    private Button makePreviousMonthButton() {
        final var button = makeButton(LumoIcon.ANGLE_LEFT.create(), event -> navigateToPreviousMonth());
        button.setVisible(!isReadOnly());
        return button;
    }

    private Button makeNextMonthButton() {
        final var button = makeButton(LumoIcon.ANGLE_RIGHT.create(), event -> navigateToNextMonth());
        button.setVisible(!isReadOnly());
        return button;
    }

    private Span makeMonthTitle() {
        final var monthTitle = new Span(yearMonthHolder.getValue().getMonth().getDisplayName(configuration.getMonthTextStyle(), getLocale()));
        monthTitle.addClassName("title");
        if (isReadOnly()) {
            monthTitle.addClassName(Styles.READONLY);
        }
        return monthTitle;
    }

    private Span makeYearTitle() {
        final var yearTitle = new Span(String.valueOf(yearMonthHolder.getValue().getYear()));
        yearTitle.addClassName("title");
        if (isReadOnly()) {
            yearTitle.addClassName(Styles.READONLY);
        }
        return yearTitle;
    }

    private Component makeMonthYearTitleLayout() {

        final var monthTitle = makeMonthTitle();
        final var yearTitle = makeYearTitle();

        var monthYearTitleLayout = new HorizontalLayout(monthTitle, yearTitle);
        monthYearTitleLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        monthYearTitleLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        monthYearTitleLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        monthYearTitleLayout.setWidth(null);
        monthYearTitleLayout.setMargin(false);
        monthYearTitleLayout.setPadding(false);
        monthYearTitleLayout.setSpacing(true);

        monthTitle.addClickListener(event -> {
            if (isInteractionDisabled()) {
                return;
            }
            var monthSelection = makeMonthSelectionComponent();
            monthYearTitleLayout.replace(monthTitle, monthSelection);
        });

        addListener(ReadOnlyStateChangeEvent.class, event -> {
            final var isEnabled = !event.isReadOnly();
            monthTitle.setEnabled(isEnabled);
            yearTitle.setEnabled(isEnabled);
            toggleStyle(yearTitle, Styles.READONLY);
        });

        yearTitle.addClickListener(event -> {
            if (isInteractionDisabled()) {
                return;
            }
            var yearSelection = makeYearSelectionComponent();
            monthYearTitleLayout.replace(yearTitle, yearSelection);
        });

        return monthYearTitleLayout;
    }

    private Component makeTitleLayout() {

        final var previousMonthButton = makePreviousMonthButton();
        final var nextMonthButton = makeNextMonthButton();
        final var  monthYearTitleLayout = makeMonthYearTitleLayout();

        var titleLayout = new HorizontalLayout(previousMonthButton, monthYearTitleLayout, nextMonthButton);
        titleLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        titleLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        titleLayout.setWidthFull();
        titleLayout.setSpacing(true);
        titleLayout.setHeight(30, Unit.PIXELS);
        titleLayout.expand(monthYearTitleLayout);

        addListener(ReadOnlyStateChangeEvent.class, event -> {
            final var isVisible = !event.isReadOnly();
            previousMonthButton.setVisible(isVisible);
            nextMonthButton.setVisible(isVisible);
        });

        return titleLayout;
    }

    private Component makeMonthSelectionComponent() {

        var monthSelect = new ComboBox<Month>();
        monthSelect.setItemLabelGenerator(month -> month.getDisplayName(configuration.getMonthTextStyle(), getLocale()));
        monthSelect.setMaxWidth(4, Unit.REM);
        monthSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        monthSelect.setItems(Month.values());
        monthSelect.setValue(yearMonthHolder.getValue().getMonth());
        monthSelect.setOpened(true);
        monthSelect.getStyle()
            .set("--vaadin-combo-box-overlay-width", "10rem")
            .set("--vaadin-combo-box-overlay-max-height", "8rem");

        monthSelect.addValueChangeListener(event ->
            yearMonthHolder.setValueFromClient(Year.of(yearMonthHolder.getValue().getYear()).atMonth(event.getValue()))
        );

        return monthSelect;
    }

    private Component makeYearSelectionComponent() {

        var yearSelect = new ComboBox<Year>();
        yearSelect.setMaxWidth(4, Unit.REM);
        yearSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        yearSelect.setItems(evaluateEligibleYears());
        yearSelect.setAllowCustomValue(true);
        yearSelect.setAllowedCharPattern("\\d");
        yearSelect.setValue(Year.of(yearMonthHolder.getValue().getYear()));
        yearSelect.setOpened(true);
        yearSelect.getStyle()
            .set("--vaadin-combo-box-overlay-width", "8rem")
            .set("--vaadin-combo-box-overlay-max-height", "8rem");

        yearSelect.addCustomValueSetListener(event -> {
            var selectedYear = Year.parse(event.getDetail());
            yearSelect.setValue(selectedYear);
        });
        yearSelect.addValueChangeListener(event ->
            yearMonthHolder.setValueFromClient(event.getValue().atMonth(yearMonthHolder.getValue().getMonth()))
        );

        return yearSelect;
    }

    private Component makeDayComponent(LocalDate forDay) {
        final var component = new DayComponent(
            forDay,
            appliedVariants,
            dayEnabledProvider,
            dayStyleProvider
        );

        if (isReadOnly()) {
            component.addClassName(Styles.READONLY);
        }

        if (isToday(forDay)) {
            component.select();
            selectedComponent = component;
        }

        component.addClickListener(event -> {

            if (isInteractionDisabled()) {
                return;
            }

            if (configuration.isAllowDeselection() && selectedComponent == event.getSource()) {
                selectedComponent.deselect();
                selectedComponent = null;
                setModelValue(null, true);
                return;
            }

            if (selectedComponent != null) {
                selectedComponent.deselect();
            }

            selectedComponent = event.getSource();
            selectedComponent.select();

            setModelValue(forDay, true);
        });

        return component;
    }

    private boolean isToday(LocalDate date) {
        return Objects.equals(getValue(), date);
    }

    private void navigateToPreviousMonth() {
        yearMonthHolder.setValueFromClient(yearMonthHolder.getValue().minusMonths(1));
    }

    private void navigateToNextMonth() {
        yearMonthHolder.setValueFromClient(yearMonthHolder.getValue().plusMonths(1));
    }


    /* Utilities */

    private List<Year> evaluateEligibleYears() {

        var minYear = yearMonthHolder.getValue().getYear() - 10;
        var maxYear = yearMonthHolder.getValue().getYear() + 10;
        var eligibleYears = new ArrayList<Year>(201);

        for (int year = minYear; year < maxYear; year++) {
            eligibleYears.add(Year.of(year));
        }

        return Collections.unmodifiableList(eligibleYears);
    }

    private void addRow(List<? extends Component> columns) {

        var rowLayout = new FlexLayout();
        rowLayout.setFlexDirection(FlexLayout.FlexDirection.ROW);
        rowLayout.setFlexWrap(FlexLayout.FlexWrap.NOWRAP);
        rowLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        rowLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        for (Component column : columns) {
            rowLayout.add(column);
        }

        content.add(rowLayout);
    }

    private boolean isInteractionDisabled() {
        return isReadOnly() || !isEnabled();
    }

    private static void toggleStyle(HasStyle hasStyle, String className) {
        if (hasStyle.hasClassName(className)) {
            hasStyle.removeClassName(className);
        } else {
            hasStyle.addClassName(className);
        }
    }

    private static Span emptySpan() {
        return span("");
    }

    private static Span span(String text) {
        var span = new Span(text);
        span.setHeight(30, Unit.PIXELS);
        span.setWidth(30, Unit.PIXELS);
        span.getStyle().set("margin", "1px");
        return span;
    }

    private static int getLastDayOfMonth(YearMonth yearMonth) {
        return yearMonth.atEndOfMonth().getDayOfMonth();
    }

    private static final class YearMonthHolder implements HasValue<ValueChangeEvent<YearMonth>, YearMonth> {

        private final HasValue<?, YearMonth> instance = this;
        private final List<ValueChangeListener<? super ValueChangeEvent<YearMonth>>> valueChangeListeners = new ArrayList<>();
        private YearMonth value;

        void setValueFromClient(YearMonth value) {
            final var oldValue = this.value;
            this.value = value;
            fireYearMonthValueChangeEvent(oldValue, value, true);
        }

        @Override
        public void setValue(YearMonth value) {
            final var oldValue = this.value;
            this.value = value;
            fireYearMonthValueChangeEvent(oldValue, value, false);
        }

        @Override
        public YearMonth getValue() {
            return value;
        }

        @Override
        public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<YearMonth>> valueChangeListener) {
            valueChangeListeners.add(valueChangeListener);
            return () -> valueChangeListeners.remove(valueChangeListener);
        }

        private void fireYearMonthValueChangeEvent(YearMonth oldValue, YearMonth newValue, boolean fromClient) {

            if (Objects.equals(oldValue, newValue)) {
                return;
            }

            final var event = new ValueChangeEvent<YearMonth>() {
                @Override
                public HasValue<?, YearMonth> getHasValue() {
                    return instance;
                }

                @Override
                public boolean isFromClient() {
                    return fromClient;
                }

                @Override
                public YearMonth getOldValue() {
                    return oldValue;
                }

                @Override
                public YearMonth getValue() {
                    return newValue;
                }
            };

            valueChangeListeners.forEach(listener -> listener.valueChanged(event));
        }

        @Override
        public void setReadOnly(boolean b) {
            throw new UnsupportedOperationException("not implemented");
        }

        @Override
        public boolean isReadOnly() {
            throw new UnsupportedOperationException("not implemented");
        }

        @Override
        public void setRequiredIndicatorVisible(boolean b) {
            throw new UnsupportedOperationException("not implemented");
        }

        @Override
        public boolean isRequiredIndicatorVisible() {
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
