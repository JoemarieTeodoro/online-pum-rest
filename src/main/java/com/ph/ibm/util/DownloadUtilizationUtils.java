package com.ph.ibm.util;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public class DownloadUtilizationUtils {

	private static final String PERCENT = "%";

	private static final String PERCENT_FORMAT = "0.00'%'";

	public static Double convertPercentageToDouble(String percentage) {
		String percent = percentage.substring(0, percentage.indexOf(PERCENT));
		return Double.parseDouble(percent);
	}

	public static String toPercent(double value) {
		DecimalFormat dFormat = new DecimalFormat(PERCENT_FORMAT);
		return dFormat.format(value);
	}

	public static TreeMap<String, String> populateTotalHoursList(List<Map<String, Double>> onshoreHours,
			List<Map<String, Double>> offshoreHours) {
		TreeMap<String, String> totalHoursMap = new TreeMap<>();

		return totalHoursMap = populateTotalHoursMap(onshoreHours, offshoreHours);
	}

	public static TreeMap<String, String> populateTotalHoursList(Map<String, Integer> offshoreHours,
			Map<String, Integer> onshoreHours) {
		TreeMap<String, String> totalMap = new TreeMap<>();
		for (Entry<String, Integer> offShoreElement : offshoreHours.entrySet()) {
			for (Entry<String, Integer> onshoreElement : onshoreHours.entrySet()) {
				if (isWeekColumnEqual(offShoreElement, onshoreElement)) {
					double total = offShoreElement.getValue() + onshoreElement.getValue();
					totalMap.put(offShoreElement.getKey(), String.valueOf(total));
					break;
				}
			}
		}
		return totalMap;
	}

	public static TreeMap<String, String> populateTotalHoursList(TreeMap<String, String> totalBillableHours,
			TreeMap<String, String> totalAvailableHours) {
		TreeMap<String, String> totalMap = new TreeMap<>();
		for (Entry<String, String> billableElement : totalBillableHours.entrySet()) {
			for (Entry<String, String> avaialableElement : totalAvailableHours.entrySet()) {
				if (isWeekEndingEqual(billableElement, avaialableElement)) {
					double billableHours = Double.valueOf(billableElement.getValue());
					double availableHours = Double.valueOf(avaialableElement.getValue());
					double total = billableHours / availableHours;
					totalMap.put(billableElement.getKey(), FormatUtils.toPercentage(total));
					break;
				}
			}
		}
		return totalMap;
	}

	private static TreeMap<String, String> populateTotalHoursMap(List<Map<String, Double>> onshoreHours,
			List<Map<String, Double>> offshoreHours) {
		TreeMap<String, String> totalMap = new TreeMap<>();
		for (Map<String, Double> onshoreMap : onshoreHours) {
			for (Map<String, Double> offshoreMap : offshoreHours) {
				for (Entry<String, Double> onshoreElement : onshoreMap.entrySet()) {
					for (Entry<String, Double> offshoreElement : offshoreMap.entrySet()) {
						if (isColumnEqual(offshoreElement, onshoreElement)) {
							double total = offshoreElement.getValue() + onshoreElement.getValue();
							totalMap.put(offshoreElement.getKey(), String.valueOf(total));
							break;
						}
					}
				}
			}

		}
		return totalMap;
	}

	private static boolean isColumnEqual(Entry<String, Double> offShoreElement, Entry<String, Double> onshoreElement) {
		return offShoreElement.getKey().equals(onshoreElement.getKey());
	}

	private static boolean isWeekColumnEqual(Entry<String, Integer> offShoreElement,
			Entry<String, Integer> onshoreElement) {
		return offShoreElement.getKey().equals(onshoreElement.getKey());
	}

	private static boolean isWeekEndingEqual(Entry<String, String> billableElement,
			Entry<String, String> avaialableElement) {
		return billableElement.getKey().equals(avaialableElement.getKey());
	}
}
