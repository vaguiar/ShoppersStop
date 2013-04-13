package com.example.shoppersstop;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private final Controller controller = new Controller();
	
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

    private class Controller{
		
    	//UI componenets
		Renderer renderer = new Renderer(getBaseContext());
		
		//Algorithmic Componenets
		MapLayout mapLayout = new MapLayout();
		ItemPlotter itemPlotter = new ItemPlotter();
		PathFinder pathFinder = new PathFinder();

		//DB components
		DBTalker dbTalker = new DBTalker();
			
		public void createList(){
			
		}
		
		private void getStoreMap(DBTalker dbTalker){
			
			
		}
		
	}
    
}
