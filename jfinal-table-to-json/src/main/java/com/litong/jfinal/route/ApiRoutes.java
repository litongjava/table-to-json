package com.litong.jfinal.route;

import com.jfinal.config.Routes;
import com.litong.jfinal.controler.ApiDbController;
import com.litong.jfinal.controler.ApiDataController;
import com.litong.jfinal.controler.IndexController;

/**
 * @author bill robot
 * @date 2020年8月16日_下午5:08:54 
 * @version 1.0 
 * @desc
 */
public class ApiRoutes extends Routes {

  @Override
  public void config() {
    add("api/data", ApiDataController.class);
    add("api/db", ApiDbController.class);
    add("/", IndexController.class);
  }

}
