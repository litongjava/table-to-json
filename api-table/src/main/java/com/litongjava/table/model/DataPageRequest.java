package com.litongjava.table.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DataPageRequest {
  private Integer pageNo, pageSize;

  public DataPageRequest() {
    this.pageNo = 1;
    this.pageSize = 10;
  }

  public DataPageRequest(TableInput kv) {
    Object pageNo = kv.remove("page_no");
    this.pageNo = getIntegerValue(pageNo, 1);
    Object pageSize = kv.remove("page_size");
    this.pageSize = getIntegerValue(pageSize, 10);
  }

  private Integer getIntegerValue(Object v, Integer defaultValue) {
    if (v == null) {
      return defaultValue;
    } else {
      if (v instanceof Integer) {
        return (Integer) v;
      } else {
        return Integer.parseInt((String) v);
      }
    }

  }
}
