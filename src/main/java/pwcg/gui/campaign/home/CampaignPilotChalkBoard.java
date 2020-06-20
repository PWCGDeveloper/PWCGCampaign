package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ToolTipManager;

public class CampaignPilotChalkBoard extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
	
	public CampaignPilotChalkBoard()  
	{
	    super(ContextSpecificImages.imagesMisc() + "BrickCenter.jpg");
	}

    public void makeSquadronPanel(List<SquadronMember> sortedPilots)  
    {
        try
        {
            this.setLayout(new BorderLayout()); 
            
            JPanel selectorPanel = createSelectorPanel();
            this.add(selectorPanel, BorderLayout.NORTH);
 
            JPanel chalkBoardPanel = createPilotListPanel(sortedPilots);
            this.add(chalkBoardPanel, BorderLayout.CENTER);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel createSelectorPanel() throws PWCGException
    {
        JPanel selectorPanel = new JPanel(new GridLayout(0, 1));
        selectorPanel.setOpaque(false);

        JLabel space1 = new JLabel("");
        selectorPanel.add(space1);

        JButton pilotsButton = makeMenuButton("Pilots", "CampPilots", "Show squadron pilot chalk board");
        selectorPanel.add(pilotsButton);

        JButton equipmentButton = makeMenuButton("Equipment", "Equipment", "Show equipment chalk board");
        selectorPanel.add(equipmentButton);

        JButton topAcesButton = makeMenuButton("Top Aces: All", "CampTopAces", "Show top aces chalk board");
        selectorPanel.add(topAcesButton);

        JButton topAcesForServiceButton = makeMenuButton("Top Aces: Service", "CampTopAcesService", "Show top aces chalk board for your service");
        selectorPanel.add(topAcesForServiceButton);

        JButton topAcesNoHistoricalButton = makeMenuButton("Top Aces: Exclude Historical", "CampTopAcesNoHistorical", "Show top aces chalk board with no historical aces");
        selectorPanel.add(topAcesNoHistoricalButton);        
        return selectorPanel;
    }

    private JButton makeMenuButton(String buttonText, String commandText, String toolTiptext) throws PWCGException
    {
        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, commandText, null);
        ToolTipManager.setToolTip(button, toolTiptext);
        return button;
    }

    private JPanel createPilotListPanel(List<SquadronMember> sortedPilots) throws PWCGException
    {
        Color buttonBG = ColorMap.CHALK_BACKGROUND;
        Color buttonFG = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getChalkboardFont();


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipadx = 3;
        constraints.ipady = 3;
        constraints.anchor = GridBagConstraints.NORTH;
        GridBagLayout squadronLayout = new GridBagLayout();        
        
        String imagePath = ContextSpecificImages.imagesMisc() + "chalkboard.png";
        ImageResizingPanel squadronPanel = new ImageResizingPanel(imagePath);
        squadronPanel.setOpaque(false);
        squadronPanel.setLayout(squadronLayout);
        
        Dimension preferredSize = this.getImageSize();
        preferredSize.height -= 200;
        preferredSize.width -= 200;
        squadronPanel.setPreferredSize(preferredSize);
        squadronPanel.setSize(preferredSize);

        JLabel lDummy = new JLabel("     ");
        lDummy.setOpaque(false);
        lDummy.setForeground(buttonFG);
        lDummy.setFont(font);
        constraints.weightx = 0.15;
        constraints.gridx = 0;
        constraints.gridy = 0;
        squadronPanel.add(lDummy, constraints);

        JLabel lRankHeader = new JLabel("Pilot", JLabel.LEFT);
        lRankHeader.setOpaque(false);
        lRankHeader.setForeground(buttonFG);
        lRankHeader.setFont(font);
        constraints.weightx = 0.15;
        constraints.gridx = 1;
        constraints.gridy = 0;
        squadronPanel.add(lRankHeader, constraints);

        JLabel lMissionHeader = new JLabel("Missions", JLabel.RIGHT);
        lMissionHeader.setOpaque(false);
        lMissionHeader.setForeground(buttonFG);
        lMissionHeader.setFont(font);
        constraints.weightx = 0.1;
        constraints.gridx = 2;
        constraints.gridy = 0;
        squadronPanel.add(lMissionHeader, constraints);

        JLabel lVictoryHeader = new JLabel("Victories", JLabel.RIGHT);
        lVictoryHeader.setOpaque(false);
        lVictoryHeader.setForeground(buttonFG);
        lVictoryHeader.setFont(font);
        constraints.weightx = 0.1;
        constraints.gridx = 3;
        constraints.gridy = 0;
        squadronPanel.add(lVictoryHeader, constraints);

        lDummy = new JLabel("     ");
        lDummy.setOpaque(false);
        lDummy.setForeground(buttonFG);
        lDummy.setFont(font);           constraints.weightx = 0.15;
        constraints.gridx = 4;
        constraints.gridy = 0;
        squadronPanel.add(lDummy, constraints);
        
        int i = 1;
        for (SquadronMember pilot : sortedPilots)
        {
            lDummy = new JLabel("     ");
            lDummy.setOpaque(false);
            lDummy.setForeground(buttonFG);
            lDummy.setFont(font);               constraints.weightx = 0.15;
            constraints.gridx = 0;
            constraints.gridy = i;
            squadronPanel.add(lDummy, constraints);

            JLabel pilotButton = new JLabel(pilot.getNameAndRank());
            pilotButton.setHorizontalAlignment(JLabel.LEFT);
            pilotButton.setOpaque(false);
            pilotButton.setBackground(buttonBG);
            pilotButton.setForeground(buttonFG);
            pilotButton.setFont(font);
            constraints.weightx = 0.15;
            constraints.gridx = 1;
            constraints.gridy = i;
            squadronPanel.add(pilotButton, constraints);

            JLabel lMissions = new JLabel("" + pilot.getMissionFlown(), JLabel.RIGHT);
            lMissions.setOpaque(false);
            lMissions.setForeground(buttonFG);
            lMissions.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 2;
            constraints.gridy = i;
            squadronPanel.add(lMissions, constraints);
            
            JLabel lVictories = new JLabel("" + pilot.getSquadronMemberVictories().getAirToAirVictories(), JLabel.RIGHT);
            lVictories.setOpaque(false);
            lVictories.setForeground(buttonFG);
            lVictories.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 3;
            constraints.gridy = i;
            squadronPanel.add(lVictories, constraints);
             
            lDummy = new JLabel("     ");
            lDummy.setOpaque(false);
            lDummy.setForeground(buttonFG);
            lDummy.setFont(font);               constraints.weightx = 0.15;
            constraints.gridx = 4;
            constraints.gridy = i;
            squadronPanel.add(lDummy, constraints);

            ++i;
        }

        return squadronPanel;
    }
}
