package com.litong.jfinal.controler;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.litong.jfinal.validate.TableNameValidator;
import com.litongjava.data.services.DbService;
import com.litongjava.data.services.PrimaryKeyService;
import com.litongjava.data.services.TableColumnService;

/**
 * @author bill robot
 * @date 2020年8月31日_下午10:13:37 
 * @version 1.0 
 * @desc
 */
//@Slf4j
public class ApiDbController extends Controller {

  @Inject
  private DbService dbService;
  @Inject
  private TableColumnService tableColumnService;

  @Inject
  private PrimaryKeyService primaryKeyService;
  /**
   * 返回表
   */
  public void tables() {
    renderJson(dbService.tables());
  }

  /**
   * 返回字段
   */
  @Before(TableNameValidator.class)
  public void cloumns(String tableName) {
    renderJson(dbService.cloumns(tableName));
  }

  /**
   * find,为了防止sql注入,放弃封装
   */
  public void find(String sql) {

  }

  public void clearColumns(String tableName) {
    renderJson(tableColumnService.clear(tableName));
  }

  /**
   * 判断cloumn在tableName中是否存在
   * @param cloumn
   * @param tableName
   * @return
   */
  public void isExists(String cloumn, String tableName) {
    renderJson(tableColumnService.isExists(cloumn, tableName));
  }
  
  public void getPrimaryKey(String tableName) {
    renderJson(primaryKeyService.getPrimaryKey(tableName));
  }

}
