package model;


public enum UserRole {
    ADMIN,
    USER;

    public static UserRole fromInt(int value) {
        return switch (value) {
            case 0 -> UserRole.ADMIN;
            case 1 -> UserRole.USER;
            default -> throw new AssertionError("You cannot pass values other than 0 or 1");
        };
    }
}
