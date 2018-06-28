package pwcg.core.utils;

import java.io.File;
import java.io.FilenameFilter;

public class PwcgFileNameFilter implements FilenameFilter
{
	String filterString = "";
	
	PwcgFileNameFilter(String filterString)
	{
		this.filterString = filterString;
	}
	
	@Override
	public boolean accept(File directory, String fileName) 
	{
		return fileName.toLowerCase().endsWith(filterString.toLowerCase());	
	}
}
