package com.example.android.simpletodo;

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

    // to remove items
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
