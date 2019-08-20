package com.assignment.documentsrepo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONObject;
import java.io.IOException;
import java.net.SocketException;
import java.util.Objects;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class RepoActivity extends AppCompatActivity implements RepoAdapter.CardClickListener {

    private RecyclerView repoRecyclerView;
    private RepoModel repoModel;
    private RecyclerView.LayoutManager layoutManager;
    private RepoAdapter repoAdapter;
    private RepoAdapter.CardClickListener cardClickListener;
    private FloatingActionButton fab;
    private DocumentCategoriesModel documentCategoriesModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);

        repoModel = null;
        cardClickListener = this;
        repoRecyclerView = findViewById(R.id.repoRecyclerView);
        repoRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(RepoActivity.this, RecyclerView.VERTICAL, false);
        repoRecyclerView.setLayoutManager(layoutManager);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(RepoActivity.this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                        .setTitle("Functionality not enabled")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("REPOSITORY");
        }

        loading();
        getRepo();

    }

    private void getRepo() {

        APIInterface apiService =
                APIClient.getClient().create(APIInterface.class);

        Single<RepoModel> call = apiService.getRepo();
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getRepoResponse());

    }

    public DisposableSingleObserver<RepoModel> getRepoResponse() {
        return new DisposableSingleObserver<RepoModel>() {
            @Override
            public void onSuccess(RepoModel value) {

                try {

                    if (progressDialog != null){
                        progressDialog.dismiss();
                    }

                    Log.e("Val ", " " + value);

                    repoModel = value;

                    repoAdapter = new RepoAdapter(RepoActivity.this, cardClickListener, value.getDocumentCategories());
                    repoRecyclerView.setAdapter(repoAdapter);
                    repoAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                }
            }

            @Override
            public void onError(Throwable e) {

                try {

                    if (progressDialog != null){
                        progressDialog.dismiss();
                    }

                    Log.e("ERROR", " " + e);

                    if (e instanceof UndeliverableException) {
                        e = e.getCause();
                        Log.w("Undeliverable excep rec", "not sure what to do", e);
                        return;
                    } else if ((e instanceof IOException) || (e instanceof SocketException)) {
                        Log.w("IOException", "SocketException", e);
                        // fine, irrelevant network problem or API that throws on cancellation
                        return;
                    } else if (e instanceof InterruptedException) {
                        Log.w("Undeliverable excep rec", "InterruptedException", e);
                        // fine, some blocking code was interrupted by a dispose call
                        return;
                    } else if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                        // that's likely a bug in the application
                        Thread.currentThread().getUncaughtExceptionHandler()
                                .uncaughtException(Thread.currentThread(), e);

                        Log.w("NullPointerException", "IllegalArgumentException", e);

                        return;
                    } else if (e instanceof IllegalStateException) {
                        // that's a bug in RxJava or in a custom operator
                        Thread.currentThread().getUncaughtExceptionHandler()
                                .uncaughtException(Thread.currentThread(), e);

                        Log.w("Undeliverable excep rec", "IllegalStateException", e);

                        return;
                    } else {
                        HttpException error = (HttpException) e;

                        Log.e("ERRORCODE", " " + error.code());

                        if (error.code() == 400) {
                            String errorBody = Objects.requireNonNull(error.response().errorBody()).string();
                            JSONObject jsonObject = new JSONObject(errorBody);
                            Log.e("HFGFB", "JSON" + jsonObject);
                            int errorCode = jsonObject.optInt("errorCode");

                            switch (errorCode) {
                            }

                        } else {

                        }

                    }

                } catch (Exception e1) {
                    //e1.printStackTrace();
                }

            }
        };
    }

    @Override
    public void onCardClicked(DocumentCategoriesModel documentCategoriesModel) {
        this.documentCategoriesModel = documentCategoriesModel;

        Log.e("onCardClicked", " kk " + String.valueOf(documentCategoriesModel));
        Intent intent = new Intent(RepoActivity.this, DocumentsListActivity.class);
        intent.putExtra("doc_model",documentCategoriesModel);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("ONRESUME", " 0");
        if (getSupportActionBar() != null) {
            // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("REPOSITORY");
        }
    }

    private void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);//you can cancel it by pressing back button
        progressDialog.setMessage("Loading ...");
        progressDialog.show();//displays the progress bar

    }
}
