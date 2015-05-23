package com.quadcoder.coinpet;

import android.bluetooth.BluetoothDevice;

public class WrapBluetoothDevice {
	BluetoothDevice mDevice;
	public WrapBluetoothDevice(BluetoothDevice device) {
		mDevice = device;
	}
	
	public BluetoothDevice getDevice() {
		return mDevice;
	}
	
	@Override
	public String toString() {
		// CREATE Auto-generated method stub
		return mDevice.getName() + "\n" + mDevice.getAddress();
	}
}
