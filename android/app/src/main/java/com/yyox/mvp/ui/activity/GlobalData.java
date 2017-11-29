package com.yyox.mvp.ui.activity;

import com.yyox.mvp.model.entity.Images;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dadaniu on 2017-07-13.
 */

public class GlobalData {
    private static List<Images> ImagesList = new ArrayList<>();

    public static void setImagesList(List<Images> imagesList) {
        ImagesList = imagesList;
    }

    public static List<Images> getImagesList() {
        return ImagesList;
    }
}
