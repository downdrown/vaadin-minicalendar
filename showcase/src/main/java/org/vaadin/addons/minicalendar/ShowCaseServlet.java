package org.vaadin.addons.minicalendar;

import com.vaadin.flow.server.InitParameters;
import com.vaadin.flow.server.VaadinServlet;

import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(
    urlPatterns = "/*",
    name = "slot",
    asyncSupported = true,
    loadOnStartup = 1,
    initParams = {
        @WebInitParam(
            name = InitParameters.I18N_PROVIDER,
            value = "org.vaadin.addons.minicalendar.i18n.I18NProviderImpl"
        )
    })
public class ShowCaseServlet extends VaadinServlet {
}
