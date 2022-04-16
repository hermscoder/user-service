package com.shareit.utils.commons.email;

import java.util.HashMap;

public interface EmailSender {
    void send(MailDetail request, HashMap<String, Object> model);
}
