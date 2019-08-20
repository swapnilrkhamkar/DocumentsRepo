package com.assignment.documentsrepo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;

public class DocumentsListAdapter extends RecyclerView.Adapter<DocumentsListAdapter.MyViewHolder> {

    private Context context;
    private DocumentsListAdapter.CardClickListener cardClickListener;
    private ArrayList<DocumentsModel> documentsModelArrayList;

    public DocumentsListAdapter(Context context, CardClickListener cardClickListener, ArrayList<DocumentsModel> documentsModelArrayList) {
        this.context = context;
        this.cardClickListener = cardClickListener;
        this.documentsModelArrayList = documentsModelArrayList;
    }

    @NonNull
    @Override
    public DocumentsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lay_doc, parent, false);

        return new DocumentsListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentsListAdapter.MyViewHolder holder, int position) {

        holder.setData(position, documentsModelArrayList.get(position));
    }


    @Override
    public int getItemCount() {
        return documentsModelArrayList.size();
    }

    public interface CardClickListener {
        void onCardClicked(View view, DocumentsModel documentsModel);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgThumbnail, imgMore;
        private TextView txtFileName, txtFileSize;
        private int position;
        private DocumentsModel documentsModel;
        private MaterialCardView cardViewDocList1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            imgMore = itemView.findViewById(R.id.imgMore);
            txtFileName = itemView.findViewById(R.id.txtFileName);
            txtFileSize = itemView.findViewById(R.id.txtFileSize);
            cardViewDocList1 = itemView.findViewById(R.id.cardViewDocList1);
            cardViewDocList1.setOnClickListener(this);
            imgMore.setOnClickListener(this);
        }

        void setData(int position, DocumentsModel documentsModel) {

            this.position = position;
            this.documentsModel = documentsModel;

            Log.e("MODELL::", " " + documentsModel);
            txtFileName.setText(documentsModel.getDoc_name());
            txtFileSize.setText("File Size " + documentsModel.getDoc_size());

            Glide.with(context).load(documentsModel.getDoc_url()).into(imgThumbnail);

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.cardViewDocList1 :
                    if (cardClickListener != null) {
                        Log.e("ONCLICK ", " CARD");
                        cardClickListener.onCardClicked(view, documentsModel);
                        //notifyDataSetChanged();

                    }
                    break;

                case R.id.imgMore :
                    if (cardClickListener != null) {
                        Log.e("ONCLICK ", " CARD");
                        cardClickListener.onCardClicked(view, documentsModel);
                        //notifyDataSetChanged();

                    }
                    break;

            }

        }
    }
}
