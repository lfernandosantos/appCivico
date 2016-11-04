package com.example.ferna.appcivico.VIEW;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ferna.appcivico.R;
import com.example.ferna.appcivico.adapter.AdapterListaEstabelecimentos;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        View layoutInclude = findViewById(R.id.layoutIncludeScrolling);
        ListView listViewScrolling = (ListView) layoutInclude.findViewById(R.id.listaScrolling);

        String[]  nomes = {"NOME", "CLINICA2", "OUTRA CLINICA", "OUTRA TAMBÉM", "MAIS OUTRA"};
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nomes);
        listViewScrolling.setAdapter(adapter);


    }
}
