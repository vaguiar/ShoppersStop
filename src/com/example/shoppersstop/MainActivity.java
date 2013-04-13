package com.example.shoppersstop;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

//	Renderer renderer = new Renderer();
	MapLayout mapLayout = new MapLayout();
	ItemPlotter itemPlotter = new ItemPlotter();
	PathFinder pathFinder = new PathFinder();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
