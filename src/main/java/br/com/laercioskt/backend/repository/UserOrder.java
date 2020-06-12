package br.com.laercioskt.backend.repository;

public class UserOrder {

    private final String sorted;
    private final boolean ascending;

    public UserOrder(String sorted, boolean ascending) {
        this.sorted = sorted;
        this.ascending = ascending;
    }

    public String getSorted() {
        return sorted;
    }

    public boolean isAscending() {
        return ascending;
    }

}

