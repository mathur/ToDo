package com.rohan.todo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.method.LinkMovementMethod;
import android.text.Html;

public class MainActivity extends Activity {
	protected ItemDbHelper db;
	List<Item> list;
	MyAdapter adapt;

    /*
     * Method that is run at the start of the program
     *
     * @param savedInstanceState is the state which the program was last run in. Keep that saved
     * and then try to resume the same activity with the same instance (of type Bundle)
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_item);
		db = new ItemDbHelper(this);
		list = db.getAllItems();
		adapt = new MyAdapter(this, R.layout.list_inner_view, list);
		ListView listItem = (ListView) findViewById(R.id.listView1);
		listItem.setAdapter(adapt);
	}

    /*
     * Method that is run at the pause of the app (when it is held in memory/exited by the
     * Android system
     */
    @Override
    protected void onPause()
    {
        int i = 0;
        while(i < list.size()) {
            if (list.get(i).getStatus() == 1) {
                db.removeItem(list.get(i));
                list.remove(i);
                adapt.notifyDataSetChanged();
            }
            else
                i++;
        }
        super.onPause();
    }

    /*
     * Method to add items to the database
     */
	public void addItemNow(View v) {
        // find the text edit field and assign it to a variable that can be used
		EditText t = (EditText) findViewById(R.id.editText1);
		String s = t.getText().toString();

        // if nothing is in the text box, then don't add anything and return a toast message to the user
        // if something is in the text box, then add it to the database
		if (s.equalsIgnoreCase("")) {
			Toast.makeText(this, "Enter the item description",
					Toast.LENGTH_SHORT).show();
		} else {
			Item item = new Item(s, 0);
			db.addItem(item);
			t.setText("");
			adapt.add(item);
			adapt.notifyDataSetChanged();
		}

	}

    /*
     * Method that creates the menu "menu" at onCreate
     *
     * @param menu is the menu that is created
     */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        // use activity_view_item.xml for the menu layout
		getMenuInflater().inflate(R.menu.activity_view_item, menu);
		return true;
	}

    /*
     * Method that is called when an option is selected in the menu
     *
     * @param item is the menu item that is selected by the user
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                // if about is pressed, then open the dialog box
                showDialog(1);
                return true;
            case R.id.help:
                // if help is pressed, then open the dialog box
                showDialog(2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Method that is called to create an Android dialog
     *
     * @param id is the id of the action that the programmer wants to happen
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                // Create our About Dialog
                TextView aboutMsg  = new TextView(this);
                aboutMsg.setMovementMethod(LinkMovementMethod.getInstance());
                aboutMsg.setPadding(30, 30, 30, 30);
                aboutMsg.setText(Html.fromHtml("<big>A simple ToDo Android Application</big><br><br><font color='black'><small>Developed by:<br>Rohan Mathur<br>Yunchao Wu<br>Kunal Hegde<br>Sachin Parikh<br><br>For AP Computer Science</small></font>"));

                Builder builder = new AlertDialog.Builder(this);
                builder.setView(aboutMsg)
                        .setTitle(Html.fromHtml("<b><font color='black'>About ToDo</font></b>"))
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });

                return builder.create();
            case 2:
                // Create our Help Dialog
                TextView helpMsg  = new TextView(this);
                helpMsg.setMovementMethod(LinkMovementMethod.getInstance());
                helpMsg.setPadding(30, 30, 30, 30);
                helpMsg.setText(Html.fromHtml("<font color='black'><small>The main app view displays all the ToDo items. To add a new item, simple type the item into the enter text field and then click the Add Item button.<br>To mark a item as done, check the box next to its name.<br><br>Notes:<br>1) Items that are checked upon app close will be removed<br>2) Items will by default sort by being done or not<br>3) Due date support is coming soon!</small></font>"));

                Builder builder2 = new AlertDialog.Builder(this);
                builder2.setView(helpMsg)
                        .setTitle(Html.fromHtml("<b><font color='black'>Help</font></b>"))
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });

                return builder2.create();
        }

        return super.onCreateDialog(id);
    }

	private class MyAdapter extends ArrayAdapter<Item> {

		Context context;
		List<Item> itemList = new ArrayList<Item>();
		int layoutResourceId;

        /*
         * Create a MyAdapter object
         *
         * @param context is the context that provides Android access to all the necessary stuff Android needs
         * @param layoutResourceId is the layout's Id number
         * @param objects is an ArrayList of the objects involved with the database in question
         */
		public MyAdapter(Context context, int layoutResourceId,
				List<Item> objects) {
			super(context, layoutResourceId, objects);
			this.layoutResourceId = layoutResourceId;
			this.itemList = objects;
			this.context = context;
		}

        /*
         * Method that is called to display the data at the specified position in the data set
         *
         * @param position is the position in the data set
         * @param convertView is the view that the user will be interacting with
         * @param parent is the view that is the parent of the current view
         *
         * @return convertView, the view the user will be interacting with
         */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CheckBox chk = null;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_inner_view,
						parent, false);
				chk = (CheckBox) convertView.findViewById(R.id.chkStatus);
				convertView.setTag(chk);

				chk.setOnClickListener(new View.OnClickListener() {

                    /*
                     * Method that is called when the item's checkbox is clicked
                     *
                     * @param v is the current view where the checkboxes are located
                     */
					@Override
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Item changeItem = (Item) cb.getTag();
						changeItem.setStatus(cb.isChecked() == true ? 1 : 0);
						db.updateItem(changeItem);
					}

				});
			} else {
				chk = (CheckBox) convertView.getTag();
			}
			Item current = itemList.get(position);
			chk.setText(current.getItemName());
			chk.setChecked(current.getStatus() == 1 ? true : false);
			chk.setTag(current);
			Log.d("listener", String.valueOf(current.getId()));
			return convertView;
		}

	}

}

