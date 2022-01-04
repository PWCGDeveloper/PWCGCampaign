package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.SquadronRoleWeight;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.mission.MissionGeneratorHelper;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;
import pwcg.mission.MissionHumanParticipants;

public class BriefingRoleChooser extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private Campaign campaign;
    private CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper;
    private MissionHumanParticipants participatingPlayers;
    private Map<Integer, JComboBox<String>> squadronToRoleMapping = new HashMap<>();

    public BriefingRoleChooser(
            Campaign campaign, 
            CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper, 
            MissionHumanParticipants participatingPlayers)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.campaignHomeGuiBriefingWrapper = campaignHomeGuiBriefingWrapper;
        this.participatingPlayers = participatingPlayers;
    }
    
    public void makePanels() 
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.BriefingRoleChooser);
            this.setImageFromName(imagePath);

            this.add(BorderLayout.WEST, makeNavigatePanel());
            this.add(BorderLayout.CENTER, makeCenterPanel());
            this.add(BorderLayout.EAST, SpacerPanelFactory.makeDocumentSpacerPanel(1400));
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

	public JPanel makeNavigatePanel() throws PWCGException  
    {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton missionButton = makeMenuButton("Generate Mission", "CreateMission", "Create a mission");
        buttonPanel.add(missionButton);

        JButton scrubButton = makeMenuButton("Scrub Mission", "ScrubMission", "Scrub this mission");
        buttonPanel.add(scrubButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    private JPanel makeCenterPanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel roleSelectionPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        roleSelectionPanel.setBorder(PwcgBorderFactory.createDocumentBorderWithExtraSpaceFromTop());

        roleSelectionPanel.setLayout(new BorderLayout());
        roleSelectionPanel.setOpaque(false);
        
        JPanel roleSelectionGrid = new JPanel();
        roleSelectionGrid.setOpaque(false);
        roleSelectionGrid.setLayout(new GridLayout(0, 2));

        for (CrewMember participatingPlayer : participatingPlayers.getAllParticipatingPlayers())
        {
            int squadronId = participatingPlayer.getCompanyId();
            
            JLabel squadronNameLabel = makeSquadronNameLabel(squadronId);
            JComboBox<String> roleSelector = makeRoleSelectorForSquadron(squadronId);
            roleSelectionGrid.add(squadronNameLabel);
            roleSelectionGrid.add(roleSelector);

            squadronToRoleMapping.put(squadronId, roleSelector);
        }
        
        roleSelectionPanel.add(roleSelectionGrid, BorderLayout.NORTH);
        return roleSelectionPanel;
    }

    private JLabel makeSquadronNameLabel(int squadronId) throws PWCGException
    {        
        Font font = PWCGMonitorFonts.getPrimaryFont();
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(squadronId);
        JLabel squadronNameLabel = PWCGLabelFactory.makeTransparentLabel(
                squadron.determineDisplayName(campaign.getDate()), ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);
        return squadronNameLabel;
    }

    private JComboBox<String> makeRoleSelectorForSquadron(int squadronId) throws PWCGException
    {
        JComboBox<String> roleSelector = new JComboBox<String>();
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(squadronId);

        roleSelector.addItem(PwcgRole.ROLE_NONE.getRoleDescription());

        for (SquadronRoleWeight weightedRoles : squadron.getSquadronRoles().selectRoleSetByDate(campaign.getDate()).getWeightedRoles())
        {
            roleSelector.addItem(weightedRoles.getRole().getRoleDescription());
        }
        
        roleSelector.setSelectedIndex(0);
        return roleSelector;
    }

    private JButton makeMenuButton(String buttonText, String command, String toolTipText) throws PWCGException
    {
        return PWCGButtonFactory.makeTranslucentMenuButton(buttonText, command, toolTipText, this);
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("CreateMission"))
            {
                Map<Integer, PwcgRole> squadronRoleOverride = buildRoleOverrideMap();
                MissionGeneratorHelper.showBriefingMap(campaign, campaignHomeGuiBriefingWrapper, participatingPlayers, squadronRoleOverride);
            }
            else if (action.equalsIgnoreCase("ScrubMission"))
            {
                MissionGeneratorHelper.scrubMission(campaign, campaignHomeGuiBriefingWrapper);
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private Map<Integer, PwcgRole> buildRoleOverrideMap()
    {
        Map<Integer, PwcgRole> squadronRoleOverride = new HashMap<>();
        for (int squadronId : squadronToRoleMapping.keySet())
        {
            JComboBox<String> roleSelector = squadronToRoleMapping.get(squadronId);
            String roleDescription = (String) roleSelector.getSelectedItem();
            PwcgRole role = PwcgRole.getRoleFromDescription(roleDescription);
            if (role != null && role != PwcgRole.ROLE_NONE)
            {
                squadronRoleOverride.put(squadronId, role);
            }
        }
        return squadronRoleOverride;
    }
}


