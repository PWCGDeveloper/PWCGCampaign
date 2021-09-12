package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;

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

    private Pane makeTabbedClaimsPanel() throws PWCGException
    {
        Pane claimPanelSet = new Pane (new BorderLayout());
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

    private Pane makeInfoPanel() throws PWCGException 
    {
        Pane infoPanel = new Pane (new BorderLayout());
        infoPanel.setOpaque(false);
        
        Font font = PWCGMonitorFonts.getTypewriterFont();

        Pane infoPanelGrid = new Pane (new GridLayout(0,1));
        infoPanelGrid.setOpaque(false);

        for (int i = 0; i < 1; ++i)
        {
            infoPanelGrid.add(ButtonFactory.makeDummy());
        }

        Label lPilots = new Label("     Pilots assigned to this mission:", Label.LEFT);
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
               
                Label lPilot = new Label(crewDesc, Label.LEFT);
                lPilot.setSize(200, 40);
                lPilot.setOpaque(false);
                lPilot.setFont(font);
                infoPanelGrid.add(lPilot);      
            }
        }
        
        Label space1 = new Label("", Label.LEFT);
        Label space2 = new Label("", Label.LEFT);
        infoPanelGrid.add(space1);
        infoPanelGrid.add(space2);

        Label lDate = new Label("     Date: " + DateUtils.getDateString(campaign.getDate()), Label.LEFT);
        lDate.setFont(font);
        infoPanelGrid.add(lDate);

        for (int i = 0; i < 1; ++i)
        {
            infoPanelGrid.add(ButtonFactory.makeDummy());
        }
        
        infoPanel.add(infoPanelGrid, BorderLayout.NORTH);

        return infoPanel;
    }
}
