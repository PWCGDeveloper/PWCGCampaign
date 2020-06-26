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

import pwcg.aar.AARCoordinator;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

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
        String imagePath = UiImageResolver.getImageMain("document.png");
        this.setImage(imagePath);
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

        SquadronMembers pilotsInMission = AARCoordinator.getInstance().getAarContext().getPreliminaryData().getCampaignMembersInMission();
        for (SquadronMember pilot : pilotsInMission.getSquadronMemberCollection().values())
        {
            if (pilot.isPlayer())
            {
                AARClaimPanel claimPanel = new AARClaimPanel();
                claimPanel.makePanel();
                claimPanels.put(pilot.getSerialNumber(), claimPanel);
                tabs.addTab(pilot.getNameAndRank(), claimPanel);
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
            infoPanelGrid.add(PWCGButtonFactory.makeDummy());
        }

        JLabel lPilots = new JLabel("     Pilots assigned to this mission:", JLabel.LEFT);
        lPilots.setOpaque(false);
        lPilots.setFont(font);
        infoPanelGrid.add(lPilots);
        
        SquadronMembers pilotsInMission = AARCoordinator.getInstance().getAarContext().getPreliminaryData().getCampaignMembersInMission();
        List<SquadronMember> pilotsInMissionSorted = pilotsInMission.sortPilots(campaign.getDate());
        for (SquadronMember pilot : pilotsInMissionSorted)
        {
            SquadronMember referencePlayer = campaign.findReferencePlayer();
            if (pilot.getSquadronId() == referencePlayer.getSquadronId())
            {
                String crewDesc = "             " + pilot.getNameAndRank();
               
                JLabel lPilot = new JLabel(crewDesc, JLabel.LEFT);
                lPilot.setSize(200, 40);
                lPilot.setOpaque(false);
                lPilot.setFont(font);
                infoPanelGrid.add(lPilot);      
            }
        }
        
        JLabel space1 = new JLabel("", JLabel.LEFT);
        JLabel space2 = new JLabel("", JLabel.LEFT);
        infoPanelGrid.add(space1);
        infoPanelGrid.add(space2);

        JLabel lDate = new JLabel("     Date: " + DateUtils.getDateString(campaign.getDate()), JLabel.LEFT);
        lDate.setFont(font);
        infoPanelGrid.add(lDate);

        for (int i = 0; i < 1; ++i)
        {
            infoPanelGrid.add(PWCGButtonFactory.makeDummy());
        }
        
        infoPanel.add(infoPanelGrid, BorderLayout.NORTH);

        return infoPanel;
    }
}
