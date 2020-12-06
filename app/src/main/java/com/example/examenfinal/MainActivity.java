package com.example.examenfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MyActivity";

    EditText txtCriterio;
    Button btnTodo,btnFecha,idSiguiente;

    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hooks();


    }

    private void hooks(){

        txtCriterio = findViewById(R.id.txtCriterio);
        btnTodo = findViewById(R.id.btnTodo);
        btnFecha = findViewById(R.id.btnFecha);
        idSiguiente = findViewById(R.id.idSiguiente);
        lista = findViewById(R.id.lista);

        btnTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"btnTodo OnClickListener");
                buscarTodo();
            }
        });

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarPorFecha();
            }
        });

        idSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redireccionarSiguiente();
            }
        });
    }

    private void buscarTodo() {
        String url = "http://aplicacionesmoviles.atwebpages.com/index.php/avisos";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> itemsList = new ArrayList<>();

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        itemsList.add(jsonObject.getString("titulo") +
                                " y la fecha de fin es " + jsonObject.getString("fecha_fin"));
                        Log.i(TAG,"buscarTodo jsonObject: " + jsonObject.toString());
                    }

                    Log.i(TAG,"buscarTodo itemsList: " + itemsList);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_list_item_1,itemsList);

                    lista.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    //http://aplicacionesmoviles.atwebpages.com/index.php/avisos/2020-04-18
    private void buscarPorFecha() {

        String fecha = txtCriterio.getText().toString();

        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");

        //COMPLETED Valida que el formato y la fecha sean apropiadas
        try {
            format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Fecha invalida : ejem. YYYY-MM-DD", Toast.LENGTH_SHORT).show();
            return;
        }


        String url = "http://aplicacionesmoviles.atwebpages.com/index.php/avisos/"+fecha;

        Log.i(TAG,"buscarPorFecha url: " + url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> itemsList = new ArrayList<>();

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        itemsList.add(jsonObject.getString("titulo") +
                                " y la fecha de fin es " + jsonObject.getString("fecha_fin"));
                        Log.i(TAG,"buscarTodo jsonObject: " + jsonObject.toString());
                    }

                    Log.i(TAG,"buscarTodo itemsList: " + itemsList);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_list_item_1,itemsList);

                    lista.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void redireccionarSiguiente() {
        Intent intent =  new Intent(this,Main2Activity.class);
        startActivity(intent);
    }


}
