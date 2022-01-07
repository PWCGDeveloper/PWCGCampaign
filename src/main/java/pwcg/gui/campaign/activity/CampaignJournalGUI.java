package pwcg.gui.campaign.activity;

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
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.io.json.CombatReportIOJson;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorBorders;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.PWCGLabelFactory;
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
			
	        Insets margins = PWCGMonitorBorders.calculateBorderMargins(0,5,5,5);
			combatReportPanel.setBorder(BorderFactory.createEmptyBorder(margins.top, margins.left, margins.bottom, margins.right)); 

			JPanel headerpanel = makeHeader();
			combatReportPanel.add(headerpanel, BorderLayout.NORTH);

			JPanel mainGrid = new JPanel(new GridLayout(0,1));
			mainGrid.setOpaque(false);
			
			Component missionResults = makeMissionResults();
			mainGrid.add(missionResults);

			JPanel narrativePanel = makeNarrative();
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

	private JPanel makeHeader() throws PWCGException  
	{
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);

		makeTitle(headerPanel);
        makeHeader(headerPanel);
		return headerPanel;
	}

    private void makeTitle(JPanel headerPanel) throws PWCGException
    {
        Font medFont = PWCGMonitorFonts.getDecorativeFont();
        String lTitleText = InternationalizationManager.getTranslation("Combats in the Air");
        JLabel lTitle = PWCGLabelFactory.makeTransparentLabel(lTitleText, ColorMap.PAPER_FOREGROUND, medFont, SwingConstants.LEFT);
        headerPanel.add(lTitle, BorderLayout.NORTH);
    }

    private void makeHeader(JPanel headerPanel) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getTypewriterFont();

        JPanel headerLeftPanel = new JPanel(new GridLayout(0,1));
		headerLeftPanel.setOpaque(false);

		headerLeftPanel.add(PWCGLabelFactory.makeDummyLabel());

        String squadronText = InternationalizationManager.getTranslation("Squadron");
        squadronText += ": " + combatReport.getSquadron();
        JLabel lSquadron = PWCGLabelFactory.makeTransparentLabel(squadronText, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);
        headerLeftPanel.add(lSquadron);

		JLabel lCrewMember = makeCrewMembersInMissionLabel(font);
		headerLeftPanel.add(lCrewMember);

        String typeText = InternationalizationManager.getTranslation("Type");
        typeText += ": " + combatReport.getType();
        JLabel lType = PWCGLabelFactory.makeTransparentLabel(typeText, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);
        headerLeftPanel.add(lType);

        String dutyText = InternationalizationManager.getTranslation("Duty");
        dutyText += ": " + combatReport.getDuty();
        JLabel lDuty = PWCGLabelFactory.makeTransparentLabel(dutyText, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);
        headerLeftPanel.add(lDuty);

		headerLeftPanel.add(PWCGLabelFactory.makeDummyLabel());

		headerLeftPanel.add(PWCGLabelFactory.makeDummyLabel());

		headerPanel.add(headerLeftPanel, BorderLayout.WEST);

		JPanel headerRightPanel = new JPanel(new GridLayout(0,1));
		headerRightPanel.setOpaque(false);

		headerRightPanel.add(PWCGLabelFactory.makeDummyLabel());
		
		String formattedCombatDate = DateUtils.getDateStringPretty(combatReport.getDate());
        String dateText = InternationalizationManager.getTranslation("Date");
        dateText += ": " + formattedCombatDate;
        JLabel lDate = PWCGLabelFactory.makeTransparentLabel(dateText, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);
        headerRightPanel.add(lDate);

        String timeText = InternationalizationManager.getTranslation("Time");
        timeText += ": " + combatReport.getTime();
        JLabel lTime = PWCGLabelFactory.makeTransparentLabel(timeText, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);
        headerRightPanel.add(lTime);

        String localityText = InternationalizationManager.getTranslation("Locality");
        localityText += ": " + combatReport.getLocality();
        JLabel lLocality = PWCGLabelFactory.makeTransparentLabel(localityText, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);
        headerRightPanel.add(lLocality);
    }

    private JLabel makeCrewMembersInMissionLabel(Font font) throws PWCGException
    {
        String crewMemberNames = "";
        for (String crewMemberName : combatReport.getFlightCrewMembers())
        {
            if (!crewMemberNames.isEmpty())
            {
                crewMemberNames += ", ";
            }
            crewMemberNames += crewMemberName;
        }
        
        String crewMembersInMissionText = InternationalizationManager.getTranslation("CrewMembers in mission");
        crewMembersInMissionText += ": " + crewMemberNames;
        JLabel lCrewMember = PWCGLabelFactory.makeTransparentLabel(crewMembersInMissionText, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);

        return lCrewMember;
    }

	private Component makeMissionResults() throws PWCGException 
	{
		Font font = PWCGMonitorFonts.getTypewriterFont();
		
		JTextArea missionResultTextArea = new JTextArea();
		missionResultTextArea.setFont(font);
		missionResultTextArea.setOpaque(false);
		missionResultTextArea.setLineWrap(true);
		missionResultTextArea.setWrapStyleWord(true);
		missionResultTextArea.setText(InternationalizationManager.getTranslation("Remarks on flight and hostile aircraft") + "\n\n" + combatReport.getHaReport());

        JScrollPane missionResultScrollPane = ScrollBarWrapper.makeScrollPane(missionResultTextArea);
		
		return missionResultScrollPane;
	}

	private JPanel makeNarrative() throws PWCGException 
	{
		JPanel narrativePanel = new JPanel(new BorderLayout());
		narrativePanel.setOpaque(false);
		
		Font medFont = PWCGMonitorFonts.getDecorativeFont();
        String narrativeDescText = InternationalizationManager.getTranslation("Height");
        JLabel lNarrative = PWCGLabelFactory.makeTransparentLabel(narrativeDescText, ColorMap.PAPER_FOREGROUND, medFont, SwingConstants.LEFT);
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
