package com.zjxtwvf.smarttime.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class AnimationUitls {

	
	public static Animation getAlphaAnimation(float from,float to,long duration){
		Animation animation = new AlphaAnimation(from, to);
		animation.setDuration(duration);
		animation.setFillAfter(true);
		animation.setFillEnabled(true);
		return animation;
	}
	
	public static Animation getRoateAnimation(float fromDegrees,float toDegrees,long duration){
		Animation animation = new RotateAnimation(fromDegrees, toDegrees, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(duration);
		animation.setFillAfter(true);
		animation.setFillEnabled(true);
		return animation;
	}
}
;