package pack.filters;

public class ContainsKey extends Filter{
	
	String key;
	
	public ContainsKey(String key) {
		this.key = key;
	}

	@Override
	public boolean evaluate(String disparo[]) {
		for(String s : disparo) {
			if(s.split("=")[0].equals(key)) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return "Contains \"" + key + "\"";
	}

}
