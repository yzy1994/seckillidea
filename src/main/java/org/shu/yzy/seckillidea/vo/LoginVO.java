package org.shu.yzy.seckillidea.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.shu.yzy.seckillidea.Validator.IsMobile;

import javax.validation.constraints.NotNull;

@Data
public class LoginVO {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}
