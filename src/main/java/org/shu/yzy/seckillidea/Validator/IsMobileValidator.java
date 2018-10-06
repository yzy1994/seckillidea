package org.shu.yzy.seckillidea.Validator;

import com.mysql.jdbc.StringUtils;
import org.shu.yzy.seckillidea.utils.ValidatorUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        //值是必要字段
        if(required){
            return ValidatorUtil.isMobile(s);
        } else{
            //值是非必要字段，为空也返回true
            if(StringUtils.isEmptyOrWhitespaceOnly(s)) {
                return true;
            }else{
                return ValidatorUtil.isMobile(s);
            }
        }
    }
}
