package org.nashua.tt151;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class ADXL345_I2C_SparkFun extends SensorBase implements Accelerometer,
		LiveWindowSendable {
	private static final byte kAddress = 83;
	private static final byte kPowerCtlRegister = 45;
	private static final byte kDataFormatRegister = 49;
	private static final byte kDataRegister = 50;
	private static final double kGsPerLSB = 0.00390625D;
	private static final byte kPowerCtl_Link = 32;
	private static final byte kPowerCtl_AutoSleep = 16;
	private static final byte kPowerCtl_Measure = 8;
	private static final byte kPowerCtl_Sleep = 4;
	private static final byte kDataFormat_SelfTest = -128;
	private static final byte kDataFormat_SPI = 64;
	private static final byte kDataFormat_IntInvert = 32;
	private static final byte kDataFormat_FullRes = 8;
	private static final byte kDataFormat_Justify = 4;
	private I2C m_i2c;
	private ITable m_table;

	public static class AllAxes {
		public double XAxis;
		public double YAxis;
		public double ZAxis;
	}

	public static class Axes {
		public final byte value;
		static final byte kX_val = 0;
		static final byte kY_val = 2;
		static final byte kZ_val = 4;
		public static final Axes kX = new Axes((byte) 0);
		public static final Axes kY = new Axes((byte) 2);
		public static final Axes kZ = new Axes((byte) 4);

		private Axes(byte value) {
			this.value = value;
		}
	}

	public ADXL345_I2C_SparkFun(I2C.Port port, Accelerometer.Range range) {
		this.m_i2c = new I2C(port, 83);

		this.m_i2c.write(45, 8);

		setRange(range);

		UsageReporting.report(5, 2);
		LiveWindow.addSensor("ADXL345_I2C", port.getValue(), this);
	}

	public void setRange(Accelerometer.Range range) {
		byte value = 0;
		switch (range) {
		case k2G:
			value = 0;
			break;
		case k4G:
			value = 1;
			break;
		case k8G:
			value = 2;
			break;
		case k16G:
			value = 3;
		}
		this.m_i2c.write(49, 0x8 | value);
	}

	public double getX() {
		return getAcceleration(Axes.kX);
	}

	public double getY() {
		return getAcceleration(Axes.kY);
	}

	public double getZ() {
		return getAcceleration(Axes.kZ);
	}

	public double getAcceleration(Axes axis) {
		byte[] rawAccel = new byte[2];
		this.m_i2c.read(50 + axis.value, rawAccel.length, rawAccel);

		return accelFromBytes(rawAccel[0], rawAccel[1]);
	}

	private double accelFromBytes(byte first, byte second) {
		short tempLow = (short) (first & 0xFF);
		short tempHigh = (short) (second << 8 & 0xFF00);
		return (tempLow | tempHigh) * 0.00390625D;
	}

	public AllAxes getAccelerations() {
		AllAxes data = new AllAxes();
		byte[] rawData = new byte[6];
		this.m_i2c.read(50, rawData.length, rawData);

		data.XAxis = accelFromBytes(rawData[0], rawData[1]);
		data.YAxis = accelFromBytes(rawData[2], rawData[3]);
		data.ZAxis = accelFromBytes(rawData[4], rawData[5]);
		return data;
	}

	public String getSmartDashboardType() {
		return "3AxisAccelerometer";
	}

	public void initTable(ITable subtable) {
		this.m_table = subtable;
		updateTable();
	}

	public void updateTable() {
		if (this.m_table != null) {
			this.m_table.putNumber("X", getX());
			this.m_table.putNumber("Y", getY());
			this.m_table.putNumber("Z", getZ());
		}
	}

	public ITable getTable() {
		return this.m_table;
	}

	public void startLiveWindowMode() {
	}

	public void stopLiveWindowMode() {
	}
}
