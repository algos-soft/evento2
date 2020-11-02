package it.algos.evento.servlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import it.algos.evento.ui.confermapren.ConfermaPrenUI;

import javax.servlet.annotation.WebServlet;

import static it.algos.evento.EventoApp.CONF_PAGE;


@SuppressWarnings("serial")
@WebServlet(value = "/"+CONF_PAGE+"/*", asyncSupported = true, displayName = "eVento - conferma")
@VaadinServletConfiguration(productionMode = false, ui = ConfermaPrenUI.class)
public class ConfermaPrenServlet extends VaadinServlet {}

