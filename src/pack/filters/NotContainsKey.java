package pack.filters;

public class NotContainsKey extends Filter{

	String key;
	
	public NotContainsKey(String key) {
		this.key = key;
	}
	
	public boolean evaluate(String disparo[]) {
		for(String s : disparo) {
			if(s.split("=")[0].equals(key)) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		return "Does NOT contains key \"" + key + "\"";
	}
}
