
public enum Handstate {
	NONE {
		String handstateName() {
			return "0";
		}
	}, INDEX {
		String handstateName() { 
			return "1";
		}
	}, MIDDLE {
		String handstateName() {
			return "2";
		}
	};
	
	abstract String handstateName();
}
