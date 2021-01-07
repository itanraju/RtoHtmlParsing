package com.example.newhtmlparsing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.File;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Map cookeis;
    String text,attr,sgj,snumber;
    Document doc;
    LazyLoader lazyLoader;
    TextView registered,registrationNumber,registrationDate,chassisNo,engineNumber,ownerInfo,vehicleClass,fuelType,markerModel,fitnessUpto,insuranceUpto,fuelNorms;
    Button getdata;
    EditText gj,number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registered=findViewById(R.id.registering);
        getdata=findViewById(R.id.getdata);
        gj=findViewById(R.id.gj);
        number=findViewById(R.id.number);
        registrationNumber=findViewById(R.id.registerednumber);
        registrationDate=findViewById(R.id.registrationDate);
        chassisNo=findViewById(R.id.chassisNumber);
        engineNumber=findViewById(R.id.engineNumber);
        ownerInfo=findViewById(R.id.ownerName);
        vehicleClass=findViewById(R.id.vehicleClass);
        fuelType=findViewById(R.id.fuelType);
        markerModel=findViewById(R.id.markerModel);
        fitnessUpto=findViewById(R.id.fitnessUpto);
        insuranceUpto=findViewById(R.id.insuranceUpto);
        fuelNorms=findViewById(R.id.fuelNorms);


        lazyLoader=findViewById(R.id.myloader);

        final LazyLoader loader = new LazyLoader(MainActivity.this, 30, 20, ContextCompat.getColor(MainActivity.this, R.color.loader_selected),
                ContextCompat.getColor(MainActivity.this, R.color.loader_selected),
                ContextCompat.getColor(MainActivity.this, R.color.loader_selected));
        loader.setInterpolator(new LinearInterpolator());
        lazyLoader.addView(loader);

        getdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                sgj=gj.getText().toString().trim();
                snumber=number.getText().toString().trim();

                if(sgj.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "please enter the number properly", Toast.LENGTH_SHORT).show();
                }
                if(snumber.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "please enter the number properly", Toast.LENGTH_SHORT).show();
                }
                if(!sgj.isEmpty() && !snumber.isEmpty())
                {
                    lazyLoader.setVisibility(View.VISIBLE);

                    Content content=new Content();
                    content.execute();

                }


            }
        });

    }

    private class Content extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {

            try {

            Connection.Response execute= Jsoup.connect("https://parivahan.gov.in/rcdlstatus/?pur_cd=102").execute();

            if(execute.statusCode()<=500)
            {
                cookeis=((Connection.Base)execute).cookies();
                org.jsoup.nodes.Document parse=Jsoup.parse(execute.body());

                org.jsoup.nodes.Element element;
                if ((element = parse.getElementsByAttributeValue("name", "javax.faces.ViewState").first()) == null) {
                    element = parse.getElementById("j_id1:javax.faces.ViewState:0");
                }
                 attr = element.attr("value");

                 text=Jsoup.connect("https://parivahan.gov.in/rcdlstatus/?pur_cd=102")
                        .data("javax.faces.partial.ajax","true")
                        .data("javax.faces.source","form_rcdl:j_idt42")
                        .data("javax.faces.partial.execute","@all")
                        .data("javax.faces.partial.render","form_rcdl:pnl_show form_rcdl:pg_show form_rcdl:rcdl_pnl")
                        .data("form_rcdl:j_idt42","form_rcdl:j_idt42")
                        .data("form_rcdl","form_rcdl")
                        .data("form_rcdl:tf_reg_no1",sgj)
                        .data("form_rcdl:tf_reg_no2",snumber)
                        .data("javax.faces.ViewState",attr)
                        .header("Content-Type", "application/x-www-form-urlencoded").header("Host", "parivahan.gov.in").header("Accept", "application/xml, text/xml, */*; q=0.01").header("Accept-Language", "en-US,en;q=0.5").header("Accept-Encoding", "gzip, deflate, br").header("X-Requested-With", "XMLHttpRequest").header("Faces-Request", "partial/ajax").header("Origin", "https://parivahan.gov.in")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                        .cookies(cookeis)
                        .timeout(5000)
                         .execute().body();

            }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Log.d("cookies",cookeis.toString());

            Log.d("attr",attr);

            System.out.println(text);

            String[] parts=text.split("Registering Authority:  ");
            String part2 = parts[1];
            parts = part2.split("</div>");
            part2 = parts[0];
            registered.setText(part2);

            String[] parts3=text.split("<td style=\"width: 45%\"><span class=\"\">");
            String part4 = parts3[1];
            parts3 = part4.split("</span></td>");
            part4 = parts3[0];
            registrationNumber.setText(part4);

            String[] parts5=text.split("<td style=\"width: 45%\"><span class=\"\">");
            String part6 = parts5[1];
            parts5 = part6.split("</span></td>");
            part6 = parts5[0];
            registrationDate.setText(part6);


            lazyLoader.setVisibility(View.INVISIBLE);
            super.onPostExecute(aVoid);
        }
    }
}