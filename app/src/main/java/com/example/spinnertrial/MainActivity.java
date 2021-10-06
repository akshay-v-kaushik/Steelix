package com.example.spinnertrial;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import Dictionary.calc_interface;
import Dictionary.dictionary;



public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "PdfCreatorActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    Context context;

    private static MainActivity instance;

    String choice = "";
    String pdf_name = "";
    String  currentDateTimeString;
    String[] units = {"Bundles", "Kgs", "Rods"};

    ArrayList<String> company_lists;


    Spinner spin, comp_tables;

    Button reset,share;


    TextView totalheading;
    TextView a[] = new TextView[' '];
    TextView h[] = new TextView[' '];
    TextView t[] = new TextView[' '];
    TextView c[] = new TextView[' '];
    TextView d[] = new TextView[' '];

    
    EditText pdfname;
    EditText b[] = new EditText[' '];


    int total[] = new int[3];
    int[] hid = {R.id.h1, R.id.h2, R.id.h3};
    int[] tid = {R.id.t2, R.id.t3, R.id.t4};
    int[] aid = {R.id.a1, R.id.a2, R.id.a3, R.id.a4, R.id.a5};
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


        instance = this;
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        spin = (Spinner) findViewById(R.id.dropdown);
        comp_tables = (Spinner) findViewById(R.id.comp_tables);


        if(db.doesTableExist("Default_Preset")<1)
            db.Default_Preset();


        dict = new dictionary(MainActivity.this);


        totalheading = findViewById(R.id.t1);
        pdfname = findViewById(R.id.pdfname1);

        for(int j=0;j<5;j++)
        {
            if(j<3)
            {
                h[j] = (TextView) findViewById(hid[j]);
                t[j] = (TextView) findViewById(tid[j]);
            }

            a[j] = (TextView) findViewById(aid[j]);
            b[j] = (EditText) findViewById(bid[j]);
            c[j] = (TextView) findViewById(cid[j]);
            d[j] = (TextView) findViewById(did[j]);
        }

        reset = (Button) findViewById(R.id.reset);
        share = findViewById(R.id.share);


        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, units);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        dataBase database = new dataBase(MainActivity.this,"Default_Preset");
        SQLiteDatabase db = database.getReadableDatabase();
        company_lists = database.getCompanyNames(db);

        Collections.reverse(company_lists);

        comp_tables.setOnItemSelectedListener(this);
        ArrayAdapter bb = new ArrayAdapter(this, android.R.layout.simple_spinner_item, company_lists);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comp_tables.setAdapter(bb);


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onButtonShowPopupWindowClick(v);


            }
        });



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


    private void createPdfWrapper(String desc) throws FileNotFoundException, DocumentException {
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf(desc);

        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private void createPdf(String desc) throws FileNotFoundException, DocumentException {
        File pdfFile = new File(getExternalFilesDir(null).getAbsolutePath(),pdf_name);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Diameter");
        table.addCell(h[0].getText().toString());
        table.addCell(h[1].getText().toString());
        table.addCell(h[2].getText().toString());
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }
        for(int j = 0;j<bid.length;j++)
        {
            table.addCell(a[j].getText().toString());
            table.addCell(b[j].getText().toString());
            table.addCell(c[j].getText().toString());
            table.addCell(d[j].getText().toString());
        }
        table.addCell(totalheading.getText().toString());
        for(int j = 0;j<units.length;j++)
        {
            table.addCell(t[j].getText().toString());
        }

        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        PdfWriter.getInstance(document, output);
        document.open();
        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f,Font.BOLD , BaseColor.BLACK);
        Font g = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL , BaseColor.BLACK);
        document.add(new Paragraph("Steel Rods \n", f));

        document.add(new Paragraph(new Paragraph(currentDateTimeString + "\n\n", g)));
        document.add(new Paragraph("File Name: "+pdf_name+ "\n",g));
        document.add(new Paragraph( desc + "\n\n", g));
        document.add(table);


        document.close();
        sharePdf();

    }
    private void sharePdf() {


        File pdfFile = new File(getExternalFilesDir(null).getAbsolutePath(),pdf_name);
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider",
                                                                                                                    pdfFile);
        if(pdfFile.exists()){

            Intent share=new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            share.setType("application/pdf");
            startActivity(share);


        }

    }


    public static MainActivity getInstance()
    {
        return instance;
    }

    void updatecomp()
    {
        dataBase database = new dataBase(MainActivity.this,"Default_Preset");
        SQLiteDatabase db = database.getReadableDatabase();
        company_lists = database.getCompanyNames(db);
        Collections.reverse(company_lists);
        comp_tables.setOnItemSelectedListener(this);
        ArrayAdapter bc = new ArrayAdapter(this, android.R.layout.simple_spinner_item, company_lists);
        bc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comp_tables.setAdapter(bc);


    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if(arg0.getId()==R.id.dropdown) {
            Toast.makeText(getApplicationContext(), units[position], Toast.LENGTH_LONG).show();
            choice = spin.getSelectedItem().toString();

            for (int j = 0; j < 5; j++) {
                b[j].setText("");
            }
            for (int j = 0; j < 3; j++) {
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
        else if(arg0.getId()==R.id.comp_tables)
        {
            for (int j = 0; j < 5; j++) {
                b[j].setText("");
            }
            for (int j = 0; j < 3; j++) {
                t[j].setText("");
            }

            try{
                dataBase database = new dataBase(MainActivity.this,comp_tables.getSelectedItem().toString());
                List<Integer> databundle = database.getValues("BUNDLES");
                List<Integer> datakg = database.getValues("KGs");
                List<Integer> datarod = database.getValues("RODS");
                dict = new dictionary(databundle,datakg,datarod);

                dict.calculate();
            }
            catch(Exception e)
            {
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
            }

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



    public void onButtonShowPopupWindowClick(View view) {

        final Dialog dialog = new Dialog(MainActivity.this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.setTitle("PDF");
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.popup_window);

        //Initializing the views of the dialog.
        final EditText pdfname = dialog.findViewById(R.id.pdfname1);
        final EditText pdfdesc = dialog.findViewById(R.id.pdfdesc);
        dialog.show();

        Button okButton = dialog.findViewById(R.id.ok);
        Button cancelButton = dialog.findViewById(R.id.cancel);



        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfname.getText().toString().equals(""))
                {
                    pdf_name = "SteelRods.pdf";
                }
                else
                {
                    pdf_name = pdfname.getText().toString()+".pdf";
                }
                String pdesc = pdfdesc.getText().toString();
                try {
                    createPdfWrapper(pdesc);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



    }
}

