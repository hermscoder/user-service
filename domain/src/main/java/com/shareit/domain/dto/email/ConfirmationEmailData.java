package com.shareit.domain.dto.email;

import com.shareit.utils.commons.email.EmailDataModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ConfirmationEmailData extends EmailDataModel {
    private String name;
    private String link;
}
