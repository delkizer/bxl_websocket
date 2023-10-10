package net.spotv.smartalarm.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.spotv.smartalarm.service.LgeMetaService;

@Component
public class LgeMetaJob implements Job {
	private static final Logger log = LoggerFactory.getLogger(LgeMetaJob.class);
	
	@Autowired
	private LgeMetaService lgeMetaService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
        if ( lgeMetaService.getXmlMeta() == true ) { 
        	log.info("Xml 메타 정보 정상 출력");
        }
        else {
        	log.error("xml 메타 파일 생성 실패");
        }
        	
        
	}

}
