package com.shareit.utils.commons.email;


import java.util.concurrent.CompletableFuture;

public interface EmailSender {
    CompletableFuture<Void> send(MailDetail request, EmailDataModel model);
}
