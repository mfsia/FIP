package siacorp.agendacontato;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import android.content.*;

import android.database.sqlite.*;
import android.database.*;

import siacorp.agendacontato.Bluetooth.MainBluetoothActivity;
import siacorp.agendacontato.app.MessageBox;
import siacorp.agendacontato.database.DataBase;
import siacorp.agendacontato.dominio.RepositorioContato;
import siacorp.agendacontato.dominio.entidades.Contato;


public class ActContato extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ImageButton btnAdicionar;
    private EditText edtPesquisa;
    private ListView lstContatos;
    private ImageButton btnBluetooth;

    private ArrayAdapter<Contato> adpContatos;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioContato repositorioContato;

    private  FiltraDados filtraDados;

    public static final String PAR_CONTATO = "CONTATO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_contato);

        btnAdicionar = (ImageButton) findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(this);

        edtPesquisa = (EditText) findViewById(R.id.edtPesquisa);
        lstContatos = (ListView) findViewById(R.id.lstContatos);
        lstContatos.setOnItemClickListener(this);

        try {


            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();

            repositorioContato = new RepositorioContato(conn);

            adpContatos = repositorioContato.buscaContatos(this);

            lstContatos.setAdapter(adpContatos);

            filtraDados = new FiltraDados(adpContatos);
            edtPesquisa.addTextChangedListener(filtraDados);



        } catch (SQLException ex) {
            MessageBox.show(this, "Erro","Erro ao criar o banco: " + ex.getMessage());

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conn != null)
        {
            conn.close();
        }
    }


    @Override
    public void onClick(View v) {

        Intent it = new Intent(this, ActCadContatos.class);
        startActivityForResult(it, 0);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        adpContatos = repositorioContato.buscaContatos(this);

        filtraDados.setArrayAdapter(adpContatos);

        lstContatos.setAdapter(adpContatos);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Contato contato = adpContatos.getItem(position);

        Intent it = new Intent(this, ActCadContatos.class);

        it.putExtra(PAR_CONTATO, contato);


        startActivityForResult(it, 0);
    }

    private class FiltraDados implements TextWatcher
    {

        private ArrayAdapter<Contato> arrayAdapter;

        private FiltraDados (ArrayAdapter<Contato> arrayAdapter)
        {

            this.arrayAdapter = arrayAdapter;

        }

        public void setArrayAdapter(ArrayAdapter<Contato> arrayAdapter)
        {
            this.arrayAdapter = arrayAdapter;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            arrayAdapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public void bluetooth (View v)
    {

       Intent it = new Intent(this, MainBluetoothActivity.class);
       startActivityForResult(it, 0);
    }
}
