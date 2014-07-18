package com.hungnguyen.qrcodescanner.ownerlibs;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class BitmapFactory {
	public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth,
			int wantedHeight) {
		Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Matrix m = new Matrix();
		m.setScale((float) wantedWidth / bitmap.getWidth(),
				(float) wantedHeight / bitmap.getHeight());
		canvas.drawBitmap(bitmap, m, new Paint());

		return output;
	}
}
