package com.example.newhtmlparsing;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;

import org.apache.commons.lang3.StringUtils;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

        /*gj.setFilters(new InputFilter[] {new InputFilter.AllCaps()});*/

        lazyLoader=findViewById(R.id.myloader);

        final LazyLoader loader = new LazyLoader(MainActivity.this, 30, 20, ContextCompat.getColor(MainActivity.this, R.color.loader_selected),
                ContextCompat.getColor(MainActivity.this, R.color.loader_selected),
                ContextCompat.getColor(MainActivity.this, R.color.loader_selected));
        loader.setInterpolator(new LinearInterpolator());
        loader.setTouchscreenBlocksFocus(false);
        lazyLoader.addView(loader);


        /*gj.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });*/

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

                    (
                            new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            Content content=new Content();
                            content.execute();

                        }
                    },2000);





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
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                        .cookies(cookeis)
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

            Log.d("text",text);

            Log.d("attr",attr);

            if(text.contains("Registration No. does not exist!!! Please check the number."))
            {
                Toast.makeText(MainActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
            }
            else {

                String[] parts = text.split("Registering Authority:  ");
                String part2 = parts[1];
                parts = part2.split("</div>");
                part2 = parts[0];
                registered.setText(StringUtils.substring(part2,0,part2.length()-25));


                String[] parts3 = text.split("<td style=\"width: 45%\"><span class=\"\">");
                String part4 = parts3[1];
                parts3 = part4.split("</span></td>");
                part4 = parts3[0];
                registrationNumber.setText(part4);

                String[] parts5 = text.split("<td style=\"width: 15%\"><span class=\"font-bold\">Registration Date:</span></td>");
                String part6 = parts5[1];
                parts5 = part6.split("</td>");
                part6 = parts5[0];
                registrationDate.setText(part6.substring(37));

                String[] parts7 = text.split("<td><span class=\"font-bold\">Chassis No:</span></td>");
                String part8 = parts7[1];
                parts7 = part8.split("</td>");
                part8 = parts7[0];
                chassisNo.setText(part8.substring(37));

                String[] parts9 = text.split("<td><span class=\"font-bold\">Engine No:</span></td>");
                String part10 = parts9[1];
                parts9 = part10.split("</td>");
                part10 = parts9[0];
                engineNumber.setText(part10.substring(37));

                String[] parts11 = text.split("<td><span class=\"font-bold\">Owner Name:</span> </td>");
                String part12 = parts11[1];
                parts11 = part12.split("</td>");
                part12 = parts11[0];
                ownerInfo.setText(part12.substring(49));

                String[] parts13 = text.split("<td><span class=\"font-bold\">Vehicle Class:</span> </td>");
                String part14 = parts13[1];
                parts13 = part14.split("</td>");
                part14 = parts13[0];
                vehicleClass.setText(part14.substring(37));

                String[] parts15 = text.split("<td><span class=\"font-bold\">Fuel Type:</span></td>");
                String part16 = parts15[1];
                parts15 = part16.split("</td>");
                part16 = parts15[0];
                fuelType.setText(part16.substring(37));

                String[] parts17 = text.split("<td><span class=\"font-bold\">Maker / Model:</span></td>");
                String part18 = parts17[1];
                parts17 = part18.split("</td>");
                part18 = parts17[0];
                markerModel.setText(part18.substring(49));

                String[] parts19 = text.split("<td><span class=\"font-bold\">Fitness Upto:</span></td>");
                String part20 = parts19[1];
                parts19 = part20.split("</td>");
                part20 = parts19[0];
                fitnessUpto.setText(part20.substring(37));

                String[] parts21 = text.split("<td><span class=\"font-bold\">Insurance Upto:</span></td>");
                String part22 = parts21[1];
                parts21 = part22.split("</td>");
                part22 = parts21[0];
                insuranceUpto.setText(part22.substring(37));

                String[] parts23 = text.split("<td><span class=\"font-bold\">Fuel Norms:</span> </td>");
                String part24 = parts23[1];
                parts23 = part24.split("</td>");
                part24 = parts23[0];
                fuelNorms.setText(part24.substring(37));

            }
            lazyLoader.setVisibility(View.INVISIBLE);
            super.onPostExecute(aVoid);
        }
    }
}