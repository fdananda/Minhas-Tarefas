package com.fdananda.minhastarefas.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fdananda.minhastarefas.R;
import com.fdananda.minhastarefas.model.Diarias;
import java.util.List;

public class AdapterDiarias extends RecyclerView.Adapter<AdapterDiarias.MyViewHolder> {

    private List<Diarias> listaDiarias;

    public AdapterDiarias(List<Diarias> lista) {
        this.listaDiarias = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tarefas, parent, false);

        return new MyViewHolder(itemLista);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) { // Visualização dos itens

        Diarias diarias = listaDiarias.get(position);

        holder.nome.setText(diarias.getNomeTarefa());

        if (diarias.getStatusTarefa().equals("S")){
            holder.nome.setChecked(true);
            holder.nome.setBackgroundColor(R.color.colorFeitoInicial);;
        }else {
            holder.nome.setChecked(false);
        }
    }

    @Override
    public int getItemCount() { //Quantidade de itens que serão apresentados
        return listaDiarias.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome    = itemView.findViewById(R.id.nomeCheckbox);

        }
    }
}