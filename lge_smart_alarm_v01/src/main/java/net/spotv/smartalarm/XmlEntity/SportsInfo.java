package net.spotv.smartalarm.XmlEntity;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SportsInfo {
	@XmlElement ( name = "lge:sportId")
	private String sportId;
	
	@XmlElement ( name = "lge:sportName")
	private String sportName;

	@XmlElement(name = "lge:leagueInfo")
	private LeagueInfo leagueInfo = new LeagueInfo();
	
	@XmlElement(name = "lge:teamInfos")
	private TeamInfos teamInfos = new TeamInfos();	
	
	@XmlElement(name = "lge:sportingEvent")
	private SportingEvent sportingEvent = new SportingEvent();	
			

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class SportingEvent {
		@XmlElement ( name = "lge:startTime")
		private String startTime;
		
		@XmlElement ( name = "lge:endTime")
		private String endTime;
		
    	public String getStartTime() {
    		return startTime;
    	}

    	public void setStartTime(String StartTime) {
    		this.startTime = StartTime;
    	}
    	
    	public String getEndTime() {
    		return endTime;
    	}

    	public void setEndTime(String EndTime) {
    		this.endTime = EndTime;
    	}    	
		
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class LeagueInfo {
		@XmlElement ( name = "lge:leagueId")
		private String leagueId;
		
		@XmlElement ( name = "lge:leagueName")
		private String leagueName;
		
    	public String getLeagueId() {
    		return leagueId;
    	}

    	public void setLeagueId(String leagueId) {
    		this.leagueId = leagueId;
    	}
    	
    	public String getLeagueName() {
    		return leagueName;
    	}

    	public void setLeagueName(String leagueName) {
    		this.leagueName = leagueName;
    	}    	
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TeamInfos {
		@XmlElement(name = "lge:teamInfo")
		private List<TeamInfo> teamInfo;
		
        public List<TeamInfo> getTeamInfos() {
            return teamInfo;
        }

        public void setTeamInfos(List<TeamInfo> teamInfo) {
            this.teamInfo = teamInfo;
        }		
		
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TeamInfo{
		@XmlElement ( name = "lge:teamId")
		private String teamId;
		
		@XmlElement ( name = "lge:teamName")
		private String teamName;
		
		@XmlElement ( name = "lge:teamType")
		private String teamType;
		
        public String getTeamId() {
    		return teamId;
    	}

    	public void setTeamId(String teamId) {
    		this.teamId = teamId;
    	}
    	
        public String getTeamName() {
    		return teamName;
    	}

    	public void setTeamName(String teamName) {
    		this.teamName = teamName;
    	}
    	
        public String getTeamType() {
    		return teamType;
    	}

    	public void setTeamType(String teamType) {
    		this.teamType = teamType;
    	}
    	
	}
	
	public String getSportId() {
		return sportId;
	}

	public void setSportId(String sportId) {
		this.sportId = sportId;
	}

	public String getSportName() {
		return sportName;
	}

	public void setSportName(String sportName) {
		this.sportName = sportName;
	}

	public LeagueInfo getLeagueInfo() {
		return leagueInfo;
	}

	public void setLeagueInfo(LeagueInfo leagueInfo) {
		this.leagueInfo = leagueInfo;
	}

	public TeamInfos getTeamInfos() {
		return teamInfos;
	}

	public void setTeamInfos(TeamInfos teamInfos) {
		this.teamInfos = teamInfos;
	}	
	
	public SportingEvent getSporringEvent() {
		return sportingEvent;
	}

	public void setSportingEvent(SportingEvent sportingEvent) {
		this.sportingEvent = sportingEvent;
	}	
}
