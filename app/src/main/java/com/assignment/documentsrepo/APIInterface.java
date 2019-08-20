package com.assignment.documentsrepo;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIInterface {

    @GET("doc_categories")
    Single<RepoModel> getRepo();

    @GET("docs_list/{cat_code}")
    Single<DocListModel> getDocs(@Path("cat_code") String cat_code);

}



