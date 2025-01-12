package com.car.common.utils.token;



import java.util.stream.Stream;

public class Baggages {

	public static String getBaggage(String key){
		if(SleuthUtils.getBaggage("Baggage") == null) {
			return null;
		}

		return Stream.of(SleuthUtils.getBaggage("Baggage").split(","))
				.filter(item -> item.startsWith(key))
				.map(item -> item.substring(key.length() + 1))
				.findFirst()
				.orElse(null);
	}

	public static void setBaggage(String key, String value){
		if(SleuthUtils.getBaggage("Baggage") == null) {
			SleuthUtils.setBaggage("Baggage", key + ":" + value);
		}

		String baggage = SleuthUtils.getBaggage("Baggage");

		String itemString = Stream.of(baggage.split(","))
				.filter(item -> item.startsWith(key))
				.findFirst()
				.orElse(null);

		if(itemString == null) {
			SleuthUtils.setBaggage("Baggage", baggage + "," + key + ":" + value);
		}else {
			SleuthUtils.setBaggage("Baggage", baggage.replaceAll(itemString, key + ":" + value));
		}
	}

	public static void setToken(String value){
		SleuthUtils.setBaggage("Token", value);
	}

	public static String getToken(){
		return SleuthUtils.getBaggage("Token");
	}

}
