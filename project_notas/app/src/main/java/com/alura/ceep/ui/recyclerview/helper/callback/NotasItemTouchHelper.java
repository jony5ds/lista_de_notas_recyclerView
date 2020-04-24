package com.alura.ceep.ui.recyclerview.helper.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alura.ceep.dao.NotaDAO;
import com.alura.ceep.ui.recyclerview.NotasAdapter;

public class NotasItemTouchHelper extends ItemTouchHelper.Callback {

    private final NotasAdapter _adapter;
    private NotaDAO _notaDao;

    public NotasItemTouchHelper(NotasAdapter adapter) {
        _adapter = adapter;
        _notaDao = new NotaDAO();
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int marcacoesDeDeslize  = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        int marcacoesDeArrastar = ItemTouchHelper.DOWN | ItemTouchHelper.UP
                | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        
        return  makeMovementFlags(marcacoesDeArrastar,marcacoesDeDeslize);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int posicaoInical = viewHolder.getAdapterPosition();
        int posicaoFinal = target.getAdapterPosition();
        trocaNotas(posicaoInical, posicaoFinal);
        return true;
    }

    private void trocaNotas(int posicaoInical, int posicaoFinal) {
        _notaDao.troca(posicaoInical,posicaoFinal);
        _adapter.troca(posicaoInical,posicaoFinal);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int notaPosicao = viewHolder.getAdapterPosition();
        removeNota(notaPosicao);
    }

    private void removeNota(int notaPosicao) {
        _notaDao.remove(notaPosicao);
        _adapter.remove(notaPosicao);
    }
}
