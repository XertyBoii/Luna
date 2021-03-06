package com.zCore.Render.Particles;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import lunadevs.luna.Connector.ParticleSync.Particles.Particle;

public class ParticleGenerator {

	public static int anzahl;
	public static int breite;
	public static int h�he;

	public ArrayList<Particle> particles = new ArrayList();
	private Random random = new Random();
	private Timer timer = new Timer();

	public ParticleGenerator(int anzahl, int breite, int h�he) {
		this.anzahl = anzahl;
		this.breite = breite;
		this.h�he = h�he;
		for (int i = 0; i < anzahl; i++) {
			this.particles.add(new Particle(this.random.nextInt(breite), this.random.nextInt(h�he)));
		}
	}

	public void drawParticles() {
		for (Particle p : this.particles)
		{		
			p.draw();
		}
	}
}