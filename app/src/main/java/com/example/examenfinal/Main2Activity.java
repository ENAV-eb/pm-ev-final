package com.example.examenfinal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private final static String TAG = "MyActivity";

    EditText txtTitulo,txtFechaInicio,txtFechaFin,txtEstado;

    Button btnRegistrar,btnAnterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        hooks();

    }

    public void anterior(View view){
        Intent anterior = new Intent(this,MainActivity.class);
        startActivity(anterior);
    }

    private void hooks() {

        txtTitulo = findViewById(R.id.txtTitulo);
        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaFin = findViewById(R.id.txtFechaFin);
        txtEstado = findViewById(R.id.txtEstado);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnAnterior = findViewById(R.id.btnAnterior);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.i(TAG,"btnRegistrar OnClickListener");
                registrarAviso();
            }
        });

        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"btnTodo OnClickListener");
                redireccionarAnterior();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void registrarAviso() {

        final String titulo = txtTitulo.getText().toString();
        final String fecha_inicio = txtFechaInicio.getText().toString();
        final String fecha_fin = txtFechaFin.getText().toString();
        final String estado = txtEstado.getText().toString();

        if(validarEntrada()) {

            String url = "http://aplicacionesmoviles.atwebpages.com/index.php/avisos";

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(Main2Activity.this, "Se insertó correctamente", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Main2Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("titulo",titulo);
                    parametros.put("fecha_inicio",fecha_inicio);
                    parametros.put("fecha_fin",fecha_fin);
                    parametros.put("estado",estado);
                    parametros.put("id_usuario","u20151a372");
                    return parametros;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validarEntrada() {

        boolean key = true;
        String titulo = txtTitulo.getText().toString();
        String fecha_inicio = txtFechaInicio.getText().toString();
        String fecha_fin = txtFechaFin.getText().toString();
        String estado = txtEstado.getText().toString();

        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");

        String stringToday = format.format(new Date());
        Date fechaInicio,fechaFin;
        LocalDate today = LocalDate.now();


        if(titulo.trim().isEmpty()) {
            Toast.makeText(Main2Activity.this, "Ingrese título", Toast.LENGTH_SHORT).show();
            key = false;
            return key;
        }

        try {
            fechaInicio = format.parse(fecha_inicio);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(Main2Activity.this, "Fecha Inicio invalida : ejem. YYYY-MM-DD", Toast.LENGTH_SHORT).show();
            key = false;
            return key;
        }


        LocalDate inicio = fechaInicio.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        Log.i(TAG,"LocalDate today " + today);
        Log.i(TAG,"LocalDate inicio " + inicio);
        Log.i(TAG,"LocalDate today " + today.isAfter(inicio));
        if(inicio.isAfter(today)) {
            Toast.makeText(Main2Activity.this, "Fecha inicio no puede ser menor a la fecha actual.", Toast.LENGTH_SHORT).show();
            key = false;
            return key;
        }

        try {
            fechaFin = format.parse(fecha_fin);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(Main2Activity.this, "Fecha Fin invalida : ejem. YYYY-MM-DD", Toast.LENGTH_SHORT).show();
            key = false;
            return key;
        }

        if(fechaInicio.after(fechaFin)) {
            Toast.makeText(Main2Activity.this, "Fecha inicio no puede ser mayor que la fecha de fin.", Toast.LENGTH_SHORT).show();
            key = false;
            return key;
        }


        if(estado.trim().isEmpty()) {
            Toast.makeText(Main2Activity.this, "Ingrese estado", Toast.LENGTH_SHORT).show();
            key = false;
            return key;
        }
        return key;
    }

    private void redireccionarAnterior() {
        Intent intent =  new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
