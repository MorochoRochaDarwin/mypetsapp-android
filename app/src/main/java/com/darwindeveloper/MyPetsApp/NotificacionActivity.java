package com.darwindeveloper.MyPetsApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.darwindeveloper.MyPetsApp.sqlite.DBManager;


public class NotificacionActivity extends AppCompatActivity {


    private WebView myWebView;
    private DBManager dbManager;
    private String establecimiento_id, establecimiento, titulo, fecha, html;
    private boolean guardado;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion);

        dbManager = new DBManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            titulo = getIntent().getStringExtra("titulo");
            establecimiento_id = getIntent().getStringExtra("establecimiento_id");
            establecimiento = getIntent().getStringExtra("nombre_establecimiento");
            fecha = getIntent().getStringExtra("publicado");
            html = getIntent().getStringExtra("html");
            guardado = getIntent().getBooleanExtra("guardado", false);


            if (establecimiento_id == null) {
                establecimiento_id = "-1";
                establecimiento = "MyPetsApp";
            }

            TextView textViewTitulo = (TextView) findViewById(R.id.titulo);
            TextView textViewRemitente = (TextView) findViewById(R.id.remitente);
            TextView textViewRId = (TextView) findViewById(R.id.remitenteId);
            TextView textViewFecha = (TextView) findViewById(R.id.fecha);
            myWebView = (WebView) findViewById(R.id.myWebView);

            textViewTitulo.setText(titulo);
            textViewRemitente.setText("Remitente: " + establecimiento);
            textViewRId.setText("Remitente ID: " + establecimiento_id);
            textViewFecha.setText("Publicado el: " + fecha);

            String mime = "text/html";
            String encoding = "utf-8";

            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.loadDataWithBaseURL(null, html, mime, encoding, null);
            //createWebPrintJob(myWebView);
        }
    }


    private void createWebPrintJob(WebView webView) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PrintManager printManager = (PrintManager) this
                    .getSystemService(Context.PRINT_SERVICE);

            PrintDocumentAdapter printAdapter =
                    null;
            printAdapter = webView.createPrintDocumentAdapter("MyDocument");
            String jobName = getString(R.string.app_name) + " Print Test";

            assert printManager != null;
            printManager.print(jobName, printAdapter,
                    new PrintAttributes.Builder().build());
        } else {
            Toast.makeText(this, "Android 5.0 o superior es requerido ", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notificaciones, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_pdf:
                createWebPrintJob(myWebView);
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}
