package net.spotv.smartalarm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.spotv.smartalarm.service.TestService;
import net.spotv.smartalarm.vo.TestVO;

@RestController
@RequestMapping("/api")
public class DBConnectTestCtrl {
	@Autowired
	private TestService testService;
	
	@GetMapping("hello")
	public TestVO HelloWorld() {
		System.out.println( "this-" + testService.getTestData() );
		return testService.getTestData();
	}
}
