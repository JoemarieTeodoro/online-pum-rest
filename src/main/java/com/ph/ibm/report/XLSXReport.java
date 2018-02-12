package com.ph.ibm.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class XLSXReport {
	
	protected abstract XSSFWorkbook populateWorkbook();
	
	protected abstract String getFileName();
	
	public Response generateReport() throws FileNotFoundException, IOException {
		XSSFWorkbook workbook = populateWorkbook();
		File file = new File(getFileName());
		FileOutputStream fileStream = new FileOutputStream(file);

		try {
			workbook.write(fileStream);

			ResponseBuilder response = Response.ok(file);
			response.header("Content-Disposition","attachement; filename=" + getFileName());
			return response.build();
		}
		catch (IOException io) {
			io.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(workbook).type(MediaType.APPLICATION_OCTET_STREAM)
					.build();
		}
		finally{
			workbook.close();
			fileStream.close();
		}
	}
}
