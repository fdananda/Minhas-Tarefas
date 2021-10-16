package com.fdananda.minhastarefas;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.fdananda.minhastarefas.adapter.AdapterDiarias;
import com.fdananda.minhastarefas.model.Diarias;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiariasFragment extends Fragment {

    public DiariasFragment() {
        // Required empty public constructor
    }

    private AdapterDiarias adapterDiarias;
    private RecyclerView recyclerViewTarefas;
    private List<Diarias> listaDiarias = new ArrayList<>();
    private TextView textoVazio;
    private ImageView imageViewIniciarDiarias, imageEditar, imageExcluir;
    private CheckBox checkBoxFeito;
    private EditarFragment editarFragment;
    private ExcluirFragment excluirFragment;
    private AdicionarFragment adicionarFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diarias, container, false);

        //Inicializar componentes
        recyclerViewTarefas     = view.findViewById(R.id.recyclerTarefasDiarias);
        textoVazio              = view.findViewById(R.id.textViewtextoVazioDiarias);
        imageViewIniciarDiarias = view.findViewById(R.id.imageViewIniciarDiarias);
        imageEditar             = view.findViewById(R.id.imageEditarDiarias);
        editarFragment          = new EditarFragment();
        imageExcluir            = view.findViewById(R.id.imageExcluirDiarias);
        excluirFragment         = new ExcluirFragment();
        adicionarFragment         = new AdicionarFragment();

        //FAB
        FloatingActionButton fab = view.findViewById(R.id.fabDiarias);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getActivity().getApplicationContext(), AdicionarFragment.class);
                //startActivity(intent);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameConteudo, adicionarFragment);
                transaction.commit();
            }
        });


        // Listagem de Tarefas
        this.criarTarefas();

        //Configurar o Adapter
        adapterDiarias = new AdapterDiarias(listaDiarias);

        //Configurar o Recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewTarefas.setLayoutManager(layoutManager);
        recyclerViewTarefas.setHasFixedSize(true);
        recyclerViewTarefas.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));  // Linha divisória
        recyclerViewTarefas.setAdapter(adapterDiarias);

        if(listaDiarias.size()>0){
            textoVazio.setVisibility(View.GONE);
        }else{
            textoVazio.setVisibility(View.VISIBLE);
        }

        //Inserir evento de click
        recyclerViewTarefas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewTarefas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                checkBoxFeito = view.findViewById(R.id.nomeCheckbox);
                                //Início alterar checkbox
                                if (checkBoxFeito.isChecked()){
                                    checkBoxFeito.setChecked(false);
                                    checkBoxFeito.setBackgroundColor(getResources().getColor(R.color.colorTransparente));

                                    final Diarias tarefasDiarias = listaDiarias.get(position);
                                    SQLiteDatabase bancoDados = getActivity().getApplicationContext().openOrCreateDatabase("minhatarefas", MODE_PRIVATE, null);
                                    bancoDados.execSQL("UPDATE tarefas SET status = '" + "N" + "' WHERE  nome = '" + tarefasDiarias.getNomeTarefa() + "'");

                                }else{
                                    checkBoxFeito.setChecked(true);
                                    checkBoxFeito.setBackgroundColor(getResources().getColor(R.color.colorFeito));

                                    final Diarias tarefasDiarias = listaDiarias.get(position);
                                    SQLiteDatabase bancoDados = getActivity().getApplicationContext().openOrCreateDatabase("minhatarefas", MODE_PRIVATE, null);
                                    bancoDados.execSQL("UPDATE tarefas SET status = '" + "S" + "' WHERE  nome = '" + tarefasDiarias.getNomeTarefa() + "'");
                                }
                                //Fim alterar checkbok
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                final Diarias tarefasDiarias = listaDiarias.get(position);

                                //Instanciar alertDialog
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setCancelable(true);
                                dialog.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
                                dialog.setTitle("Excluir Tarefa");
                                dialog.setMessage("Deseja excluir permanentemente a tarefa diária " + tarefasDiarias.getNomeTarefa() + " ?");
                                //Configurar ações para Sim e Não
                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Deletar registro
                                        try {
                                            SQLiteDatabase bancoDados = getActivity().getApplicationContext().openOrCreateDatabase("minhatarefas", MODE_PRIVATE, null);
                                            bancoDados.execSQL("DELETE from tarefas WHERE nome = '" + tarefasDiarias.getNomeTarefa() + "' AND  tipo = 'Diária' AND id = '" + tarefasDiarias.getId() + "'");
                                            startActivity(new Intent(getContext(), MainActivity.class));
                                            getActivity().finish();

                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(getContext(),
                                                "Exclusão realizada com sucesso!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });

                                dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getContext(),
                                                "Exclusão cancelada!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                                // Criar e exibir AlertDialog
                                dialog.create();
                                dialog.show();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        }
                )
        );

        // Fim evento de clique

        //Evento de clique
        imageViewIniciarDiarias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Início Alert Dialog
                if(listaDiarias.size()>0){

                    //Instanciar alertDialog
                    AlertDialog.Builder dialogLimpar = new AlertDialog.Builder(getContext());
                    dialogLimpar.setCancelable(true);
                    dialogLimpar.setIcon(android.R.drawable.ic_menu_revert);
                    dialogLimpar.setTitle("Limpar Tarefas");
                    dialogLimpar.setMessage("Esta opção irá desmarcar/limpar todas as seleções de tarefas diárias realizadas. Deseja continuar?");
                    //Configurar ações para Sim e Não
                    dialogLimpar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            limparTarefas();
                            criarTarefas();
                            Toast toastLegenda = Toast.makeText(getActivity(), "Limpeza efetuada!", Toast.LENGTH_SHORT);
                            toastLegenda.setGravity(Gravity.CENTER, 0, 0);
                            toastLegenda.show();
                        }
                    });

                    dialogLimpar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(),
                                    "Limpeza cancelada!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Criar e exibir AlertDialog
                    dialogLimpar.create();
                    dialogLimpar.show();


                }else{
                    Toast toastLegenda = Toast.makeText(getActivity(), "Sem tarefas para limpar!", Toast.LENGTH_SHORT);
                    toastLegenda.setGravity(Gravity.CENTER, 0, 0);
                    toastLegenda.show();
                }
                // Fim Alert Dialog
            }
        });

        //Abrir Página para editar
        imageEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameConteudo, editarFragment);
                transaction.commit();

            }
        });

        //Abrir Página para excluir
        imageExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameConteudo, excluirFragment);
                transaction.commit();

            }
        });

        return view;
    }

    // Início Desmarcar check

    public void limparTarefas(){

        //Banco de dados
        try {
            //Criar banco de dados
            SQLiteDatabase bancoDados = getActivity().getApplicationContext().openOrCreateDatabase("minhatarefas", MODE_PRIVATE, null);

            //Recuperar sites
            String consulta = "SELECT id, nome, tipo, status FROM tarefas WHERE tipo = 'Diária' GROUP BY nome ORDER BY id";
            Cursor cursor = bancoDados.rawQuery(consulta, null);

            if(consulta.isEmpty()){

                Toast.makeText(getContext(), "Você ainda não possui nenhuma tarefa cadastrada!", Toast.LENGTH_SHORT).show();

            }else{

                //Recuperar índices da tabela
                int indiceId = cursor.getColumnIndex("id");
                int indiceNome = cursor.getColumnIndex("nome");
                int indiceStatus = cursor.getColumnIndex("status");
                int indiceTipo = cursor.getColumnIndex("tipo");

                cursor.moveToFirst();
                while (cursor!=null){
                    Integer id = cursor.getInt(indiceId);
                    String nome = cursor.getString(indiceNome);
                    String status = cursor.getString(indiceStatus);
                    String tipo = cursor.getString(indiceTipo);

                    Diarias diarias = new Diarias(nome, tipo, status, id);
                    listaDiarias.add(diarias);
                    cursor.moveToNext();

                    bancoDados.execSQL("UPDATE tarefas SET status = '" + "N" + "' WHERE tipo = 'Diária'");
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    // Fim desmarcar checkbox


    public void criarTarefas() {

        listaDiarias.clear();

        //Banco de dados
        try {
            //Criar banco de dados
            SQLiteDatabase bancoDados = getActivity().getApplicationContext().openOrCreateDatabase("minhatarefas", MODE_PRIVATE, null);

            //Recuperar sites
            String consulta = "SELECT id, nome, tipo, status FROM tarefas WHERE tipo = 'Diária' GROUP BY nome ORDER BY id";
            Cursor cursor = bancoDados.rawQuery(consulta, null);

            if(consulta.isEmpty()){

                Toast.makeText(getContext(), "Você ainda não possui nenhuma tarefa cadastrada!", Toast.LENGTH_SHORT).show();

            }else{

                //Recuperar índices da tabela
                int indiceId = cursor.getColumnIndex("id");
                int indiceNome = cursor.getColumnIndex("nome");
                int indiceStatus = cursor.getColumnIndex("status");
                int indiceTipo = cursor.getColumnIndex("tipo");

                cursor.moveToFirst();
                while (cursor!=null){
                    Integer id = cursor.getInt(indiceId);
                    String nome = cursor.getString(indiceNome);
                    String status = cursor.getString(indiceStatus);
                    String tipo = cursor.getString(indiceTipo);

                    Diarias diarias = new Diarias(nome, tipo, status, id);
                    listaDiarias.add(diarias);
                    cursor.moveToNext();
                }

            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}