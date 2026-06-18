package org.henick.repoinfo;

class UserNotFoundException extends RuntimeException {

    String username;

    public UserNotFoundException(String username, String message) {
        super(message);
        this.username = username;
    }
}
