package producao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sucen.sisamobii.PrincipalActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utilitarios.GerenciarBanco;
import utilitarios.MyToast;

public class Edl {
    long _id;
    String dt_cadastro;
    int id_cadastro_edl;
    int id_execucao;
    int id_situacao;
    int id_municipio;
    int id_nivel;
    int larvas;
    int pupas;
    String observacao;
    String agente;
    String ocorrencias;
    int ordem;
    int status;
    String dt_insere;

    MyToast toast;
    Context context;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }
    public String getDt_cadastro() {
        return dt_cadastro;
    }

    public void setDt_cadastro(String dt_cadastro) {
        this.dt_cadastro = dt_cadastro;
    }

    public int getId_cadastro_edl() {
        return id_cadastro_edl;
    }

    public void setId_cadastro_edl(int id_cadastro_edl) {
        this.id_cadastro_edl = id_cadastro_edl;
    }

    public int getId_execucao() {
        return id_execucao;
    }

    public void setId_execucao(int id_execucao) {
        this.id_execucao = id_execucao;
    }

    public int getId_situacao() {
        return id_situacao;
    }

    public void setId_situacao(int id_situacao) {
        this.id_situacao = id_situacao;
    }

    public int getId_nivel() {
        return id_nivel;
    }

    public void setId_nivel(int id_nivel) {
        this.id_nivel = id_nivel;
    }

    public int getLarvas() {
        return larvas;
    }

    public void setLarvas(int larvas) {
        this.larvas = larvas;
    }

    public int getPupas() {
        return pupas;
    }

    public void setPupas(int pupas) {
        this.pupas = pupas;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    public String getOcorrencias() {
        return ocorrencias;
    }

    public void setOcorrencias(String ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int STATUS) {
        this.status = STATUS;
    }

    public int getId_municipio() {
        return id_municipio;
    }

    public void setId_municipio(int id_municipio) {
        this.id_municipio = id_municipio;
    }

    public String getDt_insere() {
        return dt_insere;
    }

    public void setDt_insere(String dt_insere) {
        this.dt_insere = dt_insere;
    }


    public Edl(long _id) {
        super();
        this.context = PrincipalActivity.sisamobContext;
        this._id = _id;
        if (_id>0){
            popula();
        }
    }


    public void popula(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        String selectQuery = "SELECT dt_cadastro, id_cadastro_edl, id_execucao, id_situacao, id_nivel, larvas, pupas, " +
                "observacao, agente, ocorrencias,  ordem, status, id_municipio " +
                "FROM edl v where _id=" + this._id;

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            this.dt_cadastro 	= cursor.getString(0);
            this.id_cadastro_edl= cursor.getInt(1);
            this.id_execucao	= cursor.getInt(2);
            this.id_situacao 	= cursor.getInt(3);
            this.id_nivel	    = cursor.getInt(4);
            this.larvas 		= cursor.getInt(5);
            this.pupas 	        = cursor.getInt(6);
            this.observacao 	= cursor.getString(7);
            this.agente 		= cursor.getString(8);
            this.ocorrencias	= cursor.getString(9);
            this.ordem 		    = cursor.getInt(10);
            this.status 		= cursor.getInt(11);
            this.id_municipio   = cursor.getInt(12);
        }
        db.close();

    }

    public boolean manipula() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        try {
            ContentValues valores = new ContentValues();
            valores.put("dt_cadastro", this.dt_cadastro);
            valores.put("id_cadastro_edl", this.id_cadastro_edl);
            valores.put("id_execucao", this.id_execucao);
            valores.put("id_situacao", this.id_situacao);
            valores.put("id_nivel", this.id_nivel);
            valores.put("larvas", this.larvas);
            valores.put("pupas", this.pupas);
            valores.put("observacao", this.observacao);
            valores.put("agente", this.agente);
            valores.put("status", this.status);
            valores.put("ocorrencias", this.ocorrencias);
            valores.put("ordem", this.ordem);
            valores.put("id_municipio", this.id_municipio);

            if (this._id > 0) {
                String[] args = { Long.toString(this._id) };
                db.getWritableDatabase().update("edl", valores, "_id=?",
                        args);
            } else {
                this._id = db.getWritableDatabase().insert("edl", null,
                        valores);
            }
            return true;
        } catch (SQLException e) {
            toast = new MyToast(context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public boolean delete() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        try {
            String[] args = { Long.toString(this._id) };
            db.getWritableDatabase().delete("edl", "_id=?", args);
            return true;
        } catch (SQLException e) {
            toast = new MyToast(context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public ArrayList<HashMap<String, String>> getAllEdls() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT v._id as id, i.endereco as texto FROM edl v join cadastro_edl i on v.id_cadastro_edl=i.id_cadastro_edl";

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("texto", cursor.getString(1));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return wordList;
    }

    /**
     * Compose JSON out of SQLite records
     *
     * @return
     */
    public String composeJSONfromSQLite() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);

        ArrayList<HashMap<String, Object>> wordList;
        wordList = new ArrayList<HashMap<String, Object>>();
        String selectQuery = "SELECT  _id, dt_cadastro, id_cadastro_edl, id_execucao, id_situacao, "
                + "id_nivel, larvas, pupas, observacao, agente, status, ocorrencias, "
                + "ordem, datetime(dt_insere,'localtime'), id_municipio FROM edl where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("id_edl", cursor.getString(0));
                map.put("dt_cadastro", cursor.getString(1));
                map.put("id_cadastro_edl", cursor.getString(2));
                map.put("id_execucao", cursor.getString(3));
                map.put("id_situacao", cursor.getString(4));
                map.put("id_nivel", cursor.getString(5));
                map.put("larvas", cursor.getString(6));
                map.put("pupas", cursor.getString(7));
                map.put("observacao", cursor.getString(8));
                String original = cursor.getString(9);

                if (original != null) {
                    // 1. Substitui espaços
                    String processed = original.replace(" ", "_");

                    // 2. Limita o endIndex ao tamanho mínimo entre o tamanho da string e 30
                    int maxLength = Math.min(processed.length(), 30);

                    // 3. Aplica o substring de forma segura
                    map.put("agente", processed.substring(0, maxLength));
                } else {
                    // Lida com o caso nulo
                    map.put("agente", "N/I");
                }
             //   map.put("agente", cursor.getString(9).replace(" ","_").substring(0,30));
                map.put("status", cursor.getString(10));
                map.put("ocorrencias", cursor.getString(11));
                map.put("ordem", cursor.getString(12));
                map.put("dt_insere", cursor.getString(13));
                map.put("id_municipio", cursor.getString(14));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        Gson gson = new GsonBuilder().create();
        // Use GSON to serialize Array List to JSON
        // System.out.println(wordList);
        return gson.toJson(wordList);
    }

    public int dbSyncCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM edl where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        count = cursor.getCount();
        db.close();
        return count;
    }

    public int dbCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM edl";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        count = cursor.getCount();
        db.close();
        return count;
    }

    public void atualizaStatus(String id, String status){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        String updateQuery = "Update edl set status = '"+ status +"' where _id="+"'"+ id +"'";
        //  Log.d("query",updateQuery);
        db.getWritableDatabase().execSQL(updateQuery);

        db.close();
    }

    public int Limpar(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int id = 0;
        int regs = 0;
        String sql = "DELETE FROM edl where status = 1";
        //   Log.d("query",sql);
        db.getWritableDatabase().execSQL(sql);
        db.close();
        return regs;
    }

    public int LimparTudo(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int id = 0;
        int regs = 0;
        String sql = "DELETE FROM edl";
        //  Log.d("query",sql);
        db.getWritableDatabase().execSQL(sql);
        db.close();
        return regs;
    }

    public List<RelatorioList> getList(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);

        String selectQuery = "select a.nome, a.pa,  a.tt from  (" +
                "SELECT 'EDL' as nome, ' T:(' || sum(case when id_situacao=1 then 1 else 0 end) || ') / F: (' || sum(case when id_situacao=2 then 1 else 0 end) ||') / R: (' || sum(case when id_situacao=3 then 1 else 0 end) || ')' || ' - Total: ' || count(v._id) || ' \n-> Im Lv: ' || count(distinct v._id)  as pa, count(v._id) as tt  FROM edl v join cadastro_edl i using(id_cadastro_edl)) a " +
                "group by a.nome";

        Cursor cursor = db.getReadableDatabase().rawQuery(selectQuery, null);
        List<RelatorioList> lista = new ArrayList<RelatorioList>();

        if (cursor.moveToFirst()) {
            do {
                RelatorioList list = new RelatorioList(cursor.getString(0),cursor.getString(1),cursor.getString(2));
                lista.add(list);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public boolean recupera(ContentValues[] dados){
        GerenciarBanco db = new GerenciarBanco(context);
        try{
            for (int i = 0; i<dados.length; i++){
                ContentValues valores = dados[i];
                this._id = db.getWritableDatabase().insert("edl", null, valores);
            }
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.close();
        }
    }
}
