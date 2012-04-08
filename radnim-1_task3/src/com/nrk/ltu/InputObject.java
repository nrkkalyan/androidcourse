package com.nrk.ltu;

import android.view.MotionEvent;

public class InputObject {
	public static final byte EVENT_TYPE_TOUCH = 0;
	public int eventType;
	public int x;

	public void useEvent(MotionEvent event) {
		eventType = EVENT_TYPE_TOUCH;
		x = (int) event.getX();
	}

}