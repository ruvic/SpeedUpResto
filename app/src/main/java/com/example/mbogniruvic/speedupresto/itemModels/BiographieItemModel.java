package com.example.mbogniruvic.speedupresto.itemModels;

import android.animation.TimeInterpolator;

public class BiographieItemModel {

    public final String biographie;
    public final int colorId1;
    public final int colorId2;
    public final TimeInterpolator interpolator;

    public BiographieItemModel(String biographie, int colorId1, int colorId2, TimeInterpolator interpolator) {
        this.biographie = biographie;
        this.colorId1 = colorId1;
        this.colorId2 = colorId2;
        this.interpolator = interpolator;
    }
}
