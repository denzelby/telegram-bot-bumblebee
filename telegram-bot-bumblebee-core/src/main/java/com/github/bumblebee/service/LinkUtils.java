package com.github.bumblebee.service;

import org.apache.commons.io.FilenameUtils;

public final class LinkUtils {

    private LinkUtils() {
    }

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
