package siacorp.agendacontato.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;
import android.widget.*;
import siacorp.agendacontato.R;


import java.util.Date;

import siacorp.agendacontato.ContatoArrayAdapter;
import siacorp.agendacontato.dominio.entidades.Contato;


public class RepositorioContato {

    private SQLiteDatabase conn;


    public RepositorioContato(SQLiteDatabase conn)
    {

        this.conn = conn;

    }

    private ContentValues preencherContentValues (Contato contato)
    {

        ContentValues values = new ContentValues();

        values.put(Contato.NOME, contato.getNome());
        values.put(Contato.TAG, contato.getTag());

        return values;

    }

    public void excluir (long id)
    {

        conn.delete(Contato.TABELA,"_id = ?", new String[] {String.valueOf(id) }  );

    }

    public void alterar (Contato contato)
    {
        ContentValues values = preencherContentValues(contato);

        conn.update(Contato.TABELA, values, "_id = ?", new String[] {String.valueOf(contato.getId()) } );



    }

    public void inserir (Contato contato)
    {
        ContentValues values = preencherContentValues(contato);

        conn.insertOrThrow(Contato.TABELA, null, values);

            }


    public ContatoArrayAdapter buscaContatos(Context context)
    {

        ContatoArrayAdapter adpContatos = new ContatoArrayAdapter(context, R.layout.item_contato);

        Cursor cursor = conn.query(Contato.TABELA, null, null, null, null, null, null);

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();

            do {

                Contato contato = new Contato();
                contato.setId(cursor.getLong(cursor.getColumnIndex(Contato.ID)));
                contato.setNome(cursor.getString(cursor.getColumnIndex(Contato.NOME)));
                contato.setTag(cursor.getString(cursor.getColumnIndex(Contato.TAG)));

                adpContatos.add(contato);
            }while (cursor.moveToNext());


        }

        return adpContatos;

    }
}
