package com.litongjava.data.utils;

import java.util.List;

import com.jfinal.kit.Kv;
import com.litongjava.data.model.DbJsonBean;
import com.litongjava.data.model.DbPage;
import com.litongjava.jfinal.plugin.activerecord.Page;
import com.litongjava.jfinal.plugin.activerecord.Record;

public class DbJsonBeanUtils {
  public static DbJsonBean<DbPage<Kv>> pageToDbPage(DbJsonBean<Page<Record>> jsonBean, boolean underscoreToCamel) {
    int totalRow = jsonBean.getData().getTotalRow();
    List<Record> list = jsonBean.getData().getList();
    List<Kv> newList = KvUtils.recordsToKv(list, underscoreToCamel);

    DbPage<Kv> pageData = new DbPage<>();
    pageData.setTotal(totalRow);
    pageData.setList(newList);

    return new DbJsonBean<>(jsonBean.getCode(), jsonBean.getMsg(), pageData);

  }

  public static DbJsonBean<Kv> recordToKv(DbJsonBean<Record> jsonBean) {
    Record data = jsonBean.getData();
    if (data == null) {
      return new DbJsonBean<>(jsonBean.getCode(), jsonBean.getMsg());
    } else {
      return new DbJsonBean<>(jsonBean.getCode(), jsonBean.getMsg(), KvUtils.recordToKv(data, false));
    }

  }

  public static DbJsonBean<List<Kv>> recordsToKv(DbJsonBean<List<Record>> jsonBean, boolean underscoreToCamel) {
    return new DbJsonBean<>(jsonBean.getCode(), jsonBean.getMsg(), KvUtils.recordsToKv(jsonBean.getData(), underscoreToCamel));
  }
}
