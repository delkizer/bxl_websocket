package net.spotv.smartalarm.XmlEntity;

import java.util.List;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
	//@XmlElement(name = "titleid", namespace = "http://www.lge.com/rss")
	
	@XmlElement(name = "lge:titleid")
    private String titleid;
	
	private Titles titles = new Titles();
	
	private Descriptions descriptions = new Descriptions();
	
	@XmlElement(name = "lge:images")
    private Images images = new Images();
	
	@XmlElement(name = "lge:contentType")
    private String contentType;
	
	@XmlElement(name = "lge:genres")
    private Genres genres = new Genres();
	
	@XmlElement(name = "lge:sportsInfo")
	private SportsInfo sportsInfo = new SportsInfo();
	
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Genres {
    	@XmlElement(name = "lge:genre")
        private List<Genre> genres;

        public List<Genre> getGenres() {
            return genres;
        }

        public void setGenres(List<Genre> genres) {
            this.genres = genres;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD  )
    public static class Genre {

        @XmlValue
        private String value;
        
    	public String getValue() {
    		return value;
    	}

    	public void setValue(String value) {
    		this.value = value;
    	}
    }		
	
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Images {
    	@XmlElement(name = "lge:image")
        private List<Image> image;

        public List<Image> getImages() {
            return image;
        }

        public void setImages(List<Image> image) {
            this.image = image;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD  )
    public static class Image {

        @XmlAttribute
        private String locale;
        
        @XmlAttribute
        private String type;
        
        @XmlAttribute
        private String width;

        @XmlAttribute
        private String height;
        
        @XmlValue
        private String value;
        
        public String getLocale() {
    		return locale;
    	}

    	public void setLocale(String locale) {
    		this.locale = locale;
    	}
    	
    	public String getValue() {
    		return value;
    	}

    	public void setValue(String value) {
    		this.value = value;
    	}

    	public String getType() {
    		return type;
    	}

    	public void setType(String type) {
    		this.type = type;
    	}
    	
    	public String getWidth() {
    		return width;
    	}

    	public void setWidth(String width) {
    		this.width = width;
    	}    	
    	
    	public String getHeight() {
    		return height;
    	}

    	public void setHeight(String height) {
    		this.height = height;
    	}    	
    	
    }	

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Titles {
        private List<Title> title;

        public List<Title> getTitle() {
            return title;
        }

        public void setTitle(List<Title> title) {
            this.title = title;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Title {

        @XmlAttribute
        private String locale;
        
        @XmlValue
        private String value;

    	public String getLocale() {
    		return locale;
    	}

    	public void setLocale(String locale) {
    		this.locale = locale;
    	}

    	public String getValue() {
    		return value;
    	}

    	public void setValue(String value) {
    		this.value = value;
    	}
    	
    }
    
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Descriptions {
        private List<Description> description;

        public List<Description> getDescriptions() {
            return description;
        }

        public void setDescriptions(List<Description> description) {
            this.description = description;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Description {

        @XmlAttribute
        private String locale;
        
        @XmlValue
        private String value;

    	public String getLocale() {
    		return locale;
    	}

    	public void setLocale(String locale) {
    		this.locale = locale;
    	}

    	public String getValue() {
    		return value;
    	}

    	public void setValue(String value) {
    		this.value = value;
    	}
    	
    }    
    
	public String getTitleid() {
		return titleid;
	}

	public void setTitleid(String titleid) {
		this.titleid = titleid;
	}

	public Titles getTitles() {
		return titles;
	}

	public void setTitles(Titles titles) {
		this.titles = titles;
	}

	public Descriptions getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Descriptions descriptions) {
		this.descriptions = descriptions;
	}

	public Images getImages() {
		return images;
	}

	public void setImages(Images images) {
		this.images = images;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Genres getGenres() {
		return genres;
	}

	public void setGenres(Genres genres) {
		this.genres = genres;
	}

	public SportsInfo getSportsInfo() {
		return sportsInfo;
	}

	public void setSportsInfo(SportsInfo sportsInfo) {
		this.sportsInfo = sportsInfo;
	}    
}
