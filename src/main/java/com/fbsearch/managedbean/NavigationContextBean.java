package com.fbsearch.managedbean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class NavigationContextBean {

    public String getMenuitemStyleClass(final String page) {
        final String viewId = getViewId();
        if (viewId != null && viewId.equals(page)) {
            return "active";
        }
        return "";
    }

    public String getViewId() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String viewId = fc.getViewRoot().getViewId();
        String selectedComponent;
        if (viewId != null) {
            selectedComponent = viewId.substring(viewId.lastIndexOf("/") + 1, viewId.lastIndexOf("."));
        } else {
            selectedComponent = null;
        }

        return selectedComponent;
    }
}
