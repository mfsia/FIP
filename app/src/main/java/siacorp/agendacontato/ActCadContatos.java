package siacorp.agendacontato;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import siacorp.agendacontato.app.MessageBox;
import siacorp.agendacontato.app.ViewHelper;
import siacorp.agendacontato.database.DataBase;
import siacorp.agendacontato.dominio.RepositorioContato;
import siacorp.agendacontato.dominio.entidades.Contato;
import siacorp.agendacontato.util.DateUtils;
import siacorp.agendacontato.Bluetooth.MainBluetoothActivity;
import siacorp.agendacontato.Bluetooth.ConnectionThread;
import siacorp.agendacontato.Bluetooth.DiscoveredDevices;
import siacorp.agendacontato.Bluetooth.PairedDevices;


public class ActCadContatos extends AppCompatActivity {

    private EditText edtNome;
    private TextView txtTag;


    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioContato repositorioContato;
    private Contato contato;

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    ConnectionThread connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_contatos);

        edtNome = (EditText)findViewById(R.id.edtNome);
        txtTag = (TextView)findViewById(R.id.txtTag);


        Bundle bundle = getIntent().getExtras();

        if ((bundle != null) && (bundle.containsKey(ActContato.PAR_CONTATO)))
        {
            contato = (Contato)bundle.getSerializable(ActContato.PAR_CONTATO);
            preencheDados();
        }
        else

        contato = new Contato();

        try {


            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();

            repositorioContato = new RepositorioContato(conn);

        } catch (SQLException ex) {
            MessageBox.show(this, "Erro", "Erro ao criar o banco: " + ex.getMessage());
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
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_act_cad_contatos, menu);

        if (contato.getId()!=0)
        menu.getItem(1).setVisible(true);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.mni_acao1:

                salvar();
                finish();


            break;

            case R.id.mni_acao2:

            excluir();
                finish();
            break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void preencheDados()
    {

        edtNome.setText(contato.getNome());
        txtTag.setText(contato.getTag());

    }

    private void excluir()
    {
        try {
            repositorioContato.excluir(contato.getId());
        }catch (Exception ex) {
            MessageBox.show(this, "Erro","Erro ao excluir o contato: " + ex.getMessage());
        }

    }

    private void salvar()
    {

        try
        {

            contato.setNome(edtNome.getText().toString());
            contato.setTag(txtTag.getText().toString());

            if (contato.getId()==0)
                repositorioContato.inserir(contato);
            else
                repositorioContato.alterar(contato);

        } catch (Exception ex) {
            MessageBox.show(this, "Erro","Erro ao salvar os dados: " + ex.getMessage());
        }

    }

    public void scanQR (View v)
    {

        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(ActCadContatos.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final AppCompatActivity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    //on ActivityResult method
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String contents = intent.getStringExtra("SCAN_RESULT");
                txtTag.setText(contents);
            }
        }
    }

    public void encontrar (View v)
    {
        TextView messageBox = (TextView) findViewById(R.id.txtTag);
        String messageBoxString = messageBox.getText().toString();
        byte[] data =  messageBoxString.getBytes();
        connect.write(data);
    }


}
