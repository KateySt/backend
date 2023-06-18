package starlight.backend.kudos.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import starlight.backend.proof.model.entity.ProofEntity;

import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name  = "kudos")
public class KudosEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long kudosId;
    private Long followerId;
    private Integer countKudos;
    private Instant updateData;
    private Instant createData;

    @Column(name = "sponsor_id")
    private Long sponsorId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "proof_id", nullable = false)
    private ProofEntity proof;
}
