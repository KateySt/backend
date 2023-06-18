package starlight.backend.user.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import starlight.backend.admin.model.emtity.AdminEntity;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Validated
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "talent_id")
    private Long talentId;

    @Column(name = "sponsor_id")
    private Long sponsorId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_id")
    private AdminEntity admin;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;
}
