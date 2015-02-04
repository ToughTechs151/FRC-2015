package org.nashua.tt151;

public class PIDController {
	
	double minInput;
	double maxInput;
	double minOutput;
	double maxOutput;
	
	double setPoint;
	double input;
	double output;
	
	double prevError;
	double error;
	double totalError;
	
	double kP;
	double kI;
	double kD;
	
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
