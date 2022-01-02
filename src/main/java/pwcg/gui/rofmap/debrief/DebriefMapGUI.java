package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pwcg.aar.AARCoordinator;
import pwcg.aar.MissionFileCleaner;
import pwcg.aar.MissionResultLogFileCleaner;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBase;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DirectoryReader;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.home.CampaignHomeScreen;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapScroll;
import pwcg.gui.rofmap.event.AARReportMainPanel;
import pwcg.gui.rofmap.event.AARReportMainPanel.EventPanelReason;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.ScrollBarWrapper;

public class DebriefMapGUI  extends MapGUI implements ActionListener
{
	private static final long serialVersionUID = 1L;

    private Campaign campaign;
	private CampaignHomeScreen home = null;
	private Thread initiatorThread = null;
	private JTextArea eventTextPane = new JTextArea();
	private JCheckBox maxInfoCheckBox;

    private AARCoordinator aarCoordinator;

	private DebriefMapPanel mapPanel = null;

	public DebriefMapGUI  (Campaign campaign, CampaignHomeScreen home) throws PWCGException
	{
	    super(campaign.getDate());

        SoundManager.getInstance().playSound("BriefingStart.WAV");

        this.campaign = campaign;
        this.home = home;
        this.aarCoordinator = AARCoordinator.getInstance();
	}

	public void makePanels() 
	{
		try
		{
		    this.add(BorderLayout.WEST, makeNavigationPanel());
			this.add(BorderLayout.EAST, makeEventTextPanel());
            this.add(BorderLayout.CENTER, createCenterPanel());
            			
	        centerMap();

			setSoundForScreen();
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private void setSoundForScreen() throws PWCGException
    {
        CrewMember referencePlayer = campaign.findReferencePlayer();
        AARCombatReportPanelData combatPanelData = aarCoordinator.getAarContext().
                        findUiCombatReportDataForSquadron(referencePlayer.getCompanyId()).getCombatReportPanelData();
        CampaignMissionWin missionWin = new CampaignMissionWin(combatPanelData);
        MusicManager.playMissionStatusTheme(missionWin.isMissionAWin());
    }
	

    private JPanel createCenterPanel() throws PWCGException
    {
        JPanel debriefMapCenterPanel = new JPanel(new BorderLayout());

        mapPanel = new DebriefMapPanel(this);
        mapScroll = new MapScroll(mapPanel);  
        mapPanel.setMapBackground(100);
        
        debriefMapCenterPanel.add(mapScroll.getMapScrollPane());
        
        return debriefMapCenterPanel;
    }    
    

    private void centerMap() throws PWCGException 
    {
        Coordinate firstActionCoordinate = findFirstEventCoordinate();
        Point centerPoint = mapPanel.coordinateToPoint(firstActionCoordinate);

        centerMapAt(centerPoint);
    }
    
    private Coordinate findFirstEventCoordinate() throws PWCGException
    {
        CrewMember referencePlayer = campaign.findReferencePlayer();
        List<LogBase> logEvents = aarCoordinator.getAarContext().
                findUiCombatReportDataForSquadron(referencePlayer.getCompanyId()).getCombatReportMapData().getChronologicalEvents();

        for (LogBase event : logEvents)
        {
            if (event instanceof LogDamage)
            {
                LogDamage thisDamageEvent = (LogDamage)event;
                return thisDamageEvent.getLocation();
            }
            else if (event instanceof LogVictory)
            {
                LogVictory thisDamageEvent = (LogVictory)event;
                return thisDamageEvent.getLocation();
            }
        }

        return PWCGContext.getInstance().getCurrentMap().getMapCenter();
    }


	private void makeMapEvents() throws PWCGException  
	{
        CrewMember referencePlayer = campaign.findReferencePlayer();
        List<LogBase> logEvents = aarCoordinator.getAarContext().
                        findUiCombatReportDataForSquadron(referencePlayer.getCompanyId()).getCombatReportMapData().getChronologicalEvents();

        DebriefMapPanel mapPanel = (DebriefMapPanel)mapScroll.getMapPanel();
        mapPanel.createMapEvents(logEvents);
	}

	private JPanel makeNavigationPanel() throws PWCGException 
	{
        JPanel debriefButtonPanel = new JPanel(new BorderLayout());
        debriefButtonPanel.setOpaque(false);
        
        JPanel buttonGrid = new JPanel(new GridLayout(0,1));
        buttonGrid.setOpaque(false);

        maxInfoCheckBox = makeCheckBox("Maximum Information",  "MaxInfo", buttonGrid);

        JButton startDebriefButton = makeButton("Start Debrief",  "Start", "Start debrief");
        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());  
        buttonGrid.add(startDebriefButton);  

        JButton cancelDebriefButton = makeButton("Cancel Debrief",  "Cancel", "Cancel the debrief");
        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());  
        buttonGrid.add(cancelDebriefButton);  

        JButton debriefCompleteButton = makeButton("Debrief Completed",  "Completed", "Finish the debrief");
        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());  
        buttonGrid.add(debriefCompleteButton);  

