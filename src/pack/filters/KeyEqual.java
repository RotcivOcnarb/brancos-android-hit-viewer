package pack.filters;

public class KeyEqual extends Filter{

	String key;
	String value;
	
	public KeyEqual(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public boolean evaluate(String disparo[]) {
		for(String s : disparo) {
			if(s.split("=")[0].equals(key)) {
				if(s.split("=")[1].equals(value)) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		return false;
	}
	
	public String toString() {
		return key + " equals \"" + value + "\"";
	}

}
