package com.assignment.documentsrepo;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class RepoModel {

    private ArrayList<DocumentCategoriesModel> DocumentCategories;

    public RepoModel(ArrayList<DocumentCategoriesModel> documentCategories) {
        DocumentCategories = documentCategories;
    }

    public RepoModel() {
    }

    public ArrayList<DocumentCategoriesModel> getDocumentCategories() {
        return DocumentCategories;
    }

    public void setDocumentCategories(ArrayList<DocumentCategoriesModel> documentCategories) {
        DocumentCategories = documentCategories;
    }

    @NotNull
    @Override
    public String toString() {
        return "RepoModel{" +
                "DocumentCategories=" + DocumentCategories +
                '}';
    }
}
