
public enum Mode {
	ACC {
		String modeName() {
			return "0";
		}
	}, ENTER {
		public String modeName() {
			return "1";
		}
	}, EXIT {
		public String modeName() {
			return "2";
		}
	};
	
	abstract String modeName();
}
