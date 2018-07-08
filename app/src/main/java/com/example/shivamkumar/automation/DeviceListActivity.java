package com.example.shivamkumar.automation;

import com.example.shivamkumar.automation.R;
import android.system.*;
import android.os.*;
import android.app.AlertDialog;
import java.lang.reflect.Method;
import java.util.ArrayList;
import android.content.DialogInterface;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.shivamkumar.automation.DeviceListAdapter;
import com.spark.submitbutton.SubmitButton;


public class DeviceListActivity extends Activity {
	private ListView mListView;
	private DeviceListAdapter mAdapter;
	private ArrayList<BluetoothDevice> mDeviceList;
	Button btnDevices,btnClicked;
	private static final String TAG = "DeviceListActivity";
	private static final boolean D = true;

	private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
	private long mBackPressed;
	@Override
	public void onBackPressed()
	{
		if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
		{
			super.onBackPressed();
			return;
		}
		else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

		mBackPressed = System.currentTimeMillis();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_paired_devices);
		btnDevices = (Button) findViewById(R.id.btnDevices);
		btnClicked = (Button) findViewById(R.id.btnClicked);
		mDeviceList		= getIntent().getExtras().getParcelableArrayList("device.list");
		
		mListView		= (ListView) findViewById(R.id.lv_paired);
		
		mAdapter		= new DeviceListAdapter(this);
		
		mAdapter.setData(mDeviceList);
		mAdapter.setListener(new DeviceListAdapter.OnPairButtonClickListener() {			
			@Override
			public void onPairButtonClick(int position) {
				BluetoothDevice device = mDeviceList.get(position);
				
				if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
					unpairDevice(device);
				} else {
					showToast("Pairing...");
					
					pairDevice(device);
				}
			}
		});
		
		mListView.setAdapter(mAdapter);
		
		registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));


		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		this.registerReceiver(mReceiver, filter);
		run();




	}
	@Override
	public void onDestroy() {

		try{
			if(mPairReceiver!=null)
				unregisterReceiver(mPairReceiver);

		}catch(Exception e){}

		super.onDestroy();
	}



	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				btnDevices.setEnabled(true);
			}
			else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
				btnDevices.setEnabled(true);
			}

			else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				btnDevices.setEnabled(false);

			}
		}
	};

	public void backClicked(View view){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(DeviceListActivity.this);
		builder1.setMessage("Are You Sure You Are Connected To Device?");
		builder1.setCancelable(true);

		builder1.setPositiveButton(
				"Yes I am!",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						btnDevices.setEnabled(true);
						dialog.cancel();
					}
				});

		builder1.setNegativeButton(
				"Nopes!",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						finish();
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();
		 //btnDevices.setEnabled(true);
	}


	////change this
	public void devicesBtnClicked(View view){
		startActivity(new Intent(DeviceListActivity.this, devicegrid.class));
		finish();
	}
	
	
	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
	
    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
	        	 final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
	        	 final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
	        	 
	        	 if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
	        		 showToast("Paired");
	        		 btnDevices.setEnabled(true);

	        	 } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
	        		 showToast("Unpaired");
	        	 }
	        	 
	        	 mAdapter.notifyDataSetChanged();
	        }
	    }
	};
	public void run(){
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				btnClicked.setEnabled(true);
			}
		}, 5000);

	}


}