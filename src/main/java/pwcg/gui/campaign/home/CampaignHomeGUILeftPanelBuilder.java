package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.core.exception.PWCGException;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;

public class CampaignHomeGUILeftPanelBuilder
{
    private CampaignHomeScreen parent;
    
    public CampaignHomeGUILeftPanelBuilder(CampaignHomeScreen parent)
    {
        this.parent = parent;
    }

    public JPanel makeLeftPanel() throws PWCGException
    {
        JPanel campaignButtonPanel = new JPanel(new BorderLayout());
        campaignButtonPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.setOpaque(false);

        makePlainButtons(buttonPanel);

        campaignButtonPanel.add(buttonPanel, BorderLayout.NORTH);

        return campaignButtonPanel;
    }

    private void makePlainButtons(JPanel buttonPanel) throws PWCGException
    {
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

        makeSpacedmenuItem(buttonPanel, "Mission", "CampMission", "Mission actions: new mission, lone wolf, combat report");        
        makeSpacedmenuItem(buttonPanel, "Personnel", "CampPersonnel", "Personnel actions: add crewMember, manage coop crewMembers, reference crewMember, skin management");        
        makeSpacedmenuItem(buttonPanel, "Activity", "CampActivity", "Campaign activities: leave, transfer, journal, squadron logs");        
        makeSpacedmenuItem(buttonPanel, "Intelligence", "CampIntel", "View intelligence: report, map, depot, emergency resupply");
        makeSpacedmenuItem(buttonPanel, "Configuration", "CampConfig", "Set configuration for this campaign: simple and advanced");
        makeSpacedmenuItem(buttonPanel, "Leave Campaign", "CampMainMenu", "Return to PWCG main menu");
        makeSpacedmenuItem(buttonPanel, "Report Error", "CampError", "Bundle up data files for error reporting");
    }
    
    private void makeSpacedmenuItem(JPanel buttonPanel, String buttonText, String commandText, String toolTipText) throws PWCGException
    {
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        JButton button = PWCGButtonFactory.makeTranslucentMenuButton(buttonText, commandText, toolTipText, parent);
        buttonPanel.add(button);
    }
}
