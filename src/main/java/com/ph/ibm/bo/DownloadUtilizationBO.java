package com.ph.ibm.bo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ph.ibm.model.BillableHours;
import com.ph.ibm.model.ForecastedHours;
import com.ph.ibm.model.OutlookHours;
import com.ph.ibm.model.OverallUtilizationReport;
import com.ph.ibm.model.YtdUtilization;
import com.ph.ibm.repository.DownloadUtilizationRepository;
import com.ph.ibm.repository.UtilizationRepository;
import com.ph.ibm.repository.impl.DownloadUtilizationRepositoryImpl;
import com.ph.ibm.repository.impl.UtilizationRepositoryImpl;
import com.ph.ibm.util.OpumConstants;

/**
 * @author <a HREF="dacanam@ph.ibm.com">Marjay Dacanay</a>
 * @author <a HREF="teodorj@ph.ibm.com">Joemarie Teodoro</a>
 */
public class DownloadUtilizationBO {

    private DownloadUtilizationRepository downloadUtilizationRepository;

    private OverallUtilizationReport overallUtilizationReport;

    private UtilizationRepository utilizationRepository;

    public DownloadUtilizationBO() {
        downloadUtilizationRepository = new DownloadUtilizationRepositoryImpl();
        utilizationRepository = new UtilizationRepositoryImpl();
        overallUtilizationReport = new OverallUtilizationReport();
    }

