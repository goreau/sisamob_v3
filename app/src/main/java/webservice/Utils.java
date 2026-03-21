package webservice;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entidades.Area;
import entidades.Area_nav;
import entidades.Atividade;
import entidades.Cadastro_edl;
import entidades.Censitario;
import entidades.GrupoRec;
import entidades.Imovel;
import entidades.Municipio;
import entidades.Ovitrampa;
import entidades.Produto;
import entidades.Quart_nav;
import entidades.Quarteirao;
import entidades.TipoRec;
import producao.Alado;
import producao.AladoIm;
import producao.Coordenadas;
import producao.Edl;
import producao.Recipiente;
import producao.VcFolha;
import producao.VcImovel;
import producao.VcOvitrampa;


public class Utils {
    static Context contexto;

    public Utils(Context ctx) {
        contexto = ctx;
    }

    public boolean limpar = true;

    public String getInformacao(String end){
        String json;
        String retorno;
        json = NetworkUtils.getJSONFromAPI(end);
        Log.i("Resultado", json);

        retorno = parseJson(json);

        return retorno;
    }

    public String sendInformacao(String end, int tab){
        String json;
        String retorno;
        json = NetworkUtils.getJSONFromAPI(end);
        Log.i("Resultado", json);

        retorno = parseRetorno(json, tab);

        return retorno;
    }

