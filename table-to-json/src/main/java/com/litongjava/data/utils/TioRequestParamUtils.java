package com.litongjava.data.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.jfinal.kit.StrKit;
import com.litongjava.tio.http.common.HttpRequest;
import com.litongjava.tio.utils.json.Json;

public class TioRequestParamUtils {
  public static List<String> types = new ArrayList<>();

  static {
    types.add("int");
    types.add("long");
  }

  public static Map<String, Object> getRequestMap(HttpRequest request) {
    Map<String, Object> map = new HashMap<>();
    // String contentType = request.getHeader(HttpConst.RequestHeaderKey.Content_Type);
    String contentType = request.getContentType();

    Map<String, Object> requestMap = new HashMap<>();
    Map<String, List<Object>> arrayParams = new HashMap<>();
    Map<String, Object> paramType = new HashMap<>();

    if (contentType != null && contentType.contains("application/json")) {
      // throw new RuntimeException("unspupport: application/json");
      String bodyString = request.getBodyString();
      requestMap = Json.getJson().parseToMap(bodyString, String.class, Object.class);
    } else {
      // Form data handling
      Enumeration<String> parameterNames = request.getParameterNames();
      while (parameterNames.hasMoreElements()) {
        String paramName = parameterNames.nextElement();
        String paramValue = request.getParameter(paramName);
        requestMap.put(paramName, paramValue);
      }
    }

    Set<Entry<String, Object>> entrySet = requestMap.entrySet();
    for (Entry<String, Object> entry : entrySet) {
      String paramName = entry.getKey();
      Object paramValue = entry.getValue();

      if (paramName.contains("[")) {
        // This is an array paramValue
        String arrayName = paramName.substring(0, paramName.indexOf('['));
        if (!arrayParams.containsKey(arrayName)) {
          arrayParams.put(arrayName, new ArrayList<>());
        }
        arrayParams.get(arrayName).add(paramValue);
      } else if (paramName.endsWith("Type") && types.contains(paramValue)) {
        // 前端传递指定数缺定数据类型
        paramType.put(paramName, paramValue);
      } else {
        // This is a regular paramValue
        map.put(paramName, paramValue);
      }
    }

    // Convert the lists to arrays and add them to the map
    listArrayToMap(map, arrayParams, paramType);

    return map;
  }

  private static void listArrayToMap(Map<String, Object> map, Map<String, List<Object>> arrayParams,
      Map<String, Object> paramType) {
    for (Map.Entry<String, List<Object>> entry : arrayParams.entrySet()) {
      map.put(entry.getKey(), entry.getValue().toArray(new String[0]));
    }
    for (Map.Entry<String, Object> entry : paramType.entrySet()) {
      String typeKey = entry.getKey();
      int lastIndexOf = typeKey.lastIndexOf("Type");
      String paramKey = typeKey.substring(0, lastIndexOf);
      Object paramValue = map.get(paramKey);
      if (StrKit.notNull(paramValue)) {
        Object value = entry.getValue();
        if (value instanceof String) {
          if ("int".equals(value)) {
            map.put(paramKey, Integer.parseInt((String) map.get(paramKey)));
          } else if ("long".equals(value)) {
            map.put(paramKey, Long.parseLong((String) map.get(paramKey)));
          }
        }

      }
    }
  }
}
