package com.litong.jfinal.validate;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.validate.Validator;
import com.litongjava.data.model.DataJsonBean;

public class TableNameValidator extends Validator {
  protected void validate(Controller c) {
    setRet(Ret.fail());
    validateRequired("tableName", "msg", "表名不能为空");
  }

  protected void handleError(Controller c) {
    c.renderJson(new DataJsonBean<Void>(-1, getRet().getStr("msg")));
  }
}