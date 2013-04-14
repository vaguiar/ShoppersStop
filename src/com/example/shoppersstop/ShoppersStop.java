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
import android.os.Bundle;
import android.os.Environment;
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
	List<String> shoppingList = new ArrayList<String>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dbProvider = new DataProvider(getBaseContext());
		dbProvider.open();
		
	
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
					}
					addToShoppingList(input);

					m_adapter.notifyDataSetChanged();
					et.setText("");

				}
			}
		});

		bt2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				controller.initializeStore();
				setContentView(controller.renderer);

//				controller.plotItemsOnStoreMap(shoppingList);
//				setContentView(controller.renderer);
			}
		});
	}

	private void addToShoppingList(String input) {
		if (!shoppingList.contains(input)) {
			shoppingList.add(input);
		}
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

		private void initializeStore() {
			mapLayout.createMapLayout(dbProvider.getAllStoreShelves());
		}

		private void plotItemsOnStoreMap(List<String> userItemList) {
			itemPlotter.plotItems(dbProvider.getSelectedtemsMaps(userItemList));
		}

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		dbProvider.close();
	}

	void insertIntoDB() throws IOException{
		File f = new File(Environment.getExternalStorageDirectory().getPath() + "/nitesh/StoreMap");
		FileInputStream fileIS = new FileInputStream(f);
		InputStreamReader iRdr = new InputStreamReader(fileIS);
		BufferedReader bRdr = new BufferedReader(iRdr);
		String val_ = " ";
		while(val_ != null){
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
					smap.getP1_X(), 
					smap.getP1_Y(), 
					smap.getP2_X(), 
					smap.getP2_Y());
			}
		
		bRdr.close();
		iRdr.close();
		fileIS.close();

	}
}
