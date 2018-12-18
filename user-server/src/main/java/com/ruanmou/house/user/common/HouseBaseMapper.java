package com.ruanmou.house.user.common;

import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.base.select.SelectByPrimaryKeyMapper;

/**
 * 基本的mapper，后面写的mapper需要继承此接口
 * @ProjectName: house
 * @Package: com.ruanmou.house.server.common
 * @ClassName: HouseBaseMapper
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/18 9:09
 */
public interface HouseBaseMapper<T> extends BaseMapper<T>, MySqlMapper<T> {
}
