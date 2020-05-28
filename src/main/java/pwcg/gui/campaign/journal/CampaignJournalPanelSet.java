package pwcg.gui.campaign.journal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.io.json.CombatReportIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PageTurner;

public class CampaignJournalPanelSet extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign = null;
    
    private JPanel journalPagesGridPanel = new JPanel();
    private JPanel pageTurnerPanel = null;

    private int linesPerPage = 20;
    
    private JPanel leftpage = null;
    private JPanel rightpage = null;

    private int pageNum = 0;
    private Map<Integer, JPanel> indexPages = new TreeMap<Integer, JPanel>();
    private Map<Integer, String> journalReportKeysByPage = new TreeMap<>();
    private Map<String, CombatReport> journalReports =  new TreeMap<>();

    private List<CampaignJournalGUI> activeCampaignJournals  = new ArrayList<CampaignJournalGUI>();

    public CampaignJournalPanelSet(Campaign campaign) throws PWCGException
    {
        super();
        this.campaign = campaign;
    }

    public void makeVisible(boolean visible) 
    {
    }

    public void makePanels() throws PWCGException  
    {
        calculateLinesPerPage();
        getJournalEntries();
        
        setLeftPanel(makeNavigationPanel());
        setCenterPanel(makeLogCenterPanel());
        
        makeIndexPages();
        
        makePages();        
    }

    private void getJournalEntries() 
    {
        try
        {
            journalReports.clear();
            journalReportKeysByPage.clear();
        
            loadCombatReportsForCampagn();
            mapCombatReportsToPages();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

	private void loadCombatReportsForCampagn() throws PWCGException
	{
        journalReports = CombatReportIOJson.readJson(campaign, campaign.findReferencePlayer().getSerialNumber());
	}

	private void mapCombatReportsToPages()
	{
		int journalPage = 1;
		for (String journalKey : journalReports.keySet())
		{
		    journalReportKeysByPage.put(journalPage, journalKey);
		    ++journalPage;
		}
	}

    private JPanel makeNavigationPanel() throws PWCGException  
    {
        String imagePath = getSideImage(campaign, "JournalNav.jpg");

        ImageResizingPanel journalPanel = new ImageResizingPanel(imagePath);
        journalPanel.setLayout(new BorderLayout());
        journalPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeMenuButton("Finished Reading Journal", "JournalFinished", this);
        buttonPanel.add(finishedButton);

        journalPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return journalPanel;
    }

    private JPanel  makeLogCenterPanel() throws PWCGException  
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "PilotLog.jpg";
        ImageResizingPanel journalCenterPanel = new ImageResizingPanel(imagePath);
        journalCenterPanel.setLayout(new BorderLayout());
        journalCenterPanel.setOpaque(false);
        
        journalPagesGridPanel = new JPanel();
        journalPagesGridPanel.setLayout(new GridLayout(0,2));
        journalPagesGridPanel.setOpaque(false);
        
        journalCenterPanel.add(journalPagesGridPanel, BorderLayout.CENTER);
        
        return journalCenterPanel;
    }
 
    
    /**
     * @return
     * @throws PWCGException 
     * @
     */
    private void makeIndexPages() throws PWCGException  
    {
        indexPages.clear();
        
        JPanel indexBorderPanel = new JPanel();
        indexBorderPanel.setLayout(new BorderLayout());
        indexBorderPanel.setOpaque(false);

        JPanel indexPanel = new JPanel();
        indexPanel.setLayout(new GridLayout(0,4));
        indexPanel.setOpaque(false);

        indexBorderPanel.add(indexPanel, BorderLayout.NORTH);

        for (int i = 0; i < 4; ++i)
        {
            indexPanel.add(PWCGButtonFactory.makeDummy());
            indexPanel.add(PWCGButtonFactory.makeDummy());
            indexPanel.add(PWCGButtonFactory.makeDummy());
            indexPanel.add(PWCGButtonFactory.makeDummy());
        }
        
        
        int numEntries = 0;
        for (String journalKey :  journalReports.keySet())
        {
            indexPanel.add(PWCGButtonFactory.makeDummy());
            
            Date date = DateUtils.getDateYYYYMMDD(journalKey);
            String buttonText = DateUtils.getDateStringDashDelimitedYYYYMMDD(date);
            JButton indexButton = PWCGButtonFactory.makePaperButton(buttonText, journalKey, this);
            indexPanel.add(indexButton);

            indexPanel.add(PWCGButtonFactory.makeDummy());
            indexPanel.add(PWCGButtonFactory.makeDummy());
            
            ++numEntries;
            
            // Make a new page
            if (numEntries > linesPerPage)
            {
                int pageNum = indexPages.size();
                indexPages.put(pageNum, indexBorderPanel);
                
                indexBorderPanel = new JPanel();
                indexBorderPanel.setLayout(new BorderLayout());
                indexBorderPanel.setOpaque(false);

                indexPanel = new JPanel();
                indexPanel.setLayout(new GridLayout(0,4));
                indexPanel.setOpaque(false);
                
                indexBorderPanel.add(indexPanel, BorderLayout.NORTH);

                numEntries = 0;
            }
        }
        

        int pageNum = indexPages.size();
        indexPages.put(pageNum, indexBorderPanel);
    }

    
    /**
     * @return
     * @throws PWCGException 
     * @
     */
    private void makePages() throws PWCGException  
    {
        journalPagesGridPanel.removeAll();
        
        // Causes narrative to be saved and cleared
        saveNarrativeChanges();
        activeCampaignJournals.clear();

        // Clear the left and right of the UI
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
        
        // Make the new pages
        leftpage = makePage (pageNum);
        rightpage = makePage (pageNum+1);

        if (leftpage != null)
        {
            journalPagesGridPanel.add(leftpage);
        }
        
        if (rightpage != null)
        {
            journalPagesGridPanel.add(rightpage);
        }
        
        addPageTurner();
    }

    private void addPageTurner() throws PWCGException
    {
        if (pageTurnerPanel != null)
        {
            getCenterPanel().remove(pageTurnerPanel);
        }
        
        int numPages = indexPages.size() + journalReports.size();
        pageTurnerPanel = PageTurner.makeButtonPanel(pageNum+1, numPages, this);
        getCenterPanel().add(pageTurnerPanel, BorderLayout.SOUTH);
    }

    private JPanel makePage(int pageNum) throws PWCGException 
    {
        if (indexPages.containsKey(pageNum))
        {
            makeIndexPages();
            return indexPages.get(pageNum);
        }
        
        if (journalReportKeysByPage.containsKey(pageNum))
        {
            JPanel journalBorderPanel = new JPanel();
            journalBorderPanel.setLayout(new BorderLayout());
            journalBorderPanel.setOpaque(false);
            
            // Set the border for left and right pages
            if (pageNum%2 == 0)
            {
                Insets margins = PWCGMonitorBorders.calculateBorderMargins(60,80,60,20);
                journalBorderPanel.setBorder(BorderFactory.createEmptyBorder(margins.top, margins.left, margins.bottom, margins.right)); 
            }
            else
            {
                Insets margins = PWCGMonitorBorders.calculateBorderMargins(60,30,60,40);
                journalBorderPanel.setBorder(BorderFactory.createEmptyBorder(margins.top, margins.left, margins.bottom, margins.right)); 
            }

            String journalKey = journalReportKeysByPage.get(pageNum);
            CombatReport combatReport = journalReports.get(journalKey);
                        
            CampaignJournalGUI combatReportGUI = new CampaignJournalGUI(combatReport);                   
            combatReportGUI.setCombatReport(combatReport);
            combatReportGUI.makeGUI();
            
            activeCampaignJournals.add(combatReportGUI);
            
            journalBorderPanel.add(combatReportGUI, BorderLayout.CENTER);
            
            return journalBorderPanel;
        }
        
        return null;
    }

    /**
     * @param ae
     */
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("JournalFinished"))
            {
                saveNarrativeChanges();
                finishedWithCampaignScreen();
            }
            else if (action.equalsIgnoreCase("Next Page"))
            {
                nextPage();
            }
            else if (action.equalsIgnoreCase("Previous Page"))
            {
                previousPage();
            }
            else
            {
                directToPage(action);
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    /**
     * @throws PWCGException
     */
    private void previousPage() throws PWCGException
    {
        SoundManager.getInstance().playSound("PageTurn.WAV");
        this.pageNum -= 2;
        makePages();
        refreshPages();
    }

    /**
     * @throws PWCGException
     */
    private void nextPage() throws PWCGException
    {
        SoundManager.getInstance().playSound("PageTurn.WAV");
        this.pageNum += 2;
        makePages();
        refreshPages();
    }



    /**
     * Go directly to the page of a report
     * 
     * @param action
     * @throws PWCGException
     */
    private void directToPage(String action) throws PWCGException
    {
        // Request to go directly to the page
        if (journalReports.containsKey(action))
        {
            // Work backwards from the journal key to the page
            for (int thisPageNum : journalReportKeysByPage.keySet())
            {
                String journalKey = journalReportKeysByPage.get(thisPageNum);
                if (journalKey.equals(action))
                {
                    pageNum = thisPageNum;
                    // Odd numbered page is always the first page;
                    if ((pageNum % 2 ) != 0)
                    {
                        pageNum -= 1;
                    }
                    
                    makePages();
                    refreshPages();
                }
            }

        }
    }

    private void refreshPages() throws PWCGException
    {
        getCenterPanel().setVisible(false);
        getCenterPanel().setVisible(true);
    }

    private void calculateLinesPerPage()
    {
        Dimension screenSize = PWCGMonitorSupport.getPWCGFrameSize();

        linesPerPage = 55;
        if (screenSize.getHeight() < 1200)
        {
            linesPerPage = 45;
        }
        if (screenSize.getHeight() < 1000)
        {
            linesPerPage = 35;
        }
        if (screenSize.getHeight() < 800)
        {
            linesPerPage = 25;
        }
    }

    private void saveNarrativeChanges() throws PWCGException
    {
        for (CampaignJournalGUI activeCampaignJournal : activeCampaignJournals)
        {
            activeCampaignJournal.writeCombatReport(campaign);
        }        
    }
}
