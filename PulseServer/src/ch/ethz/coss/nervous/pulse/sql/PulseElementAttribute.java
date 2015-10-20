package ch.ethz.coss.nervous.pulse.sql;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ch.ethz.coss.nervous.pulse.xml.TypeAdapter;

class PulseElementAttribute {

	public String getName() {
		return name;
	}

	public void setName(String name) {
		//System.out.println("PulseElementAttribute  name -- " + name);
		this.name = name;
	}

	@XmlJavaTypeAdapter(type = int.class, value = TypeAdapter.class)
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public PulseElementAttribute() {
	}

	private String name;

	private int type;
}
