package starlight.backend.talent.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import starlight.backend.talent.model.request.NewUser;
import starlight.backend.talent.model.response.Talent;
import starlight.backend.talent.service.TalentServiceInterface;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v3")
@Slf4j
public class TalentControllerMic {
    private TalentServiceInterface talentService;

    @PostMapping("/talent")
    public Talent saveTalent(@Valid @RequestBody NewUser newUser) {
        var t = talentService.saveTalent(newUser);
        log.info("t {}" , t);
        return t;
    }

    @GetMapping("/talent")
    public Talent getTalentByEmail(@RequestParam String email) {
        var t =talentService.getTalentByEmail(email);
        log.info("t {}" , t);
        return t;
    }
}
