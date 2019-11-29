package com.buyk.crocompany.buyk_android.Event;

import android.view.View;

public class RemoveSelectedImageEvent {
    public final String status;
    public final String path;
    public final int position;
    public final View view;
    public final int viewIndex;

    public RemoveSelectedImageEvent(String status, String path, int position, View view, int cPosition) {
        this.status=status;
        this.path = path;
        this.position = position;
        this.view = view;
        this.viewIndex =  cPosition;
    }
}
