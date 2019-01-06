package com.example.seekm.currconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    double amountt;
    TextView textView;
    String currencyA,currencyB;
    ImageView reverse,imageView;
    double calcA,calcB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<String> myList = new ArrayList<String>();
        final EditText amount;
        Button btn;
        final String[] array = getResources().getStringArray(R.array.symbols);

        amount = (EditText) findViewById(R.id.amount);
        textView = (TextView) findViewById(R.id.textView4);
        reverse = (ImageView)findViewById(R.id.imageView2);
        imageView = (ImageView)findViewById(R.id.imageView3);

        ArrayAdapter theAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_dropdown_item_1line , array);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(theAdapter);
        theAdapter.notifyDataSetChanged();

        ArrayAdapter theAdapter2 = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_dropdown_item_1line , array);
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(theAdapter2);
        theAdapter2.notifyDataSetChanged();

        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount.setText("");
                textView.setText("");
                int pos1 = spinner.getSelectedItemPosition();
                int pos2 = spinner2.getSelectedItemPosition();
                spinner.setSelection(pos2);
                spinner2.setSelection(pos1);

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currencyA=spinner.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currencyB = spinner2.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform action on key press
                    Toast.makeText(MainActivity.this, amount.getText(), Toast.LENGTH_SHORT).show();
                    if (amount.getText()!=null) {
                        amountt = Double.parseDouble(amount.getText().toString());
                        String url = "http://data.fixer.io/api/latest?access_key=16ef316143eba6121dd8168bc58fb94a";
                        Ion.with(MainActivity.this)
                                .load(url)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject data) {
                                        // get required info. From JSON object
                                        JsonObject jsonObject = (JsonObject) data.get("rates");
                                        double a = jsonObject.get(currencyB).getAsDouble();
                                        double b = jsonObject.get(currencyA).getAsDouble();

                                        calcA = amountt/b;
                                        calcB = calcA*a;
                                        String res = String.valueOf(calcB);
                                        textView.setText(res);

                                        TextView tv = (TextView)findViewById(R.id.textView5);
                                        tv.setText(currencyB+" " + a + " = " + currencyA + " " +b );
                                    }
                                });
                    }
                    else {
                        amount.setError("Enter some amount");
                    }
                }
        });

    }

}
