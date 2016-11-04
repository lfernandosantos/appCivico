package com.example.ferna.appcivico.VIEW;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ferna.appcivico.DetailFragment;
import com.example.ferna.appcivico.MODEL.Estabelecimento;
import com.example.ferna.appcivico.R;

import java.util.ArrayList;
import java.util.List;

public class DetailsEstabelecimentoActivity extends AppCompatActivity {

    private Estabelecimento selecionado;
    private TextView nomeFantasia;
    private TextView categoria;
    private TextView cnpj;
    private TextView atendimento;
    private TextView lblCNPJ;
    private List<String> strListaServicos;
    private ImageView imgServicos;
    private FloatingActionButton fabTelefone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_estabelecimento);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selecionado = (Estabelecimento) getIntent().getSerializableExtra("selecionado");

//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frameDetails, new DetailFragment());
//        transaction.commit();


        findViews();
        preencheCampos();

        imgServicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregaListaServicos();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameDetails, new DetailFragment());
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findViews() {
        nomeFantasia = (TextView) findViewById(R.id.textDetailsNome);
        categoria = (TextView) findViewById(R.id.textDetailsCategoria);
        cnpj = (TextView) findViewById(R.id.textDetailsCNPJ);
        atendimento = (TextView) findViewById(R.id.textDetailsAtendimento);
        lblCNPJ = (TextView) findViewById(R.id.lblCPNJ);
        imgServicos = (ImageView) findViewById(R.id.imgServicos);
        fabTelefone = (FloatingActionButton) findViewById(R.id.fabTelefone);

//        tipo = (TextView) findViewById(R.id.txtTipo);
//        lougradouro = (TextView) findViewById(R.id.txtLogradouro);
//        cidade = (TextView) findViewById(R.id.txtCidade);
//        uf = (TextView) findViewById(R.id.txtUF);
//        numero = (TextView) findViewById(R.id.textDetailsTel);
//        imgGoMaps = (ImageView) findViewById(R.id.imgMaps);
//        imgLogoSus = (ImageView) findViewById(R.id.imgVLogoSus);
//        imgLogoEmergencia = (ImageView) findViewById(R.id.imgVLogoEmergencia);
//        imgVCall = (ImageView) findViewById(R.id.imgVPhone);
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
                cnpj.setText(selecionado.cnpj);
//            }
//        }

        if (selecionado.telefone != null) {
//            if (Double.valueOf(selecionado.telefone) <= 0.0) {
//                telefone.setVisibility(View.INVISIBLE);
//                imgTel.setVisibility(View.INVISIBLE);
//            }else {

            fabTelefone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailsEstabelecimentoActivity.this);
                    builder.setTitle("TELEFONE");
                    builder.setMessage("Ligar para " + selecionado.telefone + " ?");
                    builder.setPositiveButton("LIGAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse("tel:" + selecionado.telefone);
                            Intent ligarPara = new Intent(Intent.ACTION_CALL, uri);

                            if (ActivityCompat.checkSelfPermission(DetailsEstabelecimentoActivity.this,
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
            fabTelefone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailsEstabelecimentoActivity.this);
                    builder.setTitle("SEM TELEFONE");
                    builder.setMessage("Unidade não possui telefone!");
                    builder.create();
                    builder.show();
                }
            });
        }


        

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