    public Response downloadOverallUtilization( YtdUtilization ytdUtilization ) throws SQLException {
        //START DATE & END DATE// can be used for headers "BILLABLE FROM start - end"
        System.out.println( ytdUtilization.getStartDate() );
        System.out.println( ytdUtilization.getEndDate() );

        TreeMap<String, TreeMap<Integer, String>> fyWeekMapping = new TreeMap<>();
        Map<String, String> weekMap = downloadUtilizationRepository.retrieveWeeks( ytdUtilization );
        fyWeekMapping = downloadUtilizationRepository.retrieveFiscalYear( ytdUtilization );
        List<Map<String, Double>> listOffshoreActualHours = new ArrayList<>();
        List<Map<String, Double>> listOnshoreActualHours = new ArrayList<>();
        List<Map<String, Double>> listOffshoreForecastHours = new ArrayList<>();
        List<Map<String, Double>> listOnshoreForecastHours = new ArrayList<>();

        TreeMap<String, TreeMap<String, String>> employeeOffshoreList =
            downloadUtilizationRepository.getRollInRollOffDate( OpumConstants.OFFSHORE );
        TreeMap<String, TreeMap<String, String>> employeeOnshoreList =
            downloadUtilizationRepository.getRollInRollOffDate( OpumConstants.ONSHORE );

        TreeMap<String, Integer> offshoreAvailableHoursMap =
            overallUtilizationReport.calculateWeeklyTotalAvailableHours(
                overallUtilizationReport.getAvailableHoursOffshore( employeeOffshoreList, weekMap ) );
        TreeMap<String, Integer> onshoreAvailableHoursMap = overallUtilizationReport.calculateWeeklyTotalAvailableHours(
            overallUtilizationReport.getAvailableHoursOnshore( employeeOnshoreList, weekMap ) );
        //System.out.println( downloadUtilizationRepository.retrieveEmployeeList() );

        retrieveOverallUtilization( fyWeekMapping, listOffshoreActualHours, listOnshoreActualHours,
            listOffshoreForecastHours, listOnshoreForecastHours );

        Double availableOffshoreTotalHours =
            overallUtilizationReport.calculateSumOfTotalAvailableHours( offshoreAvailableHoursMap );
        Double availableOnshoreTotalHours =
            overallUtilizationReport.calculateSumOfTotalAvailableHours( onshoreAvailableHoursMap );
        Double availableHoursGrandTotal = overallUtilizationReport.calculateSumOfGrandTotalAvailableHours(
            availableOffshoreTotalHours, availableOnshoreTotalHours );
        overallUtilizationReport.setGrandTotalAvailableHours( availableHoursGrandTotal );

        Map<String, Double> outlookHoursMap = getTotalOutlookHours( fyWeekMapping );
        TreeMap<String, String> sumOfOutlookHours =
            overallUtilizationReport.calculateSumOfTotalOutlookHours( outlookHoursMap );

        //start of billable hours
        System.out.println( "BILLABLE HOURS----------------------------------------------------" );
        System.out.println( "Offshore Billable Hours: " + listOffshoreActualHours );
        System.out.println( "Onshore Billable Hours: " + listOnshoreActualHours );
        TreeMap<String, String> totalBillableHoursMap =
            overallUtilizationReport.getReportGrandTotalMap( listOffshoreActualHours, listOnshoreActualHours );
        System.out.println( "Total Billable Hours: " + totalBillableHoursMap );
        Double billableOffshoreTotalHours =
            overallUtilizationReport.calculateSumOfTotalBillableHours( listOffshoreActualHours );
        Double billableOnshoreTotalHours =
            overallUtilizationReport.calculateSumOfTotalBillableHours( listOnshoreActualHours );
        Double billableHoursGrandTotal = overallUtilizationReport.calculateSumOfGrandTotalBillableHours(
            billableOffshoreTotalHours, billableOnshoreTotalHours );
        overallUtilizationReport.setGrandTotalBillableHours( billableHoursGrandTotal );
        System.out.println( "Grand Total Billable Hours: " + overallUtilizationReport.getGrandTotalBillableHours() );
        //end of billable hours

        //start of avaiable hours
        System.out.println( "AVAILABLE HOURS----------------------------------------------------" );
        System.out.println( "Offshore Available Hours: " + offshoreAvailableHoursMap );
        System.out.println( "Onshore Available Hours: " + onshoreAvailableHoursMap );
        TreeMap<String, String> totalAvailableHoursMap =
            overallUtilizationReport.getReportGrandTotalMap( offshoreAvailableHoursMap, onshoreAvailableHoursMap );
        System.out.println( "Total Available Hours: " + totalAvailableHoursMap );
        System.out.println( "Grand Total Available Hours: " + overallUtilizationReport.getGrandTotalAvailableHours() );
        //end of available hours

        //start of ute percentage
        System.out.println( "UTE ACTUALS----------------------------------------------------" );
        TreeMap<String, String> offShoreUtePercentage =
            overallUtilizationReport.calculateWeeklyUtePercentage( listOffshoreActualHours, offshoreAvailableHoursMap );
        TreeMap<String, String> onshoreUtePercentage =
            overallUtilizationReport.calculateWeeklyUtePercentage( listOnshoreActualHours, onshoreAvailableHoursMap );
        System.out.println( "Offshore UTE Percentage: " + offShoreUtePercentage );
        System.out.println( "Onshore UTE Percentage: " + onshoreUtePercentage );
        System.out.println( "Total UTE Percentage: " +
            overallUtilizationReport.getReportGrandTotalMap( totalBillableHoursMap, totalAvailableHoursMap ) );
        System.out.println( overallUtilizationReport.calculateAverageUtePercentage( offShoreUtePercentage ) );
        System.out.println( overallUtilizationReport.calculateAverageUtePercentage( onshoreUtePercentage ) );

        //end of ute percentage
        System.out.println( "FORECAST HOURS----------------------------------------------------" );
        System.out.println( "Offshore Forecast Hours: " + listOffshoreForecastHours );
        System.out.println( "Onshore Forecast Hours: " + listOnshoreForecastHours );
        System.out.println( "Total Forecast Hours: " +
            overallUtilizationReport.getReportGrandTotalMap( listOffshoreForecastHours, listOnshoreForecastHours ) );
        Double forecastedOffshoreTotalHours =
            overallUtilizationReport.calculateSumOfTotalForecastedHours( listOffshoreForecastHours );
        Double forecastedOnshoreTotalHours =
            overallUtilizationReport.calculateSumOfTotalForecastedHours( listOnshoreForecastHours );
        Double forecastedHoursGrandTotal = overallUtilizationReport.calculateSumOfGrandTotalForecastedHours(
            forecastedOffshoreTotalHours, forecastedOnshoreTotalHours );
        overallUtilizationReport.setGrandTotalForecastedHours( forecastedHoursGrandTotal );
        System.out.println( "Grand Total Billable Hours: " + overallUtilizationReport.getGrandTotalForecastedHours() );

        System.out.println( "OUTLOOK HOURS----------------------------------------------------" );
        System.out.println( outlookHoursMap );
        System.out.println( sumOfOutlookHours );

        return Response.status( Status.OK ).build();

        /* try{
            return generateExcelReport( fyWeekMapping, listOffshoreActualHours, listOnshoreActualHours,
                listOffshoreForecastHours, listOnshoreForecastHours, listOutlookHours );
        }
        catch( IOException e ){
            e.printStackTrace();
            return Response.status( Status.BAD_REQUEST ).build();
        }*/
    }

