package net.spotv.smartalarm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import net.spotv.smartalarm.vo.LgeMetaVO;

@Mapper
public interface LgeMetaMapper {
	List<LgeMetaVO> getXmlMeta( );
}
