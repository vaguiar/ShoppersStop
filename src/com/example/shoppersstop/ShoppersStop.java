package com.example.shoppersstop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShoppersStop extends Activity {

	ArrayAdapter<String> m_adapter;
	ArrayList<String> m_listItems = new ArrayList<String>();
	Button bt;
	Button bt2;
	EditText et;
	TextView tv;
	LinearLayout checkboxLayout;
	//ListView lv;

	private Controller controller;
	Context m_context;

	// DB components
	private DataProvider dbProvider;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dbProvider = new DataProvider(getBaseContext());
		dbProvider.open();

//		try {
//			insertIntoDB();
//			insertItemsIntoDB();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		controller = new Controller();

		bt = (Button) findViewById(R.id.button1);
		bt2 = (Button) findViewById(R.id.button2);
		et = (EditText) findViewById(R.id.editText1);
		//lv = (ListView) findViewById(R.id.listView1);
		//lv.canScrollVertically(0);
		
		checkboxLayout = (LinearLayout) findViewById(R.id.Checkbox_Layout);
        
		List<ItemsMap> map = dbProvider.getAllItemsMaps();
		for(int i = 0; i < map.size(); i++){
			
		    CheckBox cb = new CheckBox(getApplicationContext());
            cb.setText(map.get(i).getName());
            cb.setTextColor(Color.RED);
            checkboxLayout.addView(cb);
        	
		}
		
		m_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, m_listItems);

		bt.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				String input = et.getText().toString();

				if (null != input && input.length() > 0) {

					CheckBox cb = new CheckBox(getApplicationContext());
		            cb.setText(input);
		            cb.setTextColor(Color.RED);
		            checkboxLayout.addView(cb);
		            et.setText("");
				}
			}
		});

		bt2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				
				for (int i = 0; i < checkboxLayout.getChildCount(); i++) {
		            if (checkboxLayout.getChildAt(i) instanceof CheckBox) {
		                CheckBox cb = (CheckBox) checkboxLayout.getChildAt(i);
		                if (cb.isChecked()) {
		                	controller.addToShoppingList(cb.getText().toString());
		                }
		            }
		        }
				
				controller.initializeStore();
				controller.setItemCoordinates();
				controller.plotItemsOnStoreMap();
				controller.findShortestPath() ;

				setContentView(controller.renderer);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class Controller {

		// UI componenets
		Renderer renderer = new Renderer(getBaseContext());

		// Algorithmic Componenets
		MapLayout mapLayout = new MapLayout(renderer);
		ItemPlotter itemPlotter = new ItemPlotter(renderer);
		PathFinder pathFinder = new PathFinder(renderer);

		// Data Structure components
		private ItemsMap startPt = new ItemsMap();
		private ItemsMap endPt = new ItemsMap();
		private ItemsMap boundary = new ItemsMap();
		private List<ItemsMap> itemCoordinates;
		private List<String> shoppingList = new ArrayList<String>();

		private ItemsMap getStartPt() {
			return startPt;
		}

		private void setStartPt() {
			StoreShelf sh = dbProvider.getSelectedShelf("ENTRANCE");
			this.startPt.setX(sh.getP1_X());
			this.startPt.setY(sh.getP1_Y());
			this.startPt.setName(sh.getCatagory());
		}

		private ItemsMap getEndPt() {
			return endPt;
		}

		private void setEndPt() {
			StoreShelf sh = dbProvider.getSelectedShelf("Exit");
			this.endPt.setX(sh.getP1_X());
			this.endPt.setY(sh.getP1_Y());
			this.endPt.setName(sh.getCatagory());
		}

		@SuppressLint("NewApi")
		private void setBoundary() {
			Display dis = getWindowManager().getDefaultDisplay();
			Point p = new Point();
			dis.getSize(p);
			boundary.setX(p.x);
			boundary.setY(p.y -120);
		}


		private ItemsMap getBoundary() {
			return this.boundary;
		}
		
		// setter
		public void setItemCoordinates() {
			if (shoppingList != null) {
				this.itemCoordinates = dbProvider
						.getSelectedtemsMaps(shoppingList);
			} else {
				// Code to validate: Empty list is not propagated
			}
		}

		// getter
		public List<ItemsMap> getItemCoordinates() {
			return this.itemCoordinates;
		}

		public void initializeStore() {
			mapLayout.createMapLayout(dbProvider.getAllStoreShelves());
		}

		public void plotItemsOnStoreMap() {
			itemPlotter.plotItems(itemCoordinates);
		}

		public void addToShoppingList(String input) {

			if (!this.shoppingList.contains(input)) {
				this.shoppingList.add(input);
			}
		}

		public void findShortestPath() {
			setStartPt();
			setEndPt();
			setBoundary();
			pathFinder.calculatePath(itemCoordinates, getStartPt(), getEndPt(),
					getBoundary());

		}


	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		dbProvider.close();
	}

	@SuppressLint("NewApi")
	void insertIntoDB() throws IOException {

		Display dis = getWindowManager().getDefaultDisplay();
		Point p = new Point();
		dis.getSize(p);
		float x = p.x / 1000f;
		float y = p.y / 1000f;

		File f = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/nitesh/StoreMap");
		FileInputStream fileIS = new FileInputStream(f);
		InputStreamReader iRdr = new InputStreamReader(fileIS);
		BufferedReader bRdr = new BufferedReader(iRdr);
		String val_ = " ";
		while (val_ != null) {
			val_ = bRdr.readLine();
			if (val_ == null || val_.length() < 10)
				continue;

			StringTokenizer stok = new StringTokenizer(val_, " ");
			StoreShelf smap = new StoreShelf();
			smap.setCatagory(stok.nextToken());
			smap.setP1_X(Integer.parseInt(stok.nextToken().trim()));
			smap.setP1_Y(Integer.parseInt(stok.nextToken().trim()));
			smap.setP2_X(Integer.parseInt(stok.nextToken().trim()));
			smap.setP2_Y(Integer.parseInt(stok.nextToken().trim()));
			dbProvider.insertMap(smap.getCatagory(),
					(int) (smap.getP1_X() * x), (int) (smap.getP1_Y() * y),
					(int) (smap.getP2_X() * x), (int) (smap.getP2_Y() * y));
		}

		bRdr.close();
		iRdr.close();
		fileIS.close();

	}

	@SuppressLint("NewApi")
	void insertItemsIntoDB() throws IOException {
		Display dis = getWindowManager().getDefaultDisplay();
		Point p = new Point();
		dis.getSize(p);
		float x = p.x / 1000f;
		float y = p.y / 1000f;

		File f = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/nitesh/itemMap");
		FileInputStream fileIS = new FileInputStream(f);
		InputStreamReader iRdr = new InputStreamReader(fileIS);
		BufferedReader bRdr = new BufferedReader(iRdr);
		String val_ = " ";
		int i = 0;
		while (val_ != null) {
			val_ = bRdr.readLine();
			if (val_ == null || val_.length() < 10)
				continue;

			StringTokenizer stok = new StringTokenizer(val_, " ");
			ItemsMap map = new ItemsMap();
			map.setName(stok.nextToken());
			map.setCatagory(stok.nextToken().trim());
			if (i < 30) {
				map.setX(Integer.parseInt(stok.nextToken().trim()));
				map.setY(Integer.parseInt(stok.nextToken().trim()));
			} else {
				map.setX(Integer.parseInt(stok.nextToken().trim()) + 10);
				map.setY(Integer.parseInt(stok.nextToken().trim()) + 10);
			}

			i++;

			dbProvider.insertItemsMap(map.getName(), map.getCatagory(),
					(int) (map.getX() * x), (int) (map.getY() * y));
		}

		bRdr.close();
		iRdr.close();
		fileIS.close();

	}
}
