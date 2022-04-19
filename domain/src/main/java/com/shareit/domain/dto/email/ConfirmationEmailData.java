package com.shareit.domain.dto.email;

import com.shareit.utils.commons.email.EmailDataModel;
import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ConfirmationEmailData extends EmailDataModel {
    private String name;
    private String link;
}
