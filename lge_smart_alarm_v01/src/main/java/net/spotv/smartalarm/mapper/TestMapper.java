package net.spotv.smartalarm.mapper;

import org.apache.ibatis.annotations.Mapper;

import net.spotv.smartalarm.vo.TestVO;

@Mapper
public interface TestMapper {
	TestVO getTestData() throws Exception;

}
