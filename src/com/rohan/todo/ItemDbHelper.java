package com.rohan.todo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDbHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// database name
	private static final String DATABASE_NAME = "todoItems";

	// items table name
	private static final String TABLE_ITEMS = "items";

	// items Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_ITEMNAME = "itemName";
	private static final String KEY_STATUS = "status";

    /*
     * Constructor that sets up the ItemDbHelper object
     *
     * @param context provides Android access to the general information about the app, in this case
     * it is provided to the database
     */
	public ItemDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

    /*
     * Method that is run when the SQLite Database connection is first initiated
     *
     * @param db is the database to be created (if it doesn't already exist) or connected to
     */
	@Override
	public void onCreate(SQLiteDatabase db) {
        // created the sql query of type string, only create the table if it doesn't already exist
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + " ( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ITEMNAME
				+ " TEXT, " + KEY_STATUS + " INTEGER)";
		db.execSQL(sql);
	}

    /*
     * Method that is called when the data in the table needs to be updated
     *
     * @param db is the database that needs to be updated
     * @param oldV is the old version number of the database
     * @param newV is the new version number of the database
     */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		// Create tables again
		onCreate(db);
	}

	/*
	 * Method that is called when the user tries to add a new item to the database
	 *
	 * @param item is the item being added
	 */
	public void addItem(Item item) {
        // open the database connection
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ITEMNAME, item.getItemName()); // item name
		// status of item- can be 0 for not done and 1 for done
		values.put(KEY_STATUS, item.getStatus());

		// Inserting Row
		db.insert(TABLE_ITEMS, null, values);
		db.close(); // Closing database connection
	}

    /*
     * Method that is called when the user tries to remove an already existing item in the database
     *
     * @param item is the item being removed
     */
    public void removeItem(Item item) {
        // open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();

        String itemId= "" + item.getId();
        db.delete(TABLE_ITEMS, KEY_ID + "=" + itemId, null);

        db.close();
    }

    /*
     * Method that gets all the items from the database and returns it in an ArrayList
     *
     * @return an ArrayList with objects of type item ordered by status
     */
	public List<Item> getAllItems() {
		List<Item> itemList = new ArrayList<Item>();
		// Select all query with ordering
		String selectQuery = "SELECT  * FROM " + TABLE_ITEMS + " ORDER BY " + KEY_STATUS + " ASC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Item item = new Item();
				item.setId(cursor.getInt(0));
				item.setItemName(cursor.getString(1));
				item.setStatus(cursor.getInt(2));
				// adding item to list
				itemList.add(item);
			} while (cursor.moveToNext());
		}

		// return the now filled item list
		return itemList;
	}

    /*
     * Method to update an item's details
     *
     * @param item is the item being updated
     */
	public void updateItem(Item item) {
		// updating row
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ITEMNAME, item.getItemName());
		values.put(KEY_STATUS, item.getStatus());
		db.update(TABLE_ITEMS, values, KEY_ID + " = ?",new String[] {String.valueOf(item.getId())});
		db.close();
	}
}