package com.igoosd.rps;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.model.TUser;
import com.igoosd.rps.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RoadPricingSystemApplicationTests {

	@Autowired
	private UserService userService;


	@Test
	public void testFindPage(){

		Page<TUser> page = userService.findPage(new TUser(),new Page(1,2,"create_time",false));
		log.info("查询的分页结果:{}",page);
	}

}
