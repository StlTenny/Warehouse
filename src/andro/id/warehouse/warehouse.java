package andro.id.warehouse;

import andro.id.warehouse.azureModule;
import andro.id.warehouse.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class warehouse extends Activity {
	
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Key for message passing to Id scan
    public static final String ID_SCANNED = "idscanned";
    
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_ID_SCAN = 3;

    // Layout Views
    private TextView mTitle;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    
    // Object to handle azure module
    private azureModule mAzureModule = null;
    
    // Object to handle id scan
    private final Handler idHandler = null;
    
    //To handle intent
    public static final String CUSTOM_INTENT = "andro.id.warehouse.idtest";
    
    //Setup string to display status
    private TextView statusText;
    private TextView lastIDScanned;
    
    //storage of currently scanned string
    private String currentID;
    public static String lastID;
    
    

    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        statusText = (TextView) findViewById(R.id.statusText);
        lastIDScanned = (TextView) findViewById(R.id.lastIDScanned);
        
        Button addItem = (Button) findViewById(R.id.B_addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), addItem.class);
                startActivity(myIntent);
            }
        });
        
        Button scanItem = (Button) findViewById(R.id.B_scanItem);
        scanItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), scanItem.class);
                startActivity(myIntent);
            }
        });
        
        Button browseInventory = (Button) findViewById(R.id.B_browseInventory);
        browseInventory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), browseInventory.class);
                startActivity(myIntent);
            }
        });


        
    }
    
    @Override
    public void onStart() {
        super.onStart();

        // If BT is not on, request that it be enabled.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
        	mAzureModule = new azureModule(this, mHandler);
            //if (mItemDatabase == null) db.setupDatabase();
        }
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Get the BluetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
                mAzureModule.connect(device);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns, this is launched in the onStart() function
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                //setupChat();
            } else {
                Toast.makeText(this, "Bluetooth not enabled" , Toast.LENGTH_SHORT).show();
                finish();
            }
/*        case REQUEST_ID_SCAN:
        	if (resultCode == Activity.RESULT_OK){
        		byte[] readBuf = (byte[]) data.getExtras().getString(azureModule.scannedID);
                currentID = new String(readBuf, 0, msg.arg1);
        	}
  */      	
        	}
        }
    
    @Override
    public synchronized void onResume() {
    	super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mAzureModule != null) {
            //Only if the state is STATE_NONE, do we know that we haven't started already
            if (mAzureModule.getState() == azureModule.STATE_NONE) {
              // Start the Bluetooth chat services
            	//mAzureModule.start();
            	Toast.makeText(getApplicationContext(), "Doing Something", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case azureModule.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case azureModule.STATE_CONNECTED:
                    statusText.setText(R.string.subtext_connected_to);
                    break;
                case azureModule.STATE_CONNECTING:
                    statusText.setText(R.string.subtext_connecting);
                    break;
                case azureModule.STATE_LISTEN:
                case azureModule.STATE_NONE:
                    statusText.setText(R.string.subtext_not_connected);
                    break;
                }
                break;
            case azureModule.MESSAGE_ID_SCANNED:
                byte[] readBuf = (byte[]) msg.obj;
                lastID = new String(readBuf, 0, msg.arg1);
                Toast.makeText(getApplicationContext(), lastID, Toast.LENGTH_SHORT).show();
                lastIDScanned.setText("Last ID Scanned: ");
                lastIDScanned.append(lastID);
                Intent i = new Intent();
                i.setAction(warehouse.CUSTOM_INTENT);
                i.putExtra(ID_SCANNED, lastID);
                getApplicationContext().sendBroadcast(i);
                break;
            case azureModule.MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
        
        
    };
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.scan:
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            Toast.makeText(getApplicationContext(), "Trying to open thing again", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}