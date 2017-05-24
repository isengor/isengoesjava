package isen.IsenTanks.Client;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import isen.IsenTanks.R;

/**
 * Created by Ilya on 24.05.2017.
 */

public class DeviceList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;

    ArrayAdapter<String> adapter;
    ArrayList<BluetoothDevice> devices;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        listView = new ListView(this);

        setContentView(R.layout.device_list);

        listView = (ListView) findViewById(R.id.deviceslist);
        devices = new ArrayList<>();


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        listView.setOnItemClickListener(this);
        adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item);

        listView.setAdapter(adapter);

        bluetoothAdapter.startDiscovery();

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                devices.add(device);
                adapter.add(deviceName);
                Log.d("MyLog","Device found: " + deviceName
                        + " mac address: " + deviceHardwareAddress);
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent data = new Intent();
        data.putExtra("device",devices.get(position));
        setResult(Activity.RESULT_OK,data);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        bluetoothAdapter.cancelDiscovery();
        unregisterReceiver(mReceiver);
    }
}

