package org.vaadin.addons.minicalendar;

import com.vaadin.flow.component.shared.ThemeVariant;

/**
 * Additional themings that can be applied to the {@link MiniCalendar}.
 *
 * @author Manfred Huber
 */
public enum MiniCalendarVariant implements ThemeVariant {

    /** Highlight the background of weekend days. */
    HIGHLIGHT_WEEKEND("highlight-weekend"),
    /** Show the background of week days rounded. */
    ROUNDED("rounded"),
    /** Raise the week days when hovering. */
    HOVER_DAYS("hover");

    private final String variant;

    MiniCalendarVariant(String variant) {
        this.variant = variant;
    }

    @Override
    public String getVariantName() {
        return variant;
    }
}
