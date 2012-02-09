package andro.id.warehouse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class addNewItem extends Activity {
	
	public static final int CAMERA_PIC_REQUEST = 14;
	private TextView uniqueID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemdesc);
        
        uniqueID = (TextView) findViewById(R.id.editUniqID);
        uniqueID.setText(warehouse.lastID);
        
        Button addPhoto = (Button) findViewById(R.id.addPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            	startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });
        
        Button save = (Button) findViewById(R.id.saveData);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), warehouse.class);
                startActivity(myIntent);
            }
        });
        
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
    	     if (requestCode == CAMERA_PIC_REQUEST) {  
    	    	 Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
    	    	 ImageView itemPic = (ImageView) findViewById(R.id.itemPicture);  
    	    	 itemPic.setImageBitmap(thumbnail);
    	     }
    }
    
}