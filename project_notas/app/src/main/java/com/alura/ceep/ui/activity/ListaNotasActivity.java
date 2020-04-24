package com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alura.ceep.R;
import com.alura.ceep.dao.NotaDAO;
import com.alura.ceep.model.Nota;
import com.alura.ceep.ui.recyclerview.NotasAdapter;
import com.alura.ceep.ui.recyclerview.SetOnItemClickListener;
import com.alura.ceep.ui.recyclerview.helper.callback.NotasItemTouchHelper;

import java.util.List;

import static com.alura.ceep.ui.activity.Const.CHAVE_NOTA;
import static com.alura.ceep.ui.activity.Const.CHAVE_POSICAO;
import static com.alura.ceep.ui.activity.Const.POSICAO_INVALIDA;
import static com.alura.ceep.ui.activity.Const.REQUEST_CODE_ALTERA_NOTA;
import static com.alura.ceep.ui.activity.Const.REQUEST_CODE_INSERE_NOTA;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Notas";
    private NotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        setTitle(TITULO_APPBAR);

        List<Nota> todasNotas = getNotas();

        configuraRecyclerView(todasNotas);

        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiParaFomularioNotaAcaoIncluir();
            }
        });
    }

    private void vaiParaFomularioNotaAcaoIncluir() {
        Intent iniciaFormularioNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota, REQUEST_CODE_INSERE_NOTA);
    }

    private List<Nota> getNotas() {
        NotaDAO dao = new NotaDAO();

        return dao.todos();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ehResultadoComNota(requestCode, resultCode, data))
        {
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            adicionaNota(notaRecebida);
        }
        if(ehResultadoDeAlteracao(requestCode,resultCode,data))
        {
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO,POSICAO_INVALIDA);

            if (ehPosicaoValida(posicaoRecebida))
            {
                alteraNota(notaRecebida, posicaoRecebida);
            }
            else
            {
                Toast.makeText(this,
                        "Ocorreu um problema na alteração da nota",
                        Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alteraNota(Nota notaRecebida, int posicaoRecebida) {
        new NotaDAO().altera(posicaoRecebida,notaRecebida);
        adapter.altera(posicaoRecebida,notaRecebida);
    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private void adicionaNota(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adicionaNota(nota);
    }

    private boolean ehResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return requestCode == REQUEST_CODE_INSERE_NOTA
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.hasExtra(CHAVE_NOTA);
    }

    private boolean ehResultadoDeAlteracao(int requestCode, int resultCode, @Nullable Intent data) {
        return requestCode == REQUEST_CODE_ALTERA_NOTA
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.hasExtra(CHAVE_NOTA);
    }

    private void configuraRecyclerView(List<Nota> todasAsNotas) {
        RecyclerView lista = findViewById(R.id.lista_recyclerview);
        configuraAdapter(todasAsNotas, lista);
        irParaFormularioAcaoDeAlterar();
    }

    private void irParaFormularioAcaoDeAlterar() {
        adapter.setClickListener(new SetOnItemClickListener() {
            @Override
            public void onClick(Nota nota, int posicao) {
                Intent iniciaFormularioNota = new Intent(ListaNotasActivity.this,
                        FormularioNotaActivity.class);

                iniciaFormularioNota.putExtra(CHAVE_NOTA, nota);
                iniciaFormularioNota.putExtra(CHAVE_POSICAO,posicao);
                startActivityForResult(iniciaFormularioNota, REQUEST_CODE_ALTERA_NOTA);
            }
        });
    }

    private void configuraAdapter(List<Nota> todasAsNotas, RecyclerView lista) {
        adapter = new NotasAdapter(this, todasAsNotas);
        lista.setAdapter(adapter);
        configuraItemTouchHelper(lista);

    }

    private void configuraItemTouchHelper(RecyclerView lista) {
        ItemTouchHelper touchHelper = new ItemTouchHelper(new NotasItemTouchHelper(adapter));
        touchHelper.attachToRecyclerView(lista);
    }


}
