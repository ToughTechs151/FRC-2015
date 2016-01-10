package org.nashua.tt151.lib;

import org.nashua.tt151.util.MathUtils;

/**
 * A simple PIDController
 * 
 * @author Kareem El-Faramawi
 */
public class PIDController {
	// Some bounds for the I/O values
	double minInput = 0.0;
	double maxInput = 0.0;
	double minOutput = 0.0;
	double maxOutput = 0.0;
	
	// Current values the controller is working with
	double setPoint = 0.0;
	double input = 0.0;
	double output = 0.0;
	
	// Error change and accumulation
	double prevError = 0.0;
	double error = 0.0;
	double totalError = 0.0;
	
	// PID constants
	double kP = 0.0;
	double kI = 0.0;
	double kD = 0.0;
	
	/**
	 * Creates a PIDController with the given constants
	 * 
	 * @param proportional Proportional constant
	 * @param integral Integral constant
	 * @param derivative Derivative constant
	 */
	public PIDController( double proportional, double integral, double derivative ) {
		setPID( proportional, integral, derivative );
	}
	
	/**
	 * Updates the PID values of this controller
	 * 
	 * @param proportional Proportional constant
	 * @param integral Integral constant
	 * @param derivative Derivative constant
	 */
	public void setPID( double proportional, double integral, double derivative ) {
		kP = proportional;
		kI = integral;
		kD = derivative;
	}
	
	/**
	 * Sets the bounds on the input value of this PIDController
	 * 
	 * @param minInput Minimum input
	 * @param maxInput Maximum output
	 */
	public void setInputRange( double minInput, double maxInput ) {
		if ( maxInput > minInput ) {
			this.minInput = minInput;
			this.maxInput = maxInput;
		} else {
			System.err.println( "Invalid input range" );
		}
	}
	
	/**
	 * Sets the bounds on the output value of this PIDController
	 * 
	 * @param minOutput Minimum output
	 * @param maxOutput Maximum output
	 */
	public void setOutputRange( double minOutput, double maxOutput ) {
		if ( maxOutput > minOutput ) {
			this.minOutput = minOutput;
			this.maxOutput = maxOutput;
		} else {
			System.err.println( "Invalid output range" );
		}
	}
	
	/**
	 * @return Proportional constant
	 */
	public double getP() {
		return kP;
	}
	
	/**
	 * @return Integral constant
	 */
	public double getI() {
		return kI;
	}
	
	/**
	 * @return Derivative constant
	 */
	public double getD() {
		return kD;
	}
	
	/**
	 * @return The most recent error value
	 */
	public double getError() {
		return error;
	}
	
	/**
	 * @return The current PID output
	 */
	public double getOutput() {
		return output;
	}
	
	/**
	 * Sets the target value for this PIDController
	 * 
	 * @param setPoint Target value
	 */
	public void setSetpoint( double setPoint ) {
		this.setPoint = MathUtils.clamp( setPoint, minInput, maxInput );
	}
	
	/**
	 * @return Current target value
	 */
	public double getSetpoint() {
		return setPoint;
	}
	
	/**
	 * Sets the new input for the PIDController to use in the next calculation
	 * 
	 * @param input New input
	 */
	public void setInput( double input ) {
		this.input = input;
	}
	
	/**
	 * Reset the state of the PIDController
	 */
	public void reset() {
		this.prevError = 0;
		this.totalError = 0;
		this.output = 0;
	}
	
	/**
	 * Runs the PID calculation, updating the output value
	 */
	public void calculate() {
		// Calculate the error
		error = setPoint - input;
		
		// Integrate the error as long as it doesn't exceed the maximum output
		if ( MathUtils.inRange( ( totalError + error ) * kI, minOutput, maxOutput ) ) {
			totalError += error;
		}
		
		// Perform the PID calculation
		output = kP * error + kI * totalError + kD * ( error - prevError );
		
		// Set the current error to the previous error for the next calculation
		prevError = error;
		
		// Make sure the output is within the allowed range
		output = MathUtils.clamp( output, minOutput, maxOutput );
	}
	
	/**
	 * Calculates and returns the next value
	 * 
	 * @return Next adjustment value
	 */
	public double performPID() {
		calculate();
		return output;
	}
}
