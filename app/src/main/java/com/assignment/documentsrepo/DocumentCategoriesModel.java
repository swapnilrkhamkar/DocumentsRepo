package com.assignment.documentsrepo;

import org.jetbrains.annotations.NotNull;
import java.io.Serializable;

public class DocumentCategoriesModel implements Serializable {

    private String cat_id,cat_name,cat_icon,cat_background_img,num_docs;

    public DocumentCategoriesModel(String cat_id, String cat_name, String cat_icon, String cat_background_img, String num_docs) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.cat_icon = cat_icon;
        this.cat_background_img = cat_background_img;
        this.num_docs = num_docs;
    }

    public DocumentCategoriesModel() {
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_icon() {
        return cat_icon;
    }

    public void setCat_icon(String cat_icon) {
        this.cat_icon = cat_icon;
    }

    public String getCat_background_img() {
        return cat_background_img;
    }

    public void setCat_background_img(String cat_background_img) {
        this.cat_background_img = cat_background_img;
    }

    public String getNum_docs() {
        return num_docs;
    }

    public void setNum_docs(String num_docs) {
        this.num_docs = num_docs;
    }

    @NotNull
    @Override
    public String toString() {
        return "DocumentCategoriesModel{" +
                "cat_id='" + cat_id + '\'' +
                ", cat_name='" + cat_name + '\'' +
                ", cat_icon='" + cat_icon + '\'' +
                ", cat_background_img='" + cat_background_img + '\'' +
                ", num_docs='" + num_docs + '\'' +
                '}';
    }
}
