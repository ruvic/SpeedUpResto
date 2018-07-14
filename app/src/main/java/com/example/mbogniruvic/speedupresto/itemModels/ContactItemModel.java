package com.example.mbogniruvic.speedupresto.itemModels;

import android.animation.TimeInterpolator;

public class ContactItemModel {
    public final String phone;
    public final String email;
    public final int colorId1;
    public final int colorId2;
    public final TimeInterpolator interpolator;

    public ContactItemModel(String phone, String email, int colorId1, int colorId2, TimeInterpolator interpolator) {
        this.phone = phone;
        this.email = email;
        this.colorId1 = colorId1;
        this.colorId2 = colorId2;
        this.interpolator = interpolator;
    }
}
