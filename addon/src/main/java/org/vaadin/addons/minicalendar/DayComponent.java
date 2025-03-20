package org.vaadin.addons.minicalendar;

import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Tag(Tag.SPAN)
class DayComponent extends Component implements HasEnabled, ClickNotifier<DayComponent> {

    private static final Duration LONG_INVOCATION_THRESHOLD = Duration.ofMillis(30);
    private static final Logger log = LoggerFactory.getLogger(DayComponent.class);

    private final LocalDate date;
    private final List<MiniCalendarVariant> appliedVariants;
    private final SerializablePredicate<LocalDate> dayEnabledProvider;
    private final SerializableFunction<LocalDate, List<String>> dayStyleProvider;

    DayComponent(
        LocalDate date,
        List<MiniCalendarVariant> appliedVariants,
        SerializablePredicate<LocalDate> dayEnabledProvider,
        SerializableFunction<LocalDate, List<String>> dayStyleProvider
    ) {
        this.date = date;
        this.appliedVariants = appliedVariants;
        this.dayEnabledProvider = dayEnabledProvider;
        this.dayStyleProvider = dayStyleProvider;
        renderComponent();
    }

    void select() {
        setSelected(true);
    }

    void deselect() {
        setSelected(false);
    }

    private void setSelected(boolean selected) {
        if (selected) {
            addClassName(Styles.SELECTED);
        } else {
            removeClassName(Styles.SELECTED);
        }
    }

    private void renderComponent() {
        getElement().setText(String.valueOf(date.getDayOfMonth()));
        addClassName(Styles.DAY);
        applyVariants();
        applyExternalHandlers();
        registerObservers();
    }

    private void applyVariants() {
        appliedVariants.forEach(this::applyVariant);
    }

    private void applyVariant(MiniCalendarVariant variant) {
        if (variant.canBeAppliedFor(date)) {
            addClassName(variant.getVariantName());
        }
    }

    private void registerObservers() {
        addListener(ReadOnlyStateChangeEvent.class, event -> {
            if (event.isReadOnly()) {
                addClassName(Styles.READONLY);
            } else {
                removeClassName(Styles.READONLY);
            }
        });
    }

    private void applyExternalHandlers() {
        final var dayIsEnabled = checkIfDayIsEnabled();
        setEnabled(dayIsEnabled);
        if (!dayIsEnabled) {
            addClassName(Styles.DISABLED);
        }

        final var additionalStyles = checkIfAdditionalStylesAreApplied();
        if (additionalStyles != null && !additionalStyles.isEmpty()) {
            additionalStyles.forEach(additionalClassName -> {
                if (StringUtils.isNotBlank(additionalClassName)) {
                    addClassName(additionalClassName);
                }
            });
        }
    }

    private boolean checkIfDayIsEnabled() {
        if (dayEnabledProvider == null) {
            return true;
        }
        var result = true;

        final var invocationStart = System.currentTimeMillis();
        result = dayEnabledProvider.test(date);
        final var invocationEnd = System.currentTimeMillis();

        final var invocationDuration = Duration.ofMillis(invocationEnd - invocationStart);
        if (invocationDuration.compareTo(LONG_INVOCATION_THRESHOLD) > 0) {
            log.warn("Slow dayEnabledProvider call detected! Invocation took {}ms, threshold is {}", invocationDuration, LONG_INVOCATION_THRESHOLD);
        }

        return result;
    }

    private List<String> checkIfAdditionalStylesAreApplied() {
        if (dayStyleProvider == null) {
            return Collections.emptyList();
        }

        List<String> result;

        final var invocationStart = System.currentTimeMillis();
        result = dayStyleProvider.apply(date);
        final var invocationEnd = System.currentTimeMillis();

        final var invocationDuration = Duration.ofMillis(invocationEnd - invocationStart);
        if (invocationDuration.compareTo(LONG_INVOCATION_THRESHOLD) > 0) {
            log.warn("Slow dayStyleProvider call detected! Invocation took {}ms, threshold is {}", invocationDuration, LONG_INVOCATION_THRESHOLD);
        }

        return result;
    }

    private boolean isToday() {
        return date.equals(LocalDate.now());
    }

    private boolean isWeekend() {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
