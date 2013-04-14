package com.example.shoppersstop;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataProvider {

	// Database fields
	private SQLiteDatabase database;
	private CreateDatabase dbHelper;
	private String[] allItemColumns = { CreateDatabase.COLUMN_Name,
			CreateDatabase.COLUMN_Catagory, CreateDatabase.COLUMN_X,
			CreateDatabase.COLUMN_Y };

	private String[] allMapColumns = { CreateDatabase.COLUMN_ID,
			CreateDatabase.COLUMN_Catagory, 
			CreateDatabase.COLUMN_P1_X,
			CreateDatabase.COLUMN_P1_Y,
			CreateDatabase.COLUMN_P2_X,
			CreateDatabase.COLUMN_P2_Y };
	
	public DataProvider(Context context) {
		dbHelper = new CreateDatabase(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void insertItemsMap(String name, String catagory, int X, int Y) {
		ContentValues values = new ContentValues();
		values.put(CreateDatabase.COLUMN_Name, name);
		values.put(CreateDatabase.COLUMN_Catagory, catagory);
		values.put(CreateDatabase.COLUMN_X, X);
		values.put(CreateDatabase.COLUMN_Y, Y);
		database.insert(CreateDatabase.TABLE_Items, null, values);

	}

	public void deleteItemsMap(ItemsMap ItemsMap) {
		/*
		 * long id = ItemsMap.getId();
		 * System.out.println("ItemsMap deleted with id: " + id);
		 * database.delete(CreateDataBase.TABLE_ItemsMapS,
		 * CreateDataBase.COLUMN_ID + " = " + id, null);
		 */
	}

	public List<ItemsMap> getSelectedtemsMaps(List<String> items) {
		List<ItemsMap> ItemsMaps = new ArrayList<ItemsMap>();

		for (int i = 0; i < items.size(); i++) {

			Cursor cursor = database.query(CreateDatabase.TABLE_Items,
					allItemColumns,
					CreateDatabase.COLUMN_Name + " = '" + items.get(i) + "'",
					null, null, null, null);

			cursor.moveToFirst();
			ItemsMap newItemsMap = cursorToItemsMap(cursor);
			ItemsMaps.add(newItemsMap);
			cursor.close();

		}

		return ItemsMaps;
	}

	public List<ItemsMap> getAllItemsMaps() {
		List<ItemsMap> ItemsMaps = new ArrayList<ItemsMap>();

		Cursor cursor = database.query(CreateDatabase.TABLE_Items, allItemColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ItemsMap ItemsMap = cursorToItemsMap(cursor);
			ItemsMaps.add(ItemsMap);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return ItemsMaps;
	}

	public void insertMap(String catagory, int P1_X, int P1_Y, int P2_X,
			int P2_Y) {
		ContentValues values = new ContentValues();
		values.put(CreateDatabase.COLUMN_Catagory, catagory);
		values.put(CreateDatabase.COLUMN_P1_X, P1_X);
		values.put(CreateDatabase.COLUMN_P1_Y, P1_Y);
		values.put(CreateDatabase.COLUMN_P2_X, P2_X);
		values.put(CreateDatabase.COLUMN_P2_Y, P2_Y);
		database.insert(CreateDatabase.TABLE_Map, null, values);
	}

	public List<StoreShelf> getAllStoreShelves() {
		List<StoreShelf> storeMap = new ArrayList<StoreShelf>();

		Cursor cursor = database.query(CreateDatabase.TABLE_Map, allMapColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			StoreShelf map = cursorToStoreMap(cursor);
			storeMap.add(map);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return storeMap;
	}
	
	private ItemsMap cursorToItemsMap(Cursor cursor) {
		ItemsMap ItemsMap = new ItemsMap();
		ItemsMap.setName(cursor.getString(0));
		ItemsMap.setName(cursor.getString(1));
		ItemsMap.setX(cursor.getInt(2));
		ItemsMap.setY(cursor.getInt(3));
		return ItemsMap;
	}

	private StoreShelf cursorToStoreMap(Cursor cursor) {
		
		StoreShelf storeMap = new StoreShelf();
		storeMap.setCatagory(cursor.getString(cursor
				.getColumnIndex(CreateDatabase.COLUMN_Catagory)));
		storeMap.setP1_X(cursor.getInt(cursor
				.getColumnIndex(CreateDatabase.COLUMN_P1_X)));
		storeMap.setP1_Y(cursor.getInt(cursor
				.getColumnIndex(CreateDatabase.COLUMN_P1_Y)));
		storeMap.setP2_X(cursor.getInt(cursor
				.getColumnIndex(CreateDatabase.COLUMN_P2_X)));
		storeMap.setP2_Y(cursor.getInt(cursor
				.getColumnIndex(CreateDatabase.COLUMN_P2_Y)));

		return storeMap;
	}
}