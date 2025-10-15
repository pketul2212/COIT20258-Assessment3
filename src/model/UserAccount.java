package model;

import java.time.LocalDateTime;

public abstract class UserAccount {

    protected String id;
    protected String username;
    protected String passwordHash;
    protected String name;
    protected String email;
    protected String contact;
    protected Role role;
    protected boolean isActive;
    protected LocalDateTime createdDate;

    public UserAccount(String id, String username, String passwordHash, String name, String email, String contact, Role role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.role = role;
        this.isActive = true;
        this.createdDate = LocalDateTime.now();
    }

    public boolean authenticate(String passwordHash) {
        return this.passwordHash.equals(passwordHash);
    }

    public void updateProfile(String name, String email, String contact) {
        this.name = name;
        this.email = email;
        this.contact = contact;
    }

    public void deactivateAccount() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return role + "{"
                + "id='" + id + '\''
                + ", username='" + username + '\''
                + ", name='" + name + '\''
                + '}';
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActive() {
        return isActive;
    }
}
