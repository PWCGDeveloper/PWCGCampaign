package pwcg.gui.campaign.activity;

import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignLog;
import pwcg.campaign.CampaignLogEntry;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;

public class CampaignSquadronLogDetailPageBuilder
{
	private Campaign campaign = null;
    private int linesPerPage = 20;
    private int charsPerLine = 50;
    private int logsForSquadronId = 0;

    public CampaignSquadronLogDetailPageBuilder (int logsForSquadronId)
	{        
        this.logsForSquadronId = logsForSquadronId;

        calculateLinesPerPage();
        calculateCharsPerLine();
        adjustTextForFontSize();

		this.campaign = PWCGContext.getInstance().getCampaign();
	}

    public Map<Integer, StringBuffer> buildDetailPages()
	{
		Map<Integer, StringBuffer> squadronDetailPages = new TreeMap<Integer, StringBuffer>();
		
		StringBuffer page = new StringBuffer("\n\n");
		int pageCount = 1;

        for (CampaignLog campaignLog : campaign.getCampaignLogs().retrieveCampaignLogsInDateOrder())
		{            
			// Don't end the page with a date entry. leave room for at least one logs
			if (countLines(page.toString()) >= (linesPerPage-5))
			{
				squadronDetailPages.put(pageCount, page);
				++pageCount;
                page = new StringBuffer("\n\n");
			}
			
			String dateAsString = DateUtils.getDateStringPretty(campaignLog.getDate());
            page.append("\n");
			page.append(dateAsString);
            page.append("\n");
			
        	for (CampaignLogEntry logEntry : campaignLog.getLogs())
			{
        	    if (logEntry.getSquadronId() != logsForSquadronId)
        	    {
        	        continue;
        	    }
        	    
                int linesCountedEntry = countLines(logEntry.getLog());
                int linesCountedPage = countLines(page.toString());
				if ((linesCountedPage+linesCountedEntry) >= linesPerPage)
				{
					squadronDetailPages.put(pageCount, page);
					++pageCount;
	                page = new StringBuffer("\n\n");
				}
				
                page.append(logEntry.getLog() + "\n");
			}
		}
		
		squadronDetailPages.put(pageCount, page);
		
        return squadronDetailPages;
	}

	private int countLines(String logLine)
	{
	    String[] lines = logLine.split("\r\n|\r|\n");
	    int calculatedLines = lines.length;
	    for (String line : lines)
	    {
	        if (line.length() > charsPerLine)
	        {
	            ++calculatedLines;
	        }
	    }
	    
	    
	    return  calculatedLines;
	}

    private void adjustTextForFontSize()
    {
        try
        {
            int fontSize = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.CursiveFontSizeKey);
            
            if (fontSize > 20)
            {
                charsPerLine -= 10;
                linesPerPage -= 10;
            }
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }
    }

    private void calculateLinesPerPage()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
        linesPerPage = 40;
        if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            linesPerPage = 30;
        }
        else if (monitorSize == MonitorSize.FRAME_SMALL)
        {
            linesPerPage = 25;
        }
        else if (monitorSize == MonitorSize.FRAME_VERY_SMALL)
        {
            linesPerPage = 20;
        }
    }

    private void calculateCharsPerLine()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameWidth();
        charsPerLine = 70;
        if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            charsPerLine = 60;
        }
        else if (monitorSize == MonitorSize.FRAME_SMALL)
        {
            charsPerLine = 45;
        }
        else if (monitorSize == MonitorSize.FRAME_VERY_SMALL)
        {
            charsPerLine = 35;
        }
    }
}
