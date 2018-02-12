package com.ph.ibm.report.pum.renderer;

public interface PUMRenderer {

	public void render();
	
	public int getColumnStartIndex();
	
	public void columnUsed();
	
	public int getColumnEndIndex();
	
	public int getColumnsUsed();
}
