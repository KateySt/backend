package starlight.backend.gateway.enums;

public enum Role {
    TALENT, SPONSOR, ADMIN;

    final String roleName = "ROLE_" + name();

    public String getAuthority() {
        return roleName;
    }
}
