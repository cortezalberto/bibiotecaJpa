package org.example.entidades;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

@SuperBuilder
public class Domicilio extends Base{

    @Column(name = "Calle")
    private String calle;

    @Column(name = "Número")
    private int numero;

   // @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
   @ManyToOne()
   @JoinColumn(name = "id_localidad", nullable = false)
    private Localidad localidad;
}
