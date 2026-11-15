package domain;

import lombok.Data;

import java.util.Objects;

@Data
public class Client {
    private Long id;
    private String name;
    private String surName;

    public Client(String name, String surName) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("The first name cannot be null");

        if (surName == null || surName.isEmpty())
            throw new IllegalArgumentException("The last name cannot be null");

        this.name = name;
        this.surName = surName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(getName(), client.getName()) && Objects.equals(surName, client.surName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), surName);
    }

    public String getSureName() {
        return surName;
    }
}
