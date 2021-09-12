package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
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

        Pane equipmentPanel = createPilotListPanel(sortedPilots);
        this.add(equipmentPanel, BorderLayout.CENTER);
    }

    private Pane createPilotListPanel(List<SquadronMember> sortedPilots) throws PWCGException
    {
        Color buttonBG = ColorMap.CHALK_BACKGROUND;
        Color buttonFG = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getChalkboardFont();

        GridBagConstraints constraints = initializeGridbagConstraints();
        Pane squadronPanel = createChalkboardHeader(constraints, buttonFG, font);
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

    private Pane createChalkboardHeader(GridBagConstraints constraints, Color buttonFG, Font font) throws PWCGException
    {
        Pane squadronPanel = new Pane();
        squadronPanel.setOpaque(false);
        GridBagLayout squadronLayout = new GridBagLayout();                
        squadronPanel.setLayout(squadronLayout);

        Label lDummy = new Label("     ");
        lDummy.setOpaque(false);
        lDummy.setForeground(buttonFG);
        lDummy.setFont(font);
        constraints.weightx = 0.15;
        constraints.gridx = 0;
        constraints.gridy = 0;
        squadronPanel.add(lDummy, constraints);

        Label lRankHeader = new Label("Pilot", Label.LEFT);
        lRankHeader.setOpaque(false);
        lRankHeader.setForeground(buttonFG);
        lRankHeader.setFont(font);
        constraints.weightx = 0.15;
        constraints.gridx = 1;
        constraints.gridy = 0;
        squadronPanel.add(lRankHeader, constraints);

        Label lMissionHeader = new Label("Missions", Label.RIGHT);
        lMissionHeader.setOpaque(false);
        lMissionHeader.setForeground(buttonFG);
        lMissionHeader.setFont(font);
        constraints.weightx = 0.1;
        constraints.gridx = 2;
        constraints.gridy = 0;
        squadronPanel.add(lMissionHeader, constraints);

        Label airVictoryHeader = new Label("Air", Label.RIGHT);
        airVictoryHeader.setOpaque(false);
        airVictoryHeader.setForeground(buttonFG);
        airVictoryHeader.setFont(font);
        constraints.weightx = 0.1;
        constraints.gridx = 3;
        constraints.gridy = 0;
        squadronPanel.add(airVictoryHeader, constraints);

        Label tankVictoryHeader = new Label("Tank", Label.RIGHT);
        tankVictoryHeader.setOpaque(false);
        tankVictoryHeader.setForeground(buttonFG);
        tankVictoryHeader.setFont(font);
        constraints.weightx = 0.1;
        constraints.gridx = 4;
        constraints.gridy = 0;
        squadronPanel.add(tankVictoryHeader, constraints);

        Label trainVictoryHeader = new Label("Train", Label.RIGHT);
        trainVictoryHeader.setOpaque(false);
        trainVictoryHeader.setForeground(buttonFG);
        trainVictoryHeader.setFont(font);
        constraints.weightx = 0.1;
        constraints.gridx = 5;
        constraints.gridy = 0;
        squadronPanel.add(trainVictoryHeader, constraints);

        Label groundVictoryHeader = new Label("Ground", Label.RIGHT);
        groundVictoryHeader.setOpaque(false);
        groundVictoryHeader.setForeground(buttonFG);
        groundVictoryHeader.setFont(font);
        constraints.weightx = 0.1;
        constraints.gridx = 6;
        constraints.gridy = 0;
        squadronPanel.add(groundVictoryHeader, constraints);

        lDummy = new Label("     ");
        lDummy.setOpaque(false);
        lDummy.setForeground(buttonFG);
        lDummy.setFont(font);           constraints.weightx = 0.15;
        constraints.gridx = 7;
        constraints.gridy = 0;
        squadronPanel.add(lDummy, constraints);
        return squadronPanel;
    }

    private void addPilotsToChalkBoard(List<SquadronMember> sortedPilots, Pane squadronPanel, GridBagConstraints constraints, Color buttonBG, Color buttonFG, Font font) throws PWCGException
    {
        int gridbaRow = 1;
        for (SquadronMember pilot : sortedPilots)
        {
            Label lDummy = new Label("     ");
            lDummy.setOpaque(false);
            lDummy.setForeground(buttonFG);
            lDummy.setFont(font);               constraints.weightx = 0.15;
            constraints.gridx = 0;
            constraints.gridy = gridbaRow;
            squadronPanel.add(lDummy, constraints);

            Label pilotButton = new Label(pilot.getNameAndRank());
            pilotButton.setAlignment(Label.LEFT);
            pilotButton.setOpaque(false);
            pilotButton.setBackground(buttonBG);
            pilotButton.setForeground(buttonFG);
            pilotButton.setFont(font);
            constraints.weightx = 0.15;
            constraints.gridx = 1;
            constraints.gridy = gridbaRow;
            squadronPanel.add(pilotButton, constraints);

            Label lMissions = new Label("" + pilot.getMissionFlown(), Label.RIGHT);
            lMissions.setOpaque(false);
            lMissions.setForeground(buttonFG);
            lMissions.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 2;
            constraints.gridy = gridbaRow;
            squadronPanel.add(lMissions, constraints);
            
            Label airVictories = new Label("" + pilot.getSquadronMemberVictories().getAirToAirVictoryCount(), Label.RIGHT);
            airVictories.setOpaque(false);
            airVictories.setForeground(buttonFG);
            airVictories.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 3;
            constraints.gridy = gridbaRow;
            squadronPanel.add(airVictories, constraints);
            
            Label tankVictories = new Label("" + pilot.getSquadronMemberVictories().getTankVictoryCount(), Label.RIGHT);
            tankVictories.setOpaque(false);
            tankVictories.setForeground(buttonFG);
            tankVictories.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 4;
            constraints.gridy = gridbaRow;
            squadronPanel.add(tankVictories, constraints);
            
            Label trainVictories = new Label("" + pilot.getSquadronMemberVictories().getTrainVictoryCount(), Label.RIGHT);
            trainVictories.setOpaque(false);
            trainVictories.setForeground(buttonFG);
            trainVictories.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 5;
            constraints.gridy = gridbaRow;
            squadronPanel.add(trainVictories, constraints);
            
            Label groundVictories = new Label("" + pilot.getSquadronMemberVictories().getGroundVictoryCount(), Label.RIGHT);
            groundVictories.setOpaque(false);
            groundVictories.setForeground(buttonFG);
            groundVictories.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 6;
            constraints.gridy = gridbaRow;
            squadronPanel.add(groundVictories, constraints);
             
            lDummy = new Label("     ");
            lDummy.setOpaque(false);
            lDummy.setForeground(buttonFG);
            lDummy.setFont(font);               constraints.weightx = 0.15;
            constraints.gridx = 7;
            constraints.gridy = gridbaRow;
            squadronPanel.add(lDummy, constraints);

            ++gridbaRow;
        }
    }
}
