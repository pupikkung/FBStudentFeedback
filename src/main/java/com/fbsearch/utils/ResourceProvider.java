package com.fbsearch.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Provide a single static mechanism for retrieving resource values from the 
 * resource bundle using the key.
 * 
 * @author witcradg
 */
public class ResourceProvider {
    /**
     * Attribute for statically accessing the ResouceBundle containing externalized 
     * Strings
     */
    protected static ResourceProvider provider = null;
    
    private ResourceBundle bundle;
    private String bundleName;
    
    /**
     * Constructor
     * @param bundleName 
     */
    private ResourceProvider(String bundleName) {
        this.bundleName = bundleName;
    }

    /**
     * Get the ResourceBundle using lazy initialization
     * @return 
     */
    private ResourceBundle getBundle() {

        if (bundle == null) {
            bundle = java.util.ResourceBundle.getBundle(bundleName);
        }
        return bundle;
    }

    /**
     * Get the resource bundle string value using the key
     * @param String key
     * @return String resource value
     */
    private String getValue(String key) {

        String result;
        try {
            result = getBundle().getString(key);
          
        } catch (MissingResourceException e) {
            result = "Resource value for " + key + " not found";
        }
  
        return result;
    }

    /**
     * static resource bundle lookup by key
     * @param key
     * @return 
     */
    public static String getBundleValue(String key) {
        if (provider == null) {
            provider = new ResourceProvider("resources/bundles/message");
        }
        return provider.getValue(key);
    }
}
