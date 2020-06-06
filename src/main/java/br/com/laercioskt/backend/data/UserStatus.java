package br.com.laercioskt.backend.data;

public enum UserStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String name;

    UserStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
