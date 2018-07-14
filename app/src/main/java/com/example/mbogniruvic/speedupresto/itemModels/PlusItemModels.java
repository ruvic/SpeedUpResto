package com.example.mbogniruvic.speedupresto.itemModels;

import android.animation.TimeInterpolator;

import com.example.mbogniruvic.speedupresto.models.Restaurant;

public class PlusItemModels {
    public final Restaurant restaurant;
    public final int colorId1;
    public final int colorId2;
    public final TimeInterpolator interpolator;

    public PlusItemModels(Restaurant restaurant, int colorId1, int colorId2, TimeInterpolator interpolator) {
        this.restaurant = restaurant;
        this.colorId1 = colorId1;
        this.colorId2 = colorId2;
        this.interpolator = interpolator;
    }
}
