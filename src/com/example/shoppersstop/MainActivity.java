package com.example.shoppersstop;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;

import android.view.Menu;

public class MainActivity extends Activity {

	
	private final Controller controller = new Controller();

	//DB components
	private DataProvider dbProvider;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        controller.initializeStore();
        
        setContentView(controller.renderer);
        dbProvider = new DataProvider(this);
		dbProvider.open();
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
		MapLayout mapLayout = new MapLayout(renderer);
		ItemPlotter itemPlotter = new ItemPlotter(renderer);
		PathFinder pathFinder = new PathFinder();

	
		private void initializeStore(){			
			mapLayout.createMapLayout(dbProvider.getAllStoreShelves());
		}
		
		private void plotItemsOnStoreMap(List<String> userItemList){
			itemPlotter.plotItems(dbProvider.getSelectedtemsMaps(userItemList));
		}
		
	}
    

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		dbProvider.close();
	}

}
