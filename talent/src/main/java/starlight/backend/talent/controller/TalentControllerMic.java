package starlight.backend.talent.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import starlight.backend.talent.model.request.NewUser;
import starlight.backend.talent.model.response.Talent;
import starlight.backend.talent.service.TalentServiceInterface;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v3")
@Slf4j
public class TalentControllerMic {
    private TalentServiceInterface talentService;

    @PostMapping("/talent")
    public Talent saveTalent(@RequestBody NewUser newUser) {
        log.info("@PostMapping(\"/talent\")");
        return talentService.saveTalent(newUser);
    }

    @GetMapping("/talent")
    public Talent getTalentByEmail(@RequestParam String email) {
        log.info("@GetMapping(\"/talent\")");
        return talentService.getTalentByEmail(email);
    }
    @GetMapping("/talent/{talent-id}")
    public boolean isTalentExistedById(@PathVariable("talent-id") long talentId) {
        log.info("@GetMapping(\"/talent\")");
        return talentService.isTalentExistedById(talentId);
    }
}
