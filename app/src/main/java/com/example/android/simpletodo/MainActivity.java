package com.example.android.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvView;

    //numeric id to identify the edit item activity
    public final static int EDIT_REQUEST_CODE = 20;
    //keys uesd for passing data between activities
    public final static String ITEM_TEXT = "item text";
    public final static String ITEM_POSITION = "itemPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvView = findViewById(R.id.lvItems);
        lvView.setAdapter(itemsAdapter);

//        mock data to look at
//        items.add("Financial aid");
//        items.add("Bursar");
        setupListViewListener();
    }

    //when the button is clicked to add new item
    public void onAddItem(View v){
        EditText etNewItem = findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (itemText.isEmpty()){
            Toast.makeText(getApplicationContext(), "No data was entered reEnter", Toast.LENGTH_SHORT).show();
        }
        else {
            itemsAdapter.add(itemText);
            etNewItem.setText("");
            writeItems();
            Toast.makeText(getApplicationContext(), "Item's added to list", Toast.LENGTH_SHORT).show();
        }
    }

    // to remove items (long click)
    private void setupListViewListener(){
        Log.i("MainActivity", "Setting up listener on list view");
        lvView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Item removed from the list "+ position);
                Toast.makeText(getApplicationContext(), "Removed " + items.get(position), Toast.LENGTH_SHORT).show();
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        //set up item listener for edit (regular click)
        lvView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //create new activity
                Intent i = new Intent(MainActivity.this, editItemActivity.class);
                //pass the data being edited
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                //display the activity
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    // handle result from edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check if the edited activity's result is OK and if it is the same activity that we need
        if(resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            //extract the updated item data from the intent
            String updated = data.getExtras().getString(ITEM_TEXT);
            //extract original position from edit text
            int position = data.getExtras().getInt(ITEM_POSITION);
            //update the model arraylist at the edited position
            items.set(position, updated);
            //notify the adapter that the model changed
            itemsAdapter.notifyDataSetChanged();
            //persist the change
            writeItems();
            //toast to confirm the change
            Toast.makeText(this,"Item updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    //method for persistence and apachi common.io
    private File getDataFile(){
        Log.e("Mainactivity" , " location of the file is += " + getFilesDir());
        return new File(getFilesDir(), "todo.txt");
    }

    /**
     * read data from files and asigne data to "items"
     * <p> if something is wrong then assign "items" to empty</p>
     */
    private void readItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MainActivity", "error reading from file", e);
            items = new ArrayList<>();
            Toast.makeText(getApplicationContext(),"Error loading data", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * write data to file from "items"
     */
    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("MainActivity", "error writing into file", e);
        }
    }

}
