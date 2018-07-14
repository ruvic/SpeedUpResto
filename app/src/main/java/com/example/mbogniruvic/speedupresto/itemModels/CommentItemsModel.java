package com.example.mbogniruvic.speedupresto.itemModels;

import android.animation.TimeInterpolator;

import com.example.mbogniruvic.speedupresto.models.Review;

import java.util.List;

public class CommentItemsModel {

    public final List<Review> comments;
    public final int colorId1;
    public final int colorId2;
    public final TimeInterpolator interpolator;


    public CommentItemsModel(List<Review> comments, int colorId1, int colorId2, TimeInterpolator interpolator) {
        this.comments = comments;
        this.colorId1 = colorId1;
        this.colorId2 = colorId2;
        this.interpolator = interpolator;
    }
}
