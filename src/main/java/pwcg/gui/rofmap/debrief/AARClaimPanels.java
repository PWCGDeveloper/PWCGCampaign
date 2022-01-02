package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import pwcg.aar.AARCoordinator;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGLabelFactory;

public class AARClaimPanels extends ImageResizingPanel
{
    private static final long serialVersionUID = 1L;

    private JTabbedPane tabs = new JTabbedPane();
    private Map<Integer, AARClaimPanel> claimPanels = new HashMap<>();
    private Campaign campaign;

    public AARClaimPanels(Campaign campaign) throws PWCGException
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.campaign = campaign;
    }

    public void makePanels() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        this.setImageFromName(imagePath);
        this.setBorder(BorderFactory.createEmptyBorder(30,30,100,120));

        this.add(makeTabbedClaimsPanel(), BorderLayout.CENTER);
    }

    private JPanel makeTabbedClaimsPanel() throws PWCGException
    {
        JPanel claimPanelSet = new JPanel (new BorderLayout());
        claimPanelSet.setOpaque(false);

        Color tabBG = ColorMap.PAPER_BACKGROUND;
        tabs = new JTabbedPane();
        tabs.setBackground(tabBG);
        tabs.setOpaque(false);

        CrewMembers crewMembersInMission = AARCoordinator.getInstance().getAarContext().getPreliminaryData().getCampaignMembersInMission();
        for (CrewMember crewMember : crewMembersInMission.getCrewMemberCollection().values())
        {
            if (crewMember.isPlayer())
            {
                AARClaimPanel claimPanel = new AARClaimPanel();
                claimPanel.makePanel();
                claimPanels.put(crewMember.getSerialNumber(), claimPanel);
                tabs.addTab(crewMember.getNameAndRank(), claimPanel);
            }
        }

        for (int i = 0; i < tabs.getTabCount(); ++i)
        {
            tabs.setBackgroundAt(i, tabBG);
        }
        
        claimPanelSet.add(makeInfoPanel(), BorderLayout.NORTH);
        claimPanelSet.add(tabs, BorderLayout.CENTER);
        return claimPanelSet;
    }

    public Map<Integer, PlayerDeclarations> getPlayerDeclarations() throws PWCGException
    {
        Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
        for (Integer serialNumber : claimPanels.keySet())
        {
            AARClaimPanel claimPanel = claimPanels.get(serialNumber);
            playerDeclarations.put(serialNumber, claimPanel.getPlayerDeclarations());
        }
        return playerDeclarations;
    }

    private JPanel makeInfoPanel() throws PWCGException 
    {
        JPanel infoPanel = new JPanel (new BorderLayout());
        infoPanel.setOpaque(false);
        
        Font font = PWCGMonitorFonts.getTypewriterFont();

        JPanel infoPanelGrid = new JPanel (new GridLayout(0,1));
        infoPanelGrid.setOpaque(false);

        for (int i = 0; i < 1; ++i)
        {
            infoPanelGrid.add(PWCGLabelFactory.makeDummyLabel());
        }

        String crewMembersText = InternationalizationManager.getTranslation("CrewMembers assigned to this mission");
        crewMembersText = "     " + crewMembersText + ": ";
        JLabel lCrewMembers = PWCGLabelFactory.makeTransparentLabel(crewMembersText, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);        
        infoPanelGrid.add(lCrewMembers);
        
        CrewMembers crewMembersInMission = AARCoordinator.getInstance().getAarContext().getPreliminaryData().getCampaignMembersInMission();
        List<CrewMember> crewMembersInMissionSorted = crewMembersInMission.sortCrewMembers(campaign.getDate());
        for (CrewMember crewMember : crewMembersInMissionSorted)
        {
            CrewMember referencePlayer = campaign.findReferencePlayer();
            if (crewMember.getCompanyId() == referencePlayer.getCompanyId())
            {
                String crewDesc = "             " + crewMember.getNameAndRank();
                JLabel lCrewMember = PWCGLabelFactory.makeTransparentLabel(crewDesc, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);        
                lCrewMember.setSize(200, 40);
                infoPanelGrid.add(lCrewMember);      
            }
        }
        
        infoPanelGrid.add(PWCGLabelFactory.makeDummyLabel());
        infoPanelGrid.add(PWCGLabelFactory.makeDummyLabel());

        String dateText = InternationalizationManager.getTranslation("Date");
        dateText = "     " + dateText + ": " + DateUtils.getDateString(campaign.getDate());
        JLabel lDate = PWCGLabelFactory.makeTransparentLabel(dateText, ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);        
        infoPanelGrid.add(lDate);

        for (int i = 0; i < 1; ++i)
        {
            infoPanelGrid.add(PWCGLabelFactory.makeDummyLabel());
        }
        
        infoPanel.add(infoPanelGrid, BorderLayout.NORTH);

        return infoPanel;
    }
}
