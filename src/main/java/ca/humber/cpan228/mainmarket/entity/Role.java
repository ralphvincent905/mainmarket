package ca.humber.cpan228.mainmarket.entity;

public enum Role {
    ADMIN("Administrator - Full system access, manage users and products"),
    STAFF("Staff - Can manage products and view orders"),
    CUSTOMER("Customer - Can browse and purchase products");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
