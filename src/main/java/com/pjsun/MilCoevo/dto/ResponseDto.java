package com.pjsun.MilCoevo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@ToString
@NoArgsConstructor
public class ResponseDto {

    @ApiModelProperty(value = "응답 코드")
    private String code;

    @ApiModelProperty(value = "응답 메세지")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @ApiModelProperty(value = "응답 데이터")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    @ApiModelProperty(value = "오류 정보")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object error;

    public ResponseDto(String code) {
        this(code, null, null, null);
    }

    public ResponseDto(String code, String message) {
        this(code, message, null, null);
    }

    public ResponseDto(String code, Object data) { this(code, null, data, null); }

    public ResponseDto(String code, BindingResult bindingResult) { this(code, null, null, bindingResult); }

    public ResponseDto(String code, String message, Object data) {
        this(code, message, data, null);
    }

    public ResponseDto(String code, String message, BindingResult bindingResult) {
        this(code, message, null, bindingResult);
    }

    public ResponseDto(String code, String message, Object data, BindingResult bindingResult) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.error = bindingResult != null ? addFieldErrors(bindingResult) : null;
    }

    private List<FieldErrorDto> addFieldErrors(BindingResult bindingResult) {
        List<FieldErrorDto> collect = bindingResult.getFieldErrors()
                .stream().map(e -> {
                    FieldErrorDto error = new FieldErrorDto(e.getField(), e.getRejectedValue(), e.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        return collect;
    }

    @Getter @Setter
    @NoArgsConstructor
    private static class FieldErrorDto {

        private String field;
        private Object rejectedValue;
        private String message;

        public FieldErrorDto(String field, Object rejectedValue, String defaultMessage) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = defaultMessage;
        }
    }
}

