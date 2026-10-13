package com.bx.ultimateDonutSmp2.admin;

/** An operator typed a value the key cannot store. The message is safe to show in the dialog. */
public final class AdminConfigEditException extends Exception {

    public AdminConfigEditException(String message) {
        super(message);
    }
}
