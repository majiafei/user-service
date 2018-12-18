package com.ruanmou.house.user.mapper;

import com.ruanmou.house.user.common.HouseBaseMapper;
import com.ruanmou.house.user.domain.House;

import java.util.List;

/**
 * @ProjectName: house
 * @Package: com.runmou.house.server.houseserver.mapper
 * @ClassName: HouseMapper
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/17 17:37
 */
public interface HouseMapper extends HouseBaseMapper<House> {

    List<House> selectUserList();

}