    private String parseJson(String json){
        int quant = 0;
        int linhas = 0;
        int inseridos = 0;
        String tabela = "";
        String resultado = "";
        try {
            if (json==""){
                resultado = "Erro recebendo os registros do servidor";
            } else {
                JSONObject retorno = new JSONObject(json);

                boolean success = retorno.getBoolean("success");

                if (!success){
                    return "Erro de comunicação com o servidor de dados.";
                }
                // pega o inteiro "records"
                int records = retorno.getInt("records");

                if (records == 0){
                    return "Nenhum registro a importar.";
                }

                JSONObject dados = retorno.getJSONObject("result");

                JSONArray tabelas = dados.names();
                quant = tabelas.length();
                //  Log.w("tabelas",""+dados.names());
                for (int j = 0; j < quant; j++) {
                    tabela = tabelas.getString(j); //nome da tabela
                    JSONArray objetos = dados.getJSONArray(tabela);//array da tabela do banco (municipio, area,..)
             
                    linhas = objetos.length(); //registros da tabela
                    if (linhas == 0) continue;
                    //   Log.w("Registros",""+linhas);
                    JSONArray names = objetos.getJSONObject(0).names(); //nomes dos campos
                    //   Log.w("Campos",""+names);
                    int fields = names.length(); //quantidade de campos
                    String[] campos = new String[fields];
                    String[] valores = new String[fields];

                    for (int x = 0; x < linhas; x++) {
                        for (int i = 0; i < fields; i++) {
                            campos[i] = names.getString(i);
                            valores[i] = objetos.getJSONObject(x).getString(names.getString(i));
                        }
                        if (tabela.equals("municipio")) {
                            Municipio mun = new Municipio();
                            if (inseridos == 0 && limpar) mun.limpar();
                            if (mun.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("area_nav")) {
                          /*  Area_nav area = new Area_nav();

                            if (inseridos == 0 && limpar) area.limpar();
                            if (area.insere(campos, valores))
                                inseridos++;*/
                            Area_nav area = new Area_nav();

                            // procura se existe campo "quart_nav"
                            int idxQuart = -1;
                            for (int n = 0; n < campos.length; n++) {
                                if ("quart_nav".equals(campos[n])) {
                                    idxQuart = n;
                                    break;
                                }
                            }
                            String[] camposIn = new String[campos.length -1];
                            String[] valoresIn = new String[campos.length -1];

                            JSONArray quartNavArray = null;
                            if (idxQuart != -1) {
                                try {
                                    quartNavArray = new JSONArray(valores[idxQuart]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // remove quart_nav dos arrays
                                camposIn = removeIndex(campos, idxQuart);
                                valoresIn = removeIndex(valores, idxQuart);
                            }

                            // insere area_nav
                            if (inseridos == 0 && limpar) area.limpar();
                            if (area.insere(camposIn, valoresIn)) {
                                inseridos++;
                            }

                            // insere quart_navs se existir
                            if (quartNavArray != null) {
                                for (int m = 0; m < quartNavArray.length(); m++) {
                                    JSONObject qObj = quartNavArray.getJSONObject(m);

                                    String[] camposQ = jsonKeys(qObj);
                                    String[] valoresQ = jsonValues(qObj);

                                    Quart_nav quart = new Quart_nav();
                                    if (inseridos == 0 && limpar) quart.limpar();
                                    quart.insere(camposQ, valoresQ);
                                }
                            }


                        } else if (tabela.equals("quart_nav")) {
                            Quart_nav quart = new Quart_nav();
                            if (inseridos == 0 && limpar) quart.limpar();
                            if (quart.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("area")) {
                            Area area = new Area();
                            if (inseridos == 0 && limpar) area.limpar();
                            if (area.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("censitario")) {
                            Censitario cens = new Censitario();
                            if (inseridos == 0 && limpar) cens.limpar();
                            if (cens.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("quarteirao")) {
                            Quarteirao quart = new Quarteirao();
                            if (inseridos == 0 && limpar) quart.limpar();
                            if (quart.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("imovel")) {
                            Imovel im = new Imovel();
                            if (inseridos == 0 && limpar) im.limpar();
                            if (im.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("edl")) {
                            Cadastro_edl im = new Cadastro_edl();
                            if (inseridos == 0 && limpar) im.limpar();
                            if (im.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("ovitrampa")) {
                            Ovitrampa ovi = new Ovitrampa();
                            if (inseridos == 0 && limpar) ovi.limpar();
                            if (ovi.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("atividade")) {
                            Atividade cad = new Atividade();
                            if (inseridos == 0) cad.limpar();
                            // String[] camposat = new String[campos.length+1];
                            // String[] valoresat = new String[valores.length+1];
                            // camposat[campos.length] = "";
                            //    valoresat[valores.length] = "";
                            if (cad.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("grupo_rec")) {
                            GrupoRec cad = new GrupoRec();
                            if (inseridos == 0) cad.limpar();
                            if (cad.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("tipo_rec")) {
                            TipoRec cad = new TipoRec();
                            if (inseridos == 0) cad.limpar();
                            if (cad.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("municipio")) {
                            Municipio cad = new Municipio();
                            if (inseridos == 0) cad.limpar();
                            if (cad.insere(campos, valores))
                                inseridos++;
                        } else if (tabela.equals("produto")) {
                            Produto cad = new Produto();
                            if (inseridos == 0) cad.limpar();
                            if (cad.insere(campos, valores))
                                inseridos++;
                        }
                    }
                    resultado += "  -" + tabela + ": " + inseridos;
                    if (inseridos > 1)
                        resultado += " registros\n";
                    else
                        resultado += " registro\n";
                    inseridos = 0;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            inseridos = -1;
            resultado = e.getMessage();
        }
        return resultado;
    }

    private String[] removeIndex(String[] arr, int index) {
        String[] novo = new String[arr.length - 1];
        for (int i = 0, j = 0; i < arr.length; i++) {
            if (i != index) {
                novo[j++] = arr[i];
            }
        }
        return novo;
    }

    // transforma JSONObject em arrays paralelos
    private String[] jsonKeys(JSONObject obj) {
        Iterator<String> it = obj.keys();
        List<String> keys = new ArrayList<>();
        while (it.hasNext()) {
            keys.add(it.next());
        }
        return keys.toArray(new String[0]);
    }

    private String[] jsonValues(JSONObject obj) {
        Iterator<String> it = obj.keys();
        List<String> values = new ArrayList<>();
        while (it.hasNext()) {
            String key = it.next();
            values.add(obj.optString(key, ""));
        }
        return values.toArray(new String[0]);
    }


    //recebimento da resposta do envio (atualização do status)
    public String parseRetorno(String json, int tab){
        int quant = 0;
        int linhas = 0;
        int inseridos = 0;
        String tabela = "";
        String resultado = "";
      //  json = json.substring(4);
       // Log.i("tabela Parse ret: " , "- "+tab);
        try {
            JSONArray registros = new JSONArray(json);
            quant = registros.length();

            for (int j=0;j<quant;j++) {
                JSONObject obj = registros.getJSONObject(j);//array da tabela do banco (municipio, area,..)
                switch (tab){
                    case 1:
                        VcFolha folha = new VcFolha(0);
                        tabela = "Visitas a Imóveis ";
                        folha.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                        break;
                    case 2:
                        VcImovel im = new VcImovel(0);
                        tabela = "Imóveis Cadastrados ";
                        im.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                        break;
                    case 3:
                        VcOvitrampa ovi = new VcOvitrampa(0);
                        tabela = "Ovitrampas ";
                        ovi.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                        break;
                    case 4:
                        Coordenadas coord = new Coordenadas(0);
                        tabela = "Coordenadas ";
                        coord.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                        break;
                    case 5:
                        Alado al = new Alado(0);
                        tabela = "Alado (Pré e Pós)";
                        al.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                        break;
                    case 6:
                        Edl alim = new Edl(0);
                        tabela = "EDL";
                        alim.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                        break;
                    default:
                        Recipiente rec = new Recipiente(0);
                        rec.atualizaStatus(obj.get("id").toString(), obj.get("status").toString());
                }

                inseridos += Integer.parseInt(obj.get("status").toString());
            }
            resultado += "  -" + tabela + ": " + inseridos;
            if (inseridos>1)
                resultado += " registros\n";
            else
                resultado += " registro\n";
        } catch (JSONException e) {
            e.printStackTrace();
            inseridos = -1;
            resultado = json;//e.getMessage();
        }
        return resultado;
    }
}
