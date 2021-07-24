package com.example.spinnertrial;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.File;
import java.io.FileOutputStream;

import Dictionary.calc_interface;
import Dictionary.dictionary;



public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {





    String[] units = {"Bundles", "Kgs", "Rods"};
    String choice = "";
    Spinner spin;
    Button reset,ConfigButton;
    int total[] = new int[3];

    TextView h[] = new TextView[' '];
    TextView t[] = new TextView[' '];
    TextView c[] = new TextView[' '];
    TextView d[] = new TextView[' '];
    EditText b[] = new EditText[' '];


    int[] hid = {R.id.h1, R.id.h2, R.id.h3};
    int[] tid = {R.id.t2, R.id.t3, R.id.t4};
    int[] bid = {R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5};
    int[] cid = {R.id.c1, R.id.c2, R.id.c3, R.id.c4, R.id.c5};
    int[] did = {R.id.d1, R.id.d2, R.id.d3, R.id.d4, R.id.d5};



    dictionary dict = new dictionary();
    String keys[][] = dict.keys;
    dataBase db = new dataBase(MainActivity.this);



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Configuration:
                startActivity(new Intent(getApplicationContext(),Configuration.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        spin = (Spinner) findViewById(R.id.dropdown);


        if(db.doesTableExist("Default_Preset")<1)
            db.Default_Preset();



        dict = new dictionary(MainActivity.this);



        for(int j=0;j<5;j++)
        {
            if(j<3)
            {
                h[j] = (TextView) findViewById(hid[j]);
                t[j] = (TextView) findViewById(tid[j]);
            }

            b[j] = (EditText) findViewById(bid[j]);
            c[j] = (TextView) findViewById(cid[j]);
            d[j] = (TextView) findViewById(did[j]);
        }

        reset = (Button) findViewById(R.id.reset);

        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the steel quantities
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, units);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        TextWatcher textwatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(choice.equals("Kgs")){
                    kgs();

                }

                if(choice.equals("Bundles")){
                    bundles();

                }

                if(choice.equals("Rods")){
                    rods();

                }
                totalcalc();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        for(int j=0; j<5; j++)
        {
            b[j].addTextChangedListener(textwatcher);
        }

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Reset", Toast.LENGTH_LONG).show();

                for(int j=0; j<5; j++) {
                    b[j].setText("");
                }
                for(int j=0; j<3; j++) {
                    t[j].setText("");
                }
            }
        });

    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), units[position], Toast.LENGTH_LONG).show();
        choice = spin.getSelectedItem().toString();

        for(int j=0; j<5; j++) {
            b[j].setText("");
        }
        for(int j=0; j<3; j++) {
            t[j].setText("");
        }

        if (choice.equals("Kgs")) {
            h[0].setText(choice);
            h[1].setText("Bundles");
            h[2].setText("Rods");
        }
        if (choice.equals("Rods")) {
            h[0].setText(choice);
            h[1].setText("Bundles");
            h[2].setText("Kgs");

        }
        if (choice.equals("Bundles")) {
            h[0].setText(choice);
            h[1].setText("Kgs");
            h[2].setText("Rods");


        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    public void export(View view){
        //generate data
        StringBuilder data = new StringBuilder();
        data.append("Dia,"+h[0].getText().toString()+","+h[1].getText().toString()+","+h[2].getText().toString());
        data.append("\n8mm,"+b[0].getText().toString()+","+c[0].getText().toString()+","+d[0].getText().toString());
        data.append("\n10mm,"+b[1].getText().toString()+","+c[1].getText().toString()+","+d[1].getText().toString());
        data.append("\n12mm,"+b[2].getText().toString()+","+c[2].getText().toString()+","+d[2].getText().toString());
        data.append("\n16mm,"+b[3].getText().toString()+","+c[3].getText().toString()+","+d[3].getText().toString());
        data.append("\n20mm,"+b[4].getText().toString()+","+c[4].getText().toString()+","+d[4].getText().toString());
        data.append("\nTotal,"+t[0].getText().toString()+","+t[1].getText().toString()+","+t[2].getText().toString());

        try{
            //saving the file into device
            FileOutputStream out = openFileOutput("steel.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "steel.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.spinnertrial.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Steel Result");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Share Result"));
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }

    void bundles() {

        int n[] = new int[2];
        dictionary dict = new dictionary();

        dict.calculate();

        for(int j=0;j<5;j++)
        {
            if (!b[j].getText().toString().equals("")) {
                calc_interface[] x= (calc_interface[]) dict.function_values.get(keys[0][j]);
                for(int k=0;k<2;k++) {
                    n[k] = x[k].calc(Double.parseDouble(b[j].getText().toString()));
                }

                c[j].setText(String.valueOf(n[0]));
                d[j].setText(String.valueOf(n[1]));
            } else {
                c[j].setText("");
                d[j].setText("");
            }
        }

    }

    void kgs() {

        int n[] = new int[2];
        dictionary dict = new dictionary();

        dict.calculate();

        for(int j=0;j<5;j++) {
            if (!b[j].getText().toString().equals("")) {
                calc_interface[] x= (calc_interface[]) dict.function_values.get(keys[1][j]);
                for(int k=0;k<2;k++) {
                    n[k] = x[k].calc(Double.parseDouble(b[j].getText().toString()));
                }
                c[j].setText(String.valueOf(n[0]));
                d[j].setText(String.valueOf(n[1]));
            } else {
                c[j].setText("");
                d[j].setText("");
            }
        }


    }

    void rods() {

        int n[] = new int[2];
        dictionary dict = new dictionary();

        dict.calculate();

        for(int j=0;j<5;j++) {
            if (!b[j].getText().toString().equals("")) {
                calc_interface[] x= (calc_interface[]) dict.function_values.get(keys[2][j]);
                for(int k=0;k<2;k++) {
                    n[k] = x[k].calc(Double.parseDouble(b[j].getText().toString()));
                }
                c[j].setText(String.valueOf(n[0]));
                d[j].setText(String.valueOf(n[1]));
            } else {
                c[j].setText("");
                d[j].setText("");
            }
        }

    }

    void totalcalc(){

        int bt[] = new int[5];
        int ct[] = new int[5];
        int dt[] = new int[5];

        for (int j = 0; j < 3; j++) {
            total[j] = 0;
        }

        for(int j=0;j<5;j++) {

            if (!b[j].getText().toString().equals("")) {
                bt[j] = Integer.parseInt(b[j].getText().toString());
                ct[j] = Integer.parseInt(c[j].getText().toString());
                dt[j] = Integer.parseInt(d[j].getText().toString());
                total[0] = total[0] + bt[j];
                total[1] = total[1] + ct[j];
                total[2] = total[2] + dt[j];
            }

        }

        for(int j=0; j<3; j++)
        {
            t[j].setText(String.valueOf(total[j]));
        }

    }
}

