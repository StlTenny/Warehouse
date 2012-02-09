package andro.id.warehouse;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class scanItem extends Activity {

	private static final int ID_STRING = 15;
	private warehouse mWarehouse = null;
	private String idString = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idscan);
        //mWarehouse = new warehouse(this, idHandler);
    }
    
        private BroadcastReceiver incReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(warehouse.CUSTOM_INTENT)) {
                    scanItem.this.setResult(Activity.RESULT_OK,intent);
                    scanItem.this.finish();
                }
            }
        };


        
    }