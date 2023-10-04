package net.spotv.smartalarm.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.spotv.smartalarm.mapper.TestMapper;
import net.spotv.smartalarm.vo.TestVO;

@Service
public class TestServiceImpl implements TestService {
	
	private static final Logger log = LoggerFactory.getLogger(TestServiceImpl.class);
	
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
			log.error( "get Test Data call Error" );
		}
		return null;
	}

}
