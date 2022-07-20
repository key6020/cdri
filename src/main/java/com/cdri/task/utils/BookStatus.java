package com.cdri.task.utils;

public enum BookStatus {
    훼손(false),
    분실(false),
    정상(true);

    private final boolean isAvailable;

    BookStatus(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }
}
