package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import java.awt.Component;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.io.json.CombatReportIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ScrollBarWrapper;

public class CampaignJournalGUI extends Pane
{
	private static final long serialVersionUID = 1L;
	private CombatReport combatReport;
	private boolean made = false;
	
	private JTextArea narrativeText = new JTextArea("");

	public CampaignJournalGUI(CombatReport combatReport)
	{
		super();
		setLayout(new BorderLayout());
        setOpaque(false);

		this.combatReport = combatReport;
	}
	
	public String getHA()
	{
		return combatReport.getHaReport();
	}
	
	public String getNarrative()
	{
		return narrativeText.getText();
	}
	
	public void makeGUI() 
	{
		if (made)
		{
			return;
		}
		made = true;
		
		try
		{
		    Pane combatReportPanel = new Pane();
			combatReportPanel.setLayout(new BorderLayout());
			combatReportPanel.setOpaque(false);
			
	        Insets margins = PWCGMonitorBorders.calculateBorderMargins(0,5,5,5);
			combatReportPanel.setBorder(BorderFactory.createEmptyBorder(margins.top, margins.left, margins.bottom, margins.right)); 

			Pane headerpanel = makeHeader();
			combatReportPanel.add(headerpanel, BorderLayout.NORTH);

			Pane mainGrid = new Pane(new GridLayout(0,1));
			mainGrid.setOpaque(false);
			
			Component missionResults = makeMissionResults();
			mainGrid.add(missionResults);

			Pane narrativePanel = makeNarrative();
			mainGrid.add(narrativePanel);
			
			combatReportPanel.add(mainGrid, BorderLayout.CENTER);

			this.add(combatReportPanel);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

	private Pane makeHeader() throws PWCGException  
	{
		Pane headerPanel = new Pane(new BorderLayout());
		headerPanel.setOpaque(false);

		Font font = PWCGMonitorFonts.getTypewriterFont();
		Font medFont = PWCGMonitorFonts.getDecorativeFont();

		Label lTitle = new Label("Combats in the Air", Label.LEFT);
		lTitle.setOpaque(false);
		lTitle.setFont(medFont);
		headerPanel.add(lTitle, BorderLayout.NORTH);

		Pane headerLeftPanel = new Pane(new GridLayout(0,1));
		headerLeftPanel.setOpaque(false);

		Label lSpacer1 = new Label(" ");
		lSpacer1.setOpaque(false);
		headerLeftPanel.add(lSpacer1);
		
		Label lSquadron = new Label("Squadron: " + combatReport.getSquadron() + "          ", Label.LEFT);
		lSquadron.setOpaque(false);
		lSquadron.setFont(font);
		headerLeftPanel.add(lSquadron);
		
		Label lPilot = makePilotsInMissionLabel(font);
		headerLeftPanel.add(lPilot);

		Label lType = new Label("Type: " + combatReport.getType() + "          ", Label.LEFT);
		lType.setOpaque(false);
		lType.setFont(font);
		headerLeftPanel.add(lType);

		Label lDuty = new Label("Duty: " + combatReport.getDuty() + "          ", Label.LEFT);
		lDuty.setOpaque(false);
		lDuty.setFont(font);
		headerLeftPanel.add(lDuty);

		Label lSpacer2 = new Label(" ");
		lSpacer2.setOpaque(false);
		headerLeftPanel.add(lSpacer2);

		Label lSpacer3 = new Label(" ");
		lSpacer3.setOpaque(false);
		headerLeftPanel.add(lSpacer3);

		headerPanel.add(headerLeftPanel, BorderLayout.WEST);

		Pane headerRightPanel = new Pane(new GridLayout(0,1));
		headerRightPanel.setOpaque(false);

		Label lSpacerR1 = new Label(" ");
		lSpacerR1.setOpaque(false);
		headerRightPanel.add(lSpacerR1);
		
		String formattedCombatDate = DateUtils.getDateStringPretty(combatReport.getDate());
		
		Label lDate = new Label("Date: " + formattedCombatDate, Label.LEFT);
		lDate.setOpaque(false);
		lDate.setFont(font);
		headerRightPanel.add(lDate);
		
		Label lTime = new Label("Time: " + combatReport.getTime(), Label.LEFT);
		lTime.setOpaque(false);
		lTime.setFont(font);
		headerRightPanel.add(lTime);

		Label lLocality = new Label("Locality: " + combatReport.getLocality(), Label.LEFT);
		lLocality.setOpaque(false);
		lLocality.setFont(font);
		headerRightPanel.add(lLocality);

		Label lHeight = new Label("Height: " + combatReport.getAltitude(), Label.LEFT);
		lHeight.setOpaque(false);
		lHeight.setFont(font);
		headerRightPanel.add(lHeight);

		Label lSpacerR2 = new Label(" ");
		lSpacerR2.setOpaque(false);
		headerRightPanel.add(lSpacerR2);		

		Label lSpacerR3 = new Label(" ");
		lSpacerR3.setOpaque(false);
		headerRightPanel.add(lSpacerR3);
		

		headerPanel.add(headerRightPanel, BorderLayout.CENTER);
		
		return headerPanel;
	}

    private Label makePilotsInMissionLabel(Font font) throws PWCGException
    {
        String pilotNames = "";
        for (String pilotName : combatReport.getFlightPilots())
        {
            if (!pilotNames.isEmpty())
            {
                pilotNames += ", ";
            }
            pilotNames += pilotName;
        }
        Label lPilot = new Label("Pilots in mission: " + pilotNames, Label.LEFT);
        lPilot.setOpaque(false);
        lPilot.setFont(font);
        return lPilot;
    }

	private Component makeMissionResults() throws PWCGException 
	{
		Font font = PWCGMonitorFonts.getTypewriterFont();
		
		JTextArea missionResultTextArea = new JTextArea();
		missionResultTextArea.setFont(font);
		missionResultTextArea.setOpaque(false);
		missionResultTextArea.setLineWrap(true);
		missionResultTextArea.setWrapStyleWord(true);
		missionResultTextArea.setText("Remarks on flight and hostile aircraft\n\n" + combatReport.getHaReport());

        JScrollPane missionResultScrollPane = ScrollBarWrapper.makeScrollPane(missionResultTextArea);
		
		return missionResultScrollPane;
	}

	private Pane makeNarrative() throws PWCGException 
	{
		Pane narrativePanel = new Pane(new BorderLayout());
		narrativePanel.setOpaque(false);
		
		Font medFont = PWCGMonitorFonts.getDecorativeFont();
		Label lNarrative = new Label("Narrative", Label.LEFT);
		lNarrative.setOpaque(false);
		lNarrative.setFont(medFont);
		narrativePanel.add(lNarrative, BorderLayout.NORTH);
		
		Font font = PWCGMonitorFonts.getCursiveFont();

		narrativeText = new JTextArea();
		narrativeText.setFont(font);
		narrativeText.setOpaque(false);
		narrativeText.setLineWrap(true);
		narrativeText.setWrapStyleWord(true);
		narrativeText.setText(combatReport.getNarrative());
		
        JScrollPane narrativeScrollPane = ScrollBarWrapper.makeScrollPane(narrativeText);
		narrativePanel.add(narrativeScrollPane, BorderLayout.CENTER);
		
		return narrativePanel;
	}

	public void setCombatReport(CombatReport combatReport) 
	{
		this.combatReport = combatReport;
	}

	public void actionPerformed(ActionEvent ae)
	{
	}

	public void makeVisible(boolean visible) 
	{
	}

	public void writeCombatReport(Campaign campaign) throws PWCGException 
	{
		if (combatReport != null)
		{
			combatReport.setHaReport(getHA());
			combatReport.setNarrative(getNarrative());
			
			CombatReportIOJson.writeJson(campaign, combatReport);
		}
	}
}
