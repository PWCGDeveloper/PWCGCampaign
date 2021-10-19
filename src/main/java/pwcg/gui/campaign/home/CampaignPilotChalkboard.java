package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignPilotChalkboard extends ImageResizingPanel
{    
    private static final long serialVersionUID = 1L;

    public CampaignPilotChalkboard()
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
    }
    
    public void makePanels(List<SquadronMember> sortedPilots) throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignPilotChalkboard);
        this.setImageFromName(imagePath);
        this.setBorder(PwcgBorderFactory.createCampaignHomeChalkboardBoxBorder());        

        JPanel equipmentPanel = createPilotListPanel(sortedPilots);
        this.add(equipmentPanel, BorderLayout.CENTER);
    }

    private JPanel createPilotListPanel(List<SquadronMember> sortedPilots) throws PWCGException
    {
        Color buttonBG = ColorMap.CHALK_BACKGROUND;
        Color buttonFG = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getChalkboardFont();

        GridBagConstraints constraints = initializeGridbagConstraints();
        JPanel squadronPanel = createChalkboardHeader(constraints, buttonFG, font);
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

    private JPanel createChalkboardHeader(GridBagConstraints constraints, Color buttonFG, Font font) throws PWCGException
    {
        JPanel squadronPanel = new JPanel();
        squadronPanel.setOpaque(false);
        GridBagLayout squadronLayout = new GridBagLayout();                
        squadronPanel.setLayout(squadronLayout);

        constraints.weightx = 0.15;
        constraints.gridx = 0;
        constraints.gridy = 0;
        squadronPanel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

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

        JLabel airVictoryHeader = new JLabel("Air", JLabel.RIGHT);
        airVictoryHeader.setOpaque(false);
        airVictoryHeader.setForeground(buttonFG);
        airVictoryHeader.setFont(font);
        constraints.weightx = 0.1;
        constraints.gridx = 3;
        constraints.gridy = 0;
        squadronPanel.add(airVictoryHeader, constraints);

        JLabel tankVictoryHeader = new JLabel("Tank", JLabel.RIGHT);
        tankVictoryHeader.setOpaque(false);
        tankVictoryHeader.setForeground(buttonFG);
        tankVictoryHeader.setFont(font);
        constraints.weightx = 0.1;
        constraints.gridx = 4;
        constraints.gridy = 0;
        squadronPanel.add(tankVictoryHeader, constraints);

        JLabel trainVictoryHeader = new JLabel("Train", JLabel.RIGHT);
        trainVictoryHeader.setOpaque(false);
        trainVictoryHeader.setForeground(buttonFG);
        trainVictoryHeader.setFont(font);
        constraints.weightx = 0.1;
        constraints.gridx = 5;
        constraints.gridy = 0;
        squadronPanel.add(trainVictoryHeader, constraints);

        JLabel groundVictoryHeader = new JLabel("Ground", JLabel.RIGHT);
        groundVictoryHeader.setOpaque(false);
        groundVictoryHeader.setForeground(buttonFG);
        groundVictoryHeader.setFont(font);
        constraints.weightx = 0.1;
        constraints.gridx = 6;
        constraints.gridy = 0;
        squadronPanel.add(groundVictoryHeader, constraints);

        constraints.gridx = 7;
        constraints.gridy = 0;
        squadronPanel.add(PWCGLabelFactory.makeDummyLabel(), constraints);
        return squadronPanel;
    }

    private void addPilotsToChalkBoard(List<SquadronMember> sortedPilots, JPanel squadronPanel, GridBagConstraints constraints, Color buttonBG, Color buttonFG, Font font) throws PWCGException
    {
        int gridbaRow = 1;
        for (SquadronMember pilot : sortedPilots)
        {
            constraints.weightx = 0.15;
            constraints.gridx = 0;
            constraints.gridy = gridbaRow;
            squadronPanel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

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
            
            JLabel airVictories = new JLabel("" + pilot.getSquadronMemberVictories().getAirToAirVictoryCount(), JLabel.RIGHT);
            airVictories.setOpaque(false);
            airVictories.setForeground(buttonFG);
            airVictories.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 3;
            constraints.gridy = gridbaRow;
            squadronPanel.add(airVictories, constraints);
            
            JLabel tankVictories = new JLabel("" + pilot.getSquadronMemberVictories().getTankVictoryCount(), JLabel.RIGHT);
            tankVictories.setOpaque(false);
            tankVictories.setForeground(buttonFG);
            tankVictories.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 4;
            constraints.gridy = gridbaRow;
            squadronPanel.add(tankVictories, constraints);
            
            JLabel trainVictories = new JLabel("" + pilot.getSquadronMemberVictories().getTrainVictoryCount(), JLabel.RIGHT);
            trainVictories.setOpaque(false);
            trainVictories.setForeground(buttonFG);
            trainVictories.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 5;
            constraints.gridy = gridbaRow;
            squadronPanel.add(trainVictories, constraints);
            
            JLabel groundVictories = new JLabel("" + pilot.getSquadronMemberVictories().getGroundVictoryCount(), JLabel.RIGHT);
            groundVictories.setOpaque(false);
            groundVictories.setForeground(buttonFG);
            groundVictories.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 6;
            constraints.gridy = gridbaRow;
            squadronPanel.add(groundVictories, constraints);
             
            constraints.gridx = 7;
            constraints.gridy = gridbaRow;
            squadronPanel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

            ++gridbaRow;
        }
    }
}
