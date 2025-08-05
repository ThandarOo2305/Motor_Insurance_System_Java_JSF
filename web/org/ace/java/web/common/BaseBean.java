package org.ace.java.web.common;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.ace.accounting.common.validation.ErrorMessage;
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.SystemException;
import org.primefaces.PrimeFaces;

public class BaseBean {

    /**
     *********************** Web Utilities *******************
     */
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    protected ServletContext getServletContext() {
        return (ServletContext) getFacesContext().getExternalContext().getContext();
    }

    protected Application getApplication() {
        return getFacesContext().getApplication();
    }

    @SuppressWarnings("rawtypes")
    protected Map getApplicationMap() {
        return getFacesContext().getExternalContext().getApplicationMap();
    }

    @SuppressWarnings("rawtypes")
    protected Map getSessionMap() {
        return getFacesContext().getExternalContext().getSessionMap();
    }

    @SuppressWarnings("unchecked")
    protected void putParam(String key, Object obj) {
        getSessionMap().put(key, obj);
    }

    protected Object getParam(String key) {
        return getSessionMap().get(key);
    }

    protected boolean isExistParam(String key) {
        return getSessionMap().containsKey(key);
    }

    protected void removeParam(String key) {
        if (isExistParam(key)) {
            getSessionMap().remove(key);
        }
    }

    protected ResourceBundle getBundle() {
        return ResourceBundle.getBundle(getApplication().getMessageBundle(), getFacesContext().getViewRoot().getLocale());
    }

    protected String getWebRootPath() {
        Object context = getFacesContext().getExternalContext().getContext();
        String systemPath = ((ServletContext) context).getRealPath("/");
        return systemPath;
    }

    /**
     *********************** Message Utilities *******************
     */
    protected String getMessage(String id, Object... params) {
        String text = null;
        try {
            text = getBundle().getString(id);
        } catch (MissingResourceException e) {
            text = "!! key " + id + " not found !!";
        }
        if (params != null) {
            MessageFormat mf = new MessageFormat(text);
            text = mf.format(params, new StringBuffer(), null).toString();
        }
        return text;
    }

