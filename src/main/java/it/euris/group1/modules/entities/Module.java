package it.euris.group1.modules.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "modules")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "creation_timestamp", nullable = false)
    private Timestamp creationTimestamp;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "type", nullable = false)
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

    public Module(Long id, String name, String surname, LocalDate birthDate, Type type) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.type = type;
        setCreationTimestamp();
        setAge();

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

    public void setCreationTimestamp() {
        this.creationTimestamp = new Timestamp(System.currentTimeMillis());
    }


    public Integer getAge() {
        return age;
    }


    public void setAge() {
        if ((birthDate.getMonthValue() < LocalDate.now().getMonthValue())||(birthDate.getDayOfMonth() <= LocalDate.now().getDayOfMonth() && (birthDate.getMonthValue() == LocalDate.now().getMonthValue()))) {
            this.age = LocalDate.now().getYear() - birthDate.getYear();
           } else this.age = LocalDate.now().getYear() - birthDate.getYear() - 1;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
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