    private Response generateExcelReport( TreeMap<String, TreeMap<Integer, String>> fyWeekMapping,
                                          List<Map<String, Double>> lstOffshoreActualHours,
                                          List<Map<String, Double>> lstOnshoreActualHours,
                                          List<Map<String, Double>> lstOffshoreForecastHours,
                                          List<Map<String, Double>> lstOnshoreForecastHours,
                                          Map<String, Double> lstOutlookHours )
        throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        try{
            String filePath = "overallUtil.xlsx";
            File file = new File( filePath );
            OutputStream fileOutputStream = new FileOutputStream( file );
            Sheet sheet = workbook.createSheet();

            createActualsSection( sheet, lstOffshoreActualHours, lstOnshoreActualHours );

            workbook.write( fileOutputStream );

            ResponseBuilder response = Response.ok( file );
            response.header( "Content-Disposition", "attachement; filename=" + file.getName() );
            return response.build();
        }
        catch( IOException e ){
            e.printStackTrace();
            return Response.status( Status.BAD_REQUEST ).build();
        }
        finally{
            workbook.close();
        }
    }

    private void createActualsSection( Sheet sheet, List<Map<String, Double>> lstOffshoreActualHours,
                                       List<Map<String, Double>> lstOnshoreActualHours ) {
        int lastActualsRow = 25;
        int lastActualsColumn = 200;
        for( int i = 0; i < lastActualsRow; i++ ){
            Row row = sheet.createRow( i );
            for( int j = 0; j < lastActualsColumn; j++ ){
                Cell cell = row.createCell( j );
                cell.setCellValue( j );
            }
        }
    }

    /**
     * @param fyWeekMapping
     * @param listOffshoreActualHours
     * @param listOnshoreActualHours
     * @param listOffshoreForecastHours
     * @param listOnshoreForecastHours
     * @param listOutlookHours
     * @throws SQLException
     */
    private void retrieveOverallUtilization( TreeMap<String, TreeMap<Integer, String>> fyWeekMapping,
                                             List<Map<String, Double>> listOffshoreActualHours,
                                             List<Map<String, Double>> listOnshoreActualHours,
                                             List<Map<String, Double>> listOffshoreForecastHours,
                                             List<Map<String, Double>> listOnshoreForecastHours )
        throws SQLException {
        for( Entry<String, TreeMap<Integer, String>> fyWeekMap : fyWeekMapping.entrySet() ){
            String weekName = fyWeekMap.getKey();
            for( Entry<Integer, String> map : fyWeekMap.getValue().entrySet() ){
                int yearId = map.getKey();
                String weekEnding = map.getValue();
                listOffshoreActualHours.add( getTotalOffshoreActualHours( weekName, yearId, weekEnding ) );
                listOnshoreActualHours.add( getTotalOnshoreActualHours( weekName, yearId, weekEnding ) );
                listOffshoreForecastHours.add( getTotalOffshoreForecastHours( weekName, yearId, weekEnding ) );
                listOnshoreForecastHours.add( getTotalOnshoreForecastHours( weekName, yearId, weekEnding ) );
            }
        }
    }

    /**
     * @param weekName
     * @param yearId
     * @param weekEnding
     * @return
     */
    private Map<String, Double> getTotalOffshoreActualHours( String weekName, int yearId, String weekEnding ) {
        BillableHours billableHours = calculateTotalOffshoreHours( weekName, yearId, weekEnding );
        return billableHours.getOffshoreTotalHours();
    }

    /**
     * @param weekName
     * @param yearId
     * @param weekEnding
     * @return
     */
    private Map<String, Double> getTotalOnshoreActualHours( String weekName, int yearId, String weekEnding ) {
        BillableHours billableHours = calculateTotalOnshoreHours( weekName, yearId, weekEnding );
        return billableHours.getOnshoreTotalHours();
    }

    private BillableHours calculateTotalOnshoreHours( String weekName, int yearId, String weekEnding ) {
        BillableHours billableHours = overallUtilizationReport.getBillableHours();
        List<Map<String, List<Double>>> actualHours =
            downloadUtilizationRepository.retrieveActualHours( weekName, yearId, weekEnding, OpumConstants.ONSHORE );
        Map<String, Double> totalOnshoreHours = billableHours.calculateTotalOnshoreHours( actualHours );
        billableHours.setBillableOnshoreTotalHours( totalOnshoreHours );
        return billableHours;
    }

    private BillableHours calculateTotalOffshoreHours( String weekName, int yearId, String weekEnding ) {
        BillableHours billableHours = overallUtilizationReport.getBillableHours();
        List<Map<String, List<Double>>> actualHours =
            downloadUtilizationRepository.retrieveActualHours( weekName, yearId, weekEnding, OpumConstants.OFFSHORE );
        Map<String, Double> totalOffshoreHours = billableHours.calculateTotalOffshoreHours( actualHours );
        billableHours.setBillableOffshoreTotalHours( totalOffshoreHours );
        return billableHours;
    }

    /**
     * @param weekName
     * @param yearId
     * @param weekEnding
     * @return
     */
    private Map<String, Double> getTotalOffshoreForecastHours( String weekName, int yearId, String weekEnding ) {
        ForecastedHours forecastedHours = overallUtilizationReport.getForecastedHours();
        List<Map<String, List<Double>>> offshore_forecastedHours =
            downloadUtilizationRepository.retrieveForecastedHours( weekName, yearId, weekEnding,
                OpumConstants.OFFSHORE );
        return forecastedHours.calculateOffshoreTotalHours( offshore_forecastedHours );
    }

    /**
     * @param weekName
     * @param yearId
     * @param weekEnding
     * @return
     */
    private Map<String, Double> getTotalOnshoreForecastHours( String weekName, int yearId, String weekEnding ) {
        ForecastedHours forecastedHours = overallUtilizationReport.getForecastedHours();
        List<Map<String, List<Double>>> onshore_forecastedHours = downloadUtilizationRepository.retrieveForecastedHours(
            weekName, yearId, weekEnding, OpumConstants.ONSHORE );
        return forecastedHours.calculateOnshoreTotalHours( onshore_forecastedHours );
    }

    /**
     * @param fyWeekMapping
     * @return
     * @throws SQLException
     */
    private Map<String, Double> getTotalOutlookHours( TreeMap<String, TreeMap<Integer, String>> fyWeekMapping )
        throws SQLException {
        OutlookHours outlook = overallUtilizationReport.getOutlookHours();
        List<Map<String, List<Double>>> outlookHours = downloadUtilizationRepository.getOutlookHours( fyWeekMapping );
        return outlook.calculateTotalHours( outlookHours );
    }

}