		debriefButtonPanel.add(buttonGrid, BorderLayout.NORTH);

		return debriefButtonPanel;
	}

	private JButton makeButton(String buttonText, String command, String toolTipText) throws PWCGException 
	{
	    return PWCGButtonFactory.makeTranslucentMenuButton(buttonText, command, toolTipText, this);	    
	}

    private JCheckBox makeCheckBox(String buttonText, String command, JPanel buttonGrid) throws PWCGException 
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());  
        Font font = PWCGMonitorFonts.getPrimaryFont();
        JCheckBox button = PWCGButtonFactory.makeCheckBox(buttonText, command, font, fgColor, this);
        buttonGrid.add(button);
        return button;  
    }
    
	private JPanel makeEventTextPanel() throws PWCGException 
	{
		Color buttonBG = ColorMap.MAP_BACKGROUND;

		Font font = PWCGMonitorFonts.getPrimaryFontSmall();

        String imagePath = ContextSpecificImages.imagesMisc() + "PaperPart.jpg";
		ImageResizingPanel debriefTextPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
		debriefTextPanel.setLayout(new BorderLayout());
		debriefTextPanel.setOpaque(false);
		debriefTextPanel.setBackground(buttonBG);

        JLabel eventHeaderLabel = PWCGLabelFactory.makePaperLabelLarge("Mission Events");
        JPanel headerPanel = new JPanel(new GridLayout(0,1));
        headerPanel.add(eventHeaderLabel);
        debriefTextPanel.add(headerPanel, BorderLayout.NORTH);

		eventTextPane.setBackground(buttonBG);
		eventTextPane.setFont(font);
		eventTextPane.setOpaque(false);
		eventTextPane.setLineWrap(true);
		eventTextPane.setWrapStyleWord(true);

	    JScrollPane eventScrollPane = ScrollBarWrapper.makeScrollPane(eventTextPane);
	    
        debriefTextPanel.add(eventScrollPane, BorderLayout.CENTER);
        
        Dimension imagePanelDimensions = ImageToDisplaySizer.getTextAreaDimensionsForScreen(600);
        debriefTextPanel.setPreferredSize(new Dimension(imagePanelDimensions.width, imagePanelDimensions.height));

		return debriefTextPanel;
	}

	private void finishDebrief() throws PWCGException 
	{
        aarCoordinator.completeAAR();

        DirectoryReader directoryReader = new DirectoryReader();
        MissionResultLogFileCleaner missionResultLogFileCleaner = new MissionResultLogFileCleaner(directoryReader);
        missionResultLogFileCleaner.cleanMissionResultLogFiles();
        
        MissionFileCleaner missionFileCleaner = new MissionFileCleaner();
        missionFileCleaner.cleanMissionFiles();

        showMissionEvents();
	}


    private void showMissionEvents() throws PWCGException 
    {        
        CrewMember referencePlayer = campaign.findReferencePlayer();
        List<LogBase> logEvents = aarCoordinator.getAarContext().
                        findUiCombatReportDataForSquadron(referencePlayer.getCompanyId()).getCombatReportMapData().getChronologicalEvents();

        if (!logEvents.isEmpty())
        {
            AARReportMainPanel eventDisplay = new AARReportMainPanel(campaign, home, EventPanelReason.EVENT_PANEL_REASON_AAR);
            eventDisplay.makePanels();
            CampaignGuiContextManager.getInstance().pushToContextStack(eventDisplay);
        }
        else
        {
            home.refreshInformation();
        }
    }
    
	public void setText(String text) 
	{
		eventTextPane.setText(text);
	}
	
    private void clearTextArea()
    {
        eventTextPane.selectAll();
        eventTextPane.replaceSelection("");
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		try
		{
			String action = arg0.getActionCommand();
			if (action.equals("Completed"))
			{
		        SoundManager.getInstance().playSound("BriefingEnd.WAV");
				finishDebrief();
			}
			else
			{
	            DebriefMapPanel mapPanel = (DebriefMapPanel)mapScroll.getMapPanel();
	            
				if (initiatorThread != null)
				{
					initiatorThread.interrupt();
				}
				
                if (action.equals("MaxInfo"))
                {
                }
                else if (action.equals("Start"))
                {
                    clearTextArea();
                    makeMapEvents();
                    centerMap();
                    
                    DebriefEventDisplayInitiator initiator = new DebriefEventDisplayInitiator(mapPanel);
                    initiatorThread = new Thread(initiator);
                    initiatorThread.start();                
                }
                else if (action.equals("Cancel"))
                {
                    home.refreshInformation();
                    CampaignGuiContextManager.getInstance().backToCampaignHome();
                }
			}
			
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
	}
	
	public boolean displayMaxInfo()
	{
	    if (maxInfoCheckBox == null)
	    {
	        return false;
	    }
	    
	    if (maxInfoCheckBox.isSelected())
	    {
	        return true;
	    }
	    
	    return false;
	}
}
