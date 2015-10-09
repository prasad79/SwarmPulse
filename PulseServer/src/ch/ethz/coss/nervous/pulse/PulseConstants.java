package ch.ethz.coss.nervous.pulse;

public class PulseConstants {

	public static String PULSE_LIGHT_LABEL = "Light";
	public static String PULSE_NOISE_LABEL = "Noise";
	public static String PULSE_TEXT_LABEL = "Message";
	
	
	
	
	public static String getLabel(int readingType){
		
		switch(readingType){
		case 0:
			default:
			return PULSE_LIGHT_LABEL;
		case 1:
			return PULSE_NOISE_LABEL;
		case 2:
			return PULSE_TEXT_LABEL; 
			
		}
	}
	
	

	
}
