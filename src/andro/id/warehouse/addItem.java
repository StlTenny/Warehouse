package andro.id.warehouse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class addItem extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additem);
        
        Button addNewItem = (Button) findViewById(R.id.addNewItem);
        addNewItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), addNewItem.class);
                startActivity(myIntent);
            }
        });
        
        Button addExistingItem = (Button) findViewById(R.id.addExistingItem);
        addExistingItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), scanItem.class);
                startActivity(myIntent);
            }
        });
        
    }
}



