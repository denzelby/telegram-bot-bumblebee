package com.github.bumblebee.command.imagesearch.domain;

import java.util.List;

public interface ImagesPreprocessor {

    void process(List<Image> images);
}
