package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.config.InternationalizationManager;
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
        Font font = PWCGMonitorFonts.getChalkboardFont();

        GridBagConstraints constraints = initializeGridbagConstraints();
        JPanel squadronPanel = createChalkboardHeader(constraints, font);
        //addPilotsToChalkBoard(sortedPilots, squadronPanel, constraints, font);

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

    private JPanel createChalkboardHeader(GridBagConstraints constraints, Font font) throws PWCGException
    {
        JPanel squadronPanel = new JPanel();
        squadronPanel.setOpaque(false);
        GridBagLayout squadronLayout = new GridBagLayout();                
        squadronPanel.setLayout(squadronLayout);

        JLabel lLeftSpace = PWCGLabelFactory.makeTransparentLabel("   ", ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.15;
        constraints.gridx = 0;
        constraints.gridy = 0;
        squadronPanel.add(lLeftSpace, constraints);

        String lPilotLabelText = InternationalizationManager.getTranslation("Pilot");
        JLabel lPilotLabel = PWCGLabelFactory.makeTransparentLabel(lPilotLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.15;
        constraints.gridx = 1;
        constraints.gridy = 0;
        squadronPanel.add(lPilotLabel, constraints);

        String lMissionLabelText = "    " + InternationalizationManager.getTranslation("Missions");
        JLabel lMissionLabel = PWCGLabelFactory.makeTransparentLabel(lMissionLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.1;
        constraints.gridx = 2;
        constraints.gridy = 0;
        squadronPanel.add(lMissionLabel, constraints);

        String lAirLabelText = "    " + InternationalizationManager.getTranslation("Air");
        JLabel lAirLabel = PWCGLabelFactory.makeTransparentLabel(lAirLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.1;
        constraints.gridx = 3;
        constraints.gridy = 0;
        squadronPanel.add(lAirLabel, constraints);

        String lTankLabelText = "    " + InternationalizationManager.getTranslation("Tank");
        JLabel lTankLabel = PWCGLabelFactory.makeTransparentLabel(lTankLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.1;
        constraints.gridx = 4;
        constraints.gridy = 0;
        squadronPanel.add(lTankLabel, constraints);

        String lTrainLabelText = "    " + InternationalizationManager.getTranslation("Train");
        JLabel lTrainLabel = PWCGLabelFactory.makeTransparentLabel(lTrainLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.1;
        constraints.gridx = 5;
        constraints.gridy = 0;
        squadronPanel.add(lTrainLabel, constraints);

        String lGroundLabelText = "    " + InternationalizationManager.getTranslation("Ground");
        JLabel lGroundLabel = PWCGLabelFactory.makeTransparentLabel(lGroundLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.1;
        constraints.gridx = 6;
        constraints.gridy = 0;
        squadronPanel.add(lGroundLabel, constraints);
        
        JLabel lRightSpace = PWCGLabelFactory.makeTransparentLabel("   ", ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.15;
        constraints.gridx = 7;
        constraints.gridy = 0;
        squadronPanel.add(lRightSpace, constraints);

        return squadronPanel;
    }

    private void addPilotsToChalkBoard(List<SquadronMember> sortedPilots, JPanel squadronPanel, GridBagConstraints constraints, Font font) throws PWCGException
    {
        int gridbagRow = 1;
        for (SquadronMember pilot : sortedPilots)
        {

            JLabel lLeftSpace = PWCGLabelFactory.makeTransparentLabel("   ", ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
            constraints.weightx = 0.15;
            constraints.gridx = 0;
            constraints.gridy = gridbagRow;
            squadronPanel.add(lLeftSpace, constraints);
            
            JLabel pilotLabel = PWCGLabelFactory.makeTransparentLabel(
                    pilot.getNameAndRank(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
            constraints.weightx = 0.20;
            constraints.gridx = 1;
            constraints.gridy = gridbagRow;
            squadronPanel.add(pilotLabel, constraints);

            JLabel missionsFlownLabel = PWCGLabelFactory.makeTransparentLabel(
                    "" + pilot.getMissionFlown(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 2;
            constraints.gridy = gridbagRow;
            squadronPanel.add(missionsFlownLabel, constraints);
            
            JLabel airToAirVictoryLabel = PWCGLabelFactory.makeTransparentLabel(
                    "" + pilot.getSquadronMemberVictories().getAirToAirVictoryCount(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 3;
            constraints.gridy = gridbagRow;
            squadronPanel.add(airToAirVictoryLabel, constraints);
            
            JLabel tankVictoryLabel = PWCGLabelFactory.makeTransparentLabel(
                    "" + pilot.getSquadronMemberVictories().getTankVictoryCount(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 4;
            constraints.gridy = gridbagRow;
            squadronPanel.add(tankVictoryLabel, constraints);
            
            JLabel trainVictoryLabel = PWCGLabelFactory.makeTransparentLabel(
                    "" + pilot.getSquadronMemberVictories().getTrainVictoryCount(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 5;
            constraints.gridy = gridbagRow;
            squadronPanel.add(trainVictoryLabel, constraints);
            
            JLabel groundVictoryLabel = PWCGLabelFactory.makeTransparentLabel(
                    "" + pilot.getSquadronMemberVictories().getGroundVictoryCount(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 6;
            constraints.gridy = gridbagRow;
            squadronPanel.add(groundVictoryLabel, constraints);
            
            JLabel lRightSpace = PWCGLabelFactory.makeTransparentLabel("   ", ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 7;
            constraints.gridy = 0;
            squadronPanel.add(lRightSpace, constraints);

            ++gridbagRow;
        }
    }
}
