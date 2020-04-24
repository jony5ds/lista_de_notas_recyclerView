package com.alura.ceep.ui.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alura.ceep.R;
import com.alura.ceep.model.Nota;

import java.util.Collections;
import java.util.List;

public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.NotasViewHolder> {

    private Context _context;
    private List<Nota> _notas;
    private SetOnItemClickListener _clickListener;

    public NotasAdapter(Context context, List<Nota> notas)
    {
        this._context = context;
        this._notas = notas;
    }

    public void setClickListener(SetOnItemClickListener _clickListener) {
        this._clickListener = _clickListener;
    }

    @NonNull
    @Override
    public NotasAdapter.NotasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.item_nota,
                parent,
                false);
        return new NotasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotasAdapter.NotasViewHolder holder, int position) {
        Nota nota = _notas.get(position);
        holder.vincula(nota);
    }

    @Override
    public int getItemCount() {
        return _notas.size();
    }

    public void altera(int posicao, Nota nota) {
        _notas.set(posicao,nota);
        notifyDataSetChanged();
    }

    public void remove(int notaPosicao) {
        _notas.remove(notaPosicao);
        notifyItemRemoved(notaPosicao);
    }

    public void troca(int posicaoInical, int posicaoFinal) {
        Collections.swap(_notas,posicaoInical,posicaoFinal);
        notifyItemMoved(posicaoInical, posicaoFinal);
    }

    public class NotasViewHolder extends RecyclerView.ViewHolder{

        private final TextView descricao;
        private final TextView titulo;
        private Nota nota;

        public NotasViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _clickListener.onClick(nota,getAdapterPosition());
                }
            });
        }

        public void vincula(Nota nota)
        {
            preencheCampo(nota);
            this.nota = nota;
        }

        private void preencheCampo(Nota nota) {
            titulo.setText(nota.getTitulo());
            descricao.setText(nota.getDescricao());
        }
    }

    public void adicionaNota(Nota nota)
    {
        _notas.add(nota);
        notifyDataSetChanged();

    }
}
