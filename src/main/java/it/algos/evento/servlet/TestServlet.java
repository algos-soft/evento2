package it.algos.evento.servlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import it.algos.evento.ui.company.CompanyUI;
import it.algos.evento.ui.test.TestUI;
import it.algos.webbase.web.servlet.AlgosServlet;

import javax.servlet.annotation.WebServlet;

/**
 * Created by alex on 19/12/15.
 */
@WebServlet(value = {"/test/*"}, asyncSupported = true, displayName = "eVento test")
@VaadinServletConfiguration(productionMode = false, ui = TestUI.class)
public class TestServlet extends VaadinServlet {
}
