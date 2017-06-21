package com.zCore.Render.Particles;

import java.util.Random;

import com.zCore.Core.zCore;

import net.minecraft.util.MathHelper;

public class Particle {
	public int x;
	public int y;
	public int k;
	public ParticleGenerator pg;
	public boolean reset;
	public float size;
	private Random random = new Random();

	public Particle(int x, int y) {
		this.x = x;

		this.y = y;

		this.size = genRandom(0.1F, 0.1F);
	}

	public void draw() {
		if (this.x == -12) {
			this.x = ParticleGenerator.breite;

			this.reset = true;
		}
		if (this.y == -2) {
			this.y = ParticleGenerator.höhe;

			this.reset = true;
		}
		this.x -= this.random.nextInt(1);
		this.y -= this.random.nextInt(7);

		int xx = (int) (MathHelper.cos(0.116F * (this.x + this.k)) * 11.0F);

		zCore.drawBorderedCircle(this.x + xx, this.y, this.size, 0, -1);
	}

	public float genRandom(float min, float max) {
		return (float) (min + Math.random() * (max - min + 1.6F));
	}
}