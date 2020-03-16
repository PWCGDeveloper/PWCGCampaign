package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pwcg.aar.AARCoordinator;
import pwcg.aar.MissionFileCleaner;
import pwcg.aar.MissionResultLogFileCleaner;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBase;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DirectoryReader;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapScroll;
import pwcg.gui.rofmap.debrief.DebriefMapPanel.DebriefStates;
import pwcg.gui.rofmap.event.AARMainPanel;
import pwcg.gui.rofmap.event.AARMainPanel.EventPanelReason;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;

public class DebriefMapGUI  extends MapGUI implements ActionListener
{
	private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private SquadronMember referencePlayer;
	private CampaignHomeGUI home = null;
	private Thread initiatorThread = null;
	private JTextArea eventTextPane = new JTextArea();
		
    private AARCoordinator aarCoordinator;

	private DebriefMapPanel mapPanel = null;

	public DebriefMapGUI  (Campaign campaign, CampaignHomeGUI home) throws PWCGException
	{
	    super(campaign.getDate());

        SoundManager.getInstance().playSound("BriefingStart.WAV");

        this.campaign = campaign;
        this.home = home;
        this.aarCoordinator = AARCoordinator.getInstance();
        this.referencePlayer = PWCGContext.getInstance().getReferencePlayer();
	}

