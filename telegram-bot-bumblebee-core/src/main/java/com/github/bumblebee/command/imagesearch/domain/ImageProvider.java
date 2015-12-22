package com.github.bumblebee.command.imagesearch.domain;

import java.util.List;

public interface ImageProvider {

    List<Image> search(String query);

    default String name() {
        return getClass().getSimpleName();
    }

}
