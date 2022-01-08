package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankSorter;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGLabelFactory;
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

        CrewMember referencePlayer = campaign.findReferencePlayer();            
        Map<Integer, EquippedTank> planesForSquadron = campaign.getEquipmentManager().getEquipmentForCompany(referencePlayer.getCompanyId()).getActiveEquippedTanks();
        
        JPanel equipmentPanel = createEquipmentListPanel(campaign, planesForSquadron);
        this.add(equipmentPanel, BorderLayout.CENTER);
    }

    private JPanel createEquipmentListPanel(Campaign campaign, Map<Integer, EquippedTank> planesForSquadron) throws PWCGException
    {
        List<EquippedTank> sortedAircraftOnInventory = TankSorter.sortEquippedTanksByGoodness(new ArrayList<EquippedTank>(planesForSquadron.values()));

        Font font = PWCGMonitorFonts.getChalkboardFont();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipadx = 3;
        constraints.ipady = 3;
        GridBagLayout equipmentLayout = new GridBagLayout();
        
        JPanel equipmentChalkboardPanel = new JPanel();
        equipmentChalkboardPanel.setOpaque(false);
        equipmentChalkboardPanel.setLayout(equipmentLayout);

        constraints.weightx = 0.15;
        constraints.gridx = 0;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

        String aircraftTypeLabelText = InternationalizationManager.getTranslation("Aircraft Type");
        JLabel aircraftTypeLabel = PWCGLabelFactory.makeTransparentLabel(aircraftTypeLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.15;
        constraints.gridx = 1;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(aircraftTypeLabel, constraints);
        
        String lSerialNumberText = InternationalizationManager.getTranslation("Serial Number");
        JLabel lSerialNumber = PWCGLabelFactory.makeTransparentLabel(lSerialNumberText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
        constraints.weightx = 0.1;
        constraints.gridx = 2;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(lSerialNumber, constraints);
        
        String lAircraftIdCodeText = InternationalizationManager.getTranslation("ID Code");
        JLabel lAircraftIdCode = PWCGLabelFactory.makeTransparentLabel(lAircraftIdCodeText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
        constraints.weightx = 0.1;
        constraints.gridx = 3;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(lAircraftIdCode, constraints);

        constraints.gridx = 4;
        constraints.gridy = 0;
        equipmentChalkboardPanel.add(PWCGLabelFactory.makeDummyLabel(), constraints);
        
        int i = 1;
        for (EquippedTank plane : sortedAircraftOnInventory)
        {
            constraints.weightx = 0.15;
            constraints.gridx = 0;
            constraints.gridy = i;
            equipmentChalkboardPanel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

            JLabel aircraftNameLabel = PWCGLabelFactory.makeTransparentLabel(plane.getDisplayName(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
            constraints.weightx = 0.15;
            constraints.gridx = 1;
            constraints.gridy = i;
            equipmentChalkboardPanel.add(aircraftNameLabel, constraints);

            JLabel aircraftSerialNumberLabel = PWCGLabelFactory.makeTransparentLabel("" + plane.getSerialNumber(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 2;
            constraints.gridy = i;
            equipmentChalkboardPanel.add(aircraftSerialNumberLabel, constraints);
             
            constraints.gridx = 4;
            constraints.gridy = i;
            equipmentChalkboardPanel.add(PWCGLabelFactory.makeDummyLabel(), constraints);

            ++i;
        }
        return equipmentChalkboardPanel;
    }
}
