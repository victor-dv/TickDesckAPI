package br.com.tick.tickdesck.models.user_externo.domain;

import br.com.tick.tickdesck.models.requisitantes.domain.RequisitanteEntity; // <-- MUDANÇA: Importar o Pai
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn; // <-- MUDANÇA: Importar
import jakarta.persistence.Table; // <-- MUDANÇA: Importar
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuarios_externos")
@PrimaryKeyJoinColumn(name = "id")
public class UserExternoEntity extends RequisitanteEntity {

}