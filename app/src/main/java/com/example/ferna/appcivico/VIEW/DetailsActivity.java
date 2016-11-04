package com.example.ferna.appcivico.VIEW;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ferna.appcivico.DetailFragment;
import com.example.ferna.appcivico.MODEL.Estabelecimento;
import com.example.ferna.appcivico.R;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private Estabelecimento selecionado;
    private TextView nomeFantasia;
    private TextView categoria;
    private TextView cnpj;
    private TextView atendimento;
    private FloatingActionButton fabCall;
    private FloatingActionButton fabGoMaps;
    private FloatingActionButton fabServicos;
    private List<String> strListaServicos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selecionado = (Estabelecimento) getIntent().getSerializableExtra("selecionado");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, new DetailFragment());
        transaction.commit();

        findViews();
        preencheCampos();

    }

    private void findViews() {
        View layout = findViewById(R.id.include);
        nomeFantasia = (TextView) layout.findViewById(R.id.textViewNome);
        categoria = (TextView) layout.findViewById(R.id.tCategoria);
        cnpj = (TextView) layout.findViewById(R.id.tCNPJ);
        atendimento = (TextView) layout.findViewById(R.id.tAtendimento);
        fabCall = (FloatingActionButton) layout.findViewById(R.id.fabCall);
        fabGoMaps = (FloatingActionButton) layout.findViewById(R.id.fabGoMaps);
        fabServicos = (FloatingActionButton) layout.findViewById(R.id.fabServicos);
    }

    private void preencheCampos() {
        nomeFantasia.setText(selecionado.nomeFantasia);
        categoria.setText(selecionado.categoriaUnidade);
        atendimento.setText(selecionado.turnoAtendimento);

//        if (selecionado.cnpj != null) {
//            if (Double.valueOf(selecionado.cnpj) <= 0.0) {
//                cnpj.setVisibility(View.INVISIBLE);
//                lblCNPJ.setVisibility(View.INVISIBLE);
//            } else {
        cnpj.setText("CNPJ: :" + selecionado.cnpj);
//            }
//        }

        if (selecionado.telefone != null) {
//            if (Double.valueOf(selecionado.telefone) <= 0.0) {
//                telefone.setVisibility(View.INVISIBLE);
//                imgTel.setVisibility(View.INVISIBLE);
//            }else {

            fabCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                    builder.setTitle("TELEFONE");
                    builder.setMessage("Ligar para " + selecionado.telefone + " ?");
                    builder.setPositiveButton("LIGAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse("tel:" + selecionado.telefone);
                            Intent ligarPara = new Intent(Intent.ACTION_CALL, uri);

                            if (ActivityCompat.checkSelfPermission(DetailsActivity.this,
                                    android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            startActivity(ligarPara);
                        }
                    });
                    builder.setNegativeButton("CANCELAR", null);
                    builder.create();
                    builder.show();
                }
            });

//            }
        }else {
            fabCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                    builder.setTitle("SEM TELEFONE");
                    builder.setMessage("Unidade não possui telefone!");
                    builder.create();
                    builder.show();
                }
            });
        }

        fabServicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregaListaServicos();
            }
        });



    }

    private void carregaListaServicos(){
        LayoutInflater layoutInflater = getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.layout_dialog_servicos_estab, null);

        ListView listaServicos = (ListView) view.findViewById(R.id.listView_servicos_dialog);

        strListaServicos = getServicos();
        if (strListaServicos != null) {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, strListaServicos);
            listaServicos.setAdapter(adapter);
        }else {
            String[] strElse = {"Não possui!"};
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, strElse);
            listaServicos.setAdapter(adapter);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SERVIÇOS");
        builder.setView(view);
        builder.create();
        builder.show();
    }

    private List<String> getServicos(){
        List<String> listGetServicos = new ArrayList<>();
        if (selecionado.temAtendimentoUrgencia.equals("Sim")){
            listGetServicos.add("Atendimento Urgencia");
        }
        if (selecionado.temAtendimentoAmbulatorial.equals("Sim")){
            listGetServicos.add("Atendimento Ambulatorial");
        }
        if (selecionado.temCentroCirurgico.equals("Sim")){
            listGetServicos.add("Centro Cirurgico");
        }
        if (selecionado.temObstetra.equals("Sim")){
            listGetServicos.add("Obstetra");
        }
        if (selecionado.temNeoNatal.equals("Sim")){
            listGetServicos.add("Neo Natal");
        }
        if (selecionado.temDialise.equals("Sim")){
            listGetServicos.add("Dialise");
        }

        return listGetServicos;
    }


}
