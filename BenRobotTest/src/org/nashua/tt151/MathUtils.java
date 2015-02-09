package org.nashua.tt151;

public final class MathUtils {
	
	public static double clamp( double val, double min, double max ) {
		return Math.min( max, Math.max( val, min ) );
	}
	
	public static double clamp_i( int val, int min, int max ) {
		return Math.min( max, Math.max( val, min ) );
	}
	
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
	
	public static double absMax( double... nums ) {
		double[] abs = new double[nums.length];
		for ( int i = 0; i < nums.length; i++ ) {
			abs[i] = Math.abs( nums[i] );
		}
		return max( abs );
	}
	
	public static int absMax_i( int... nums ) {
		int[] abs = new int[nums.length];
		for ( int i = 0; i < nums.length; i++ ) {
			abs[i] = Math.abs( nums[i] );
		}
		return max_i( abs );
	}
	
}