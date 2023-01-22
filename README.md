<a href="https://www.buymeacoffee.com/downdrown" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-blue.png" alt="Buy Me A Coffee" style="height: 40px !important; !important;" ></a>

# MiniCalendar

`MiniCalendar` is a [server-only](https://github.com/vaadin/addon-starter-flow) Vaadin component for displaying and
selecting `LocalDate` values.

|                                              The *sunny* side 🌞                                              |                                          The *dark* side (of the moon 🌒)                                          |
|:-------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/default_standard.png" alt="Default Standard" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_default_standard.png" alt="Default Standard" width="300" style="border-radius: 10px"/> |

## Motivation
I am building a small [Diary](https://github.com/downdrown/diary) application and needed a way to easily let users select
days in a month with a single click (just to make it as comfortable as possible). I first tried to use the built-in
`DatePicker` component, but unfortunately there's no way to have this component "always open".

Hence, I decided to build one on my own.

The next thought was *"oh, this could be useful for somebody else as well"* and I quickly checked how "hard" it is to
contribute components to the Vaadin Directory. Luckily this is not problematic at all and I love that it's this easy ❤️

I hope you enjoy the component, if there's anything you want to chit-chat about feel free to say
[hello@downdrown.at](mailto:hello@downdrown.at).

## Fundamentals
The internals are built on the [Java Time API](https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html)
, the displayed values are localized with the locale that is set for the current UI.

The component implements the `LocaleChangeObserver`. It listens for locale changes and will redraw itself when the
locale has changed.

It is highly customizable, offers a lot of configuration- and interaction possibilities. You can either use the built-in
`MiniCalendarVariant` or provide custom CSS classes using the `setDayStyleProvider()`.

## Features

### Single Value Selection
The Component is designed to have a single value selected. It implements the `HasValue`
interface and can therefore be used with a `Binder` like any other default Vaadin field.

You can listen to value changes as well as `YearMonth` changes which will be triggered when
the user navigates through the months *or* the component gets a new value set which differs
from the previous `YearMonth` value.

```java
var miniCalendar = new MiniCalendar();

miniCalendar.addValueChangeListener(event -> {
    Notification.show("Value changed to " + event.getValue());
});

miniCalendar.addYearMonthChangeListener(event -> {
    Notification.show("Value changed to " + event.getValue());
});
```

When adding a listener you'll get an instance of `Registration` back which can be used to remove said listener again.

```java
var registration = miniCalendar.addYearMonthChangeListener(...);

registration.remove();
```

### Dynamically En- or Disable certain days
You can dynamically en- or disable certain days in the calendar view by setting up a `DayEnabledProvider` with
`setDayEnabledProvider()`. The method takes a `SerializablePredicate<LocallDate>` as argument which will be used to
evaluate the enabled state of a day when rendering the component.

> **Warning**
> `DayEnabledProvider` could cause performance issues!
>
> The `DayEnabledProvider` may be called quite a few times during the lifetime of a `MiniCalendar`, you should ensure
> that the backing function **is not an expensive backend operation**, else you could experience some performance issues.

```java
var disabledDays = getDisabledDays();

var miniCalendar = new MiniCalendar();
miniCalendar.addThemeVariants(MiniCalendarVariant.HOVER_DAYS, MiniCalendarVariant.HIGHLIGHT_WEEKEND);
miniCalendar.setDayEnabledProvider(value -> !disabledDays.contains(value));
```

<img src="docs/screens/disabled_days.gif" />

> **Warning**
> Disabled days can still be selected by the server!
>
> Even though a day is disabled in the calendar view, it can still be marked as the selected value from server side.
> Disabled days are **not** considered as *invalid value* from the server, the feature's purpose is to indicate valid
> selections to the user.

<details>
    <summary>Details</summary>

```java
var disabledDays = getDisabledDays();

var miniCalendar = new MiniCalendar();
miniCalendar.setValue(disabledDays.get(0));
miniCalendar.addThemeVariants(MiniCalendarVariant.HOVER_DAYS, MiniCalendarVariant.HIGHLIGHT_WEEKEND);
miniCalendar.setDayEnabledProvider(value -> !disabledDays.contains(value));
```
<img src="docs/screens/disabled_days_server_value.gif" />
</details>

## Appearance

### Theming
The component is based on the [Lumo Theme](https://vaadin.com/docs/latest/styling/lumo), and it's appearance can easily
be changed by using the built-in [Theme Variants](https://vaadin.com/docs/latest/styling/lumo/variants).

To apply a Theme Variant you simply call the `addThemeVariants()` method.

```java
miniCalendar.addThemeVariants(MiniCalendarVariant.ROUNDED);
miniCalendar.addThemeVariants(MiniCalendarVariant.HIGHLIGHT_WEEKEND);
```
To remove an already applied Theme Variant simply call the `removeThemeVariants()` method.

```java
miniCalendar.removeThemeVariants(MiniCalendarVariant.HOVER_DAYS);
```

You can combine multiple Theme Variants to change the component's appearance.

<details>
    <summary>Show examples</summary>

#### Highlight weekends

|                                            Light Mode                                            |                                               Dark Mode                                               |
|:------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/default_highlight_weekends.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_default_highlight_weekends.png" width="300" style="border-radius: 10px"/> |


#### Shifted beginning of the week

|                                               Light Mode                                                |                                                  Dark Mode                                                   |
|:-------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/default_shifted_beginning_of_week.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_default_shifted_beginning_of_week.png" width="300" style="border-radius: 10px"/> |


#### Hover days

|                                        Light Mode                                        |                                           Dark Mode                                           |
|:----------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------:|
| <img src="docs/screens/default_hover_days.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_default_hover_days.png" width="300" style="border-radius: 10px"/> |


#### Rounded

|                                      Light Mode                                       |                                         Dark Mode                                          |
|:-------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------:|
| <img src="docs/screens/default_rounded.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_default_rounded.png" width="300" style="border-radius: 10px"/> |


#### Rounded, Highlight weekends

|                                                Light Mode                                                |                                                   Dark Mode                                                   |
|:--------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/default_rounded_highlight_weekends.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_default_rounded_highlight_weekends.png" width="300" style="border-radius: 10px"/> |

</details>

### Component State
The component's state implicitly affects the appearance of the component. For instance a *disabled* component will look
gray-ish to indicate that the user cannot interact with it. A component in *read only* state won't change the cursor
when hovering over interaction parts and hide the navigation buttons.

Check out these examples of the component in different states.

<details>
    <summary>Read Only</summary>

#### No Theme Variant

|                                       Light Mode                                        |                                          Dark Mode                                           |
|:---------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------:|
| <img src="docs/screens/readonly_standard.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_readonly_standard.png" width="300" style="border-radius: 10px"/> |

#### Highlight weekends

|                                            Light Mode                                             |                                               Dark Mode                                                |
|:-------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/readonly_highlight_weekends.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_readonly_highlight_weekends.png" width="300" style="border-radius: 10px"/> |


#### Shifted beginning of the week

|                                                Light Mode                                                |                                                   Dark Mode                                                   |
|:--------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/readonly_shifted_beginning_of_week.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_readonly_shifted_beginning_of_week.png" width="300" style="border-radius: 10px"/> |


#### Hover days

|                                        Light Mode                                         |                                           Dark Mode                                            |
|:-----------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| <img src="docs/screens/readonly_hover_days.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_readonly_hover_days.png" width="300" style="border-radius: 10px"/> |


#### Rounded

|                                       Light Mode                                       |                                          Dark Mode                                          |
|:--------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------:|
| <img src="docs/screens/readonly_rounded.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_readonly_rounded.png" width="300" style="border-radius: 10px"/> |


#### Rounded, Highlight weekends

|                                                Light Mode                                                 |                                                   Dark Mode                                                    |
|:---------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/readonly_rounded_highlight_weekends.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_readonly_rounded_highlight_weekends.png" width="300" style="border-radius: 10px"/> |

</details>

<details>
    <summary>Disabled</summary>

#### No Theme Variant

|                                       Light Mode                                        |                                          Dark Mode                                           |
|:---------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------:|
| <img src="docs/screens/disabled_standard.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_disabled_standard.png" width="300" style="border-radius: 10px"/> |

#### Highlight weekends

|                                            Light Mode                                             |                                               Dark Mode                                                |
|:-------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/disabled_highlight_weekends.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_disabled_highlight_weekends.png" width="300" style="border-radius: 10px"/> |


#### Shifted beginning of the week

|                                                Light Mode                                                |                                                   Dark Mode                                                   |
|:--------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/disabled_shifted_beginning_of_week.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_disabled_shifted_beginning_of_week.png" width="300" style="border-radius: 10px"/> |


#### Hover days

|                                        Light Mode                                         |                                           Dark Mode                                            |
|:-----------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| <img src="docs/screens/disabled_hover_days.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_disabled_hover_days.png" width="300" style="border-radius: 10px"/> |


#### Rounded

|                                       Light Mode                                       |                                          Dark Mode                                          |
|:--------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------:|
| <img src="docs/screens/disabled_rounded.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_disabled_rounded.png" width="300" style="border-radius: 10px"/> |


#### Rounded, Highlight weekends

|                                                Light Mode                                                 |                                                   Dark Mode                                                    |
|:---------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/disabled_rounded_highlight_weekends.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_disabled_rounded_highlight_weekends.png" width="300" style="border-radius: 10px"/> |

</details>

<details>
    <summary>Disabled, Read Only</summary>

#### No Theme Variant

|                                            Light Mode                                            |                                               Dark Mode                                               |
|:------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/disabled_readonly_standard.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_disabled_readonly_standard.png" width="300" style="border-radius: 10px"/> |

#### Highlight weekends

|                                                 Light Mode                                                 |                                                    Dark Mode                                                    |
|:----------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/disabled_readonly_highlight_weekends.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_disabled_readonly_highlight_weekends.png" width="300" style="border-radius: 10px"/> |


#### Shifted beginning of the week

|                                                    Light Mode                                                     |                                                       Dark Mode                                                        |
|:-----------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/disabled_readonly_shifted_beginning_of_week.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_disabled_readonly_shifted_beginning_of_week.png" width="300" style="border-radius: 10px"/> |


#### Hover days

|                                             Light Mode                                             |                                                Dark Mode                                                |
|:--------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/disabled_readonly_hover_days.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_disabled_readonly_hover_days.png" width="300" style="border-radius: 10px"/> |


#### Rounded

|                                           Light Mode                                            |                                              Dark Mode                                               |
|:-----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/disabled_readonly_rounded.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_disabled_readonly_rounded.png" width="300" style="border-radius: 10px"/> |


#### Rounded, Highlight weekends

|                                                     Light Mode                                                     |                                                        Dark Mode                                                        |
|:------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------:|
| <img src="docs/screens/disabled_readonly_rounded_highlight_weekends.png" width="300" style="border-radius: 10px"/> | <img src="docs/screens/dark_disabled_readonly_rounded_highlight_weekends.png" width="300" style="border-radius: 10px"/> |

</details>


### Adding custom CSS classes
You can easily provide custom css classes for the day components by using the `setDayStyleProvider()` method.
The method takes a `SerializableFunction<LocalDate, List<String>>` as argument which will be used to
evaluate the additional classes for a day when rendering the component.

> **Warning**
> `DayStyleProvider` could cause performance issues!
>
> The `DayStyleProvider` may be called quite a few times during the lifetime of a `MiniCalendar`, you should ensure
> that the backing function **is not an expensive backend operation**, else you could experience some performance issues.

`funky.css`
```css
@-webkit-keyframes bounce {
    0%, 20%, 50%, 80%, 100% {-webkit-transform: translateY(0);}
    40% {-webkit-transform: translateY(-30px);}
    60% {-webkit-transform: translateY(-15px);}
}

@keyframes bounce {
    0%, 20%, 50%, 80%, 100% {transform: translateY(0);}
    40% {transform: translateY(-30px);}
    60% {transform: translateY(-15px);}
}

.minicalendar .day.funky {
    color: var(--lumo-primary-contrast-color);
    -webkit-animation-duration: 1s;
    animation-duration: 1s;
    -webkit-animation-fill-mode: both;
    animation-fill-mode: both;
    background: linear-gradient(
            90deg,
            rgba(255, 0, 0, 1) 0%,
            rgba(255, 154, 0, 1) 10%,
            rgba(208, 222, 33, 1) 20%,
            rgba(79, 220, 74, 1) 30%,
            rgba(63, 218, 216, 1) 40%,
            rgba(47, 201, 226, 1) 50%,
            rgba(28, 127, 238, 1) 60%,
            rgba(95, 21, 242, 1) 70%,
            rgba(186, 12, 248, 1) 80%,
            rgba(251, 7, 217, 1) 90%,
            rgba(255, 0, 0, 1) 100%
    );
}

.bounce:hover {
    -webkit-animation-name: bounce;
    animation-name: bounce;
}
```

`StyleProviderShowcaseView.java`
```java
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
}
```

<img src="docs/screens/styleprovider.gif" />
