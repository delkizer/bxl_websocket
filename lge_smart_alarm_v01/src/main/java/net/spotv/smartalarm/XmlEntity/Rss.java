package net.spotv.smartalarm.XmlEntity;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "rss")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rss {
    @XmlAttribute
    private String version = "2.0";

    @XmlAttribute(name = "xmlns:lge")
    private String xmlnsLge = "http://www.lge.com/rss";

    private Channel channel;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getXmlnsLge() {
		return xmlnsLge;
	}

	public void setXmlnsLge(String xmlnsLge) {
		this.xmlnsLge = xmlnsLge;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}
