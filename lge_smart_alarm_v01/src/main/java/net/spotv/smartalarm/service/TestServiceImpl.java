package net.spotv.smartalarm.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.spotv.smartalarm.mapper.TestMapper;
import net.spotv.smartalarm.vo.TestVO;

@Service
public class TestServiceImpl implements TestService {

	@Autowired
	private TestMapper testMapper;
	
	@Override
	public TestVO getTestData()  {
		// TODO Auto-generated method stub
		try {
			return testMapper.getTestData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
