package com.buyk.crocompany.buyk_android.Event;


import java.util.ArrayList;
import java.util.List;

public class EndIdentifyImages {
    public final ArrayList<Boolean> isMotocycle = new ArrayList<Boolean>();


    public EndIdentifyImages(ArrayList<Boolean> isMotocycle) {
        this.isMotocycle.addAll(isMotocycle);
    }
}
