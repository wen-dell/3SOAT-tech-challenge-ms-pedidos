package br.com.tech.challenge.domain.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Table
@Generated
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_sequence")
    @SequenceGenerator(name = "cliente_sequence", sequenceName = "cliente_seq", allocationSize = 1)
    private Long id;

    @Column(length = 200)
    private String nome;

    @Column
    private String cpf;

    @Column
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="cliente")
    @Transient
    private Set<Pedido> pedido;

}
