package starlight.backend.admin.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import starlight.backend.admin.AdminService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v3")
@Slf4j
public class AdminControllerMic {
    private AdminService adminService;

    @GetMapping("/admin/{admin-id}")
    public boolean isTalentExistedById(@PathVariable("admin-id") long adminId) {
        log.info("@GetMapping(\"/admin\")");
        return adminService.isAdminExistedById(adminId);
    }
}
