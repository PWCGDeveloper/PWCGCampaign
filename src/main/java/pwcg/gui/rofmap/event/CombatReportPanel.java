package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.io.json.CombatReportIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ScrollBarWrapper;

public class CombatReportPanel extends ImageResizingPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private CombatReport combatReport;
	private boolean made = false;
	
	private JTextArea narrativeText = new JTextArea("");

	public CombatReportPanel(CombatReport combatReport)
	{
		super(ContextSpecificImages.imagesMisc() + "paperRotated.jpg");
		setLayout(new BorderLayout());
		this.combatReport = combatReport;
	}
	
	public CombatReportPanel()
	{
		super(ContextSpecificImages.imagesMisc() + "paperRotated.jpg");
		setLayout(new BorderLayout());
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
			Color bgColor = ColorMap.PAPER_BACKGROUND;
			this.setBackground(bgColor);
			
			JPanel combatReportPanel = makeCombatReportPanel();
			JPanel mainGrid = makePrimaryGrid();
			
			combatReportPanel.add(mainGrid, BorderLayout.CENTER);
			this.add(combatReportPanel);
		}
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private JPanel makeCombatReportPanel() throws PWCGException
    {
        
        JPanel combatReportPanel = new JPanel();
        combatReportPanel.setLayout(new BorderLayout());
        combatReportPanel.setOpaque(false);
        
        Insets margins = MonitorSupport.calculateInset(0,10,10,10);
        combatReportPanel.setBorder(BorderFactory.createEmptyBorder(margins.top, margins.left, margins.bottom, margins.right)); 

        JPanel headerpanel = makeHeader();
        combatReportPanel.add(headerpanel, BorderLayout.NORTH);
        return combatReportPanel;
    }

    private JPanel makePrimaryGrid() throws PWCGException
    {
        JPanel mainGrid = new JPanel(new GridLayout(0,1));
        mainGrid.setOpaque(false);
        
        Component missionResults = makeMissionResults();
        mainGrid.add(missionResults);

        JPanel narrativePanel = makeNarrative();
        mainGrid.add(narrativePanel);
        return mainGrid;
    }

	private JPanel makeHeader() throws PWCGException  
	{

        Font font = MonitorSupport.getTypewriterFont();
        Font medFont = MonitorSupport.getDecorativeFont();

        JPanel headerPanel = makeHeaderPanel(medFont);

		JPanel headerLeftPanel = makeLeftHeaderPanel(font);
		headerPanel.add(headerLeftPanel, BorderLayout.WEST);

		JPanel headerRightPanel = makeRightHeaderPanel(font);
		headerPanel.add(headerRightPanel, BorderLayout.CENTER);
		
		return headerPanel;
	}

    private JPanel makeRightHeaderPanel(Font font)
    {
        JPanel headerRightPanel = new JPanel(new GridLayout(0,1));
		headerRightPanel.setOpaque(false);

		JLabel lSpacerR1 = new JLabel(" ");
		lSpacerR1.setOpaque(false);
		headerRightPanel.add(lSpacerR1);
		
		JLabel lDate = new JLabel("Date: " + combatReport.getDate(), JLabel.LEFT);
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
        return headerRightPanel;
    }

    private JPanel makeLeftHeaderPanel(Font font)
    {
        JPanel headerLeftPanel = new JPanel(new GridLayout(0,1));
		headerLeftPanel.setOpaque(false);

		JLabel lSpacer1 = new JLabel(" ");
		lSpacer1.setOpaque(false);
		headerLeftPanel.add(lSpacer1);
		
		JLabel lSquadron = new JLabel("Squadron: " + combatReport.getSquadron() + "          ", JLabel.LEFT);
		lSquadron.setOpaque(false);
		lSquadron.setFont(font);
		headerLeftPanel.add(lSquadron);
		
		JLabel lPilot = new JLabel("Pilot: " + combatReport.getPilot() + "          ", JLabel.LEFT);
		lPilot.setOpaque(false);
		lPilot.setFont(font);
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
        return headerLeftPanel;
    }

    private JPanel makeHeaderPanel(Font medFont)
    {
        JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);

        Insets margins = MonitorSupport.calculateInset(10,30,5,30);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(margins.top, margins.left, margins.bottom, margins.right)); 

		JLabel lTitle = new JLabel("Combats in the Air", JLabel.LEFT);
		lTitle.setOpaque(false);
		lTitle.setFont(medFont);
		headerPanel.add(lTitle, BorderLayout.NORTH);
        return headerPanel;
    }

	private Component makeMissionResults() throws PWCGException 
	{
		// Mission text and scroll pane
		Font font = MonitorSupport.getTypewriterFont();
		
        JTextArea haText = new JTextArea("");
        haText.setOpaque(false);
        haText.setLineWrap(true);
        haText.setWrapStyleWord(true);
        haText.setFont(font);

		haText.setText("Remarks on flight and hostile aircraft\n\n" + combatReport.getHaReport());

        Insets margins = MonitorSupport.calculateInset(5,30,5,30);
        haText.setMargin(margins); 

        JScrollPane haScrollPane = ScrollBarWrapper.makeScrollPane(haText);
		
		return haScrollPane;
	}

	private JPanel makeNarrative() throws PWCGException 
	{
		JPanel narrativePanel = new JPanel(new BorderLayout());
		narrativePanel.setOpaque(false);
		
        Insets margins = MonitorSupport.calculateInset(5,30,5,30);
        narrativePanel.setBorder(BorderFactory.createEmptyBorder(margins.top, margins.left, margins.bottom, margins.right)); 

		Font medFont = MonitorSupport.getDecorativeFont();
		JLabel lNarrative = new JLabel("Narrative", JLabel.LEFT);
		lNarrative.setOpaque(false);
		lNarrative.setFont(medFont);
		narrativePanel.add(lNarrative, BorderLayout.NORTH);
		
		Font font = MonitorSupport.getCursiveFont();

		//narrativeText = new ImageTextArea(imagePath);
        narrativeText = new JTextArea("");
        narrativeText.setOpaque(false);
        narrativeText.setLineWrap(true);
        narrativeText.setWrapStyleWord(true);
		narrativeText.setFont(font);
		
		//narrativeText.setText(combatReport.getNarrative());
		
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
