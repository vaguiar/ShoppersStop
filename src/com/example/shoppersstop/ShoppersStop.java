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
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ShoppersStop extends Activity {

	ArrayAdapter<String> m_adapter;
	ArrayList<String> m_listItems = new ArrayList<String>();
	Button bt;
	Button bt2;
	EditText et;
	TextView tv;
	ListView lv;

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

/*		try {
			insertIntoDB();
			insertItemsIntoDB();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		controller = new Controller();

		bt = (Button) findViewById(R.id.button1);
		bt2 = (Button) findViewById(R.id.button2);
		et = (EditText) findViewById(R.id.editText1);
		lv = (ListView) findViewById(R.id.listView1);
		lv.canScrollVertically(0);

		m_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, m_listItems);
		lv.setAdapter(m_adapter);
		final String input = et.getText().toString();

		bt.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				String input = et.getText().toString();

				if (null != input && input.length() > 0) {

					if (!m_listItems.contains(input)) {
						m_listItems.add(input);
						controller.addToShoppingList(input);
					}

					m_adapter.notifyDataSetChanged();
					et.setText("");

				}
			}
		});

		bt2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				controller.initializeStore();
				controller.setItemCoordinates();
				controller.plotItemsOnStoreMap();

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
		PathFinder pathFinder = new PathFinder();

		// Data Structure components
		private List<ItemsMap> itemCoordinates;
		private List<String> shoppingList = new ArrayList<String>();

		// setter
		public void setItemCoordinates() {
			if (shoppingList != null) {
				this.itemCoordinates = dbProvider
						.getSelectedtemsMaps(shoppingList);
			}
			else{
				//Code to validate: Empty list is not propagated
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
