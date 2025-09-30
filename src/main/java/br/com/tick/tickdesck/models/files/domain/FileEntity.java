package br.com.tick.tickdesck.models.files.domain;

import br.com.tick.tickdesck.models.action_call.domain.Actions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String content;
    private String type;

    private String path;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "action_id", referencedColumnName = "id")
    private Actions action;

}
