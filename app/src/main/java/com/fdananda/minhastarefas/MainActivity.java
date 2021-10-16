package com.fdananda.minhastarefas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private DiariasFragment diariasFragment;
    private EventuaisFragment eventuaisFragment;
    private AdicionarFragment adicionarFragment;
    private VerTodasFragment verTodasFragment;
    private RealizadasFragment realizadasFragment;
    private ARealizarFragment aRealizarFragment;
    private Button buttonAtvsDiarias;
    private Button buttonAtvsEventuais;
    private Button buttonAtvsAdicionar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("Minhas Tarefas");
        setSupportActionBar(toolbar);

        //Inicializar componentes
        diariasFragment   = new DiariasFragment();
        eventuaisFragment = new EventuaisFragment();
        adicionarFragment = new AdicionarFragment();
        verTodasFragment  = new VerTodasFragment();
        realizadasFragment = new RealizadasFragment();
        aRealizarFragment = new ARealizarFragment();
        buttonAtvsDiarias = findViewById(R.id.buttonAtvsDiarias);
        buttonAtvsEventuais = findViewById(R.id.buttonAtvsEventuais);
        buttonAtvsAdicionar = findViewById(R.id.buttonAtvsAdicionar);


        //Apresentar o conteúdo inicial (DiariasFragment) sem o clique do botão
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameConteudo, diariasFragment);
        transaction.commit();

        //Clique no botão Diárias
        buttonAtvsDiarias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Apresentar o conteúdo DiáriasFragment com o clique do botão
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameConteudo, diariasFragment);
                transaction.commit();
            }
        });

        //Clique no botão Eventuais
        buttonAtvsEventuais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Apresentar o conteúdo EventuaisFragment com o clique do botão
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameConteudo, eventuaisFragment);
                transaction.commit();
            }
        });

        //Clique no botão Adicionar
        buttonAtvsAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Apresentar o conteúdo AdicionarFragment com o clique do botão
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameConteudo, adicionarFragment);
                transaction.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.verTodasMenu:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameConteudo, verTodasFragment);
                transaction.commit();
                break;

            case R.id.verRealizadasMenu:
                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                transaction2.replace(R.id.frameConteudo, realizadasFragment);
                transaction2.commit();
                break;

            case R.id.verARelizarMenu:
                FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                transaction3.replace(R.id.frameConteudo, aRealizarFragment);
                transaction3.commit();
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}