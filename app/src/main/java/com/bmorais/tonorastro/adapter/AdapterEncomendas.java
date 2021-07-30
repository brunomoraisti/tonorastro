package com.bmorais.tonorastro.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bmorais.tonorastro.AndamentoObjetoActivity;
import com.bmorais.tonorastro.PrincipalActivity;
import com.bmorais.tonorastro.R;
import com.bmorais.tonorastro.model.EncomendasModel;
import com.bmorais.tonorastro.pages.EntreguesPage;
import com.bmorais.tonorastro.pages.PendentesPage;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class AdapterEncomendas extends RecyclerView.Adapter<AdapterEncomendas.ViewHolder> {
    private ArrayList<EncomendasModel> mDataset;
    private Context context;
    public View view;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvtitulo;
        public TextView tvsubtitulo;
        public TextView tvAndamento;
        public ImageView imgIcone;
        public LinearLayout itemObjeto;

        private int position;

        private EncomendasModel encomendasModel;

        public ViewHolder(View v) {
            super(v);
            view = v;
            tvtitulo = (TextView) v.findViewById(R.id.tvItemEmergenciaTitulo);
            tvsubtitulo = (TextView) v.findViewById(R.id.tvItemEmergenciaSubtitulo);
            tvAndamento = v.findViewById(R.id.tvItemAndamento);
            imgIcone = (ImageView) v.findViewById(R.id.iconeEmergencia);
            itemObjeto = v.findViewById(R.id.linearLayoutItemObjeto);

            //EVENTO ONCLICK
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.v("### LOG: ", String.valueOf(mDataset.get(position).getSituacao()));
                    if (mDataset.get(position).getSituacao() != 2) {
                        Intent intent = new Intent(context, AndamentoObjetoActivity.class);
                        intent.putExtra("nome", mDataset.get(position).getNome());
                        intent.putExtra("codencomenda", mDataset.get(position).getCodencomenda());
                        context.startActivity(intent);
                    } else {
                        Snackbar.make(view, "Esse objeto não possui andamentos", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }

                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(((PrincipalActivity) context));
                    builder.setTitle("Atenção!");
                    builder.setMessage("Deseja realmente remover o objeto?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EncomendasModel.excluirEncomenda(mDataset.get(position).getCodencomenda(), ((PrincipalActivity) context));
                            remove(mDataset.get(position));
                        }
                    });
                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    AlertDialog dialog = builder.create();
                    builder.show();
                    return true;
                }
            });

        }
    }

    public void add(int position, EncomendasModel item) {
        mDataset.add(position, item);
        notifyItemChanged(mDataset.size() - 1);
    }

    public void add(EncomendasModel ve) {
        mDataset.add(ve);
    }

    public void addAll(ArrayList<EncomendasModel> veList) {
        for (EncomendasModel ve : veList) {
            this.add(ve);
        }
        notifyDataSetChanged();
    }

    public void remove(final EncomendasModel item, View viewItem) {
        final int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);

    }

    public void remove(EncomendasModel ve) {
        int position = mDataset.indexOf(ve);
        if (position > -1) {
            mDataset.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        final int size = mDataset.size();
        if (size > 0) {
            mDataset.clear();
            notifyItemRangeRemoved(0, size);
        }

    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        add(new EncomendasModel());
    }

    public EncomendasModel getItem(int position) {
        return mDataset.get(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterEncomendas(Context context2) {
        this.context = context2;
        this.mDataset = new ArrayList<EncomendasModel>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_objeto, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.position = position;
        holder.encomendasModel = mDataset.get(position);

        if (mDataset.get(position).getAndamentos().size()>0) {
            holder.tvAndamento.setText(mDataset.get(position).getAndamentos().get(0).getAction());

            if (mDataset.get(position).getAndamentos().get(0).getAction().contains("Objeto postado")) {
                holder.imgIcone.setImageResource(R.drawable.ic_postado);
            } else if (mDataset.get(position).getAndamentos().get(0).getAction().contains("Objeto em trânsito")) {
                holder.imgIcone.setImageResource(R.drawable.ic_encaminhado);
            } else if (mDataset.get(position).getAndamentos().get(0).getAction().contains("saiu para entrega")) {
                holder.imgIcone.setImageResource(R.drawable.ic_saiuentrega);
            } else if (mDataset.get(position).getAndamentos().get(0).getAction().contains("Objeto entregue")) {
                holder.imgIcone.setImageResource(R.drawable.ic_entrega);
            } else {
                holder.imgIcone.setImageResource(R.drawable.ic_diverso);
            }
        } else {
            holder.tvAndamento.setText("Objeto não teve andamento");
            holder.imgIcone.setImageResource(R.drawable.ic_nao_encontrado);
        }

        if (mDataset.get(position).getLeitura() == 0) {
            holder.itemObjeto.setBackgroundColor(view.getResources().getColor(R.color.cinza_muito_claro));
            holder.tvtitulo.setTypeface(null, Typeface.BOLD);
            holder.tvsubtitulo.setTypeface(null, Typeface.BOLD);
            holder.tvAndamento.setTypeface(null, Typeface.BOLD);

            holder.tvtitulo.setText(mDataset.get(position).getNome());
            holder.tvsubtitulo.setText(mDataset.get(position).getObjeto());
        } else {
            holder.itemObjeto.setBackgroundColor(view.getResources().getColor(R.color.branco));
            holder.tvtitulo.setTypeface(null, Typeface.NORMAL);
            holder.tvsubtitulo.setTypeface(null, Typeface.NORMAL);
            holder.tvAndamento.setTypeface(null, Typeface.NORMAL);

            holder.tvtitulo.setText(mDataset.get(position).getNome());
            holder.tvsubtitulo.setText(mDataset.get(position).getObjeto());
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setSearchResult(ArrayList<EncomendasModel> result) {
        mDataset = result;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_objeto;
        //return (position == mDataset.size()) ? R.layout.button_enviar_sugestao : R.layout.item_objeto;
    }

}
