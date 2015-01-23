package org.nashua.tt151;

public class PIDController {
	private double kp = 0.0;
	private double ki = 0.0;
	private double kd = 0.0;
	private double maximumOutput = 1.0;
	private double minimumOutput = -1.0;
	private double maximumInput = 0.0;
	private double minimumInput = 0.0;
	
	public PIDController( double proportional, double integral, double derivative ) {
		kp = proportional;
		ki = integral;
		kd = derivative;
	}
	
}
