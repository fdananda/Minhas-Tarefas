package com.fdananda.minhastarefas;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdananda.minhastarefas.EditarFragment;
import com.fdananda.minhastarefas.MainActivity;
import com.fdananda.minhastarefas.R;
import com.fdananda.minhastarefas.adapter.AdapterEventuais;
import com.fdananda.minhastarefas.model.Eventuais;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class EventuaisFragment extends Fragment {

    public EventuaisFragment() {
        // Required empty public constructor
    }

    private AdapterEventuais adapterEventuais;
    private RecyclerView recyclerViewTarefas;
    private List<Eventuais> listaEventuais = new ArrayList<>();
    private TextView textoVazio;
    private ImageView imageViewIniciarEventuais,  imageEditar;
    private CheckBox checkBoxFeito;
    private EditarFragment editarFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eventuais, container, false);

        //Inicializar componentes
        recyclerViewTarefas = view.findViewById(R.id.recyclerTarefasEventuais);
        textoVazio          = view.findViewById(R.id.textViewtextoVazio);
        imageViewIniciarEventuais = view.findViewById(R.id.imageViewIniciarEventuais);
        imageEditar             = view.findViewById(R.id.imageEditarEventuais);
        editarFragment   = new EditarFragment();


        // Listagem de Tarefas
        this.criarTarefas();

        //Configurar o Adapter
        adapterEventuais = new AdapterEventuais(listaEventuais);

        //Configurar o Recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewTarefas.setLayoutManager(layoutManager);
        recyclerViewTarefas.setHasFixedSize(true);
        recyclerViewTarefas.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));  // Linha divisória
        recyclerViewTarefas.setAdapter(adapterEventuais);

        if(listaEventuais.size()>0){
            textoVazio.setVisibility(View.GONE);
        }else{
            textoVazio.setVisibility(View.VISIBLE);
        }


        //Início evento de clique

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

                                    final Eventuais tarefasEventuais = listaEventuais.get(position);
                                    SQLiteDatabase bancoDados = getActivity().getApplicationContext().openOrCreateDatabase("minhatarefas", MODE_PRIVATE, null);
                                    bancoDados.execSQL("UPDATE tarefas SET status = '" + "N" + "' WHERE  nome = '" + tarefasEventuais.getNomeTarefa() + "'");

                                }else{
                                    checkBoxFeito.setChecked(true);
                                    checkBoxFeito.setBackgroundColor(getResources().getColor(R.color.colorFeito));

                                    final Eventuais tarefasEventuais = listaEventuais.get(position);
                                    SQLiteDatabase bancoDados = getActivity().getApplicationContext().openOrCreateDatabase("minhatarefas", MODE_PRIVATE, null);
                                    bancoDados.execSQL("UPDATE tarefas SET status = '" + "S" + "' WHERE  nome = '" + tarefasEventuais.getNomeTarefa() + "'");

                                }


                                //Fim alterar checkbok

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                final Eventuais tarefasEventuais = listaEventuais.get(position);

                                //Instanciar alertDialog
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setCancelable(true);
                                dialog.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
                                dialog.setTitle("Excluir a tarefa");
                                dialog.setMessage("Deseja excluir a tarefa eventual " + tarefasEventuais.getNomeTarefa() + " ?");
                                //Configurar ações para Sim e Não
                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Deletar um registro
                                        try {
                                            SQLiteDatabase bancoDados = getActivity().getApplicationContext().openOrCreateDatabase("minhatarefas", MODE_PRIVATE, null);
                                            bancoDados.execSQL("DELETE from tarefas WHERE nome = '" + tarefasEventuais.getNomeTarefa() + "'");
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


        imageViewIniciarEventuais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Início 15/03/2021 - Alert Dialog
                if(listaEventuais.size()>0){

                    //Instanciar alertDialog
                    AlertDialog.Builder dialogLimpar = new AlertDialog.Builder(getContext());
                    dialogLimpar.setCancelable(true);
                    dialogLimpar.setIcon(android.R.drawable.ic_menu_revert );
                    dialogLimpar.setTitle("Limpar Tarefas");
                    dialogLimpar.setMessage("Esta opção irá excluir todas as seleções de tarefas eventuais realizadas. Deseja continuar?");
                    //Configurar ações para Sim e Não
                    dialogLimpar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Toast toastLegenda = Toast.makeText(getActivity(), "Limpeza efetuada!", Toast.LENGTH_SHORT);
                            toastLegenda.setGravity(Gravity.CENTER, 0, 0);
                            toastLegenda.show();
                            //textoLegendaVazio.setVisibility(View.VISIBLE);
                            //textoLegendaLimpar.setVisibility(View.VISIBLE);
                            limparTarefas();
                            criarTarefas();

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
                //Fim 15/03/2021 - Fim Alert Dialog
            }
        });

        //Abrir Página para editar - 17/03/2021
        imageEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameConteudo, editarFragment);
                transaction.commit();

            }
        });


        return view;
    }

    // Início 06/03/2021 - Desmarcar check

    public void limparTarefas() {

        //Banco de dados
        try {
            //Criar banco de dados
            SQLiteDatabase bancoDados = getActivity().getApplicationContext().openOrCreateDatabase("minhatarefas", MODE_PRIVATE, null);

            //Recuperar sites
            String consulta = "SELECT id, nome, tipo, status FROM tarefas WHERE tipo = 'Eventual' GROUP BY nome ORDER BY id";
            Cursor cursor = bancoDados.rawQuery(consulta, null);

            if (consulta.isEmpty()) {

                Toast.makeText(getContext(), "Você ainda não possui nenhuma tarefa cadastrada!", Toast.LENGTH_SHORT).show();

            } else {

                //Recuperar índices da tabela
                int indiceId = cursor.getColumnIndex("id");
                int indiceNome = cursor.getColumnIndex("nome");
                int indiceStatus = cursor.getColumnIndex("status");
                int indiceTipo = cursor.getColumnIndex("tipo");

                cursor.moveToFirst();
                while (cursor != null) {
                    Integer id = cursor.getInt(indiceId);
                    String nome = cursor.getString(indiceNome);
                    String status = cursor.getString(indiceStatus);
                    String tipo = cursor.getString(indiceTipo);

                    Eventuais eventuais = new Eventuais(nome, tipo, status, id);
                    listaEventuais.add(eventuais);
                    cursor.moveToNext();

                    bancoDados.execSQL("UPDATE tarefas SET status = '" + "N" + "' WHERE tipo = 'Eventual'");
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Fim 14/03/2021



    public void criarTarefas() {

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
