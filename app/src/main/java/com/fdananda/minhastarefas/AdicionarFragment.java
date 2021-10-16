package com.fdananda.minhastarefas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdicionarFragment extends Fragment {

    private TextInputEditText tarefaNome;
    private RadioGroup tarefaTipo;
    private RadioButton tarefaTipoDiaria, tarefaTipoEventual;
    private String nomeTarefaIncluir, tipoTarefaIncluir, statusTarefaIncluir;
    private Button buttonCadastrarTarefa;
    private ImageView imageVoltar;
    private DiariasFragment diariasFragment;

    public AdicionarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_adicionar, container, false);

        //Inicializar componentes
        tarefaNome          = view.findViewById(R.id.textInputEditTextAdicionarTarefa);
        tarefaTipo          = view.findViewById(R.id.radioGroupTipo);
        tarefaTipoDiaria    = view.findViewById(R.id.radioButtonDiarias);
        tarefaTipoEventual  = view.findViewById(R.id.radioButtonEventuais);
        buttonCadastrarTarefa = view.findViewById(R.id.buttonCadastrarTarefa);
        imageVoltar          = view.findViewById(R.id.imageVoltarAdicionar);
        diariasFragment      = new DiariasFragment();

        //Início Botão voltar
        imageVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameConteudo, diariasFragment);
                transaction.commit();
            }
        });
        //Fim botão voltar

        //Ação do botão Cadastrar
        buttonCadastrarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nomeTarefaIncluir = tarefaNome.getText().toString();
                statusTarefaIncluir = "N";
                //Opção status
                if (tarefaTipoDiaria.isChecked()) {
                    tipoTarefaIncluir = "Diária";
                } else if (tarefaTipoEventual.isChecked()) {
                    tipoTarefaIncluir = "Eventual";
                }

                //Conferir preenchimento do formulário
                if(!nomeTarefaIncluir.isEmpty()){
                    try {
                        //Criar banco de dados
                        SQLiteDatabase bancoDados = getActivity().getApplicationContext().openOrCreateDatabase("minhatarefas", MODE_PRIVATE, null);

                        //Criar tabela
                        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS tarefas (id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR, tipo VARCHAR, status VARCHAR)");

                        //Inserir dados
                        bancoDados.execSQL("INSERT INTO tarefas(nome, tipo, status) VALUES ( '" + nomeTarefaIncluir + "', '" + tipoTarefaIncluir + "', '" + statusTarefaIncluir + "')");

                        //Recuperar sites
                        String consulta = "SELECT id, nome, tipo, status FROM tarefas ORDER BY id";
                        Cursor cursor = bancoDados.rawQuery(consulta, null);

                        //Recuperar índices da tabela
                        int indiceId = cursor.getColumnIndex("id");
                        int indiceNome = cursor.getColumnIndex("nome");
                        int indiceTipo = cursor.getColumnIndex("tipo");
                        int indiceStatus = cursor.getColumnIndex("status");

                        cursor.moveToFirst();
                        while (cursor!=null){
                            String id = cursor.getString(indiceId);
                            String nome = cursor.getString(indiceNome);
                            String tipo = cursor.getString(indiceTipo);
                            String status = cursor.getString(indiceStatus);
                            cursor.moveToNext();

                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getActivity(), "Tarefa cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();

                }else{
                    Toast.makeText(getActivity(),
                            "Preencha o Nome da Tarefa!",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
        return view;
    }
}