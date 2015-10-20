package ch.ethz.coss.nervous.pulse.sql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class PulseElementConfiguration {

	private Long elementID;
	private String elementName;

	public PulseElementConfiguration() {

	}

	@XmlElementWrapper(name = "attributes")
	@XmlElement(name = "attribute")
	public List<PulseElementAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<PulseElementAttribute> attributes) {
		this.attributes = attributes;

	}

	private List<PulseElementAttribute> attributes = new ArrayList<PulseElementAttribute>();

	@XmlElement(name = "sensorName")
	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		//System.out.println("PulseElementConfiguration ElementName = "
//				+ elementName);
		this.elementName = elementName;
	}

	@XmlElement(name = "sensorID")
	public Long getElementID() {
		return elementID;
	}

	public void setElementID(Long elementID) {
		this.elementID = elementID;
	}

}
