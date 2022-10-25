package ua.nix.onishchenko.mfc.rest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@ToString
@Entity
@Table(name = "\"user\"")
public class User implements UUIDEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "creation")
    private Instant creation = Instant.now();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "holder")
    private Set<Account> accounts = new LinkedHashSet<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "creator")
    private Set<OperationType> operationTypes = new LinkedHashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getCreation() {
        return creation;
    }

    public void setCreation(Instant creation) {
        this.creation = creation;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<OperationType> getOperationTypes() {
        return operationTypes;
    }

    public void setOperationTypes(Set<OperationType> operationTypes) {
        this.operationTypes = operationTypes;
    }

}