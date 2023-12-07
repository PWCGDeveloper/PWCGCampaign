package pwcg.gui.campaign.activity;

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
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHomeContext;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PageTurner;

public class CampaignSquadronLogScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private JPanel logPagesGridPanel = new JPanel();
    private JPanel pageTurnerPanel = null;

	private Campaign campaign = null;
    private int logsForSquadronId = 0;
	
	private JPanel leftpage = null;
	private JPanel rightpage = null;

	private int pageNum = 0;
	private Map<Integer, StringBuffer> pages = new TreeMap<>();

	public CampaignSquadronLogScreen (Campaign campaign, int logsForSquadronId)
	{        
        super();
        this.campaign = campaign;
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.logsForSquadronId = logsForSquadronId;
	}

	public void makeVisible(boolean visible) 
	{
	}
	
	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignSquadronLogScreen);
        this.setThemedImageFromName(CampaignHomeContext.getCampaign().getReferenceService(), imagePath);

        CampaignSquadronLogSummaryPageBuilder summaryPageBuilder = new CampaignSquadronLogSummaryPageBuilder(campaign, logsForSquadronId);
        Map<Integer, StringBuffer> summaryPages = summaryPageBuilder.buildSummaryPage();
        pages.putAll(summaryPages);

        CampaignSquadronLogDetailPageBuilder detailPageBuilder = new CampaignSquadronLogDetailPageBuilder(campaign, logsForSquadronId);
        Map<Integer, StringBuffer> detailPages = detailPageBuilder.buildDetailPages();
        pages.putAll(detailPages);
        
		this.add(BorderLayout.WEST, makeLogLeftPanel());
		this.add(BorderLayout.CENTER, makeLogCenterPanel());
		
        makePages();        
	}

	private JPanel makeLogLeftPanel() throws PWCGException  
	{

        JPanel squadronLogPanel = new JPanel(new BorderLayout());
		squadronLogPanel.setOpaque(false);

		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
		buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished Reading", "SquadronLogFinished", "Leave Squadron Log", this);
        buttonPanel.add(finishedButton);
        
        JButton firstPageButton = PWCGButtonFactory.makeTranslucentMenuButton("First Page", "FirstPage", "Leave Journal", this);
        buttonPanel.add(firstPageButton);
        
        JButton lastPageButton = PWCGButtonFactory.makeTranslucentMenuButton("Last Page", "LastPage", "Leave Journal", this);
        buttonPanel.add(lastPageButton);

		squadronLogPanel.add(buttonPanel, BorderLayout.NORTH);
		
		return squadronLogPanel;
	}

	private JPanel  makeLogCenterPanel() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.OpenSquadronLog);
        ImageResizingPanel logCenterPanel = new ImageResizingPanel(CampaignHomeContext.getCampaign().getReferenceService(), imagePath);
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
            else if (action.equalsIgnoreCase("FirstPage"))
            {
                firstPage();
            }
            else if (action.equalsIgnoreCase("LastPage"))
            {
                lastPage();
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

    private void firstPage() throws PWCGException
    {
        SoundManager.getInstance().playSound("PageTurn.WAV");
        this.pageNum = 0;
        displayPages();
    }

    private void lastPage() throws PWCGException
    {
        SoundManager.getInstance().playSound("PageTurn.WAV");
        this.pageNum = getNumPages() - 1;
        displayPages();
    }

    private void previousPage() throws PWCGException
    {
        SoundManager.getInstance().playSound("PageTurn.WAV");
        this.pageNum -= 2;
        displayPages();
    }

    private void displayPages() throws PWCGException
    {
        setPageNumToLeftPage();
        makePages();
        this.revalidate();
        this.repaint();
    }

    private void nextPage() throws PWCGException
    {
        SoundManager.getInstance().playSound("PageTurn.WAV");
        this.pageNum += 2;
        displayPages();
    }

    private void setPageNumToLeftPage()
    {
        if ((pageNum % 2 ) != 0)
        {
            pageNum -= 1;
        }
    }

    private int getNumPages()
    {
        int numPages = pages.size();
        return numPages;
    }
}
