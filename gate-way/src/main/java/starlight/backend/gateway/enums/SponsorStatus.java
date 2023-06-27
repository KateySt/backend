package starlight.backend.gateway.enums;

public enum SponsorStatus {
    ACTIVE,DELETING;

    public static SponsorStatus fromString(String status){
        return valueOf(status.toUpperCase());
    }
}
