
public enum Sensor {
	ACC {
		String sensorName() {
			return "0";
		}
	}, MGN {
		String sensorName() {
			return "1";
		}
	}, GYR {
		String sensorName() {
			return "2";
		}
	};
	abstract String sensorName();
}
