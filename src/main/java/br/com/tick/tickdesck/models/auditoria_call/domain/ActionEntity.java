package br.com.tick.tickdesck.models.auditoria_call.domain;

import br.com.tick.tickdesck.models.auditoria_call.application.dto.RoleStatusAction;
import br.com.tick.tickdesck.models.call.domain.CallsEntity;
import br.com.tick.tickdesck.models.files.domain.FileEntity;
import br.com.tick.tickdesck.models.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class ActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "call_id", nullable = false)
    private CallsEntity callsEntity;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_action", nullable = false)
    private RoleStatusAction statusAction = RoleStatusAction.PUBLIC;

    @CreationTimestamp
    @Column(name = "data", nullable = false, updatable = false)
    private LocalDateTime data;

    @OneToMany(mappedBy = "action", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileEntity> file;

}
