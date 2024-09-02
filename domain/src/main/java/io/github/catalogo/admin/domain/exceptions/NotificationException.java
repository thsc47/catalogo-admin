package io.github.catalogo.admin.domain.exceptions;

import io.github.catalogo.admin.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(final String aMessage,
                                    final Notification notification) {
        super(aMessage, notification.getErrors());
    }

    public NotificationException(final Notification notification) {
        super("", notification.getErrors());
    }
}
