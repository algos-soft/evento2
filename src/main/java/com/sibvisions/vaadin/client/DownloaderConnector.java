/*
 * Copyright 2014 SIB Visions GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 * History
 *
 * 12.04.2014 - [JR] - creation
 */
package com.sibvisions.vaadin.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.ui.Widget;
import com.sibvisions.vaadin.server.DownloaderExtension;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

/**
 * The <code>DownloaderConnector</code> is a special extension that uses an iframe for downloading resources.
 * This class is important for portlet applications.
 *
 * @author Ren� Jahn
 *
 * THIS CONNECTOR IS PART OF JVx.vaadin (http://blog.sibvisions.com/2013/03/07/welcome-jvx-vaadin-ui/)
 */
@Connect(DownloaderExtension.class)
public class DownloaderConnector extends AbstractExtensionConnector
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the connected widget. **/
    private Widget widget;

    /** the iframe. */
    private IFrameElement iframe;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>DownloaderConnector</code>.
     */
    public DownloaderConnector()
    {
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    protected void extend(ServerConnector pTarget)
    {
        widget = ((ComponentConnector) pTarget).getWidget();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStateChanged(StateChangeEvent pStateChangeEvent)
    {
        super.onStateChanged(pStateChangeEvent);

        configureIFrame();
    }

    @Override
    public void setParent(ServerConnector parent)
    {
        super.setParent(parent);

        if (parent == null && iframe != null)
        {
            iframe.removeFromParent();
            iframe = null;
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Configures the iframe with the current url from the state. The iframe will be added on-demand
     */
    private void configureIFrame()
    {
        String sURL = getResourceUrl("dl");

        if (sURL == null)
        {
            if (iframe != null)
            {
                iframe.setSrc("");
            }

            return;
        }

        if (iframe == null)
        {
            iframe = Document.get().createIFrameElement();

            Style style = iframe.getStyle();
            style.setVisibility(Visibility.HIDDEN);
            style.setHeight(0, Unit.PX);
            style.setWidth(0, Unit.PX);

            iframe.setFrameBorder(0);
            iframe.setTabIndex(-1);
            iframe.setSrc(sURL);
            iframe.setClassName("ui_downloader");

            widget.getElement().appendChild(iframe);
        }
        else
        {
            iframe.setSrc("");
            iframe.setSrc(sURL);
        }
    }

}   // DownloaderConnector
