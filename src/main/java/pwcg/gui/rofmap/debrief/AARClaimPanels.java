package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import pwcg.aar.AARCoordinator;
import pwcg.aar.inmission.phase3.reconcile.victories.PlayerDeclarations;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageJTabbedPane;
import pwcg.gui.utils.ImageResizingPanel;

public class AARClaimPanels extends AARPanel
{
    private static final long serialVersionUID = 1L;

    private ImageJTabbedPane tabs = new ImageJTabbedPane();
    private Map<Integer, AARClaimPanel> claimPanels = new HashMap<>();

    public AARClaimPanels() throws PWCGException
    {
        super();
        this.setOpaque(false);
    }

    public void makePanels()
    {
        ImageResizingPanel claimPanelSet = null;
        try
        {
            String imagePath = ContextSpecificImages.imagesMisc() + "Paper.jpg";
            claimPanelSet = new ImageResizingPanel(imagePath);
            claimPanelSet.setLayout(new BorderLayout());

            Color tabBG = ColorMap.PAPER_BACKGROUND;
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

            claimPanelSet.add(tabs, BorderLayout.CENTER);
        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }

        this.add(claimPanelSet);
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
}
