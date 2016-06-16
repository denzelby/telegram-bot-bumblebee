package com.github.bumblebee.utils;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dzianis.baburkin on 6/16/2016.
 */
public class BumblebeeUtils {

    private static final Logger log = LoggerFactory.getLogger(BumblebeeUtils.class);

    public static String getFileName(String url) {
        String name = FilenameUtils.getName(url);
        // if no extension defined - let's guess it
        if (FilenameUtils.getExtension(name).isEmpty()) {
            name += ".jpg";
        } else {
            // skip url params
            int urlParamsIndex = name.indexOf('?');
            if (urlParamsIndex > 0) {
                name = name.substring(0, urlParamsIndex);
            }
        }
        return name;
    }
}
