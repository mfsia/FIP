package siacorp.agendacontato.dominio.entidades;


import java.io.Serializable;
import java.util.Date;

public class Contato implements Serializable{

    public static String TABELA = "CONTATO";
    public static String ID = "_id";
    public static String NOME = "NOME";
    public static String TAG = "TAG";

    private long id;
    private String nome;
    private String tag;

    public Contato()
    {

        id=0;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString()
    {
        return nome + "  " + tag;

    }
}
