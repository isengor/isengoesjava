package isen.IsenTanks;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import isen.IsenTanks.Client.DeviceList;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    private Button startBtn,connectBtn,exitBtn;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,1);
        }

        startBtn = (Button) findViewById(R.id.button);
        connectBtn = (Button) findViewById(R.id.button2);
        exitBtn = (Button) findViewById(R.id.button3);

        setListeners();
    }



    private void setListeners(){
        startBtn.setOnClickListener(this);
        connectBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:{
                Intent discoverableIntent =
                        new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
                Intent intent = new Intent(this,GameActivity.class);
                intent.putExtra("server",true);
                startActivity(intent);
                break;
            }
            case R.id.button2:{


                    Intent intent = new Intent(this,DeviceList.class);
                    startActivityForResult(intent,1);

                    break;

            }
            case R.id.button3:{
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
       // Toast.makeText(this, data.getExtras().get("id").toString(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("client",data.getExtras().getParcelable("device"));
        startActivity(intent);

    }


}

