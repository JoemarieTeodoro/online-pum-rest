package com.ph.ibm.bo;

import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.ph.ibm.model.PUMDownloadReportMonth;
import com.ph.ibm.repository.PUMMonthRepository;
import com.ph.ibm.repository.impl.PUMMonthRepositoryImpl;

public class PUMMonthBO {
	private PUMMonthRepository pumMonthRepository = new PUMMonthRepositoryImpl();

	public void saveMonth(List<PUMDownloadReportMonth> pumMonth) throws Exception {
		pumMonthRepository.saveMonthEndingDates(pumMonth);
	}

}
