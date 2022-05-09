package com.shareit.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class Media {
    private Long id;
    private String url;
    private String type;
}