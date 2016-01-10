package org.nashua.tt151.util;

/**
 * A bunch of useful math utility methods
 */
public final class MathUtils {
	
	/**
	 * Forces a value to be within a specified range
	 * 
	 * @param val Value to be clamped
	 * @param min Minimum value of range
	 * @param max Maximum value of range
	 * @return If value > max, max is returned
	 * If value < min, min is returned
	 * If min <= value <= max, value is returned.
	 */
	public static double clamp( double val, double min, double max ) {
		return Math.min( max, Math.max( val, min ) );
	}
	
	/**
	 * Forces an integer value to be within a specified range
	 * 
	 * @param val Integer value to be clamped
	 * @param min Minimum value of range
	 * @param max Maximum value of range
	 * @return If value > max, max is returned
	 * If value < min, min is returned
	 * If min <= value <= max, value is returned.
	 */
	public static double clamp_i( int val, int min, int max ) {
		return Math.min( max, Math.max( val, min ) );
	}
	
	/**
	 * @param nums Array of numbers
	 * @return The maximum of all the numbers
	 */
	public static double max( double... nums ) {
		if ( nums.length == 0 ) {
			return 0;
		}
		double max = nums[0];
		for ( int i = 1; i < nums.length; i++ ) {
			max = Math.max( max, nums[i] );
		}
		return max;
	}
	
	/**
	 * @param nums Array of integers
	 * @return The maximum of all the integers
	 */
	public static int max_i( int... nums ) {
		if ( nums.length == 0 ) {
			return 0;
		}
		int max = nums[0];
		for ( int i = 1; i < nums.length; i++ ) {
			max = Math.max( max, nums[i] );
		}
		return max;
	}
	
	/**
	 * @param nums Array of numbers
	 * @return The absolute maximum of all the numbers
	 */
	public static double absMax( double... nums ) {
		double[] abs = new double[nums.length];
		for ( int i = 0; i < nums.length; i++ ) {
			abs[i] = Math.abs( nums[i] );
		}
		return max( abs );
	}
	
	/**
	 * @param nums Array of integers
	 * @return The absolute maximum of all the integers
	 */
	public static int absMax_i( int... nums ) {
		int[] abs = new int[nums.length];
		for ( int i = 0; i < nums.length; i++ ) {
			abs[i] = Math.abs( nums[i] );
		}
		return max_i( abs );
	}
	
	/**
	 * Checks if a number is inside an exclusive range
	 * 
	 * @param val Value to be checked
	 * @param min Minimum value
	 * @param max Maximum value
	 * @return if val is in the range (min, max)
	 */
	public static boolean inRange_ex( double val, double min, double max ) {
		return min < val && val < max;
	}
	
	/**
	 * Checks if a number is inside an inclusive range
	 * 
	 * @param val Value to be checked
	 * @param min Minimum value
	 * @param max Maximum value
	 * @return if val is in the range [min, max]
	 */
	public static boolean inRange( double val, double min, double max ) {
		return min <= val && val <= max;
	}
	
	/**
	 * Floors a number to a certain number of decimal places
	 * 
	 * @param val Number to be floored
	 * @param places Number of decimal places to be used
	 * @return Floored number
	 */
	public static double floor( double val, int places ) {
		double pow = Math.pow( 10, places );
		return Math.floor( val * pow ) / pow;
	}
	
	/**
	 * Rounds a number to a certain number of decimal places
	 * 
	 * @param val Number to be rounded
	 * @param places Number of decimal places to be used
	 * @return Rounded number
	 */
	public static double round( double val, int places ) {
		double pow = Math.pow( 10, places );
		return Math.round( val * pow ) / pow;
	}
	
	/**
	 * Ceils a number to a certain number of decimal places
	 * 
	 * @param val Number to be ceil'd
	 * @param places Number of decimal places to be used
	 * @return Ceil'd number
	 */
	public static double ceil( double val, int places ) {
		double pow = Math.pow( 10, places );
		return Math.ceil( val * pow ) / pow;
	}
}
