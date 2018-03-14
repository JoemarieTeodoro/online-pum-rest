package com.ph.ibm.report;

public class DownloadUtilizationReportTable {

	private String firstColumnLabel;
	private String totalLabel;
	private String sumLabel;
	private String headerColumnLabel;
	private String dateLabelFormat;
	private int[] excelRows;

    public DownloadUtilizationReportTable(String firstColumnLabel, String totalLabel, String sumLabel,
			String headerColumnLabel, String dateLabelFormat, int[] excelRows) {
		super();
		this.firstColumnLabel = firstColumnLabel;
		this.totalLabel = totalLabel;
		this.sumLabel = sumLabel;
		this.headerColumnLabel = headerColumnLabel;
		this.dateLabelFormat = dateLabelFormat;
		this.excelRows = excelRows;
	}

	public String getFirstColumnLabel() {
		return firstColumnLabel;
	}

    public void setFirstColumnLabel(String firstColumnLabel) {
		this.firstColumnLabel = firstColumnLabel;
    }

	public String getTotalLabel() {
		return totalLabel;
    }

	public void setTotalLabel(String totalLabel) {
		this.totalLabel = totalLabel;
	}

	public String getSumLabel() {
		return sumLabel;
	}

	public void setSumLabel(String sumLabel) {
		this.sumLabel = sumLabel;
	}
	
	public String getHeaderColumnLabel() {
		return headerColumnLabel;
	}

	public void setHeaderColumnLabel(String headerColumnLabel) {
		this.headerColumnLabel = headerColumnLabel;
	}

	public String getDateLabelFormat() {
		return dateLabelFormat;
	}

	public void setDateLabelFormat(String dateLabelFormat) {
		this.dateLabelFormat = dateLabelFormat;
	}

	public int[] getExcelRows() {
		return excelRows;
	}

	public void setExcelRows(int[] excelRows) {
		this.excelRows = excelRows;
	}
}
