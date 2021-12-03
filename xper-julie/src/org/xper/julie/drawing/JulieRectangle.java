package org.xper.julie.drawing;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.opengl.GL11;
import org.xper.drawing.Context;
import org.xper.drawing.object.Rectangle;
import org.xper.util.MathUtil;


public class JulieRectangle extends Rectangle{

	public JulieRectangle(double width, double height) {
		super(width, height);
	}

	public JulieRectangle() {
		super(0,0);
	}

	RectangleSpec spec;

	static ByteBuffer makeTexture(int w, int h) {
		ByteBuffer texture = ByteBuffer.allocateDirect(
				w * w * Float.SIZE / 8).order(
						ByteOrder.nativeOrder());
		double dist;
		int i, j;

		double std = 0.3f;
		double norm_max = MathUtil.normal(0, 0, std);

		for (i = 0; i < w; i++) {
			double x = (double) i / (w - 1) * 2 - 1;
			for (j = 0; j < h; j++) {
				double y = (double) j / (h - 1) * 2 - 1;
				dist = Math.sqrt(x * x + y * y);
				float n = (float) (MathUtil.normal(dist, 0, std) / norm_max);
				texture.putFloat(n);
			}
		}
		texture.flip();

		return texture;
	}


	public void setSpec(String s) {
		spec = RectangleSpec.fromXml(s);
		spec
		
	}

	public static void initGL() {
		int w = 1024;
		int h = 1024;
		ByteBuffer texture = makeTexture(w, h);

		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_ALPHA, w,
				h, 0, GL11.GL_ALPHA, GL11.GL_FLOAT, texture);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
				GL11.GL_CLAMP);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
				GL11.GL_CLAMP);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_NEAREST);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_NEAREST);
		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE,
				GL11.GL_MODULATE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
	}
	
}
