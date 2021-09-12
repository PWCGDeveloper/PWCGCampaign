package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.IPlaneMarkingManager;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignEquipmentChalkboard extends ImageResizingPanel
{
    private static final long serialVersionUID = 1L;
    
    private Campaign campaign;
    
    public CampaignEquipmentChalkboard(Campaign campaign)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
    }
    
    public void makePanels() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignEquipmentChalkboard);
        this.setImageFromName(imagePath);
        this.setBorder(PwcgBorderFactory.createCampaignHomeChalkboardBoxBorder());        

        SquadronMember referencePlayer = campaign.findReferencePlayer();            
        Map<Integer, EquippedPlane> planesForSquadron = campaign.getEquipmentManager().getEquipmentForSquadron(referencePlayer.getSquadronId()).getActiveEquippedPlanes();
        
        Pane equipmentPanel = createEquipmentListPanel(campaign, planesForSquadron);
        this.add(equipmentPanel, BorderLayout.CENTER);
    }

    private Pane createEquipmentListPanel(Campaign campaign, Map<Integer, EquippedPlane> planesForSquadron) throws PWCGException
    {
        List<EquippedPlane> sortedAircraftOnInventory = PlaneSorter.sortEquippedPlanesByGoodness(new ArrayList<EquippedPlane>(planesForSquadron.values()));
        IPlaneMarkingManager planeMarkingManager = campaign.getPlaneMarkingManager();

        Color buttonBG = ColorMap.CHALK_BACKGROUND;
        Color buttonFG = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getChalkboardFont();


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipadx = 3;
        constraints.ipady = 3;
        GridBagLayout equipmentLayout = new GridBagLayout();
        
        Pane equipmentChalkboardPanel = new Pane();
        equipmentChalkboardPanel.setOpaque(false);
        equipmentChalkboardPanel.setLayout(equipmentLayout);

        Label lDummy = new Label("     ");
        lDummy.setOpaque(false);
        lDummy.setForeground(buttonFG);
        lDummy.setFont(font);
        constraints.weightx = 0.15;
        constraints.gridx = 0;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(lDummy, constraints);

        Label lRankHeader = new Label("Aircraft Type", Label.LEFT);
        lRankHeader.setOpaque(false);
        lRankHeader.setForeground(buttonFG);
        lRankHeader.setFont(font);
        constraints.weightx = 0.15;
        constraints.gridx = 1;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(lRankHeader, constraints);

        Label lMissionHeader = new Label("Serial Number", Label.RIGHT);
        lMissionHeader.setOpaque(false);
        lMissionHeader.setForeground(buttonFG);
        lMissionHeader.setFont(font);
        constraints.weightx = 0.1;
        constraints.gridx = 2;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(lMissionHeader, constraints);

        Label lVictoryHeader = new Label("ID Code", Label.RIGHT);
        lVictoryHeader.setOpaque(false);
        lVictoryHeader.setForeground(buttonFG);
        lVictoryHeader.setFont(font);
        constraints.weightx = 0.1;
        constraints.gridx = 3;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(lVictoryHeader, constraints);

        lDummy = new Label("     ");
        lDummy.setOpaque(false);
        lDummy.setForeground(buttonFG);
        lDummy.setFont(font);           constraints.weightx = 0.15;
        constraints.gridx = 4;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(lDummy, constraints);
        
        int i = 1;
        for (EquippedPlane plane : sortedAircraftOnInventory)
        {
            lDummy = new Label("     ");
            lDummy.setOpaque(false);
            lDummy.setForeground(buttonFG);
            lDummy.setFont(font);               constraints.weightx = 0.15;
            constraints.gridx = 0;
            constraints.gridy = i;
            equipmentChalkboardPanel.add(lDummy, constraints);

            Label aircraftNameLabel = new Label(plane.getDisplayName());
            aircraftNameLabel.setAlignment(Label.LEFT);
            aircraftNameLabel.setOpaque(false);
            aircraftNameLabel.setBackground(buttonBG);
            aircraftNameLabel.setForeground(buttonFG);
            aircraftNameLabel.setFont(font);
            constraints.weightx = 0.15;
            constraints.gridx = 1;
            constraints.gridy = i;
            equipmentChalkboardPanel.add(aircraftNameLabel, constraints);

            Label aircraftSerialNumberLabel = new Label("" + plane.getSerialNumber(), Label.RIGHT);
            aircraftSerialNumberLabel.setOpaque(false);
            aircraftSerialNumberLabel.setForeground(buttonFG);
            aircraftSerialNumberLabel.setFont(font);
            constraints.weightx = 0.1;
            constraints.gridx = 2;
            constraints.gridy = i;
            equipmentChalkboardPanel.add(aircraftSerialNumberLabel, constraints);
            
            Label lIdCode = new Label(planeMarkingManager.determineDisplayMarkings(campaign, plane), Label.RIGHT);
            lIdCode.setOpaque(false);
            lIdCode.setForeground(buttonFG);
            lIdCode.setFont(font);               constraints.weightx = 0.15;
            constraints.gridx = 3;
            constraints.gridy = i;
            equipmentChalkboardPanel.add(lIdCode, constraints);
             
            lDummy = new Label("     ");
            lDummy.setOpaque(false);
            lDummy.setForeground(buttonFG);
            lDummy.setFont(font);               constraints.weightx = 0.15;
            constraints.gridx = 4;
            constraints.gridy = i;
            equipmentChalkboardPanel.add(lDummy, constraints);

            ++i;
        }
        return equipmentChalkboardPanel;
    }
}
