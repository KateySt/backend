package starlight.backend.admin;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
@Transactional
public class AdminService {
    private AdminRepository adminRepository;

    public boolean isAdminExistedById(long adminId) {
        return adminRepository.existsByAdminId(adminId);
    }
}
