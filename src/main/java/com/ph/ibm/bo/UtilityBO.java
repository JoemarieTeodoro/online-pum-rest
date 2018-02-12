package com.ph.ibm.bo;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.ph.ibm.model.Holiday;
import com.ph.ibm.model.PUMYear;
import com.ph.ibm.model.Utilization;
import com.ph.ibm.model.UtilizationJson;
import com.ph.ibm.model.UtilizationYear;
import com.ph.ibm.model.Year;
import com.ph.ibm.report.pum.data.PUMExcelData;
import com.ph.ibm.repository.HolidayEngagementRepository;
import com.ph.ibm.repository.PUMYearRepository;
import com.ph.ibm.repository.UtilizationEngagementRepository;
import com.ph.ibm.repository.impl.HolidayRepositoryImpl;
import com.ph.ibm.repository.impl.PUMYearRepositoryImpl;
import com.ph.ibm.repository.impl.UtilizationEngagementRepositoryImpl;
import com.ph.ibm.util.ObjectMapperAdapter;
import com.ph.ibm.report.pum.PUMYearlyUtilizationXLSXReport;

public class UtilityBO {

	private UtilizationEngagementRepository utilizationEngagementRepository = new UtilizationEngagementRepositoryImpl();

	private HolidayEngagementRepository holidayRepository = new HolidayRepositoryImpl();

    private PUMYearRepository pumYearRepository = new PUMYearRepositoryImpl();
    
    private ReportUtilizationBO reportUtilizationBO = new ReportUtilizationBO();

	public Response downloadUtilizationReport(String periodKey, int periodValue, String filePath) throws Exception {
		PUMExcelData pumExcelData = setupPumExcelData();
		pumExcelData = reportUtilizationBO.getUtilizationPerEmployee(pumExcelData); 
		PUMYearlyUtilizationXLSXReport report = new PUMYearlyUtilizationXLSXReport(pumExcelData);	
		return report.generateReport();
	}

	private PUMExcelData setupPumExcelData() throws SQLException {
		PUMYear currentFiscalYear = pumYearRepository.retrieveCurrentFY();
		List<Holiday> listOfHolidays = holidayRepository.getAllHoliday(currentFiscalYear);
		return new PUMExcelData(String.valueOf(currentFiscalYear.getPumYear()), listOfHolidays, null);
	}

	public boolean saveUtilization(Utilization utilization) throws SQLException {
		return utilizationEngagementRepository.saveUtilization(utilization);
	}

	/**
	 * This method is used to get utilization from utilization table
	 *
	 * @param employeeIdNumber
	 * @param year
	 * @return String
	 * @throws SQLException
	 */
	public String fetchUtilizations(String employeeIdNumber, String year) throws SQLException {
		List<Utilization> utilizations = new ArrayList<Utilization>();
		utilizations = utilizationEngagementRepository.retrieveUtilizations(employeeIdNumber, year);
		if (utilizations.size() > 0) {
			UtilizationYear utilizationYear = ObjectMapperAdapter.unmarshal(utilizations.get(0).getUtilizationJson(),
					UtilizationYear.class);

			LocalDateTime now = LocalDateTime.now();
			int currentYear = now.getYear();
			int currentMonth = now.getMonthValue();
			int currentDay = now.getDayOfMonth();
			int counter = 0;
			for (UtilizationJson json : utilizationYear.getUtilizationJSON()) {
				if (utilizationYear.getYear() < currentYear) {
					json.setEditable("D");
					counter++;
				} else if (utilizationYear.getYear() == currentYear) {
					// Reduce indentation
					if (json.getMonth() < currentMonth) {
						json.setEditable("D");
						counter++;
					} else if (json.getMonth() == currentMonth) {
						if (json.getDayOfMonth() < currentDay) {
							json.setEditable("D");
							counter++;
						} else {
							json.setEditable("E");
							counter++;
						}
					} else {
						json.setEditable("E");
						counter++;
					}
				} else {
					json.setEditable("E");
					counter++;
				}
			}
			String finalJson = ObjectMapperAdapter.marshal(utilizationYear);
			System.out.println(finalJson);
			return finalJson;
		} else {
			return null;
		}
	}

	/**
	 *
	 *
	 * @param
	 * @param year
	 * @return Response
	 * @throws SQLException
	 * @throws ParseException
	 */
	public Year getYTDComputation(String employeeSerial, int year) throws SQLException, ParseException {
		Utilization utilization = utilizationEngagementRepository.getComputation(employeeSerial, year);
		UtilizationYear utilization_Year = ObjectMapperAdapter.unmarshal(utilization.getUtilizationJson(),
				UtilizationYear.class);
		Year ytdComputation = new Year();
		double hours = 0; // TODO: change to big decimal
		double totalActualHours = 0;
		double availableHours = 0;
		double VLcount = 0;
		double SLcount = 0;
		double ELcount = 0;
		double OLcount = 0;
		double HOcount = 0;
		double TRcount = 0;
		double CDOcount = 0;
		double availableHoursCounter = 0;
		double YTD = 0;
		DecimalFormat formatter = new DecimalFormat("#0.00");
		// String ytdResult = formatter.format(YTD);

		// ytdBD.

		for (UtilizationJson json : utilization_Year.getUtilizationJSON()) {
			if (json.getUtilizationHours().equalsIgnoreCase("VL") || json.getUtilizationHours().equals("SL")
					|| json.getUtilizationHours().equals("OL") || json.getUtilizationHours().equals("EL")
					|| json.getUtilizationHours().equals("HO") || json.getUtilizationHours().equals("TR")
					|| json.getUtilizationHours().equals("CDO") || json.getUtilizationHours().equals("")) {

				hours = 0;
			} else if (json.getUtilizationHours() != null) {
				hours = Integer.parseInt(json.getUtilizationHours());

			}
			if (json.getUtilizationHours().equals("VL")) {
				VLcount++;
			}
			if (json.getUtilizationHours().equals("SL")) {
				SLcount++;
			}
			if (json.getUtilizationHours().equals("OL")) {
				OLcount++;
			}
			if (json.getUtilizationHours().equals("EL")) {
				ELcount++;
			}
			if (json.getUtilizationHours().equals("CDO")) {
				CDOcount++;
			}
			if (json.getUtilizationHours().equals("TR")) {
				TRcount++;
			}
			if (json.getUtilizationHours().equals("HO")) {
				HOcount++;
			}
			if (json.getDay() != 6 && json.getDay() != 1) {
				availableHoursCounter++;
			}

			if (json.getMonth() <= 12) {
				totalActualHours = totalActualHours + hours;
			}
			availableHours = availableHoursCounter * 8;
			YTD = (totalActualHours / availableHours) * 100;

		}

		ytdComputation.setTotalHours(totalActualHours);
		ytdComputation.setNumberOfVL(VLcount);
		ytdComputation.setNumberOfSL(SLcount);
		ytdComputation.setNumberOfEL(ELcount);
		ytdComputation.setNumberOfOL(OLcount);
		ytdComputation.setNumberOfTR(TRcount);
		ytdComputation.setNumberOfHO(HOcount);
		ytdComputation.setNumberOfCDO(CDOcount);
		ytdComputation.setNumberOfAvailableHours(availableHours);
		ytdComputation.setYearToDateUtilization(Double.parseDouble(formatter.format(YTD)));

		return ytdComputation;
	}

}
