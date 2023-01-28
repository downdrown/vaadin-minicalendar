package org.vaadin.addons.minicalendar.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.select.SelectVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.HighlightAction;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;
import elemental.json.impl.JreJsonString;
import org.vaadin.addons.minicalendar.Configuration;
import org.vaadin.addons.minicalendar.view.View;

import java.util.List;
import java.util.Locale;

import static org.vaadin.addons.minicalendar.i18n.I18NUtils.i18n;

@CssImport("css/showcase.css")
@JsModule("js/os-theme-module.js")
@PreserveOnRefresh
public class ShowCaseLayout extends AppLayout {

    private SerializableConsumer<String> onThemeSelectionChanged = theme -> {};

    public ShowCaseLayout() {
        initEnvironment();
        addToNavbar(navigation());
        addToDrawer(controls());
        setDrawerOpened(false);
    }

    private void initEnvironment() {
        UI.getCurrent().getPage()
            .executeJs("return getUserPreferredColorScheme()")
            .then(JreJsonString.class, this::setUserPreferredTheme);
    }

    private void setUserPreferredTheme(JreJsonString userPreferredTheme) {
        if (userPreferredTheme.asString().equals(Lumo.DARK)) {
            onThemeSelectionChanged.accept(Lumo.DARK);
        }
    }

    private static Component navigation() {

        final var navigationLayout = new HorizontalLayout(
            drawerToggle(),
            title(),
            tabs(),
            buyMeACoffee()
        );

        navigationLayout.setWidthFull();
        navigationLayout.setMinHeight(60, Unit.PIXELS);
        navigationLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        navigationLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        navigationLayout.setSpacing(true);
        navigationLayout.setPadding(true);

        return navigationLayout;
    }

    private static Component drawerToggle() {
        var toggle = new DrawerToggle();
        toggle.addThemeVariants(ButtonVariant.LUMO_SMALL);
        toggle.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.setIcon(VaadinIcon.MENU.create());
        return toggle;
    }

    private static Component title() {
        var title = new H1(i18n("nav.title"));
        title.addClassName("navtitle");
        return title;
    }

    private static Component tabs() {

        var tabs = new Tabs();
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setAutoselect(false);
        tabs.getStyle()
            .set("margin", "auto");
        tabs.add(
            tab(tabs, i18n("nav.overview"), View.Overview),
            tab(tabs, i18n("nav.enabled-provider"), View.EnabledProvider),
            tab(tabs, i18n("nav.style-provider"), View.StyleProvider)
        );

        return tabs;
    }

    private static Tab tab(Tabs tabs, String viewName, View view) {

        var link = new RouterLink(view.getNavigationTarget());
        link.setHighlightCondition(HighlightConditions.sameLocation());
        link.add(viewName);
        link.setTabIndex(-1);

        var tab = new Tab(link);

        link.setHighlightAction((HighlightAction<RouterLink>) (routerLink, highlight) -> {
            if (highlight) {
                tabs.setSelectedTab(tab);
            }
        });

        return tab;
    }

    private static Component buyMeACoffee() {

        final var conf = Configuration.INSTANCE;

        var buyMeACoffeeImage = new Image(
            conf.getBuyMeACoffeeImageUrl(),
            conf.getBuyMeACoffeeImageAltText()
        );
        buyMeACoffeeImage.setMaxHeight(40, Unit.PIXELS);
        buyMeACoffeeImage.addClassName("pulse");

        return new Anchor(conf.getBuyMeACoffeeCoffeeUrl(), buyMeACoffeeImage);
    }

    private Component controls() {

        var controlsLayout = new VerticalLayout(
            localeSelect(),
            themeSelect()
        );
        controlsLayout.setPadding(true);
        controlsLayout.setSpacing(true);

        return controlsLayout;
    }

    private static Component localeSelect() {

        var currentLocale = UI.getCurrent().getLocale();

        var localeDataProvider = new ListDataProvider<>(List.of(Locale.getAvailableLocales()));
        localeDataProvider.setSortOrder(Locale::getDisplayName, SortDirection.ASCENDING);

        var localeSelect = new Select<Locale>();
        localeSelect.setLabel(i18n("showcaselayout.controls.locale-select"));
        localeSelect.addValueChangeListener(e -> UI.getCurrent().setLocale(e.getValue()));
        localeSelect.setWidthFull();
        localeSelect.addThemeVariants(SelectVariant.LUMO_SMALL);
        localeSelect.setItems(localeDataProvider);
        localeSelect.setItemLabelGenerator(locale -> locale.getDisplayName(currentLocale));
        localeSelect.setValue(currentLocale);

        return localeSelect;
    }

    private Component themeSelect() {

        var selectedTheme = isDarkThemeSelected() ? Lumo.DARK : Lumo.LIGHT;

        var themeRadioButtonGroup = new RadioButtonGroup<String>();
        themeRadioButtonGroup.setLabel(i18n("showcaselayout.controls.theme-select"));
        themeRadioButtonGroup.setItems(Lumo.LIGHT, Lumo.DARK);
        themeRadioButtonGroup.setValue(selectedTheme);
        themeRadioButtonGroup.addValueChangeListener(event -> toggleThemeTo(event.getValue()));
        themeRadioButtonGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        themeRadioButtonGroup.setItemLabelGenerator(item -> {
            switch (item) {
                case Lumo.LIGHT: return "Light";
                case Lumo.DARK: return "Dark";
                default: return "Unknown";
            }
        });

        onThemeSelectionChanged = themeRadioButtonGroup::setValue;

        return themeRadioButtonGroup;
    }

    private static void toggleThemeTo(String selectedTheme) {

        final var themeList = UI.getCurrent().getElement().getThemeList();
        final var deselectedTheme = selectedTheme.equals(Lumo.DARK) ? Lumo.LIGHT : Lumo.DARK;

        themeList.set(selectedTheme, true);
        themeList.set(deselectedTheme, false);
    }

    private static boolean isDarkThemeSelected() {
        return UI.getCurrent().getElement().getThemeList().contains(Lumo.DARK);
    }
}
