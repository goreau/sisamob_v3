package utilitarios;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import webservice.Utils;

public class PostService {

    public interface OnServiceCallCompleted {
        void onServiceCallComplete(String result);
    }

    public static void enviarDados(
            final Context context,
            final String mTipo,
            final String mDados,
            final int mTab,
            final TextView tvResumo,
            final MyToast toast,
            final OnServiceCallCompleted listener
    ) {
        final String url = "https://sisapi.saude.sp.gov.br/api/recebe/dados";
        RequestQueue queue = Volley.newRequestQueue(context);

        // 1. Cria o corpo JSON
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("tipo", mTipo);
            jsonBody.put("dados", new JSONArray(mDados));
        } catch (JSONException e) {
            e.printStackTrace();

            if (toast != null) {
                toast.show("Erro ao formatar os dados JSON.");
            }
            return;
        }

        // 2. Requisição assíncrona do Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arrayRetorno = response.getJSONArray("retorno");

                            Utils util = new Utils(context);
                            String retorno = util.parseRetorno(arrayRetorno.toString(), mTab);

                            if (listener != null) {
                                listener.onServiceCallComplete(retorno);
                            }
                        } catch (JSONException | JsonParseException e) {
                            Log.e(mTipo, e.getMessage() != null ? e.getMessage() : "Erro de parsing");
                            if (tvResumo != null) {
                                tvResumo.append("\n" + e.getMessage());
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String mensagemErro = "Erro desconhecido";

                        if (error instanceof TimeoutError) {
                            mensagemErro = "O tempo de conexão expirou.";
                        } else if (error instanceof NoConnectionError) {
                            mensagemErro = "Sem conexão com a internet.";
                        } else if (error.networkResponse != null && error.networkResponse.data != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String body = new String(error.networkResponse.data);
                            mensagemErro = "Status: " + statusCode + " - " + body;
                        } else if (error.getMessage() != null) {
                            mensagemErro = error.getMessage();
                        }

                        if (tvResumo != null) {
                            tvResumo.append("\nErro: " + mensagemErro);
                        }
                        if (toast != null) {
                            toast.show("Verifique se os registros foram recebidos.");
                        }


                        Log.e("VOLLEY_ERROR", mensagemErro);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer \"07WesTIoBfxZTUQ7G9BVcRlX4cuVba5+JjVoW4szUUg=\"");
                return headers;
            }
        };

        // Adiciona à fila de execução do Volley
        queue.add(jsonRequest);
    }
}