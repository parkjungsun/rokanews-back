package com.pjsun.MilCoevo.domain.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginRequestDto {

    @ApiModelProperty(value = "이메일")
    @NotNull
    @Size(min = 3, max = 50)
    private String email;

    @ApiModelProperty(value = "비밀번호")
    @NotNull
    @Size(min = 3, max = 100)
    private String password;
}