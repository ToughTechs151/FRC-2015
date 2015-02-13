package org.nashua.tt151;

public class PIDController {
	
	double minInput = 0.0;
	double maxInput = 0.0;
	double minOutput = 0.0;
	double maxOutput = 0.0;
	
	double setPoint = 0.0;
	double input = 0.0;
	double output = 0.0;
	
	double prevError = 0.0;
	double error = 0.0;
	double totalError = 0.0;
	
	double kP = 0.0;
	double kI = 0.0;
	double kD = 0.0;
	
	public PIDController( double proportional, double integral, double derivative ) {
		setPID( proportional, integral, derivative );
	}
	
	public void setPID( double proportional, double integral, double derivative ) {
		kP = proportional;
		kI = integral;
		kD = derivative;
	}
	
	public void setInputRange( double minInput, double maxInput ) {
		if ( maxInput > minInput ) {
			this.minInput = minInput;
			this.maxInput = maxInput;
		} else {
			System.err.println( "Invalid input range" );
		}
	}
	
	public void setOutputRange( double minOutput, double maxOutput ) {
		if ( maxOutput > minOutput ) {
			this.minOutput = minOutput;
			this.maxOutput = maxOutput;
		} else {
			System.err.println( "Invalid output range" );
		}
	}
	
	public double getP() {
		return kP;
	}
	
	public double getI() {
		return kI;
	}
	
	public double getD() {
		return kD;
	}
	
	public double getError() {
		return error;
	}
	
	public double getOutput() {
		return output;
	}
	
	public void setSetpoint( double setPoint ) {
		this.setPoint = MathUtils.clamp( setPoint, minInput, maxInput );
	}
	
	public double getSetpoint() {
		return setPoint;
	}
	
	public void setInput( double input ) {
		this.input = input;
	}
	
	public void reset() {
		this.prevError = 0;
		this.totalError = 0;
		this.output = 0;
	}
	
	public void calculate() {
		// Calculate the error
		error = setPoint - input;
		
		// Integrate the error as long as it doesn't exceed the maximum output
		if ( MathUtils.inRange( ( totalError + error ) * kI, minOutput, maxOutput ) ) {
			totalError += error;
		}
		
		//Perform the PID calculation
		output = kP * error + kI * totalError + kD * (error - prevError);
		
		//Set the current error to the previous error for the next calculation
		prevError = error;
		
		//Make sure the output is within the allowed range
		output = MathUtils.clamp( output, minOutput, maxOutput );
	}
	
	public double performPID() {
		calculate();
		return output;
	}
}
