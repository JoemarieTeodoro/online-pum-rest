package com.ph.ibm.bo;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ph.ibm.model.UtilizationJson;

public class TimeAwayTokensTest {
	
	@Test
	public void correctlyIncrementsCountOfTimeAwayToken() {
		List<TimeAwayTokens> timeAwayTokens = Arrays.asList(TimeAwayTokens.values());
		Map<TimeAwayTokens, Double> tokenValueMap = new HashMap<>();
		
		double count = 0;
		for (TimeAwayTokens timeAwayToken : timeAwayTokens) {
			tokenValueMap.put(timeAwayToken, count);
		}
		
		List<UtilizationJson> stubJSON = new ArrayList<>();
		
		UtilizationJson json1 = new UtilizationJson();
		json1.setUtilizationHours("CDO");

		UtilizationJson json2 = new UtilizationJson();
		json2.setUtilizationHours("EL");
		
		stubJSON.add(json1);
		stubJSON.add(json2);
		
		for (UtilizationJson utilizationJson : stubJSON) {
			String utilizationHours = utilizationJson.getUtilizationHours();
			
			TimeAwayTokens key = TimeAwayTokens.valueOf(utilizationHours);
			if (tokenValueMap.get(key) != null) {
				double timeAwayCount = tokenValueMap.get(key);
				assertThat(timeAwayCount, is(0d));
				tokenValueMap.put(key, timeAwayCount + 1d);
			}
			double newTimeAwayCount = tokenValueMap.get(key);
			assertThat(newTimeAwayCount, is(1d));
		}
		
		assertThat(tokenValueMap.get(TimeAwayTokens.valueOf("CDO")), is(1d));
		assertThat(tokenValueMap.get(TimeAwayTokens.valueOf("EL")), is(1d));
	}
	
}
