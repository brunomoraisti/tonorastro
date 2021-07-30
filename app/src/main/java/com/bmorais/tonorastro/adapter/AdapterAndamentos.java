package com.bmorais.tonorastro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bmorais.tonorastro.R;
import com.bmorais.tonorastro.model.AndamentosModel;

import java.util.ArrayList;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class AdapterAndamentos extends RecyclerView.Adapter<AdapterAndamentos.ViewHolder>{
    private ArrayList<AndamentosModel> mDataset;
    private Context context;
    public View view;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvNumero;
        public TextView tvDescricao;
        public TextView tvData;
        public TextView tvAction;
        public TextView tvLocal;
        public ImageView imgAndamento;
        public Button btnChamaBombeiro;
        public ImageView imgIcone;

        private int position;

        private AndamentosModel andamentosModelBean;

        public ViewHolder(View v) {
            super(v);
            view = v;
            tvAction = (TextView) v.findViewById(R.id.tvItemAction);
            tvDescricao = (TextView) v.findViewById(R.id.tvItemMessage);
            tvData = (TextView) v.findViewById(R.id.tvItemData);
            tvLocal = (TextView) v.findViewById(R.id.tvItemLocal);

            imgAndamento = v.findViewById(R.id.iconeAndamento);

/*            //EVENTO ONCLICK
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PassosEmergenciaActivity.class);
                    intent.putExtra("TITULO",mDataset.get(position).getTitulo());
                    intent.putExtra("IMAGEM",mDataset.get(position).getImagem());
                    intent.putExtra("CODEMERGENCIA",mDataset.get(position).getCodemergencia());
                    context.startActivity(intent);

                }
            });*/
           /* btnChamaBombeiro.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void onClick(View v) {

                    final int PERMISSION_ALL = 1;
                    final String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};

                    if(!Funcoes.verificaPermissao((AndamentoObjetoActivity) context, Manifest.permission.CALL_PHONE)){
                        //ActivityCompat.requestPermissions((PassosEmergenciaActivity) context, PERMISSIONS, PERMISSION_ALL);
                        *//*if (!ActivityCompat.shouldShowRequestPermissionRationale((PassosEmergenciaActivity) context,Manifest.permission.CALL_PHONE)) {
                            Funcoes.showMessageOKCancel("Precisamos de permissão para efetuar a chamada!", (PassosEmergenciaActivity) context, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((PassosEmergenciaActivity) context, PERMISSIONS, PERMISSION_ALL);
                                }
                            });
                        }*//*
                        Toast.makeText(context, "Sem permissão para fazer chamada", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "193"));
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Toast.makeText(context, "Função de ligação está indisponível", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });*/

        }
    }

    public void add(int position, AndamentosModel item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void add(AndamentosModel ve) {
        mDataset.add(ve);
        notifyItemInserted(mDataset.size() - 1);
    }

    public void addAll(ArrayList<AndamentosModel> veList) {
        for (AndamentosModel ve : veList) {
            this.add(ve);
        }
        notifyDataSetChanged();
    }

    public void remove(final AndamentosModel item, View viewItem) {
        final int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);

    }

    public void remove(AndamentosModel ve) {
        int position = mDataset.indexOf(ve);
        if (position > -1) {
            mDataset.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        add(new AndamentosModel());
    }

    public AndamentosModel getItem(int position) {
        return mDataset.get(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterAndamentos(Context context2) {
        this.context = context2;
        this.mDataset = new ArrayList<AndamentosModel>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_andamento_objeto, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.position = position;
        //Armazena objeto Vaga de Emprego
        holder.andamentosModelBean = mDataset.get(position);
        final AndamentosModel andamentosModel = mDataset.get(position);

        holder.tvAction.setText(mDataset.get(position).getAction());
        holder.tvDescricao.setText(mDataset.get(position).getMessage());
        holder.tvData.setText(mDataset.get(position).getDataandamento());
        holder.tvLocal.setText(mDataset.get(position).getLocal());

        if (mDataset.get(position).getAction().contains("Objeto postado")) {
            holder.imgAndamento.setImageResource(R.drawable.ic_postado);
        } else if (mDataset.get(position).getAction().contains("Objeto em trânsito")){
            holder.imgAndamento.setImageResource(R.drawable.ic_encaminhado);
        } else if (mDataset.get(position).getAction().contains("saiu para entrega")){
            holder.imgAndamento.setImageResource(R.drawable.ic_saiuentrega);
        } else if (mDataset.get(position).getAction().contains("Objeto entregue")){
            holder.imgAndamento.setImageResource(R.drawable.ic_entrega);
        } else {
            holder.imgAndamento.setImageResource(R.drawable.ic_diverso);
        }


        /*if (mDataset.get(position).getCodandamento()==1)
            holder.btnChamaBombeiro.setVisibility(View.VISIBLE);*/

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
