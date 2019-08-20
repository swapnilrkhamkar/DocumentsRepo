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

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.MyViewHolder> {

    private Context context;
    private RepoAdapter.CardClickListener cardClickListener;
    private ArrayList<DocumentCategoriesModel> documentCategoriesModelArrayList;

    public RepoAdapter(Context context, CardClickListener cardClickListener, ArrayList<DocumentCategoriesModel> documentCategoriesModelArrayList) {
        this.context = context;
        this.cardClickListener = cardClickListener;
        this.documentCategoriesModelArrayList = documentCategoriesModelArrayList;
    }

    @NonNull
    @Override
    public RepoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_repo, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoAdapter.MyViewHolder holder, int position) {

        holder.setData(position, documentCategoriesModelArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return documentCategoriesModelArrayList.size();
    }

    public interface CardClickListener {
        void onCardClicked(DocumentCategoriesModel documentCategoriesModel);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgRepoBg, imgRepoIc;
        private TextView txtRepoName, txtRepoCount;
        private int position;
        private DocumentCategoriesModel documentCategoriesModel;
        private MaterialCardView cardViewDocList;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgRepoBg = itemView.findViewById(R.id.imgRepoBg);
            imgRepoIc = itemView.findViewById(R.id.imgRepoIc);
            txtRepoName = itemView.findViewById(R.id.txtRepoName);
            txtRepoCount = itemView.findViewById(R.id.txtRepoCount);
            cardViewDocList = itemView.findViewById(R.id.cardViewDocList);
            cardViewDocList.setOnClickListener(this);
        }

        void setData(int position, DocumentCategoriesModel documentCategoriesModel) {

            this.position = position;
            this.documentCategoriesModel = documentCategoriesModel;

            txtRepoName.setText(documentCategoriesModel.getCat_name());
            txtRepoCount.setText("(" + documentCategoriesModel.getNum_docs()+ ")");

            switch (documentCategoriesModel.getCat_name()){

                case "Forms" :
                    Glide.with(context).load(R.drawable.form_bg).into(imgRepoBg);
                    Glide.with(context).load(R.drawable.forms_ic).into(imgRepoIc);
                    break;

                case "Images" :
                    Glide.with(context).load(R.drawable.images_bg).into(imgRepoBg);
                    Glide.with(context).load(R.drawable.images_ic).into(imgRepoIc);
                    break;

                case "My Documents" :
                    Glide.with(context).load(R.drawable.mydocuments_bg).into(imgRepoBg);
                    Glide.with(context).load(R.drawable.mydocuments_ic).into(imgRepoIc);
                    break;

                case "Bank Statement" :
                    Glide.with(context).load(R.drawable.bankstatement_bg).into(imgRepoBg);
                    Glide.with(context).load(R.drawable.bankstatement_ic).into(imgRepoIc);
                    break;

                case "Other Documents" :
                    Glide.with(context).load(R.drawable.otherdocuments_bg).into(imgRepoBg);
                    Glide.with(context).load(R.drawable.otherdocuments_ic).into(imgRepoIc);
                    break;
            }

        }

        @Override
        public void onClick(View view) {

            if (cardClickListener != null) {
                Log.e("ONCLICK ", " CARD");
                cardClickListener.onCardClicked(documentCategoriesModel);

            }

        }
    }
}
