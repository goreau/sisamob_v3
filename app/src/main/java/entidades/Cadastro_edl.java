package entidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.widget.Toast;

import com.sucen.sisamobii.PrincipalActivity;

import java.util.ArrayList;
import java.util.List;

import utilitarios.GerenciarBanco;
import utilitarios.MyToast;

public class Cadastro_edl {
    private int id_cadastro_edl;
    private int id_municipio;
    private String cadastro;
    private String endereco;
    public List<String> id_Im;
    private Context context;
    MyToast toast;

    public Cadastro_edl() {
        context = PrincipalActivity.getSisamobContext();
    }

    public Cadastro_edl(Context context) {
        super();
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }
    public Cadastro_edl(int id_cadastro_edl, int id_municipio, String cadasro, String endereco, Context context) {
        super();
        this.id_cadastro_edl = id_cadastro_edl;
        this.id_municipio = id_municipio;
        this.cadastro = cadastro;
        this.endereco = endereco;
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }

    public int getId_cadastro_edl() {
        return id_cadastro_edl;
    }
    public void setId_cadastro_edl(int id_cadastro_edl) {
        this.id_cadastro_edl = id_cadastro_edl;
    }
    public int getId_municipio() {
        return id_municipio;
    }
    public void setId_municipio(int id_municipio) {
        this.id_municipio = id_municipio;
    }
    public String getCadastro() {
        return cadastro;
    }
    public void setCadastro(String cadastro) {
        this.cadastro = cadastro;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void limpar(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        try{
            db.getWritableDatabase().delete("cadastro_edl", null, null);
        } catch (SQLException e) {
            toast.show(e.getMessage());
        }
    }

    public boolean insere(String[] campos, String[] val){
        GerenciarBanco db = new GerenciarBanco(this.context);

        try{
            ContentValues valores = new ContentValues();
            for (int i = 0; i<val.length;i++){
                valores.put(campos[i],val[i]);
            }
            db.getWritableDatabase().insert("cadastro_edl", null, valores);
            return true;
        } catch (SQLException e) {
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public List<String> combo(String id){
        List<String> im = new ArrayList<String>();
        id_Im = new ArrayList<String>();
        String sql = null;

        GerenciarBanco db = new GerenciarBanco(this.context);

            sql = "SELECT id_cadastro_edl, ' Cad: ' || trim(cadastro)|| ' - ' || trim(endereco) as codigo " +
                    "from cadastro_edl where id_municipio=? ORDER BY cadastro";

        Cursor cursor = db.getReadableDatabase().rawQuery(sql, new String[]{id});
        if(cursor.moveToFirst()){
            do {
                im.add(cursor.getString(1));
                id_Im.add(cursor.getString(0));
            } while (cursor.moveToNext());
        } else {
            im.add("Favor recarregar o conteudo");
        }
        cursor.close();
        db.close();
        return im;
    }
}
