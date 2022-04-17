package com.shareit.utils.commons.email;


public interface EmailSender {
    void send(MailDetail request, EmailDataModel model);
}
