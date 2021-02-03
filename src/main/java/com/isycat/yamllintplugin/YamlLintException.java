package com.isycat.yamllintplugin;

public class YamlLintException extends RuntimeException {
    public YamlLintException(final String message) {
        super(message);
    }

    public YamlLintException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
