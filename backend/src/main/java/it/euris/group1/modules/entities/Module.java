package it.euris.group1.modules.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "modules")
@AllArgsConstructor
@NoArgsConstructor
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @Column(name = "name", nullable = false)
    @Getter
    @Setter
    private String name;

    @Column(name = "surname", nullable = false)
    @Getter
    @Setter
    private String surname;

    @Column(name = "birth_date", nullable = false)
    @Getter
    @Setter
    private LocalDate birthDate;

    @Column(name = "creation_timestamp", nullable = false)
    @JsonFormat(timezone = "GMT+02")
    @Getter
    @Setter
    private Timestamp creationTimestamp;

    @Column(name = "age", nullable = false)
    @Getter
    @Setter
    private Integer age;

    @Column(name = "type", nullable = false)
    @Getter
    @Setter
    private Type type;

    public Module(Long id, String name, String surname, LocalDate birthDate, Type type) {
        this(name, surname, birthDate, type);
        this.id = id;
    }

    public Module(String name, String surname, LocalDate birthDate, Type type) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.type = type;
        setCreationTimestamp();
        setAge();
    }

    public void setAge() {
        if ((birthDate.getMonthValue() < LocalDate.now().getMonthValue()) ||
                (birthDate.getDayOfMonth() <= LocalDate.now().getDayOfMonth() &&
                (birthDate.getMonthValue() == LocalDate.now().getMonthValue()))) {
            this.age = LocalDate.now().getYear() - birthDate.getYear();
        } else
            this.age = LocalDate.now().getYear() - birthDate.getYear() - 1;
    }

    public void setCreationTimestamp() {
        this.creationTimestamp = new Timestamp(System.currentTimeMillis());
    }
}
