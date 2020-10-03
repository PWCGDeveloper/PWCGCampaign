package pwcg.gui.campaign.journal;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignLog;
import pwcg.campaign.CampaignLogEntry;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PageTurner;

public class CampaignSquadronLogScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private JPanel logPagesGridPanel = new JPanel();
    private JPanel pageTurnerPanel = null;

	private Campaign campaign = null;
    private int linesPerPage = 20;
    private int charsPerLine = 50;
    private int logsForSquadronId = 0;
	
	private JPanel leftpage = null;
	private JPanel rightpage = null;

	private int pageNum = 0;
	private Map<Integer, StringBuffer> pages = null;

	public CampaignSquadronLogScreen (int logsForSquadronId)
	{        
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.logsForSquadronId = logsForSquadronId;

        calculateLinesPerPage();
        calculateCharsPerLine();
        adjustTextForFontSize();

		this.campaign = PWCGContext.getInstance().getCampaign();
	}

	public void makeVisible(boolean visible) 
	{
	}
	
	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignSquadronLogScreen);
        this.setImage(imagePath);

		pages = orderPageEntries();
		this.add(BorderLayout.WEST, makeLogLeftPanel());
		this.add(BorderLayout.CENTER, makeLogCenterPanel());
		
        makePages();        
	}

	private Map<Integer, StringBuffer> orderPageEntries()
	{
		Map<Integer, StringBuffer> pages = new TreeMap<Integer, StringBuffer>();
		
		StringBuffer page = new StringBuffer("\n\n");
		int pageCount = 0;

        for (CampaignLog campaignLog : campaign.getCampaignLogs().retrieveCampaignLogsInDateOrder())
		{            
			// Don't end the page with a date entry. leave room for at least one logs
			if (countLines(page.toString()) >= (linesPerPage-5))
			{
				pages.put(pageCount, page);
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
					pages.put(pageCount, page);
					++pageCount;
	                page = new StringBuffer("\n\n");
				}
				
                page.append(logEntry.getLog() + "\n");
			}
		}
		
		pages.put(pageCount, page);
		
		return pages;
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

	private JPanel makeLogLeftPanel() throws PWCGException  
	{

        JPanel squadronLogPanel = new JPanel(new BorderLayout());
		squadronLogPanel.setOpaque(false);

		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
		buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished Reading", "SquadronLogFinished", "Leave Squadron Log", this);
        buttonPanel.add(finishedButton);

		squadronLogPanel.add(buttonPanel, BorderLayout.NORTH);
		
		return squadronLogPanel;
	}

	private JPanel  makeLogCenterPanel() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.OpenSquadronLog);
        ImageResizingPanel logCenterPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        logCenterPanel.setLayout(new BorderLayout());
        logCenterPanel.setOpaque(false);
        
        logPagesGridPanel = new JPanel();
        logPagesGridPanel.setLayout(new GridLayout(0,2));
        logPagesGridPanel.setOpaque(false);
        
        logCenterPanel.add(logPagesGridPanel, BorderLayout.CENTER);
        
        return logCenterPanel;

	}

	private void makePages() throws PWCGException  
	{
	    logPagesGridPanel.removeAll();

		if (leftpage != null)
		{
			leftpage.removeAll();
			leftpage = null;
		}
		
		if (rightpage != null)
		{
			rightpage.removeAll();
			rightpage = null;
		}
		
		StringBuffer leftPageEntries = new StringBuffer("");
		int leftPageNum = pageNum;
		if (pages.size() > leftPageNum)
		{
			leftPageEntries = pages.get(leftPageNum);
		}

        StringBuffer rightPageEntries = new StringBuffer("");
		int rightPageNum = pageNum + 1;
		if (pages.size() > (rightPageNum))
		{
			rightPageEntries = pages.get(rightPageNum);
		}

		leftpage = makePage (leftPageEntries, leftPageNum);
		rightpage = makePage (rightPageEntries, rightPageNum);

        if (leftpage != null)
        {
            logPagesGridPanel.add(leftpage);
        }
        
        if (rightpage != null)
        {
            logPagesGridPanel.add(rightpage);
        }
        
		
		addPageTurner();
	}

    private void addPageTurner() throws PWCGException
    {
        if (pageTurnerPanel != null)
        {
            this.remove(pageTurnerPanel);
        }
        
        int numPages = pages.size();
        pageTurnerPanel = PageTurner.makeButtonPanel(pageNum+1, numPages, this);
        this.add(pageTurnerPanel, BorderLayout.SOUTH);
    }

	private JPanel makePage(StringBuffer pageEntries, int pageNum) throws PWCGException 
	{
		JPanel campaignLogPanelBorder = new JPanel();
		campaignLogPanelBorder.setLayout(new BorderLayout());
		campaignLogPanelBorder.setOpaque(false);

		JPanel campaignLogPanel = new JPanel();
		campaignLogPanel.setLayout(new GridLayout(0,1));
		campaignLogPanel.setOpaque(false);

		Font font = PWCGMonitorFonts.getCursiveFont();

        final JTextArea logTextArea= new JTextArea();
        
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setEditable(false);
        logTextArea.setOpaque(false);
        logTextArea.setFont(font);
        logTextArea.setSize(logTextArea.getPreferredSize().width, 1);
        
        Insets margins = PWCGMonitorBorders.calculateBorderMargins(15, 50, 15, 20);
        if (pageNum%2 != 0)
        {
            margins = PWCGMonitorBorders.calculateBorderMargins(15, 20, 15, 35);           
        }
        logTextArea.setMargin(margins);

		logTextArea.setText(pageEntries.toString());
		campaignLogPanel.add(logTextArea);
		
        final JTextArea bufferTextArea= new JTextArea();
        bufferTextArea.setOpaque(false);
        
		campaignLogPanelBorder.add(bufferTextArea, BorderLayout.WEST);
		campaignLogPanelBorder.add(campaignLogPanel, BorderLayout.CENTER);
		
		return campaignLogPanelBorder;
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("SquadronLogFinished"))
            {
                campaign.write();                
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
            else if (action.equalsIgnoreCase("Next Page"))
			{
                nextPage();
			}
			else if (action.equalsIgnoreCase("Previous Page"))
			{
                previousPage();
			}
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private void previousPage() throws PWCGException
    {
        SoundManager.getInstance().playSound("PageTurn.WAV");
        this.pageNum -= 2;
        makePages();
        this.revalidate();
        this.repaint();
    }

    private void nextPage() throws PWCGException
    {
        SoundManager.getInstance().playSound("PageTurn.WAV");
        this.pageNum += 2;
        makePages();
        this.revalidate();
        this.repaint();
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
