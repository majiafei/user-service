package com.ruanmou.house.user.controller;

import com.ruanmou.house.common.ApiResponse;
import com.ruanmou.house.user.domain.House;
import com.ruanmou.house.user.mapper.HouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 房屋控制器
 *
 * @ProjectName: house
 * @Package: com.runmou.house.server.houseserver.controller
 * @ClassName: HouseController
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/17 17:44
 */
@RestController
@RequestMapping("/house")
public class HouseController {

    @Autowired
    private HouseMapper houseMapper;

    /**
     * 获取房屋列表
     * @return
     */
    @GetMapping("/list")
    public ApiResponse getHouseList(){
        List<House> houses = houseMapper.selectAll();

        List<House> houses1 = houseMapper.selectUserList();

        return ApiResponse.ofSuccess(houses1);
    }

}
