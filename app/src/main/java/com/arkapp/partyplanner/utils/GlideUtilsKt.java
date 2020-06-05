package com.arkapp.partyplanner.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Abdul Rehman on 29-03-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * This is a utility class for the Loading the image using glide library
 */

public class GlideUtilsKt {
    public static void loadImage(@NotNull ImageView imageView, int resId) {
        try {
            Glide.with(imageView.getContext())
                    .load(resId)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
