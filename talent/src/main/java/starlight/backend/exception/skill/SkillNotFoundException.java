package starlight.backend.exception.skill;

public class SkillNotFoundException extends RuntimeException {
    public SkillNotFoundException(long id) {
        super("Skill not found with id: " + id);
    }

    public SkillNotFoundException(String id) {
        super("Skill not found with id: " + id);
    }
}
