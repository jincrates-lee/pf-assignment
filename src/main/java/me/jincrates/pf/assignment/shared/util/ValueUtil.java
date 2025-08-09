package me.jincrates.pf.assignment.shared.util;

import java.util.Collection;

public final class ValueUtil {

    private ValueUtil() {
    }

    public static <T> T defaultIfNull(
        T newValue,
        T existingValue
    ) {
        return newValue != null ? newValue : existingValue;
    }

    public static String defaultIfBlank(
        String newValue,
        String existingValue
    ) {
        return (newValue != null && !newValue.isBlank()) ? newValue : existingValue;
    }

    public static <T extends Collection<?>> T defaultIfNullOrEmpty(
        T newValue,
        T existingValue
    ) {
        return (newValue != null && !newValue.isEmpty()) ? newValue : existingValue;
    }

}
