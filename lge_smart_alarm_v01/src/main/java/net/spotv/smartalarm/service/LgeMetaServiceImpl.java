package net.spotv.smartalarm.service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import net.spotv.smartalarm.XmlEntity.Channel;
import net.spotv.smartalarm.XmlEntity.Item;
import net.spotv.smartalarm.XmlEntity.Rss;
import net.spotv.smartalarm.XmlEntity.SportsInfo;
import net.spotv.smartalarm.XmlEntity.SportsInfo.LeagueInfo;
import net.spotv.smartalarm.XmlEntity.SportsInfo.SportingEvent;
import net.spotv.smartalarm.XmlEntity.SportsInfo.TeamInfo;
import net.spotv.smartalarm.XmlEntity.SportsInfo.sportingEvent;
import net.spotv.smartalarm.XmlEntity.TeamInfos;
import net.spotv.smartalarm.config.ConfigReader;
import net.spotv.smartalarm.mapper.LgeMetaMapper;
import net.spotv.smartalarm.vo.LgeMetaVO;

@Service
public class LgeMetaServiceImpl implements LgeMetaService {
	private static final Logger log = LoggerFactory.getLogger(LgeMetaService.class);
	
	@Autowired
	private LgeMetaMapper lgeMetaMapper;
	
	ConfigReader config = new ConfigReader();
	String basePath = config.getProperty("file.basepath");
	String prefix = config.getProperty("file.prefix");
	String extension = config.getProperty("file.extension");	
	
	@Override
	public boolean getXmlMeta()  {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedNow = now.format(formatter);		
		
		List<LgeMetaVO> lgeMetaVOList = null;

		try {
			lgeMetaVOList = lgeMetaMapper.getXmlMeta();
			
		} catch ( Exception e ) {
			e.printStackTrace();
			log.error( "LGE XML META data select error " );
		} 
		
		try { 
	        // Create the objects
	        Channel channel = new Channel();
	        channel.setTitle("Sport TV NOW");
	        channel.setDescription("아시안게임, PL 전 경기, 챔피언스리그 등 해외축구부터 MLB, NBA까지! 프리미엄 스포츠 생중계를 만나보세요");
	        channel.setLink("https://www.spotvnow.co.kr/");
	        channel.setPubDate("Fri, 27 Sep 2023 00:00:01");
	        
	        List<Item> items = new ArrayList<>();
	        for ( LgeMetaVO lgeMetaVO : lgeMetaVOList ) {
		        Item item = new Item();
		        
		        item.setTitleid(  UUID.randomUUID().toString().replace("-", "")  );

		        Item.Title titleKo = new Item.Title();
		        titleKo.setLocale("ko-KR");
		        titleKo.setValue( lgeMetaVO.getTitlekr() );

		        item.getTitles().setTitle(Arrays.asList(titleKo));
		        
		        Item.Description descriptionKo = new Item.Description();
		        descriptionKo.setLocale("ko-KR");
		        descriptionKo.setValue( lgeMetaVO.getDescriptionKr() );
		        
		        item.getDescriptions().setDescriptions(Arrays.asList(descriptionKo));
		        
		        Item.Image imageKo = new Item.Image();
		        imageKo.setLocale("ko-KR");
		        imageKo.setType( "poster" );
		        imageKo.setWidth( "282" );
		        imageKo.setValue( lgeMetaVO.getImageKr() + "=W" + "282" );		        
		        
		        Item.Image imageStillKo = new Item.Image();
		        imageStillKo.setLocale("ko-KR");
		        imageStillKo.setType( "stillcut" );
		        imageStillKo.setWidth( "414" );		        
		        imageStillKo.setValue( lgeMetaVO.getImageKr() + "=W" + "414" );
		        
		        item.getImages().setImages( Arrays.asList(imageKo, imageStillKo));
		        
	        	item.setContentType( lgeMetaVO.getContentType().toString() );
		        
		        Item.Genre genre = new Item.Genre();
		        genre.setValue("Sport");
		        
		        item.getGenres().setGenres( Arrays.asList( genre ));
		        
		        SportsInfo sportsInfo = new SportsInfo();
		        
		        sportsInfo.setSportId( lgeMetaVO.getSportId() );
		        sportsInfo.setSportName( lgeMetaVO.getSportName() );
		        
		        LeagueInfo leagueInfo = new LeagueInfo();
		        leagueInfo.setLeagueId( lgeMetaVO.getLeagueId() );
		        leagueInfo.setLeagueName(lgeMetaVO.getLeagueName() );
		        sportsInfo.setLeagueInfo(leagueInfo);
		        
		        TeamInfo teamInfoHome = new TeamInfo(); 
		        teamInfoHome.setTeamId( lgeMetaVO.getTeamTypeIdHome().toString() );
		        teamInfoHome.setTeamName( lgeMetaVO.getTeamTypeNameHome().toString() );
		        teamInfoHome.setTeamType("Home");
		        
		        TeamInfo teamInfoAway = new TeamInfo();
		        teamInfoAway.setTeamId( lgeMetaVO.getTeamTypeIdAway().toString() );
		        teamInfoAway.setTeamName( lgeMetaVO.getTeamTypeNameAway().toString() );
		        teamInfoAway.setTeamType("AWAY");
		        
		        sportsInfo.getTeamInfos().setTeamInfos( Arrays.asList( teamInfoHome, teamInfoAway) );
		        
		        
		        SportingEvent sportingEvent = new SportingEvent();
		        sportingEvent.setStartTime( lgeMetaVO.getStartTime() );
		        sportingEvent.setEndTime( lgeMetaVO.getEndTime() );
		        
		        sportsInfo.setSportingEvent(sportingEvent);
		        
		        
		        item.setSportsInfo(sportsInfo);
		        
		        items.add(item);		        
	        }
	        
	        channel.setItem(items);

	        Rss rss = new Rss();
	        rss.setChannel(channel);			
			
			File file = new File(basePath + prefix + formattedNow + extension);
			JAXBContext jaxbContext = JAXBContext.newInstance( Rss.class );
			Marshaller jaxbMarshaller  = jaxbContext.createMarshaller();
			
			jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal( rss, file );

			return true;
			
		} catch ( JAXBException e ) {
			e.printStackTrace();
			log.error( "LGE XML META Xml file create error " );
			
		}
		
		return false;
	}
	

}
