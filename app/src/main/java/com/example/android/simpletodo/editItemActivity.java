package com.example.android.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.example.android.simpletodo.MainActivity.ITEM_POSITION;
import static com.example.android.simpletodo.MainActivity.ITEM_TEXT;

public class editItemActivity extends AppCompatActivity {

    //track edit text
    EditText etItemText;
    //position of edited item in the list
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        //resolve the edit text from layout
        etItemText = findViewById(R.id.etItemText);
        //set edit text value to the value to be changed
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        //update the position from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        //update the title bar of the activity
        getSupportActionBar().setTitle("Edit item");
    }

    //handeler for save button
    public void onSaveItem(View v){
        //prepare new intent to pass the data back to new activity
        Intent i = new Intent();
        //pass update item text as extra
        i.putExtra(ITEM_TEXT, etItemText.getText().toString());
        //pass the position as extra
        i.putExtra(ITEM_POSITION, position);
        //set the intent as the result of this activity
        setResult(RESULT_OK, i);
        //finally close this activity and redirect to main
        finish();
    }

}
