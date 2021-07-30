package com.bmorais.tonorastro.pages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bmorais.tonorastro.AndamentoObjetoActivity;
import com.bmorais.tonorastro.PrincipalActivity;
import com.bmorais.tonorastro.R;
import com.bmorais.tonorastro.lib.SharedPrefManager;
import com.bmorais.tonorastro.lib.Variaveis;
import com.bmorais.tonorastro.model.AndamentosModel;
import com.bmorais.tonorastro.service.MyWorkerManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

public class ConfigPage extends Fragment {

    public View view;
    private LinearLayout btnFaleConosco = null;
    private LinearLayout llRemoverPropaganda = null;
    private Button btnComprar = null;
    private LinearLayout lytIntervaloAtualizacao = null, btnPolitica = null, lytAvaliar = null, lytCompartilhar = null;
    public Switch swNotificacoes, swAtualizacao;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page_config, container, false);

        swNotificacoes = (Switch) view.findViewById(R.id.swNotificacoes);
        swAtualizacao = view.findViewById(R.id.swAtualizacao);
        btnFaleConosco = view.findViewById(R.id.lytContaFaleConosco);
        lytIntervaloAtualizacao = view.findViewById(R.id.lytIntervaloAtualizacao);
        lytAvaliar = view.findViewById(R.id.lytAvaliarConfig);
        lytCompartilhar = view.findViewById(R.id.lytCompartilharConfig);

        lytCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = "Acompanhe suas encomendas com o *Tonorastro - Rastreamento de Encomendas*, baixe agora: http://bit.ly/app-tonorastro\n";

                final Intent emailIntent = new Intent();
                emailIntent.setAction(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_TEXT, msg);
                emailIntent.setType("text/plain");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Selecione o local"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "Não possui aplicativo", Toast.LENGTH_SHORT).show();
                }

            }
        });

        lytAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.bmorais.tonorastro"));
                startActivity(myIntent);

                /*ReviewManager manager = ReviewManagerFactory.create(getActivity());
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // We can get the ReviewInfo object
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(getActivity(), reviewInfo);
                        flow.addOnCompleteListener(task2 -> {
                            //Toast.makeText(getActivity(), "Sucesso", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.bmorais.tonorastro"));
                        startActivity(myIntent);
                    }
                });*/
            }
        });

        lytIntervaloAtualizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedItem;
                String tempo = SharedPrefManager.getInstance(getActivity()).pegarCampo(Variaveis.ATUALIZACAO_MINUTOS_NOME);
                switch (tempo) {
                    case "15 minutos":
                        checkedItem = 0;
                        break;
                    case "30 minutos":
                        checkedItem = 1;
                        break;
                    case "1 hora":
                        checkedItem = 2;
                        break;
                    case "2 horas":
                        checkedItem = 3;
                        break;
                    case "4 horas":
                        checkedItem = 4;
                        break;
                    case "6 horas":
                        checkedItem = 5;
                        break;
                    case "12 horas":
                        checkedItem = 6;
                        break;
                    case "24 horas":
                        checkedItem = 7;
                        break;
                    default:
                        checkedItem = 2;
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Intervalo de atualização");
                String[] items = {"15 minutos","30 minutos","60 minutos","2 horas","4 horas","6 horas","12 horas","24 horas"};
                alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS_NOME, "15 minutos");
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS, "15");
                                MyWorkerManager.ativarWorkManager(getActivity(), 15, Variaveis.ATUALIZACAO);
                                break;
                            case 1:
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS_NOME, "30 minutos");
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS, "30");
                                MyWorkerManager.ativarWorkManager(getActivity(), 30, Variaveis.ATUALIZACAO);
                                break;
                            case 2:
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS_NOME, "1 hora");
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS, "60");
                                MyWorkerManager.ativarWorkManager(getActivity(), 60, Variaveis.ATUALIZACAO);
                                break;
                            case 3:
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS_NOME, "2 horas");
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS, "120");
                                MyWorkerManager.ativarWorkManager(getActivity(), 120, Variaveis.ATUALIZACAO);
                                break;
                            case 4:
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS_NOME, "4 horas");
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS, "240");
                                MyWorkerManager.ativarWorkManager(getActivity(), 240, Variaveis.ATUALIZACAO);
                                break;
                            case 5:
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS_NOME, "6 horas");
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS, "360");
                                MyWorkerManager.ativarWorkManager(getActivity(), 360, Variaveis.ATUALIZACAO);
                                break;
                            case 6:
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS_NOME, "12 horas");
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS, "720");
                                MyWorkerManager.ativarWorkManager(getActivity(), 720, Variaveis.ATUALIZACAO);
                                break;
                            case 7:
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS_NOME, "24 horas");
                                SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS, "1440");
                                MyWorkerManager.ativarWorkManager(getActivity(), 1440, Variaveis.ATUALIZACAO);
                                break;
                        }

                        dialog.dismiss();
                    }
                });
                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // cancelar
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
        });


        if (SharedPrefManager.getInstance(getActivity()).pegarCampo(Variaveis.NOTIFICACOES)==null || SharedPrefManager.getInstance(getActivity()).pegarCampo(Variaveis.NOTIFICACOES).equals("1")) {
            swNotificacoes.setChecked(true);
        } else {
            swNotificacoes.setChecked(false);
        }

        swNotificacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefManager.getInstance(getActivity()).pegarCampo(Variaveis.NOTIFICACOES)==null || SharedPrefManager.getInstance(getActivity()).pegarCampo(Variaveis.NOTIFICACOES).equals("1")) {
                    SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.NOTIFICACOES,"0");
                    Snackbar.make(view, "Notificações desativadas. :(", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.NOTIFICACOES,"1");
                    Snackbar.make(view, "Notificações ativadas. :)", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
            }
        });

        if (SharedPrefManager.getInstance(getActivity()).pegarCampo(Variaveis.ATUALIZACAO)==null || SharedPrefManager.getInstance(getActivity()).pegarCampo(Variaveis.ATUALIZACAO).equals("1")) {
            swAtualizacao.setChecked(true);
        } else {
            swAtualizacao.setChecked(false);
        }
        swAtualizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefManager.getInstance(getActivity()).pegarCampo(Variaveis.ATUALIZACAO)==null || SharedPrefManager.getInstance(getActivity()).pegarCampo(Variaveis.ATUALIZACAO).equals("1")) {
                    SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO,"0");
                    MyWorkerManager.cancelarWorkManager(getActivity(),Variaveis.ATUALIZACAO);
                    Snackbar.make(view, "Atualizações automática desativadas. :(", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    SharedPrefManager.getInstance(getActivity()).gravarCampo(Variaveis.ATUALIZACAO,"1");
                    MyWorkerManager.ativarWorkManager(getActivity(), Integer.parseInt(SharedPrefManager.getInstance(getActivity()).pegarCampo(Variaveis.ATUALIZACAO_MINUTOS)), Variaveis.ATUALIZACAO);
                    Snackbar.make(view, "Atualizações automáticas ativadas. :)", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
            }
        });

        btnFaleConosco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                /* Create the Intent */
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"contato@bmorais.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Fale Conosco - TONORASTRO");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

                /* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(emailIntent, "Enviar Email"));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
