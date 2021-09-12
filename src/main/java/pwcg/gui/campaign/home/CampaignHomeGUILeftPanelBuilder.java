package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.ButtonFactory;

public class CampaignHomeGUILeftPanelBuilder
{
    private CampaignHomeScreen parent;
    
    public CampaignHomeGUILeftPanelBuilder(CampaignHomeScreen parent)
    {
        this.parent = parent;
    }

    public Pane makeLeftPanel() throws PWCGException
    {
        Pane campaignButtonPanel = new Pane(new BorderLayout());
        campaignButtonPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0, 1));
        buttonPanel.setOpaque(false);

        makePlainButtons(buttonPanel);

        campaignButtonPanel.add(buttonPanel, BorderLayout.NORTH);

        return campaignButtonPanel;
    }

    private void makePlainButtons(Pane buttonPanel) throws PWCGException
    {
        Label spacer = new Label("");
        buttonPanel.add(spacer);

        makeSpacedmenuItem(buttonPanel, "Mission", "CampMission", "Mission actions: new mission, lone wolf, combat report");        
        makeSpacedmenuItem(buttonPanel, "Personnel", "CampPersonnel", "Personnel actions: add pilot, manage coop pilots, reference pilot, skin management");        
        makeSpacedmenuItem(buttonPanel, "Activity", "CampActivity", "Campaign activities: leave, transfer, journal, squadron logs");        
        makeSpacedmenuItem(buttonPanel, "Intelligence", "CampIntel", "View intelligence: report, map, depot, emergency resupply");
        makeSpacedmenuItem(buttonPanel, "Configuration", "CampConfig", "Set configuration for this campaign: simple and advanced");
        makeSpacedmenuItem(buttonPanel, "Leave Campaign", "CampMainMenu", "Return to PWCG main menu");
        makeSpacedmenuItem(buttonPanel, "Report Error", "CampError", "Bundle up data files for error reporting");
    }
    
    private void makeSpacedmenuItem(Pane buttonPanel, String buttonText, String commandText, String toolTipText) throws PWCGException
    {
        Label space5 = new Label("");
        buttonPanel.add(space5);
        
        Button button = ButtonFactory.makeTranslucentMenuButton(buttonText, commandText, toolTipText, parent);
        buttonPanel.add(button);
    }
}
