package com.evgateway.server.common;

import java.util.Locale;
import java.util.Properties;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;


/**
 * 
 * @author Abhishek
 * @date 10/14/2019
 * @time 14:56
 * @version 1.0.0
 * To change this template use File | Settings | File Templates.
 */
public class SerializableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

    public Properties getAllProperties(Locale locale) {
        clearCacheIncludingAncestors();
        PropertiesHolder propertiesHolder = getMergedProperties(locale);
        Properties properties = propertiesHolder.getProperties();

        return properties;
    }
}