    protected void addWranningMessage(String id, String errorCode, Object... params) {
        String message = getMessage(errorCode, params);
        getFacesContext().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_WARN, message, ""));
    }

    protected void addInfoMessage(String id, String errorCode, Object... params) {
        String message = getMessage(errorCode, params);
        getFacesContext().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));
    }

    protected void addErrorMessage(String id, String errorCode, Object... params) {
        String message = getMessage(errorCode, params);
        getFacesContext().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, id == null ? "" : message));
    }

    protected void addErrorMessage(String message) {
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
    }

    protected void addErrorMessage(ErrorMessage errorMessage) {
        String message = getMessage(errorMessage.getErrorcode(), errorMessage.getParams());
        getFacesContext().addMessage(errorMessage.getId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
    }

    protected void addInfoMessage(String message) {
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
    }

    protected void addWranningMessage(String message) {
        getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
    }

    protected void handleSysException(SystemException systemException) {
        String errorCode = systemException.getErrorCode();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage(errorCode), "");
        getFacesContext().addMessage(null, facesMessage);
    }

    protected void handleException(Exception exception) {
        String message = exception.getMessage() == null ? "System Error." : exception.getMessage();
        exception.printStackTrace();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, message, "");
        getFacesContext().addMessage(null, facesMessage);
    }

    /**
     *********************** Dialog Information *******************
     */
    private static Map<String, Object> dialogOptions;

    public static Map<String, Object> getDialogOptions() {
        if (dialogOptions == null) {
            dialogOptions = new HashMap<String, Object>();
            dialogOptions.put("modal", true);
            dialogOptions.put("draggable", false);
            dialogOptions.put("resizable", false);
            dialogOptions.put("width", 640);
            dialogOptions.put("height", 340);
            dialogOptions.put("contentHeight", "100%");
            dialogOptions.put("contentWidth", "100%");
        }
        return dialogOptions;
    }

    public static Map<String, Object> getDialogOptions(int width, int height, int contentHeight, int contentWidth) {
        if (dialogOptions == null) {
            dialogOptions = new HashMap<String, Object>();
        }
        dialogOptions.put("modal", true);
        dialogOptions.put("draggable", false);
        dialogOptions.put("resizable", false);
        dialogOptions.put("width", width);
        dialogOptions.put("height", height);
        dialogOptions.put("contentHeight", contentHeight + "%");
        dialogOptions.put("contentWidth", contentWidth + "%");
        return dialogOptions;
    }

    public static Map<String, Object> getCustomDialogOptions() {
        if (dialogOptions == null) {
            dialogOptions = new HashMap<String, Object>();
        }
        dialogOptions.put("modal", true);
        dialogOptions.put("draggable", false);
        dialogOptions.put("resizable", true);
        dialogOptions.put("width", 85 + "%");
        dialogOptions.put("height", 350);
        dialogOptions.put("position", "top");
        dialogOptions.put("contentHeight", 100 + "%");
        dialogOptions.put("contentWidth", 100 + "%");
        return dialogOptions;
    }

    public void selectRole() {
        PrimeFaces.current().dialog().openDynamic(DialogId.ROLE_DIALOG, getDialogOptions(), null);
        /*
         * RequestContext.getCurrentInstance().openDialog(DialogId.ROLE_DIALOG,
         * getDialogOptions(), null);
         */
    }

    public void selectBranch() {
        PrimeFaces.current().dialog().openDynamic(DialogId.BRANCH_DIALOG, getDialogOptions(), null);
        /*
         * RequestContext.getCurrentInstance().openDialog(DialogId.BRANCH_DIALOG,
         * getDialogOptions(800, 500, 100, 100), null);
         */
    }
    // select occupation
    public void selectOccupation() {
    	PrimeFaces.current().dialog().openDynamic(DialogId.OCCUPATION_DIALOG, getDialogOptions(), null);
    }

    public void selectDepartment() {
        PrimeFaces.current().dialog().openDynamic(DialogId.DEPT_DIALOG, getDialogOptions(), null);
        /*
         * RequestContext.getCurrentInstance().openDialog(DialogId.DEPT_DIALOG,
         * getDialogOptions(), null);
         */
    }

    public void selectCoa() {
        PrimeFaces.current().dialog().openDynamic(DialogId.COA_DIALOG, getDialogOptions(), null);
        /*
         * RequestContext.getCurrentInstance().openDialog(DialogId.COA_DIALOG,
         * getCustomDialogOptions(), null);
         */
    }

    public void selectFFCoa() {
        Map<String, Object> custom = getDialogOptions();
//      custom.put("width", "80%");
//      custom.put("height", "100%");
        custom.put("modal", true);
        custom.put("draggable", false);
        custom.put("resizable", false);
        custom.put("width",900);
        custom.put("height",500);
        PrimeFaces.current().dialog().openDynamic(DialogId.FF_COA_DIALOG, getDialogOptions(), null);
        /*
         * RequestContext.getCurrentInstance().openDialog(DialogId.FF_COA_DIALOG,
         * getDialogOptions(), null);
         */
    }

    public void selectCCOAAccountCode() {
        PrimeFaces.current().dialog().openDynamic(DialogId.CCOA_DIALOG, getDialogOptions(800, 500, 100, 100), null);
        /*
         * RequestContext.getCurrentInstance().openDialog(DialogId.CCOA_DIALOG,
         * getDialogOptions(800, 500, 100, 100), null);
         */
    }

    public void selectCCOAAccountCode(Currency currency) {
        putParam(ParamId.CURRENCY_DATA, currency);
        PrimeFaces.current().dialog().openDynamic(DialogId.CCOA_DIALOG, getDialogOptions(800, 500, 100, 100), null);
        /*
         * RequestContext.getCurrentInstance().openDialog(DialogId.CCOA_DIALOG,
         * getDialogOptions(800, 500, 100, 100), null);
         */
    }

    public void selectVoucherNo() {
        PrimeFaces.current().dialog().openDynamic(DialogId.VOUCHER_NO_DIALOG, getDialogOptions(), null);
        /*
         * RequestContext.getCurrentInstance().openDialog(DialogId.VOUCHER_NO_DIALOG,
         * getDialogOptions(), null);
         */
    }
}
