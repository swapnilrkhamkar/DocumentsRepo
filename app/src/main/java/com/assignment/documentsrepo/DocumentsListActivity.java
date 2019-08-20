package com.assignment.documentsrepo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class DocumentsListActivity extends AppCompatActivity implements DocumentsListAdapter.CardClickListener {

    private DocumentCategoriesModel documentCategoriesModel;
    private DocListModel docListModel;
    private DocumentsListAdapter documentsListAdapter;
    private RecyclerView docsRecyclerView;
    private DocumentsListAdapter.CardClickListener cardClickListener;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fab;
    private ImageView imgSortAsc, imgSortDsc;
    private ProgressDialog progressDialog, pDialog;
    private DocumentsModel documentsModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_list);

        docListModel = null;
        cardClickListener = this;
        docsRecyclerView = findViewById(R.id.docsRecyclerView);
        fab = findViewById(R.id.fab);
        imgSortAsc = findViewById(R.id.imgSortAsc);
        imgSortDsc = findViewById(R.id.imgSortDsc);

        docsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(DocumentsListActivity.this, RecyclerView.VERTICAL, false);
        docsRecyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            documentCategoriesModel = (DocumentCategoriesModel) getIntent().getSerializableExtra("doc_model");
            Log.e("MDOELD D", " " + documentCategoriesModel);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(documentCategoriesModel.getCat_name());
        }

        loading();
        getDocList();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(DocumentsListActivity.this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                        .setTitle("Document uploads not allowed for this user")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        imgSortAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("CLIC", " cd ASC");
                imgSortAsc.setVisibility(View.GONE);
                imgSortDsc.setVisibility(View.VISIBLE);
                Collections.sort(docListModel.getDocuments(), new Comparator<DocumentsModel>() {
                    @Override
                    public int compare(DocumentsModel u1, DocumentsModel u2) {
                        return u1.getDoc_name().compareToIgnoreCase(u2.getDoc_name());
                    }
                });

                documentsListAdapter = new DocumentsListAdapter(DocumentsListActivity.this, cardClickListener, docListModel.getDocuments());
                docsRecyclerView.setAdapter(documentsListAdapter);
                documentsListAdapter.notifyDataSetChanged();
                //imgSortAsc.setBackground(getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp));
            }
        });

        imgSortDsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("CLIC", " cd DSC");
                imgSortAsc.setVisibility(View.VISIBLE);
                imgSortDsc.setVisibility(View.GONE);
                Collections.sort(docListModel.getDocuments(), new Comparator<DocumentsModel>() {
                    @Override
                    public int compare(DocumentsModel u1, DocumentsModel u2) {
                        return u2.getDoc_name().compareToIgnoreCase(u1.getDoc_name());
                    }
                });

                documentsListAdapter = new DocumentsListAdapter(DocumentsListActivity.this, cardClickListener, docListModel.getDocuments());
                docsRecyclerView.setAdapter(documentsListAdapter);
                documentsListAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getDocList() {

        APIInterface apiService =
                APIClient.getClient().create(APIInterface.class);

        Single<DocListModel> call = apiService.getDocs(documentCategoriesModel.getCat_id());
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getDocListResponse());
    }

    public DisposableSingleObserver<DocListModel> getDocListResponse() {
        return new DisposableSingleObserver<DocListModel>() {
            @Override
            public void onSuccess(DocListModel value) {

                try {
                    Log.e("Val ", "DOC " + value);

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    docListModel = value;

                    documentsListAdapter = new DocumentsListAdapter(DocumentsListActivity.this, cardClickListener, value.getDocuments());
                    docsRecyclerView.setAdapter(documentsListAdapter);
                    documentsListAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                }
            }

            @Override
            public void onError(Throwable e) {

                try {

                    if (progressDialog != null) {
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

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            saving();
            //activity.showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"
                        + documentsModel.getDoc_name());

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            //activity.dismissDialog(progress_bar_type);
            pDialog.dismiss();

            File file = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"
                    + documentsModel.getDoc_name());

            if ((documentsModel.getDoc_name().endsWith(".pdf")) || (documentsModel.getDoc_name().endsWith(".PDF"))) {

                if (file.exists() && file.length() != 0) {

                    Intent intent = new Intent();

                    //intent.setClassName("com.adobe.reader","com.adobe.reader.AdobeReader");

                    intent.setAction(android.content.Intent.ACTION_VIEW);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");

                    try {

                        startActivity(intent);

                    } catch (Exception e) {

                        Log.e("EXCEPTION ", " FD " + e);

                        new MaterialAlertDialogBuilder(DocumentsListActivity.this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                                .setTitle("No Application Found")
                                .setMessage("Download application from Android play store!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();
                    }

                }
            } else if ((documentsModel.getDoc_name().endsWith(".jpg")) || (documentsModel.getDoc_name().endsWith(".bmp")) ||
                    (documentsModel.getDoc_name().endsWith(".BMP")) || (documentsModel.getDoc_name().endsWith(".png")) || (documentsModel.getDoc_name().endsWith(".PNG")) || (documentsModel.getDoc_name().endsWith(".gif")) || (documentsModel.getDoc_name().endsWith(".GIF")) ||
                    (documentsModel.getDoc_name().endsWith(".JPG"))) {

                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "image/*");
                try {

                    startActivity(intent);

                } catch (Exception e) {

                    Log.e("EXCEPTION ", " FD " + e);

                    new MaterialAlertDialogBuilder(DocumentsListActivity.this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                            .setTitle("No Application Found")
                            .setMessage("Download application from Android play store!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }

            }

        }

    }

    @Override
    public void onCardClicked(View view, DocumentsModel documentsModel) {

        try {

            switch (view.getId()) {
                case R.id.cardViewDocList1:
                    Log.e("DOC MODEL", " " + documentsModel);

                    this.documentsModel = documentsModel;
                    new DocumentsListActivity.DownloadFileFromURL().execute(documentsModel.getDoc_url());
                    break;

                case R.id.imgMore:


                    LayoutInflater inflater = LayoutInflater.from(DocumentsListActivity.this);
                    MaterialCardView cardView = (MaterialCardView) inflater.inflate(R.layout.lay_more, null, false);

                    TextView txtView = cardView.findViewById(R.id.txtView);

                    final AlertDialog materialDialogs = new MaterialAlertDialogBuilder(DocumentsListActivity.this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                            .setView(cardView)
                            .setCancelable(true)
                            .show();

                    txtView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            materialDialogs.dismiss();
                            new DocumentsListActivity.DownloadFileFromURL().execute(documentsModel.getDoc_url());
                        }
                    });
                    break;
            }

        } catch (Exception e) {

            Log.e("EXCEP", " " + e);
        }

    }

    private void saving() {
        pDialog = new ProgressDialog(DocumentsListActivity.this);
        pDialog.setCancelable(false);//you can cancel it by pressing back button
        pDialog.setMessage("Saving ...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMax(100);
        pDialog.show();//displays the progress bar
    }

    private void loading() {
        progressDialog = new ProgressDialog(DocumentsListActivity.this);
        progressDialog.setCancelable(true);//you can cancel it by pressing back button
        progressDialog.setMessage("Loading ...");
        progressDialog.show();//displays the progress bar

    }
}
