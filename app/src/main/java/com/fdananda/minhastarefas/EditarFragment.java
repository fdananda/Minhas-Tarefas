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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.fdananda.minhastarefas.adapter.AdapterDiarias;
import com.fdananda.minhastarefas.adapter.AdapterEventuais;
import com.fdananda.minhastarefas.model.Diarias;
import com.fdananda.minhastarefas.model.Eventuais;
import java.util.ArrayList;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditarFragment extends Fragment {

    public EditarFragment() {
        // Required empty public constructor
    }

    private ImageView imageVoltar;
    private DiariasFragment diariasFragment;
    private AdapterDiarias adapterDiarias;
    private RecyclerView recyclerViewTarefasDiarias;
    private List<Diarias> listaDiarias = new ArrayList<>();
    private TextView textoVazioDiarias;
    private CheckBox checkBoxDiariasEditar;
    private AdapterEventuais adapterEventuais;
    private RecyclerView recyclerViewTarefasEventuais;
    private List<Eventuais> listaEventuais = new ArrayList<>();
    private TextView textoVazioEventuais;
    private CheckBox checkBoxEventuaisEditar;
    private EditText textoEditar;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar, container, false);

        //Inicializar componentes
        recyclerViewTarefasDiarias   = view.findViewById(R.id.recyclerEditarTarefasDiarias);
        recyclerViewTarefasEventuais = view.findViewById(R.id.recyclerEditarTarefasEventuais);
        textoVazioDiarias            = view.findViewById(R.id.textViewtextoVazioDiariasEditar);
        textoVazioEventuais          = view.findViewById(R.id.textViewtextoVazioEventuaisEditar);
        imageVoltar                  = view.findViewById(R.id.imageVoltar);
        diariasFragment              = new DiariasFragment();

        // Listagem de Tarefas
        this.criarTarefasDiarias();
        this.criarTarefasEventuais();

        //Configurar o Adapter
        adapterDiarias = new AdapterDiarias(listaDiarias);
        adapterEventuais = new AdapterEventuais(listaEventuais);

        //Configurar o Recyclerview Diárias
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewTarefasDiarias.setLayoutManager(layoutManager);
        recyclerViewTarefasDiarias.setHasFixedSize(true);
        recyclerViewTarefasDiarias.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));  // Linha divisória
        recyclerViewTarefasDiarias.setAdapter(adapterDiarias);

        //Configurar o Recyclerview Eventuais
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        recyclerViewTarefasEventuais.setLayoutManager(layoutManager2);
        recyclerViewTarefasEventuais.setHasFixedSize(true);
        recyclerViewTarefasEventuais.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));  // Linha divisória
        recyclerViewTarefasEventuais.setAdapter(adapterEventuais);

        //Mostrar texto vazio Diárias
        if(listaDiarias.size()>0){
            textoVazioDiarias.setVisibility(View.GONE);
        }else{
            textoVazioDiarias.setVisibility(View.VISIBLE);
        }

        //Mostrar texto vazio Eventuais
        if(listaEventuais.size()>0){
            textoVazioEventuais.setVisibility(View.GONE);
        }else{
            textoVazioEventuais.setVisibility(View.VISIBLE);
        }

        //Botão voltar
        imageVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameConteudo, diariasFragment);
                transaction.commit();
            }
        });
        //Fim botão voltar

        //Inserir evento de click em Tarefas Diárias
        recyclerViewTarefasDiarias.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewTarefasDiarias,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                final Diarias tarefasDiarias = listaDiarias.get(position);
                                checkBoxDiariasEditar = view.findViewById(R.id.nomeCheckbox);

                                //Início alterar checkbox
                                if (checkBoxDiariasEditar.isChecked()){
                                    checkBoxDiariasEditar.setChecked(false);
                                }else{
                                    checkBoxDiariasEditar.setChecked(true);
                                }
                                //Fim alterar checkbok

                                //Instanciar alertDialog
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setCancelable(true);
                                dialog.setIcon(android.R.drawable.ic_menu_edit);
                                dialog.setTitle("Editar Tarefa Diária");

                                final View dialogLayout = inflater.inflate(R.layout.editar_tarefa, null);
                                dialog.setView(dialogLayout);
                                textoEditar = dialogLayout.findViewById(R.id.editTextEditar);
                                textoEditar.setText(""+ tarefasDiarias.getNomeTarefa() +"");

                                //Configurar ações para Sim e Não
                                dialog.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Deletar um registro
                                        try {
                                            SQLiteDatabase bancoDados = getActivity().getApplicationContext().openOrCreateDatabase("minhatarefas", MODE_PRIVATE, null);
                                            textoEditar = dialogLayout.findViewById(R.id.editTextEditar);
                                            String textoAlterado = textoEditar.getText().toString();
                                            bancoDados.execSQL("UPDATE tarefas SET nome = '" + textoAlterado + "' WHERE  tipo = 'Diária' AND id = '" + tarefasDiarias.getId() + "' AND nome = '" + tarefasDiarias.getNomeTarefa() + "'");

                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(getContext(),
                                                "Alteração realizada com sucesso!",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getContext(), MainActivity.class));
                                        getActivity().finish();

                                    }
                                });

                                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getContext(),
                                                "Alteração cancelada!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                                // Criar e exibir AlertDialog
                                dialog.create();
                                dialog.show();

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        }
                )
        );
        // Fim evento de clique

        //Inserir evento de click em Tarefas Eventuais
        recyclerViewTarefasEventuais.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewTarefasEventuais,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                final Eventuais tarefasEventuais = listaEventuais.get(position);
                                checkBoxEventuaisEditar = view.findViewById(R.id.nomeCheckbox);

                                //Início alterar checkbox
                                if (checkBoxEventuaisEditar.isChecked()){
                                    checkBoxEventuaisEditar.setChecked(false);
                                }else{
                                    checkBoxEventuaisEditar.setChecked(true);
                                }
                                //Fim alterar checkbok

                                //Instanciar alertDialog
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setCancelable(true);
                                dialog.setIcon(android.R.drawable.ic_menu_edit);
                                dialog.setTitle("Editar Tarefa Eventual");

                                final View dialogLayout = inflater.inflate(R.layout.editar_tarefa, null);
                                dialog.setView(dialogLayout);
                                textoEditar = dialogLayout.findViewById(R.id.editTextEditar);
                                textoEditar.setText(""+ tarefasEventuais.getNomeTarefa() +"");

                                //Configurar ações para Sim e Não
                                dialog.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Deletar um registro
                                        try {
                                            SQLiteDatabase bancoDados = getActivity().getApplicationContext().openOrCreateDatabase("minhatarefas", MODE_PRIVATE, null);
                                            textoEditar = dialogLayout.findViewById(R.id.editTextEditar);
                                            String textoAlterado = textoEditar.getText().toString();
                                            bancoDados.execSQL("UPDATE tarefas SET nome = '" + textoAlterado + "' WHERE  tipo = 'Eventual' AND id = '" + tarefasEventuais.getId() + "' AND nome = '" + tarefasEventuais.getNomeTarefa() + "'");
                                            //bancoDados.execSQL("UPDATE tarefas SET nome = '" + textoAlterado + "' WHERE  tipo = 'Eventual' AND nome = '" + tarefasEventuais.getNomeTarefa() + "'");

                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(getContext(),
                                                "Alteração realizada com sucesso!",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getContext(), MainActivity.class));
                                        getActivity().finish();

                                    }
                                });

                                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getContext(),
                                                "Alteração cancelada!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                                // Criar e exibir AlertDialog
                                dialog.create();
                                dialog.show();

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        }
                )
        );

        // Fim evento de clique



        return view;
    }

    public void criarTarefasDiarias() {

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

    public void criarTarefasEventuais() {

        listaEventuais.clear();

        //Banco de dados
        try {
            //Criar banco de dados
            SQLiteDatabase bancoDados = getActivity().getApplicationContext().openOrCreateDatabase("minhatarefas", MODE_PRIVATE, null);

            //Recuperar sites
            String consulta = "SELECT id, nome, tipo, status FROM tarefas WHERE tipo = 'Eventual' GROUP BY nome ORDER BY id";
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

                    Eventuais eventuais = new Eventuais(nome, tipo, status, id);
                    listaEventuais.add(eventuais);
                    cursor.moveToNext();
                }

            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}