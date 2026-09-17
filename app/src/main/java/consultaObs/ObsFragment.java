package consultaObs;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

//import android.app.Fragment;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.sucen.sisamobii.OvitrampaFragment;
import com.sucen.sisamobii.PrincipalActivity;
import com.sucen.sisamobii.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utilitarios.GerenciarBanco;

public class ObsFragment extends Fragment {

    Context context;
    GerenciarBanco db;

    ExpandableListView listView;

    public ObsFragment() {
        // Required empty public constructor
    }

    private OvitrampaFragment.OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = PrincipalActivity.sisamobContext;
        db = new GerenciarBanco(context);

        // Creating view correspoding to the fragment
        View v = inflater.inflate(R.layout.main_consulta_obs, container, false);

        listView = (ExpandableListView) v.findViewById(R.id.myExpandableList);

        carregarDadosExpansiveis();
        // Inflate the layout for this fragment
        return v;
    }

    public void carregarDadosExpansiveis() {
        List<String> headerList = new ArrayList<>();
        HashMap<String, List<String>> childList = new HashMap<>();
        Cursor cursor;

        String sqlx = "SELECT  v.dt_cadastro, (trim(q.numero_quarteirao)|| ' - ' || trim(q.sub_numero)) as quart," +
                "a.nome as ativ, " +
                "(case id_situacao when 1 then 'T' when 2 then 'F' when 3 then 'D' when 4 then 'Tp' when 5 then 'P' else 'R' end) as sit, v.obs, v._id, imovel, casa " +
                "FROM vc_folha v join municipio m using(id_municipio) join quarteirao q using(id_quarteirao) " +
                "join atividade a using(id_atividade) ORDER BY v.dt_cadastro, ativ, quart, imovel";

        // Importante: Ordene por atividade e quadra para o agrupamento funcionar
        cursor = db.getWritableDatabase().rawQuery(sqlx, null);

        if (cursor.moveToFirst()) {
            int idxData = cursor.getColumnIndex("dt_cadastro");
            int idxAtiv = cursor.getColumnIndex("ativ");
            int idxQuadra = cursor.getColumnIndex("quart");
            int idxCasa = cursor.getColumnIndex("casa");
            int idxImovel = cursor.getColumnIndex("imovel");
            int idxSit = cursor.getColumnIndex("sit");
            int idxObs = cursor.getColumnIndex("obs");

            // Validamos se todas as colunas foram encontradas (índice >= 0)
            if (idxData != -1 && idxQuadra != -1 && idxCasa != -1 && idxObs != -1) {
                do {
                    String data = cursor.getString(idxData);
                    String ativ = cursor.getString(idxAtiv);
                    String quadra = cursor.getString(idxQuadra);
                    String casa = cursor.getString(idxCasa);
                    String obs = cursor.getString(idxObs);
                    String imovel = cursor.getString(idxImovel);
                    String sit = cursor.getString(idxSit);

                    String header = "Data: " + data + "\nAtividade: " + ativ + "\nQuadra: " + quadra;
                    String detalhe = "Imóvel: " + imovel + " Casa: " + casa + "(" + sit + ") \nObs: " + (obs == null ? "" : obs);

                    if (!headerList.contains(header)) {
                        headerList.add(header);
                        childList.put(header, new ArrayList<String>());
                    }
                    childList.get(header).add(detalhe);

                } while (cursor.moveToNext());
            } else {
                Log.e("SQL_ERROR", "Uma ou mais colunas não foram encontradas no banco.");
            }

        }
        cursor.close();

        // Agora é só passar para o Adapter
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(context, headerList, childList);
        listView.setAdapter(adapter);


// FORÇAR ATUALIZAÇÃO VISUAL
        listView.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                listView.invalidateViews();
                Log.d("SUCESSO", "Total de Grupos visíveis: " + listView.getCount());
            }
        });
    }

    public interface OnFragmentInteractionListener {
    }
}