package page.clab.api.domain.memberManagement.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "Guest User"),
    USER("ROLE_USER", "Normal User"),
    ADMIN("ROLE_ADMIN", "Administrator"),
    SUPER("ROLE_SUPER", "Super Administrator");

    private final String key;
    private final String description;

    public Long toRoleLevel() {
        return switch (this) {
            case GUEST -> 0L;
            case USER -> 1L;
            case ADMIN -> 2L;
            case SUPER -> 3L;
        };
    }

    public boolean isHigherThan(Role role) {
        return this.toRoleLevel() > role.toRoleLevel();
    }

    public boolean isHigherThanOrEqual(Role role) {
        return this.toRoleLevel() >= role.toRoleLevel();
    }

    public boolean isGuestRole() {
        return this.equals(GUEST);
    }

    public boolean isAdminRole() {
        return this.equals(ADMIN);
    }

    public boolean isSuperRole() {
        return this.equals(SUPER);
    }
}
