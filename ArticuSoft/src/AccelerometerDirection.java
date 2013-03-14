
public enum AccelerometerDirection {
	X {
		String accName() {
			return "0";
		}
	}, Y {
		String accName() {
			return "1";
		}
	}, Z {
		String accName() {
			return "2";
		}
	};
	
	abstract String accName();
}
