package pwcg.gui.campaign.journal;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.io.json.CombatReportIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ScrollBarWrapper;

public class CampaignJournalGUI extends JPanel
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
		    JPanel combatReportPanel = new JPanel();
			combatReportPanel.setLayout(new BorderLayout());
			combatReportPanel.setOpaque(false);
			
	        Insets margins = MonitorSupport.calculateInset(0,5,5,5);
			combatReportPanel.setBorder(BorderFactory.createEmptyBorder(margins.top, margins.left, margins.bottom, margins.right)); 

			// Header Panel
			JPanel headerpanel = makeHeader();
			combatReportPanel.add(headerpanel, BorderLayout.NORTH);


			// Mission Results and Narrative grid
			JPanel mainGrid = new JPanel(new GridLayout(0,1));
			mainGrid.setOpaque(false);
			
			// Mission Results 
			Component missionResults = makeMissionResults();
			mainGrid.add(missionResults);

			// Narrative 
			JPanel narrativePanel = makeNarrative();
			mainGrid.add(narrativePanel);
			
			combatReportPanel.add(mainGrid, BorderLayout.CENTER);

			this.add(combatReportPanel);
		}
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

	private JPanel makeHeader() throws PWCGException  
	{
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);

		Font font = MonitorSupport.getTypewriterFont();

		// Center of header
		Font medFont = MonitorSupport.getDecorativeFont();

		JLabel lTitle = new JLabel("Combats in the Air", JLabel.LEFT);
		lTitle.setOpaque(false);
		lTitle.setFont(medFont);
		headerPanel.add(lTitle, BorderLayout.NORTH);

		// Left side of header
		JPanel headerLeftPanel = new JPanel(new GridLayout(0,1));
		headerLeftPanel.setOpaque(false);

		JLabel lSpacer1 = new JLabel(" ");
		lSpacer1.setOpaque(false);
		headerLeftPanel.add(lSpacer1);
		
		JLabel lSquadron = new JLabel("Squadron: " + combatReport.getSquadron() + "          ", JLabel.LEFT);
		lSquadron.setOpaque(false);
		lSquadron.setFont(font);
		headerLeftPanel.add(lSquadron);
		
		JLabel lPilot = makePilotsInMissionLabel(font);
		headerLeftPanel.add(lPilot);

		JLabel lType = new JLabel("Type: " + combatReport.getType() + "          ", JLabel.LEFT);
		lType.setOpaque(false);
		lType.setFont(font);
		headerLeftPanel.add(lType);

		JLabel lDuty = new JLabel("Duty: " + combatReport.getDuty() + "          ", JLabel.LEFT);
		lDuty.setOpaque(false);
		lDuty.setFont(font);
		headerLeftPanel.add(lDuty);

		JLabel lSpacer2 = new JLabel(" ");
		lSpacer2.setOpaque(false);
		headerLeftPanel.add(lSpacer2);

		JLabel lSpacer3 = new JLabel(" ");
		lSpacer3.setOpaque(false);
		headerLeftPanel.add(lSpacer3);

		headerPanel.add(headerLeftPanel, BorderLayout.WEST);

		// Right side of header
		JPanel headerRightPanel = new JPanel(new GridLayout(0,1));
		headerRightPanel.setOpaque(false);

		JLabel lSpacerR1 = new JLabel(" ");
		lSpacerR1.setOpaque(false);
		headerRightPanel.add(lSpacerR1);
		
		String formattedCombatDate = DateUtils.getDateStringPretty(combatReport.getDate());
		
		JLabel lDate = new JLabel("Date: " + formattedCombatDate, JLabel.LEFT);
		lDate.setOpaque(false);
		lDate.setFont(font);
		headerRightPanel.add(lDate);
		
		JLabel lTime = new JLabel("Time: " + combatReport.getTime(), JLabel.LEFT);
		lTime.setOpaque(false);
		lTime.setFont(font);
		headerRightPanel.add(lTime);

		JLabel lLocality = new JLabel("Locality: " + combatReport.getLocality(), JLabel.LEFT);
		lLocality.setOpaque(false);
		lLocality.setFont(font);
		headerRightPanel.add(lLocality);

		JLabel lHeight = new JLabel("Height: " + combatReport.getAltitude(), JLabel.LEFT);
		lHeight.setOpaque(false);
		lHeight.setFont(font);
		headerRightPanel.add(lHeight);

		JLabel lSpacerR2 = new JLabel(" ");
		lSpacerR2.setOpaque(false);
		headerRightPanel.add(lSpacerR2);		

		JLabel lSpacerR3 = new JLabel(" ");
		lSpacerR3.setOpaque(false);
		headerRightPanel.add(lSpacerR3);
		

		headerPanel.add(headerRightPanel, BorderLayout.CENTER);
		
		return headerPanel;
	}

    private JLabel makePilotsInMissionLabel(Font font) throws PWCGException
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
        JLabel lPilot = new JLabel("Pilots in mission: " + pilotNames, JLabel.LEFT);
        lPilot.setOpaque(false);
        lPilot.setFont(font);
        return lPilot;
    }

	
	/**
	 * @return
	 * @throws PWCGException 
	 */
	private Component makeMissionResults() throws PWCGException 
	{
		// Mission text and scroll pane
		Font font = MonitorSupport.getTypewriterFont();
		

		JTextArea missionResultTextArea = new JTextArea();
		missionResultTextArea.setFont(font);
		missionResultTextArea.setOpaque(false);
		missionResultTextArea.setLineWrap(true);
		missionResultTextArea.setWrapStyleWord(true);
		missionResultTextArea.setText("Remarks on flight and hostile aircraft\n\n" + combatReport.getHaReport());

		//PWCGScrollPane haScrollPane = new PWCGScrollPane(haText);
        JScrollPane missionResultScrollPane = ScrollBarWrapper.makeScrollPane(missionResultTextArea);
		
		return missionResultScrollPane;
	}

	private JPanel makeNarrative() throws PWCGException 
	{
		JPanel narrativePanel = new JPanel(new BorderLayout());
		narrativePanel.setOpaque(false);
		
		// Label
		Font medFont = MonitorSupport.getDecorativeFont();
		JLabel lNarrative = new JLabel("Narrative", JLabel.LEFT);
		lNarrative.setOpaque(false);
		lNarrative.setFont(medFont);
		narrativePanel.add(lNarrative, BorderLayout.NORTH);
		
		// Narrative text and scroll pane
		Font font = MonitorSupport.getCursiveFont();

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
