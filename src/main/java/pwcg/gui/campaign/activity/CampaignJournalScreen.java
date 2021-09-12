package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
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
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.io.json.CombatReportIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.PageTurner;

public class CampaignJournalScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign = null;
    
    private Pane journalPagesGridPanel = new Pane();
    private Pane pageTurnerPanel = null;

    private int linesPerPage = 20;
    
    private Pane leftpage = null;
    private Pane rightpage = null;

    private int pageNum = 0;
    private Map<Integer, Pane> indexPages = new TreeMap<Integer, Pane>();
    private Map<Integer, String> journalReportKeysByPage = new TreeMap<>();
    private Map<String, CombatReport> journalReports =  new TreeMap<>();

    private List<CampaignJournalGUI> activeCampaignJournals  = new ArrayList<CampaignJournalGUI>();

    public CampaignJournalScreen(Campaign campaign) throws PWCGException
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
    }

    public void makeVisible(boolean visible) 
    {
    }

    public void makePanels() throws PWCGException  
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignJournalScreen);
        this.setImageFromName(imagePath);

        calculateLinesPerPage();
        getJournalEntries();
        
        this.add(BorderLayout.WEST, makeNavigationPanel());
        this.add(BorderLayout.CENTER, makeLogCenterPanel());
        
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

    private Pane makeNavigationPanel() throws PWCGException  
    {
        Pane journalPanel = new Pane(new GridLayout(0,1));
        journalPanel.setLayout(new BorderLayout());
        journalPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        Button finishedButton = ButtonFactory.makeTranslucentMenuButton("Finished Reading", "JournalFinished", "Leave Journal", this);
        buttonPanel.add(finishedButton);
        
        Button firstPageButton = ButtonFactory.makeTranslucentMenuButton("First Page", "FirstPage", "Leave Journal", this);
        buttonPanel.add(firstPageButton);
        
        Button lastPageButton = ButtonFactory.makeTranslucentMenuButton("Last Page", "LastPage", "Leave Journal", this);
        buttonPanel.add(lastPageButton);

        journalPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return journalPanel;
    }

    private Pane  makeLogCenterPanel() throws PWCGException  
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.OpenJournal);
        ImageResizingPanel journalCenterPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        journalCenterPanel.setLayout(new BorderLayout());
        journalCenterPanel.setOpaque(false);
        
        journalPagesGridPanel = new Pane();
        journalPagesGridPanel.setLayout(new GridLayout(0,2));
        journalPagesGridPanel.setOpaque(false);
        
        journalCenterPanel.add(journalPagesGridPanel, BorderLayout.CENTER);
        
        return journalCenterPanel;
    }

    private void makeIndexPages() throws PWCGException  
    {
        indexPages.clear();
        
        Pane indexBorderPanel = new Pane();
        indexBorderPanel.setLayout(new BorderLayout());
        indexBorderPanel.setOpaque(false);

        Pane indexPanel = new Pane();
        indexPanel.setLayout(new GridLayout(0,4));
        indexPanel.setOpaque(false);
        indexBorderPanel.add(indexPanel, BorderLayout.NORTH);

        addSpaceToTopOfIndexPage(indexPanel);
        
        
        int numEntries = 0;
        for (String journalKey :  journalReports.keySet())
        {
            makeJournalIndexEntry(indexPanel, journalKey);
            ++numEntries;
            
            if (numEntries > linesPerPage)
            {
                int pageNum = indexPages.size();
                indexPages.put(pageNum, indexBorderPanel);
                
                indexBorderPanel = new Pane();
                indexBorderPanel.setLayout(new BorderLayout());
                indexBorderPanel.setOpaque(false);

                indexPanel = new Pane();
                indexPanel.setLayout(new GridLayout(0,4));
                indexPanel.setOpaque(false);
                
                addSpaceToTopOfIndexPage(indexPanel);

                indexBorderPanel.add(indexPanel, BorderLayout.NORTH);

                numEntries = 0;
            }
        }

        int pageNum = indexPages.size();
        indexPages.put(pageNum, indexBorderPanel);
    }

    private void addSpaceToTopOfIndexPage(Pane indexPanel)
    {
        for (int i = 0; i < 2; ++i)
        {
            indexPanel.add(ButtonFactory.makeDummy());
            indexPanel.add(ButtonFactory.makeDummy());
            indexPanel.add(ButtonFactory.makeDummy());
            indexPanel.add(ButtonFactory.makeDummy());
        }
    }

    private void makeJournalIndexEntry(Pane indexPanel, String journalKey) throws PWCGException
    {
        indexPanel.add(ButtonFactory.makeDummy());
        
        Date date = DateUtils.getDateYYYYMMDD(journalKey);
        String buttonText = DateUtils.getDateStringDashDelimitedYYYYMMDD(date);
        Button indexButton = ButtonFactory.makePaperButton(buttonText, journalKey, this);
        indexPanel.add(indexButton);

        indexPanel.add(ButtonFactory.makeDummy());
        indexPanel.add(ButtonFactory.makeDummy());
    }

    private void makePages() throws PWCGException  
    {
        journalPagesGridPanel.removeAll();
        
        saveNarrativeChanges();
        activeCampaignJournals.clear();

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
            this.remove(pageTurnerPanel);
        }
        
        int numPages = getNumPages();
        pageTurnerPanel = PageTurner.makeButtonPanel(pageNum+1, numPages, this);
        this.add(pageTurnerPanel, BorderLayout.SOUTH);
    }

    private Pane makePage(int pageNum) throws PWCGException 
    {
        if (indexPages.containsKey(pageNum))
        {
            makeIndexPages();
            return indexPages.get(pageNum);
        }
        
        if (journalReportKeysByPage.containsKey(pageNum))
        {
            Pane journalBorderPanel = new Pane();
            journalBorderPanel.setLayout(new BorderLayout());
            journalBorderPanel.setOpaque(false);
            
            if (pageNum % 2 == 0)
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

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            
            if (action.equalsIgnoreCase("JournalFinished"))
            {
                saveNarrativeChanges();
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

    private void nextPage() throws PWCGException
    {
        SoundManager.getInstance().playSound("PageTurn.WAV");
        this.pageNum += 2;
        displayPages();
    }

    private void displayPages() throws PWCGException
    {
        setPageNumToLeftPage();
        makePages();
        refreshPages();
    }

    private void directToPage(String action) throws PWCGException
    {
        if (journalReports.containsKey(action))
        {
            for (int thisPageNum : journalReportKeysByPage.keySet())
            {
                String journalKey = journalReportKeysByPage.get(thisPageNum);
                if (journalKey.equals(action))
                {
                    pageNum = thisPageNum;
                    displayPages();
                }
            }

        }
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
        int numPages = indexPages.size() + journalReports.size();
        return numPages;
    }

    private void refreshPages() throws PWCGException
    {
        this.revalidate();
        this.repaint();
    }

    private void calculateLinesPerPage()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
        linesPerPage = 26;
        if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            linesPerPage = 21;
        }
        else if (monitorSize == MonitorSize.FRAME_SMALL)
        {
            linesPerPage = 18;
        }
        else if (monitorSize == MonitorSize.FRAME_VERY_SMALL)
        {
            linesPerPage = 12;
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
