
public enum Side {
	L {
		String sideName() {
			return "0";
		}
	}, R {
		String sideName() {
			return "1";
		}
	};
	
	abstract String sideName();
}
