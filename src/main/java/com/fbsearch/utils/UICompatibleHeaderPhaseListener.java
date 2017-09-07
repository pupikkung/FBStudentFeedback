//************************************************
//Copyright 2013
//DST Output of California, Inc.
//All rights reserved.
//************************************************
// Created on May 29, 2014, 4:04:54 PM
package com.fbsearch.utils;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dt87105
 */
public class UICompatibleHeaderPhaseListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent pe) {
    }

    @Override
    public void beforePhase(PhaseEvent pe) {
        final FacesContext facesContext = pe.getFacesContext();
        final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.addHeader("X-UA-Compatible", "IE=edge");
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

}
