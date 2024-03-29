package com.litongjava.data.services;

import java.util.Collections;
import java.util.List;

import com.jfinal.kit.StrKit;
import com.litongjava.data.constants.OperatorConstants;
import com.litongjava.data.utils.ObjectUtils;

public class OperatorService {
  public void addOperator(StringBuffer where, List<Object> paramList, String fieldName, Object value, String operator) {
    if (OperatorConstants.EQ.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        addWhereField(where, fieldName, "=");
        paramList.add(value);
      }

    } else if (OperatorConstants.NE.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        addWhereField(where, fieldName, "!=");
        paramList.add(value);
      }

    } else if (OperatorConstants.GT.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        addWhereField(where, fieldName, ">");
        paramList.add(value);
      }

    } else if (OperatorConstants.GE.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        addWhereField(where, fieldName, ">=");
        paramList.add(value);
      }

    } else if (OperatorConstants.LT.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        addWhereField(where, fieldName, "<");
        paramList.add(value);

      }
    } else if (OperatorConstants.LE.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        addWhereField(where, fieldName, "<=");
        paramList.add(value);

      }

    } else if (OperatorConstants.BT.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        Object[] valueArray = (Object[]) value;
        if (valueArray.length > 1) {
          addWhereField(where, fieldName, "between", "and");
          paramList.add(valueArray[0]);
          paramList.add(valueArray[1]);
        }

      }

    } else if (OperatorConstants.NB.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        addWhereField(where, fieldName, "not between", "and");
        Object[] valueArray = (Object[]) value;
        paramList.add(valueArray[0]);
        paramList.add(valueArray[1]);
      }

    } else if (OperatorConstants.CT.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        addWhereField(where, fieldName, "like");
        paramList.add("%" + value + "%");
      }

    } else if (OperatorConstants.SW.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        if (StrKit.notNull(value)) {
          addWhereField(where, fieldName, "like");
          paramList.add("%" + value);
        }

      }

    } else if (OperatorConstants.EW.equals(operator)) { // EndWith
      if (!ObjectUtils.isEmpyt(value)) {
        addWhereField(where, fieldName, "like");
        paramList.add(value + "%");
      }

    } else if (OperatorConstants.OL.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        if (value instanceof Object[]) {
          Object[] valueArray = (Object[]) value;
          addWhereOrField(where, fieldName, "like", valueArray);
          for (int i = 0; i < valueArray.length; i++) {
            paramList.add(valueArray[i]);
          }
        }
      }

    } else if (OperatorConstants.NK.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        addWhereField(where, fieldName, "not like");
        paramList.add(value);
      }

    } else if (OperatorConstants.IL.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        if (value instanceof Object[]) {
          Object[] valueArray = (Object[]) value;
          addWhereInField(where, fieldName, "in", valueArray);
          for (int i = 0; i < valueArray.length; i++) {
            paramList.add(valueArray[i]);
          }
        }
      }

    } else if (OperatorConstants.NI.equals(operator)) {
      if (!ObjectUtils.isEmpyt(value)) {
        if (value instanceof Object[]) {
          Object[] valueArray = (Object[]) value;
          addWhereInField(where, fieldName, "not in", valueArray);
          for (int i = 0; i < valueArray.length; i++) {
            paramList.add(valueArray[i]);
          }
        }
      }

    } else if (OperatorConstants.NL.equals(operator)) {
      addWhereField(where, fieldName, "is null");
    } else if ("nn".equals(operator)) {
      addWhereField(where, fieldName, "is not null");

    } else if (OperatorConstants.EY.equals(operator)) {
      addWhereEmpytField(where, fieldName);

    } else if (OperatorConstants.NY.equals(operator)) {
      addWhereNotEmpytField(where, fieldName);
    }
  }

  /**
   * x in (?, ?, ...)
   */
  private void addWhereInField(StringBuffer sql, String fieldName, String operator, Object[] valueArray) {
    if (!sql.toString().endsWith("where ")) {
      sql.append(" and ");
    }
    String format = "%s %s (%s)";
    String questionMarks = String.join(",", Collections.nCopies(valueArray.length, "?"));

    sql.append(String.format(format, fieldName, operator, questionMarks));

  }

  /**
   * sql 示例如下,用于完成括号中的多个内容
   * select * from cf_alarm_ai where deleted=0 and (text like 'Ai%' or text like 'ce%')
   */
  public void addWhereOrField(StringBuffer sql, String fieldName, String operator, Object[] valueArray) {
    if (!sql.toString().endsWith("where ")) {
      sql.append(" and ");
    }

    sql.append("(");
    String format = "%s %s ?";
    sql.append(String.format(format, fieldName, operator));

    String orFormat = "or %s %s ?";
    for (int i = 1; i < valueArray.length; i++) {
      sql.append(String.format(orFormat, fieldName, operator));
    }

    sql.append(")");
  }

  /**
   * 添加where添加,判断and是否存在
   *
   * @param sql
   * @param fieldName
   * @param operator
   */
  public void addWhereField(StringBuffer sql, String fieldName, String operator) {
    if (!sql.toString().endsWith("where ")) {
      sql.append(" and ");
    }

    String format = "%s %s ?";
    sql.append(String.format(format, fieldName, operator));
  }

  public void addWhereField(StringBuffer sql, String fieldName, String operator1, String operator2) {
    if (!sql.toString().endsWith("where ")) {
      sql.append(" and ");
    }

    // eg: time between ? and ?
    String format = "%s %s ? %s ?";
    sql.append(String.format(format, fieldName, operator1, operator2));
  }

  public void addWhereNotEmpytField(StringBuffer sql, String fieldName) {
    if (!sql.toString().endsWith("where ")) {
      sql.append(" and ");
    }

    String format = "%s is null or %s = ''";
    sql.append(String.format(format, fieldName, fieldName));
  }

  public void addWhereEmpytField(StringBuffer sql, String fieldName) {
    if (!sql.toString().endsWith("where ")) {
      sql.append(" and ");
    }

    String format = "%s is not null and %s != ''";
    sql.append(String.format(format, fieldName, fieldName));

  }

}