	public void makePanels() 
	{
		try
		{
		    setLeftPanel(makeNavigationPanel());
			setRightPanel(makeEventTextPanel());
            setCenterPanel(createCenterPanel());
            
			makeMapEvents();
			
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
        AARCombatReportPanelData combatPanelData = aarCoordinator.getAarContext().
                        findUiCombatReportDataForSquadron(referencePlayer.getSquadronId()).getCombatReportPanelData();
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
    

    public void centerMap() throws PWCGException 
    {
        Coordinate lowerLeft = new Coordinate();
        Coordinate upperRight = new Coordinate();
        
        double lowerLeftX = 10000000.0;
        double lowerLeftZ = 10000000.0;

        double upperRightX = 0.0;
        double upperRightZ = 0.0;

        for (DebriefMapPoint mapPoint : mapPanel.getEventPoints())
        {
            if (mapPoint.coord.getXPos() < lowerLeftX)
            {
                lowerLeftX =mapPoint.coord.getXPos();
                lowerLeft.setXPos(lowerLeftX);
            }
            if (mapPoint.coord.getZPos() < lowerLeftZ)
            {
                lowerLeftZ =mapPoint.coord.getZPos();
                lowerLeft.setZPos(lowerLeftZ);
            }
            if (mapPoint.coord.getXPos() > upperRightX)
            {
                upperRightX =mapPoint.coord.getXPos();
                upperRight.setXPos(upperRightX);
            }
            if (mapPoint.coord.getZPos() > upperRightZ)
            {
                upperRightZ =mapPoint.coord.getZPos();
                upperRight.setZPos(upperRightZ);
            }
        }
        
        Coordinate centerCoord = new Coordinate();
        double centerX = lowerLeft.getXPos() + ((upperRight.getXPos() - lowerLeft.getXPos()) / 2);
        centerCoord.setXPos(centerX);
        
        double centerZ = lowerLeft.getZPos() + ((upperRight.getZPos() - lowerLeft.getZPos()) / 2);
        centerCoord.setZPos(centerZ);
        
        Point centerPoint = mapPanel.coordinateToPoint(centerCoord);

        centerMapAt(centerPoint);
    }


	protected void makeMapEvents() throws PWCGException  
	{
        List<LogBase> logEvents = aarCoordinator.getAarContext().
                        findUiCombatReportDataForSquadron(referencePlayer.getSquadronId()).getCombatReportMapData().getChronologicalEvents();

        LogDamage lastDamageEvent = null;
		for (LogBase event : logEvents)
		{
		    DebriefMapPanel mapPanel = (DebriefMapPanel)mapScroll.getMapPanel();
		    
			if (event instanceof LogDamage)
			{
				LogDamage thisDamageEvent = (LogDamage)event;
				
				if (lastDamageEvent == null || 
					!(thisDamageEvent.getVictim().getId().equals(lastDamageEvent.getVictim().getId())))
				{
					mapPanel.addEvent(event);
					lastDamageEvent = thisDamageEvent;
				}
			}
			else
			{
	            mapPanel.addEvent(event);
			}
		}
	}

	protected JPanel makeNavigationPanel() throws PWCGException 
	{
        String imagePath = getSideImage("DebriefNav.jpg");
        ImageResizingPanel debriefButtonPanel = new ImageResizingPanel(imagePath);
        debriefButtonPanel.setLayout(new BorderLayout());
        debriefButtonPanel.setOpaque(false);
        
        JPanel buttonGrid = new JPanel(new GridLayout(0,1));
        buttonGrid.setOpaque(false);

		makeButton("Start Debrief",  "Start", buttonGrid);

		makeButton("Cancel AAR",  "Cancel", buttonGrid);

		makeButton("Debrief Completed",  "Completed",  buttonGrid);

		debriefButtonPanel.add(buttonGrid, BorderLayout.NORTH);

		return debriefButtonPanel;
	}

	private void makeButton(String buttonText, String command, JPanel buttonGrid) throws PWCGException 
	{
	    buttonGrid.add(PWCGButtonFactory.makeDummy());  

        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, command, this);
        buttonGrid.add(button);  
	}

	protected JPanel makeEventTextPanel() throws PWCGException 
	{
		Color buttonBG = ColorMap.MAP_BACKGROUND;

		Font font = MonitorSupport.getPrimaryFontSmall();

        String imagePath = ContextSpecificImages.imagesMisc() + "PaperPart.jpg";
		ImageResizingPanel debriefTextPanel = new ImageResizingPanel(imagePath);
		debriefTextPanel.setLayout(new BorderLayout());
		debriefTextPanel.setOpaque(false);
		debriefTextPanel.setBackground(buttonBG);

        JLabel eventTextLabel = PWCGButtonFactory.makePaperLabelLarge("Mission Events:");
        debriefTextPanel.add(eventTextLabel, BorderLayout.NORTH);

		eventTextPane.setBackground(buttonBG);
		eventTextPane.setFont(font);
		eventTextPane.setOpaque(false);
		eventTextPane.setLineWrap(true);
		eventTextPane.setWrapStyleWord(true);

	    JScrollPane eventScrollPane = ScrollBarWrapper.makeScrollPane(eventTextPane);
	    
        debriefTextPanel.add(eventScrollPane, BorderLayout.CENTER);

		return debriefTextPanel;
	}

	private void finishDebrief() throws PWCGException 
	{
        aarCoordinator.completeAAR();

        DirectoryReader directoryReader = new DirectoryReader();
        FileUtils fileUtils = new FileUtils();
        MissionResultLogFileCleaner missionResultLogFileCleaner = new MissionResultLogFileCleaner(directoryReader, fileUtils);
        missionResultLogFileCleaner.cleanMissionResultLogFiles();
        
        MissionFileCleaner missionFileCleaner = new MissionFileCleaner();
        missionFileCleaner.cleanMissionFiles();

        showMissionEvents();
	}


    private void showMissionEvents() throws PWCGException 
    {        
        List<LogBase> logEvents = aarCoordinator.getAarContext().
                        findUiCombatReportDataForSquadron(referencePlayer.getSquadronId()).getCombatReportMapData().getChronologicalEvents();

        if (!logEvents.isEmpty())
        {
            AARMainPanel eventDisplay = new AARMainPanel(campaign, home, EventPanelReason.EVENT_PANEL_REASON_AAR);
            eventDisplay.makePanels();
            CampaignGuiContextManager.getInstance().pushToContextStack(eventDisplay);
        }
        else
        {
            home.createPilotContext();
        }
    }
    
	public void setText(String text) 
	{
		eventTextPane.setText(text);
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

				if (action.equals("Start"))
                {
                    DebriefEventDisplayInitiator initiator = new DebriefEventDisplayInitiator(mapPanel);
                    initiatorThread = new Thread(initiator);
                    initiatorThread.start();                
                }
                else if (action.equals("Cancel"))
                {
                    home.clean();
                    home.createPilotContext();

                    home.enableButtonsAsNeeded();
                    CampaignGuiContextManager.getInstance().popFromContextStack();
                }
				else if (action.equals("Pause"))
				{
					mapPanel.setDebriefState(DebriefStates.PAUSE);
				}
				else if (action.equals("Stop"))
				{
					mapPanel.setDebriefState(DebriefStates.STOP);
				}
				else if (action.equals("Next"))
				{
					mapPanel.setDebriefState(DebriefStates.NEXT);
				}
				else if (action.equals("Previous"))
				{
					mapPanel.setDebriefState(DebriefStates.PREV);
				}
			}
			
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
		}
	}
}
