package com.example.ferna.appcivico.VIEW;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.ferna.appcivico.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Spinner spinnerCategorias;
    private Button btnPorEndereco;
    private Button btnPorLocal;
    private EditText edtEndereco;
    private GoogleApiClient locationApiClient;
    private Double lat, lon;
    private String itemSpinnerCategoria;
    private MaterialDialog mDialog;
    private static final int REQUEST_PERMISSIONS_CODE = 128;
    private SeekBar seekBarRadio;
    private TextView txtSeekBarStatus;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.ferna.appcivico.R.layout.activity_main);

        itemSpinnerCategoria = "Selecione a categoria";

        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        btnPorLocal = (Button) findViewById(R.id.btnPorLocalizacao);
        btnPorEndereco = (Button) findViewById(R.id.button2);
        spinnerCategorias = (Spinner) findViewById(R.id.spinner);
        seekBarRadio = (SeekBar) findViewById(R.id.seekBarRadio);
        txtSeekBarStatus = (TextView) findViewById(R.id.txtStatusSeekbarRaio);

        setSpinnerCategorias();
        controlSeekBar();

        btnPorEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnPorEndereco.setClickable(false);
                btnPorEndereco.setPressed(true);
                String textEndereco = String.valueOf(edtEndereco.getText());
                String txtRaio = getRaio();

                if (textEndereco.equals("")) {

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Informe o endereço!")
                            .setCancelable(false)
                            .setPositiveButton("OK", null)
                            .show();

                } else {

                    getCoordenadasPorEndereco(textEndereco);

                    if (itemSpinnerCategoria.equals("Selecione a categoria")) {

                        Intent irEstabelecimentoPorEnd = new Intent(MainActivity.this, EstabelecimentosActivity.class);
                        irEstabelecimentoPorEnd.putExtra("lat", lat);
                        irEstabelecimentoPorEnd.putExtra("lon", lon);
                        irEstabelecimentoPorEnd.putExtra("raio", txtRaio);
                        irEstabelecimentoPorEnd.putExtra("categoria", "");
                        startActivity(irEstabelecimentoPorEnd);

                    } else {

                        Intent irEstabelecimentoPorEnd = new Intent(MainActivity.this, EstabelecimentosActivity.class);
                        irEstabelecimentoPorEnd.putExtra("lat", lat);
                        irEstabelecimentoPorEnd.putExtra("lon", lon);
                        irEstabelecimentoPorEnd.putExtra("raio", txtRaio);
                        irEstabelecimentoPorEnd.putExtra("categoria", itemSpinnerCategoria);
                        startActivity(irEstabelecimentoPorEnd);
                    }
                }

                btnPorEndereco.setClickable(true);
                btnPorEndereco.setPressed(false);
            }
        });


        callConnection();

        btnPorLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnPorLocal.setClickable(false);
                btnPorLocal.setPressed(true);
                Intent irEstabelecimento = new Intent(MainActivity.this, EstabelecimentosActivity.class);
                irEstabelecimento.putExtra("lat", lat);
                irEstabelecimento.putExtra("lon", lon);
                startActivity(irEstabelecimento);

                btnPorLocal.setClickable(true);
                btnPorLocal.setPressed(false);


            }
        });
    }

    @NonNull
    private String getRaio() {
        String txtRaio;

        if (seekBarRadio.getProgress() <= 0) {

            txtRaio = "10";
        } else {
            txtRaio = String.valueOf(seekBarRadio.getProgress());
        }
        return txtRaio;
    }

    private void controlSeekBar() {
        txtSeekBarStatus.setText(seekBarRadio.getProgress() + "/" + seekBarRadio.getMax());

        seekBarRadio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtSeekBarStatus.setText(progress + "/" + seekBarRadio.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void getCoordenadasPorEndereco(String textEndereco) {
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(textEndereco, 1);
            Address address = addresses.get(0);
            Log.i("GEO", address.toString());

            lat = address.getLatitude();
            lon = address.getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSpinnerCategorias() {
        List<String> categorias = new ArrayList<String>();

        categorias.add("Selecione a categoria");
        categorias.add("HOSPITAL");
        categorias.add("POSTO DE SAÚDE");
        categorias.add("URGÊNCIA");
        categorias.add("SAMU");
        categorias.add("FARMÁCIA");
        categorias.add("CLÍNICA");
        categorias.add("CONSULTÓRIO");
        categorias.add("LABORATÓRIO");
        categorias.add("APOIO À SAÚDE");
        categorias.add("ATENÇÃO ESPECÍFICA");
        categorias.add("UNIDADE ADMINISTRATIVA");
        categorias.add("ATENDIMENTO DOMICILIAR");

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias);

        spinnerCategorias.setAdapter((SpinnerAdapter) adapter);

        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSpinnerCategoria = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private synchronized void callConnection() {
        locationApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        locationApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location l = LocationServices.FusedLocationApi.getLastLocation(locationApiClient);

        lat = l.getLatitude();
        lon = l.getLongitude();

        Log.i("GPS", "LAT: " + String.valueOf(l.getLatitude()));
        Log.i("GPS", "LON: " + String.valueOf(l.getLongitude()));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("GPS", "Suspended: " + String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("GPS", "Failed: " + connectionResult);
    }

    public void callDialog(final String msg, final String[] permissions) {

        mDialog = new MaterialDialog(this)
                .setTitle("Permission")
                .setMessage(msg)
                .setPositiveButton(" OK ", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_PERMISSIONS_CODE);
                        mDialog.dismiss();
                    }
                })
                .setNegativeButton(" Cancel ", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });

        mDialog.show();
    }

    public void callAcessLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                callDialog("É necessário habilitar as permissões para utilizar o App.", new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION});
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE);
            }
        }
    }
}
