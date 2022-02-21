package com.pjsun.MilCoevo.domain.notice.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchNoticeDto {

    @Size(max = 50)
    private String searchTitle;
}
