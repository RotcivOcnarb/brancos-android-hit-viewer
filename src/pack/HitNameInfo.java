package pack;

import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class HitNameInfo {
	
	String raw_name;
	public static HashMap<String, String> param_names;
	public static HashMap<String, String> compose_names;
	public static HashMap<String, String> enhanced_names;
	
	String fullName;
	boolean enhanced_ecommerce;
	String type_of_enhance = "";
	String value;
	
	int[] ids = new int[0];
	
	public HitNameInfo(String raw_name) {
		
		
		this.raw_name = raw_name;
		
		
		if(raw_name.endsWith("=")) {
			value = "";
		}
		else {	
			try {
				value = raw_name.split("=")[1];
			}
			catch(Exception e) {
				fullName = "erro";
				return;
			}
		}
		
		fullName = calculate();
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public String getRawName() {
		return raw_name;
	}
	
	public String getCode() {
		return raw_name.split("=")[0];
	}
	
	public int[] getIds() {
		return ids;
	}
	
	public boolean isEnhancedEcommerce() {
		return enhanced_ecommerce;
	}
	
	public String getTypeOfEnhance() {
		return type_of_enhance;
	}
	
	public String toString() {
		String texto = "(" + getCode() + ")" +
				"\n\t" + fullName +
				"\n\t" + "Enhanced Ecommerce: " + enhanced_ecommerce;
		
		for(int i : ids) {
			texto += "\n\t" + i; 
		}
		
		return texto;
		
	}
	
	public String calculate() {
		String codename = getCode();
		if(param_names.get(codename) == null) {
			if(!codename.split("\\d+")[0].equals(codename)) {

				String result = "";
				
				String[] ltr = codename.split("\\d+");
				String[] nms = removeEmpties(codename.split("[a-z]+"));

				ids = new int[nms.length];
				for(int i = 0; i < nms.length; i ++) {
					ids[i] = Integer.parseInt(nms[i]);
				}
				
				int nums = 0;
				enhanced_ecommerce = false;
				for(String s : ltr) {
					if(nums < nms.length) {
						if(nums == 0 && enhanced_names.containsKey(s)) {
							result += enhanced_names.get(s) + " " + nms[nums] + " ";
							type_of_enhance = enhanced_names.get(s);
							enhanced_ecommerce = true;
						}
						else {
							result += compose_names.get(s) + " " + nms[nums] + " ";
						}
					}
					else {
						result += compose_names.get(s);
					}
					
					nums++;
				}
				return result;
			}
			else {
				return codename;
			}
		}
		else {
			return "(" + codename + ") " + param_names.get(codename);
		}
	}

	public String[] removeEmpties(String[] arr) {
		int cont = 0;
		for(String s : arr) {
			if(!s.equals("")) {
				cont++;
			}
		}
		
		String[] result = new String[cont];
		
		int i = 0;
		for(String s : arr) {
			if(!s.equals("")) {
				result[i] = s;
				i++;
			}
		}
		
		return result;
	}

	public String getValue() {
		return value;
	}
	
}
