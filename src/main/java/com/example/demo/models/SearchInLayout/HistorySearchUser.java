package com.example.demo.models.SearchInLayout;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class HistorySearchUser {
    @Id
    private String id;
    private String idUser;
    private String idSearch;
    private long dateSearch;

    public HistorySearchUser() {
        super();
    }

    public HistorySearchUser(String idUser, String idSearch, long dateSearch) {
        this.idUser = idUser;
        this.idSearch = idSearch;
        this.dateSearch = dateSearch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdSearch() {
        return idSearch;
    }

    public void setIdSearch(String idSearch) {
        this.idSearch = idSearch;
    }

    public long getDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(long dateSearch) {
        this.dateSearch = dateSearch;
    }
}
