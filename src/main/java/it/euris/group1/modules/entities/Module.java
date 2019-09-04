package it.euris.group1.modules.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private Timestamp creationTimestamp;

    @Column(nullable = false)
    private Integer age;

    private Type type;

    public Module() {
    }

    public Module(Long id, String name, String surname, LocalDate birthDate, Timestamp creationTimestamp, Integer age, Type type) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.creationTimestamp = creationTimestamp;
        this.age = age;
        this.type = type;
    }

    public Module(String name, String surname, LocalDate birthDate, Timestamp creationTimestamp, Integer age, Type type) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.creationTimestamp = creationTimestamp;
        this.age = age;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
