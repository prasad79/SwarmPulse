/*******************************************************************************
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ETH Zürich.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * *******************************************************************************/
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
		// System.out.println("PulseElementConfiguration ElementName = "
		// + elementName);
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
