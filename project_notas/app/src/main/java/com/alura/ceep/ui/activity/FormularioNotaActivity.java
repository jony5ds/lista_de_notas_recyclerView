package com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.alura.ceep.R;
import com.alura.ceep.model.Nota;

import static com.alura.ceep.ui.activity.Const.CHAVE_NOTA;
import static com.alura.ceep.ui.activity.Const.CHAVE_POSICAO;
import static com.alura.ceep.ui.activity.Const.POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity {

    public static final String INSERIR_NOTA_TITLE = "Inserir Nota";
    public static final String ALTERAR_NOTA_TITLE = "Alterar Nota";
    private int _posicao = POSICAO_INVALIDA;
    private TextView _notaDescricao;
    private TextView _notaTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
        setTitle(INSERIR_NOTA_TITLE);
        inicializaCampos();
        Intent dadosRecebidos = getIntent();
        if(dadosRecebidos.hasExtra(CHAVE_NOTA))
        {
            setTitle(ALTERAR_NOTA_TITLE);
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            _posicao = dadosRecebidos.getIntExtra(CHAVE_POSICAO,POSICAO_INVALIDA);
            preencheCampos(notaRecebida);
        }

    }

    private void preencheCampos(Nota notaRecebida) {

        _notaTitulo.setText(notaRecebida.getTitulo());
        _notaDescricao.setText(notaRecebida.getDescricao());
    }

    private void inicializaCampos() {
        _notaTitulo = findViewById(R.id.formulario_nota_titulo);
        _notaDescricao = findViewById(R.id.formulario_nota_descricao);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ehMenuSalvar(item))
        {
            Nota nota = criaNota();
            retornaNota(nota);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        Intent resultadoNota = new Intent();
        resultadoNota.putExtra(CHAVE_NOTA,nota);
        resultadoNota.putExtra(CHAVE_POSICAO,_posicao);
        setResult(Activity.RESULT_OK,resultadoNota);
    }

    private Nota criaNota() {

        return new Nota(_notaTitulo.getText().toString(),
                _notaDescricao.getText().toString());
    }

    private boolean ehMenuSalvar(MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_salvar;
    }
}
