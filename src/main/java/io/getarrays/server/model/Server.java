package io.getarrays.server.model;

import io.getarrays.server.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import java.io.Serializable;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data //von LOMBOK
@NoArgsConstructor
@AllArgsConstructor

public class Server implements Serializable { //die einzelnen Server serilizable zur Hilfe bei der Konvertierung zu JSON
    @Id
    @GeneratedValue (strategy = AUTO)
    @Column (unique = true, nullable = false, updatable = false) //muss unique sein
                        //Definition der ID
    private Long id;
    @Column (unique = true, nullable = false, updatable = false) //muss unique sein
    @NotEmpty (message = "IP Adresse kann nicht 0 oder leer sein")
    private String ipAddress;
    private String name;
    private String memory;
    private String type;
    private String imageUrl;
    private Status status;

}
