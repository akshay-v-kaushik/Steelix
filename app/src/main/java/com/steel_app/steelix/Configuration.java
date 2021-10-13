package com.example.steelix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Dictionary.dictionary;

public class Configuration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public List<Integer> databundle;
    public List<Integer> datakg;
    public List<Integer> datarod;
    dataBase database = new dataBase(Configuration.this) ;




    Button add,del,clear;
    EditText company;
    EditText c[] = new EditText[' '];
    TextView d[] = new TextView[' '];
    Spinner spin;
    int[] cid = {R.id.c1, R.id.c2, R.id.c3, R.id.c4, R.id.c5, R.id.c6,R.id.c7};
    int[] did = {R.id.d1, R.id.d2, R.id.d3, R.id.d4, R.id.d5, R.id.d6,R.id.d7};
    dictionary dict;
    ArrayList<String> dropItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        database = new dataBase(Configuration.this,"Default_Preset");
        databundle = database.getValues("BUNDLES");
        datakg = database.getValues("KGs");
        datarod = database.getValues("RODS");
        dict = new dictionary(databundle,datakg,datarod);

        spin = (Spinner) findViewById(R.id.dropdown);
        spin.setOnItemSelectedListener(this);

        SQLiteDatabase db = database.getReadableDatabase();
        dropItems = database.getCompanyNames(db);

        Collections.reverse(dropItems);

        //Creating the ArrayAdapter instance having the steel quantities
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dropItems);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        for(int j=0;j<7;j++)
        {
            c[j] = findViewById(cid[j]);
            d[j] = findViewById(did[j]);
        }

        add = findViewById(R.id.add);
        del = findViewById(R.id.delete);
        clear = findViewById(R.id.clear);

        company = findViewById(R.id.company);
        company.setText(dropItems.get(dropItems.size()-1));




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!company.getText().toString().equals("") && !company.getText().toString().equals("Default_Preset"))
                {
                    boolean noNull=true;

                    for(int j=0; j<7; j++) {
                        if(c[j].getText().toString().equals("") || c[j].getText().toString().equals("0"))
                        {
                            noNull=false;
                        }
                    }

                    if(noNull)
                    {
                        database.delete(company.getText().toString());
                        database = new dataBase(Configuration.this,company.getText().toString());
                        SQLiteDatabase db = database.getWritableDatabase();
                        database.onCreate(db);

                        try {

                            database.addItem(c, d);
                            Toast.makeText(Configuration.this, "Added Successfully", Toast.LENGTH_LONG).show();

                            }
                        catch (Exception e) {
                            Toast.makeText(Configuration.this, "Enter a Valid Name", Toast.LENGTH_LONG).show();
                            }

                        finish();
                        startActivity(getIntent());
                    }

                    else
                    {
                        Toast.makeText(Configuration.this, "Enter Valid Values", Toast.LENGTH_LONG).show();
                    }

                }
                else if(company.getText().toString().equals("Default_Preset"))
                {
                    Toast.makeText(Configuration.this,"Default_Preset Cannot be Overwritten",Toast.LENGTH_LONG).show();
                }

                else if(company.getText().toString().equals(""))
                {
                    Toast.makeText(Configuration.this, "Enter Name", Toast.LENGTH_LONG).show();
                }

                }


        });


        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean noNull=false;

                for(int j=0; j<dropItems.size(); j++) {
                    if(dropItems.get(j).equals(company.getText().toString()))
                    {
                        noNull=true;
                    }
                }

                if(!company.getText().toString().equals("") && !company.getText().toString().equals("Default_Preset") && noNull){

                    database = new dataBase(Configuration.this,company.getText().toString());

                    database.delete(company.getText().toString());

                    Toast.makeText(Configuration.this,"Deleted Successfully",Toast.LENGTH_LONG).show();

                    finish();
                    startActivity(getIntent());

                }

                else if(company.getText().toString().equals(""))
                {
                    Toast.makeText(Configuration.this,"Enter Name",Toast.LENGTH_LONG).show();
                }
                else if(company.getText().toString().equals("Default_Preset"))
                {
                    Toast.makeText(Configuration.this,"Default_Preset Cannot be Deleted",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(Configuration.this,"Name is Not Added",Toast.LENGTH_LONG).show();
                }



            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Cleared", Toast.LENGTH_LONG).show();

                for(int j=0; j<7; j++) {
                    c[j].setText("");
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.tickmark,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ConfigConfirm:
                Intent i = new Intent(Configuration.this, MainActivity.class );

                MainActivity.getInstance().updatecomp();
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        String choice = arg0.getItemAtPosition(position).toString();
        company.setText(choice);

        database = new dataBase(Configuration.this,company.getText().toString());
        databundle = database.getValues("BUNDLES");
        datakg = database.getValues("KGs");
        datarod = database.getValues("RODS");

        for(int j=0;j<7;j++)
        {
            c[j].setText(datakg.get(j).toString());
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}