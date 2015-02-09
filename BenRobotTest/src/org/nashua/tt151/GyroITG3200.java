package org.nashua.tt151;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import java.io.PrintStream;
import java.util.Arrays;

public class GyroITG3200 extends SensorBase implements PIDSource,
		LiveWindowSendable {
	int devAddr;
	byte[] buffer = new byte[7];
	public static final boolean DEBUG = true;
	I2C m_i2c;
	private short offsetX = 0;
	private short offsetY = 0;
	private short offsetZ = 0;
	private ITable m_table;
	public static final byte ITG3200_ADDRESS_AD0_LOW = 104;
	public static final byte ITG3200_ADDRESS_AD0_HIGH = 105;
	public static final int ITG3200_SPARKFUN_ADDRES = 210;
	public static final int ITG3200_DEFAULT_ADDRESS = 104;
	public static final byte ITG3200_RA_WHO_AM_I = 0;
	public static final byte ITG3200_RA_SMPLRT_DIV = 21;
	public static final byte ITG3200_RA_DLPF_FS = 22;
	public static final byte ITG3200_RA_INT_CFG = 23;
	public static final byte ITG3200_RA_INT_STATUS = 26;
	public static final byte ITG3200_RA_TEMP_OUT_H = 27;
	public static final byte ITG3200_RA_TEMP_OUT_L = 28;
	public static final byte ITG3200_RA_GYRO_XOUT_H = 29;
	public static final byte ITG3200_RA_GYRO_XOUT_L = 30;
	public static final byte ITG3200_RA_GYRO_YOUT_H = 31;
	public static final byte ITG3200_RA_GYRO_YOUT_L = 32;
	public static final byte ITG3200_RA_GYRO_ZOUT_H = 33;
	public static final byte ITG3200_RA_GYRO_ZOUT_L = 34;
	public static final byte ITG3200_RA_PWR_MGM = 62;
	public static final short ITG3200_DEVID_BIT = 6;
	public static final short ITG3200_DEVID_LENGTH = 6;
	public static final short ITG3200_DF_FS_SEL_BIT = 4;
	public static final short ITG3200_DF_FS_SEL_LENGTH = 2;
	public static final short ITG3200_DF_DLPF_CFG_BIT = 2;
	public static final short ITG3200_DF_DLPF_CFG_LENGTH = 3;
	public static final byte ITG3200_FULLSCALE_2000 = 3;
	public static final byte ITG3200_DLPF_BW_256 = 0;
	public static final byte ITG3200_DLPF_BW_188 = 1;
	public static final byte ITG3200_DLPF_BW_98 = 2;
	public static final byte ITG3200_DLPF_BW_42 = 3;
	public static final byte ITG3200_DLPF_BW_20 = 4;
	public static final byte ITG3200_DLPF_BW_10 = 5;
	public static final byte ITG3200_DLPF_BW_5 = 6;
	public static final byte ITG3200_INTCFG_ACTL_BIT = 7;
	public static final byte ITG3200_INTCFG_OPEN_BIT = 6;
	public static final byte ITG3200_INTCFG_LATCH_INT_EN_BIT = 5;
	public static final byte ITG3200_INTCFG_INT_ANYRD_2CLEAR_BIT = 4;
	public static final byte ITG3200_INTCFG_ITG_RDY_EN_BIT = 2;
	public static final byte ITG3200_INTCFG_RAW_RDY_EN_BIT = 0;
	public static final byte ITG3200_INTMODE_ACTIVEHIGH = 0;
	public static final byte ITG3200_INTMODE_ACTIVELOW = 1;
	public static final byte ITG3200_INTDRV_PUSHPULL = 0;
	public static final byte ITG3200_INTDRV_OPENDRAIN = 1;
	public static final byte ITG3200_INTLATCH_50USPULSE = 0;
	public static final byte ITG3200_INTLATCH_WAITCLEAR = 1;
	public static final byte ITG3200_INTCLEAR_STATUSREAD = 0;
	public static final byte ITG3200_INTCLEAR_ANYREAD = 1;
	public static final byte ITG3200_INTSTAT_ITG_RDY_BIT = 2;
	public static final byte ITG3200_INTSTAT_RAW_DATA_READY_BIT = 0;
	public static final byte ITG3200_PWR_H_RESET_BIT = 7;
	public static final byte ITG3200_PWR_SLEEP_BIT = 6;
	public static final byte ITG3200_PWR_STBY_XG_BIT = 5;
	public static final byte ITG3200_PWR_STBY_YG_BIT = 4;
	public static final byte ITG3200_PWR_STBY_ZG_BIT = 3;
	public static final byte ITG3200_PWR_CLK_SEL_BIT = 2;
	public static final byte ITG3200_PWR_CLK_SEL_LENGTH = 3;
	public static final byte ITG3200_CLOCK_INTERNAL = 0;
	public static final byte ITG3200_CLOCK_PLL_XGYRO = 1;
	public static final byte ITG3200_CLOCK_PLL_YGYRO = 2;
	public static final byte ITG3200_CLOCK_PLL_ZGYRO = 3;
	public static final byte ITG3200_CLOCK_PLL_EXT32K = 4;
	public static final byte ITG3200_CLOCK_PLL_EXT19M = 5;

	public GyroITG3200(I2C.Port port) {
		this.devAddr = 104;

		this.m_i2c = new I2C(port, this.devAddr);

		LiveWindow.addSensor("ITG3200_Gyro_I2C", port.getValue(), this);
	}

	public GyroITG3200(I2C.Port port, byte address) {
		this.devAddr = address;

		this.m_i2c = new I2C(port, address);

		LiveWindow.addSensor("ITG3200_Gyro_I2C", port.getValue(), this);
	}

	public void initialize() {
		if (!testConnection()) {
			DriverStation.reportError("Test connection failed!", false);
		}
		setFullScaleRange((byte) 3);
		setClockSource((byte) 1);
		setIntDeviceReadyEnabled(true);
		setIntDataReadyEnabled(true);
	}

	public boolean testConnection() {
		return getDeviceID() == 52;
	}

	private void writeBit(int register, byte bit, boolean value) {
		byte[] buf = new byte[1];
		ReadI2CBuffer(register, 1, buf);
		byte newValue = (byte) (value ? buf[0] | 1 << bit : buf[0]
				& (1 << bit ^ 0xFFFFFFFF));

		writeI2CBuffer(register, newValue);

		ReadI2CBuffer(register, 1, buf);
		if (newValue != buf[0]) {
			System.out.println("Expected " + newValue + " seeing " + buf[0]);
		}
	}

	public static byte updateByte(byte original, int bit, int numBits,
			byte value) {
		if (numBits > 7) {
			throw new IllegalArgumentException(
					"This routine is intended to use 8-bit bytes. \n Value: "
							+ GetBinaryString(value) + "\n Number bits: "
							+ numBits);
		}
		if (bit > 7) {
			throw new IllegalArgumentException(
					"This routine is intended to use 8-bit bytes. \n Value: "
							+ GetBinaryString(value) + "\n Bit: " + bit);
		}
		if (bit < numBits - 1) {
			throw new IllegalArgumentException(
					"This routine is intended to use 8-bit bytes. \n Value: "
							+ GetBinaryString(value) + "\n Bit: " + bit
							+ "\n Number bits: " + numBits);
		}
		if (value > Math.pow(2.0D, numBits)) {
			throw new IllegalArgumentException(
					"Cannot encode a number this big using the number of bits requested \n Value: "
							+ GetBinaryString(value) + "\n Number bits: "
							+ numBits);
		}
		if ((bit < 0) || (numBits < 0) || (value < 0)) {
			throw new IllegalArgumentException(
					"This routine is intended to use 8-bit bytes. \n All inputs should be greater than 0. \n Value: "
							+ GetBinaryString(value)
							+ "\n Bit: "
							+ bit
							+ "\n Number bits: " + numBits);
		}
		byte mask = getMask(bit, numBits);
		byte maskedOriginal = (byte) (original & mask & 0xFF);
		byte shiftedValue = (byte) (value << 1 + bit - numBits & 0xFF);
		byte result = (byte) ((shiftedValue | maskedOriginal) & 0xFF);

		return result;
	}

	public static String GetBinaryString(byte value) {
		return String.format("%8s",
				new Object[] { Integer.toBinaryString(value & 0xFF) }).replace(
				' ', '0');
	}

	public boolean writeI2CBuffer(int registerAddress, int data) {
		boolean retVal = false;
		try {
			retVal = this.m_i2c.write(registerAddress, data);

			byte[] buf = new byte[1];
			ReadI2CBuffer(registerAddress, 1, buf);
			if (data != buf[0]) {
				DriverStation.reportError("Expected " + data + "\nseeing "
						+ buf[0] + "\n", false);
			}
		} catch (Throwable t) {
			DriverStation.reportError(
					"ERROR Unhandled exception: " + t.toString() + " at "
							+ Arrays.toString(t.getStackTrace()), false);
		}
		return retVal;
	}

	private void writeBits(int register, int bit, int numBits, byte value) {
		try {
			byte[] rawData = new byte[1];
			ReadI2CBuffer(register, 1, rawData);
			byte newValue = updateByte(rawData[0], bit, numBits, value);
			writeI2CBuffer(register, newValue);
		} catch (Throwable t) {
			DriverStation.reportError(
					"ERROR Unhandled exception: " + t.toString() + " at "
							+ Arrays.toString(t.getStackTrace()), false);
		}
	}

	private boolean readBit(int register, byte bit) {
		byte[] buf = new byte[1];
		ReadI2CBuffer(register, 1, buf);
		return (buf[0] & bit) != 0;
	}

	private static byte getBits(byte bitField, int bit, int numBits) {
		if (numBits > 7) {
			throw new IllegalArgumentException(
					"This routine is intended to use 8-bit bytes.\n Number bits: "
							+ numBits);
		}
		if (bit > 7) {
			throw new IllegalArgumentException(
					"This routine is intended to use 8-bit bytes. \n Bit: "
							+ bit);
		}
		if (bit < numBits - 1) {
			throw new IllegalArgumentException(
					"This routine is intended to use 8-bit bytes. \n Bit: "
							+ bit + "\n Number bits: " + numBits);
		}
		if ((bit < 0) || (numBits < 0)) {
			throw new IllegalArgumentException(
					"This routine is intended to use 8-bit bytes. \n All inputs should be greater than 0. \n Bit: "
							+ bit + "\n Number bits: " + numBits);
		}
		byte result = 0;

		byte mask = (byte) ((getMask(bit, numBits) ^ 0xFFFFFFFF) & 0xFF);
		byte maskedInput = (byte) (bitField & mask & 0xFF);
		result = (byte) (maskedInput >>> 1 + bit - numBits & 0xFF);

		return result;
	}

	private static byte getMask(int bit, int numBits) {
		int newMask = 0;
		for (int i = 0; i <= 7; i++) {
			if ((i > bit) || (i <= bit - numBits)) {
				newMask = (int) (newMask + Math.pow(2.0D, i));
			}
		}
		byte mask = (byte) (newMask & 0xFF);
		return mask;
	}

	private byte getRegisterByte(int register) {
		byte[] buf = new byte[1];
		ReadI2CBuffer(register, 1, buf);
		return buf[0];
	}

	private byte getRegisterBits(int register, int bit, int numBits) {
		byte containingByte = getRegisterByte(register);
		return getBits(containingByte, bit, numBits);
	}

	public byte getDeviceID() {
		return getRegisterBits(0, 6, 6);
	}

	public void setDeviceID(byte id) {
		writeBits(0, 6, 6, id);
	}

	public byte getRate() {
		return getRegisterByte(21);
	}

	public void setRate(byte rate) {
		writeI2CBuffer(21, rate);
	}

	public byte getFullScaleRange() {
		return getRegisterBits(22, 4, 2);
	}

	public void setFullScaleRange(byte range) {
		writeBits(22, 4, 2, range);
	}

	public byte getDLPFBandwidth() {
		return getRegisterBits(22, 2, 3);
	}

	public void setDLPFBandwidth(byte bandwidth) {
		writeBits(22, 2, 3, bandwidth);
	}

	public boolean getInterruptMode() {
		return readBit(23, (byte) 7);
	}

	public void setInterruptMode(boolean mode) {
		writeBit(23, (byte) 7, mode);
	}

	public boolean getInterruptDrive() {
		return readBit(23, (byte) 6);
	}

	public void setInterruptDrive(boolean drive) {
		writeBit(23, (byte) 6, drive);
	}

	public boolean getInterruptLatch() {
		return readBit(23, (byte) 5);
	}

	public void setInterruptLatch(boolean latch) {
		writeBit(23, (byte) 5, latch);
	}

	public boolean getInterruptLatchClear() {
		return readBit(23, (byte) 4);
	}

	public void setInterruptLatchClear(boolean clear) {
		writeBit(23, (byte) 4, clear);
	}

	public boolean getIntDeviceReadyEnabled() {
		return readBit(23, (byte) 2);
	}

	public void setIntDeviceReadyEnabled(boolean enabled) {
		writeBit(23, (byte) 2, enabled);
	}

	public boolean getIntDataReadyEnabled() {
		return readBit(23, (byte) 0);
	}

	public void setIntDataReadyEnabled(boolean enabled) {
		writeBit(23, (byte) 0, enabled);
	}

	public boolean getIntDeviceReadyStatus() {
		return readBit(26, (byte) 2);
	}

	public boolean getIntDataReadyStatus() {
		return readBit(26, (byte) 0);
	}

	public short getTemperature() {
		byte[] buf = new byte[2];
		ReadI2CBuffer(27, 2, buf);
		return (short) ((short) buf[0] << 8 | (short) buf[1]);
	}

	public AllAxes getRotation() {
		AllAxes data = new AllAxes();
		byte[] buffer = new byte[6];
		ReadI2CBuffer(29, 6, buffer);
		data.XAxis = ((short) ((short) buffer[0] << 8 | buffer[1]));
		data.YAxis = ((short) ((short) buffer[2] << 8 | buffer[3]));
		data.ZAxis = ((short) ((short) buffer[4] << 8 | buffer[5]));
		return data;
	}

	public void ReadI2CBuffer(int registerAddress, int count, byte[] buffer) {
		try {
			this.m_i2c.read(registerAddress, count, buffer);
		} catch (Throwable t) {
			DriverStation.reportError(
					"ERROR Unhandled exception in I2C Read: " + t.toString()
							+ " at " + Arrays.toString(t.getStackTrace()),
					false);
		}
	}

	public short ReadShortFromRegister(byte register, int count) {
		byte[] buffer = new byte[count];
		ReadI2CBuffer(register, count, buffer);
		return (short) ((short) buffer[0] << 8 | buffer[1]);
	}

	public void initRotation() {
		int accumX = 0;
		int accumY = 0;
		int accumZ = 0;
		for (int i = 0; i < 20; i++) {
			accumX += getRotationX();
			accumY += getRotationY();
			accumZ += getRotationZ();
			try {
				Thread.sleep(5L);
			} catch (Exception e) {
			}
		}
		this.offsetX = ((short) (int) (accumX / 20.0D));
		this.offsetY = ((short) (int) (accumY / 20.0D));
		this.offsetZ = ((short) (int) (accumZ / 20.0D));
	}

	public short getRotationX() {
		return (short) (ReadShortFromRegister((byte) 29, 2) - this.offsetX);
	}

	public short getRotationY() {
		return (short) (ReadShortFromRegister((byte) 31, 2) - this.offsetY);
	}

	public short getRotationZ() {
		return (short) (ReadShortFromRegister((byte) 33, 2) - this.offsetZ);
	}

	public void reset() {
		writeBit(62, (byte) 7, true);
	}

	public boolean getSleepEnabled() {
		return readBit(62, (byte) 6);
	}

	public void setSleepEnabled(boolean enabled) {
		writeBit(62, (byte) 6, enabled);
	}

	public boolean getStandbyXEnabled() {
		return readBit(62, (byte) 5);
	}

	public void setStandbyXEnabled(boolean enabled) {
		writeBit(62, (byte) 5, enabled);
	}

	public boolean getStandbyYEnabled() {
		return readBit(62, (byte) 4);
	}

	public void setStandbyYEnabled(boolean enabled) {
		writeBit(62, (byte) 4, enabled);
	}

	public boolean getStandbyZEnabled() {
		return readBit(62, (byte) 3);
	}

	public void setStandbyZEnabled(boolean enabled) {
		writeBit(62, (byte) 3, enabled);
	}

	public byte getClockSource() {
		byte[] buf = new byte[1];
		ReadI2CBuffer(62, 1, buf);

		return (byte) (buf[0] & 0x2);
	}

	public void setClockSource(byte source) {
		writeBits(62, 2, 3, source);
	}

	public void initTable(ITable subtable) {
		this.m_table = subtable;
		updateTable();
	}

	public ITable getTable() {
		return this.m_table;
	}

	public void updateTable() {
		if (this.m_table != null) {
			this.m_table.putNumber("GyroX", getRotationX());
			this.m_table.putNumber("GyroY", getRotationY());
			this.m_table.putNumber("GyroZ", getRotationZ());
			this.m_table.putNumber("GyroPIDValue", pidGet());
		}
	}

	public String getSmartDashboardType() {
		return "Gyro";
	}

	public void startLiveWindowMode() {
	}

	public void stopLiveWindowMode() {
	}

	public double pidGet() {
		AllAxes var = getRotation();
		double result = Math.cbrt(var.XAxis * var.XAxis + var.YAxis * var.YAxis
				+ var.ZAxis * var.ZAxis);
		return result;
	}

	public static class AllAxes {
		public short XAxis;
		public short YAxis;
		public short ZAxis;
	}
}
