package com.ph.ibm.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class UploaderUtils {

    /**
     * This method is to populate list directly from CSV file
     *
     * @param rawData Data from the CSV file
     * @return Populated list of employee row data from CSV file
     */
	public static Map<String, List<String>> populateList(String rawData) {
		Map<String, List<String>> rawDataMap = new HashMap<String, List<String>>();
		
        List<String> row;
        String delimiter = ",";
        Scanner sc = new Scanner( rawData );
        ignoreFirstRow( sc );
        while( sc.hasNextLine() ){
            String line = sc.nextLine();
            if( !isRowEmpty( line ) && !line.startsWith( "----" ) ){
                row = Arrays.asList( line.split( delimiter ) );
                
                rawDataMap.put(row.get(0), row);
            }
        }
        sc.close();
        return rawDataMap;
	}

    /**
     * This method used to ignore the header of the CSV file
     *
     * @param sc Scanner
     */
    public static void ignoreFirstRow( Scanner sc ) {
        while( sc.hasNextLine() ){
            String line = sc.nextLine();
            if( isRowEmpty( line ) ){
                sc.nextLine();
                break;
            }
        }
    }

    /**
     * This method used to validate if CSV row data is Empty
     *
     * @param line represents row in csv file
     * @return boolean true if row is empty otherwise false
     */
    public static boolean isRowEmpty( String line ) {
        return line == null || line.equals( "\\n" ) || line.equals( "" );
    }

    /**
     * This method is used to generate error message for Upload List
     *
     * @param uriInfo uri information
     * @param e employee object
     * @param errorMessage error message
     * @return Response response
     */
    public static Response invalidCsvResponseBuilder( UriInfo uriInfo, Object e, String errorMessage ) {
        String invalidCsv;
        invalidCsv = String.format(
            "Invalid CSV for %s \n\nError message: %s",
            e.toString(), errorMessage, uriInfo );
        return Response.status( 206 ).entity(
            invalidCsv ).build();
    }
}
