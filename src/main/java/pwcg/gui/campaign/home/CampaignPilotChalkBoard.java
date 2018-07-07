package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class CampaignPilotChalkBoard extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
	
	public CampaignPilotChalkBoard()  
	{
	    super(ContextSpecificImages.imagesMisc() + "chalkboard.jpg");
	}

    public void makeSquadronPanel(Map<String, SquadronMember> sortedPilots)  
    {
        try
        {
            this.setLayout(new BorderLayout()); 
            JPanel squadronPanel = createPilotListPanel(sortedPilots);
            this.add(squadronPanel, BorderLayout.CENTER);
        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel createPilotListPanel(Map<String, SquadronMember> sortedPilots) throws PWCGException
    {
        Color buttonBG = ColorMap.CHALK_BACKGROUND;
        Color buttonFG = ColorMap.CHALK_FOREGROUND;
        Font font = MonitorSupport.getChalkboardFont();


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipadx = 3;
        constraints.ipady = 3;
        GridBagLayout squadronLayout = new GridBagLayout();
        
        JPanel squadronPanel = new JPanel(squadronLayout);
        squadronPanel.setOpaque(false);
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
        for (SquadronMember pilot : sortedPilots.values())
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
