package pwcg.gui.campaign.home;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;

public class PilotChalkboardBuilder
{    
    public PilotChalkboardBuilder()
    {
    }

    public JPanel createPilotListPanel(List<SquadronMember> sortedPilots) throws PWCGException
    {
        Color buttonBG = ColorMap.CHALK_BACKGROUND;
        Color buttonFG = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getChalkboardFont();

        GridBagConstraints constraints = initializeGridbagConstraints();
        ImageResizingPanel squadronPanel = createChalkboardHeader(constraints, buttonFG, font);
        addPilotsToChalkBoard(sortedPilots, squadronPanel, constraints, buttonBG, buttonFG, font);

        return squadronPanel;
    }

    private GridBagConstraints initializeGridbagConstraints()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipadx = 3;
        constraints.ipady = 3;
        constraints.anchor = GridBagConstraints.NORTH;
        return constraints;
    }

    private ImageResizingPanel createChalkboardHeader(GridBagConstraints constraints, Color buttonFG, Font font)
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "chalkboard.png";
        ImageResizingPanel squadronPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        squadronPanel.setOpaque(false);
        GridBagLayout squadronLayout = new GridBagLayout();                
        squadronPanel.setLayout(squadronLayout);

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
        return squadronPanel;
    }

    private void addPilotsToChalkBoard(List<SquadronMember> sortedPilots, ImageResizingPanel squadronPanel, GridBagConstraints constraints, Color buttonBG, Color buttonFG, Font font)
    {
        int gridbaRow = 1;
        for (SquadronMember pilot : sortedPilots)
        {
            JLabel lDummy = new JLabel("     ");
            lDummy.setOpaque(false);
            lDummy.setForeground(buttonFG);
            lDummy.setFont(font);               constraints.weightx = 0.15;
            constraints.gridx = 0;
            constraints.gridy = gridbaRow;
            squadronPanel.add(lDummy, constraints);

            JLabel pilotButton = new JLabel(pilot.getNameAndRank());
            pilotButton.setHorizontalAlignment(JLabel.LEFT);
            pilotButton.setOpaque(false);
            pilotButton.setBackground(buttonBG);
            pilotButton.setForeground(buttonFG);
            pilotButton.setFont(font);
            constraints.weightx = 0.15;
            constraints.gridx = 1;
            constraints.gridy = gridbaRow;
            squadronPanel.add(pilotButton, constraints);

            JLabel lMissions = new JLabel("" + pilot.getMissionFlown(), JLabel.RIGHT);
            lMissions.setOpaque(false);
            lMissions.setForeground(buttonFG);
            lMissions.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 2;
            constraints.gridy = gridbaRow;
            squadronPanel.add(lMissions, constraints);
            
            JLabel lVictories = new JLabel("" + pilot.getSquadronMemberVictories().getAirToAirVictories(), JLabel.RIGHT);
            lVictories.setOpaque(false);
            lVictories.setForeground(buttonFG);
            lVictories.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 3;
            constraints.gridy = gridbaRow;
            squadronPanel.add(lVictories, constraints);
             
            lDummy = new JLabel("     ");
            lDummy.setOpaque(false);
            lDummy.setForeground(buttonFG);
            lDummy.setFont(font);               constraints.weightx = 0.15;
            constraints.gridx = 4;
            constraints.gridy = gridbaRow;
            squadronPanel.add(lDummy, constraints);

            ++gridbaRow;
        }
    }
}
