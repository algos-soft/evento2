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
package com.sibvisions.vaadin.server;

import java.io.IOException;

import com.vaadin.server.AbstractExtension;
import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractComponent;

/**
 * The <code>DownloaderExtension</code> is an extension that allows download of generic content through an embedded
 * iframe.
 *
 * @author Ren� Jahn
 *
 * THIS EXTENSION IS PART OF JVx.vaadin (http://blog.sibvisions.com/2013/03/07/welcome-jvx-vaadin-ui/)
 */
public class DownloaderExtension extends AbstractExtension
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>DownloaderExtension</code>.
     */
    public DownloaderExtension()
    {
    }

    /**
     * Creates a new instance of <code>DownloaderExtension</code> with the given resource.
     *
     * @param pResource the resource to download
     */
    public DownloaderExtension(Resource pResource)
    {
        if (pResource == null)
        {
            throw new IllegalArgumentException("resource may not be null");
        }

        setResource("dl", pResource);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Assigns the extension with the component.
     *
     * @param pTarget the server side component
     */
    public void extend(AbstractComponent pTarget)
    {
        super.extend(pTarget);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws IOException
    {
        if (!path.matches("dl(/.*)?"))
        {
            // Ignore if it isn't for us
            return false;
        }

        VaadinSession session = getSession();

        session.lock();

        DownloadStream stream;

        try
        {
            Resource resource = getDownloadResource();

            if (!(resource instanceof ConnectorResource))
            {
                return false;
            }

            stream = ((ConnectorResource)resource).getStream();
        }
        finally
        {
            session.unlock();

        }

        stream.writeResponse(request, response);

        return true;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the resource set for download.
     *
     * @return the resource that will be downloaded
     */
    public Resource getDownloadResource()
    {
        return getResource("dl");
    }

    /**
     * Sets the resource that is downloaded.
     *
     * @param pResource the resource to download
     */
    public void setDownloadResource(Resource pResource)
    {
        //forces re-transfer
        getUI().getConnectorTracker().setDiffState(this, null);

        setResource("dl", pResource);

        markAsDirty();
    }

}   // DownloaderExtension
