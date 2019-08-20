package com.assignment.documentsrepo;

import androidx.annotation.NonNull;
import java.io.Serializable;
import java.util.ArrayList;

public class DocListModel implements Serializable {

    private ArrayList<DocumentsModel> documents;

    public DocListModel(ArrayList<DocumentsModel> documents) {
        this.documents = documents;
    }

    public DocListModel() {
    }

    public ArrayList<DocumentsModel> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<DocumentsModel> documents) {
        this.documents = documents;
    }

    @NonNull
    @Override
    public String toString() {
        return "DocListModel{" +
                "documents=" + documents +
                '}';
    }
}
