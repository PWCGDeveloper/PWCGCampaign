package pwcg.gui.campaign.intel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronViability;
import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignIntelligenceSquadronListPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private CampaignIntelligenceReportScreen parent;
    private Side side;

	public CampaignIntelligenceSquadronListPanel(Campaign campaign, CampaignIntelligenceReportScreen parent, Side side)
	{
        this.campaign = campaign;
        this.parent = parent;
        this.side = side;
	}

	public void makePanel() throws PWCGException  
    {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

        JPanel squadronListPanel = makeSquadronListPanel();
        this.add(squadronListPanel, BorderLayout.CENTER);
        
        ImageToDisplaySizer.setDocumentSize(this);
    }

    private JPanel makeSquadronListPanel() throws PWCGException 
    {
        ImageResizingPanel squadronListPanel = new ImageResizingPanel("");
        squadronListPanel.setOpaque(false);
        squadronListPanel.setLayout(new BorderLayout());
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        squadronListPanel.setImageFromName(imagePath);
        squadronListPanel.setBorder(PwcgBorderFactory.createStandardDocumentBorder());
 
        JPanel squadronListGrid = makeSquadronGrid(); 
        squadronListPanel.add(squadronListGrid, BorderLayout.NORTH);
        
        return squadronListPanel;
    }

    private JPanel makeSquadronGrid() throws PWCGException
    {
        JPanel squadronListGrid = new JPanel(new GridLayout(0,2));
        squadronListGrid.setOpaque(false);

        // Row 1
        JPanel squadronRoleGrid = formSquadronDescForRole(PwcgRoleCategory.FIGHTER);
        squadronListGrid.add(squadronRoleGrid);
                
        squadronRoleGrid = formSquadronDescForRole(PwcgRoleCategory.ATTACK);
        squadronListGrid.add(squadronRoleGrid);
        
        // Row 2
        squadronRoleGrid = formSquadronDescForRole(PwcgRoleCategory.BOMBER);
        squadronListGrid.add(squadronRoleGrid);
                
        squadronRoleGrid = formSquadronDescForRole(PwcgRoleCategory.RECON);
        squadronListGrid.add(squadronRoleGrid);

        //Row 3
        squadronRoleGrid = formSquadronDescForRole(PwcgRoleCategory.TRANSPORT);
        squadronListGrid.add(squadronRoleGrid);
                
        return squadronListGrid;
    }
    

    private JPanel formSquadronDescForRole(PwcgRoleCategory roleCategory) throws PWCGException
    {
        JPanel squadronRolePanel = new JPanel(new BorderLayout());
        squadronRolePanel.setOpaque(false);

        JPanel squadronRoleGrid = new JPanel(new GridLayout(0,1));
        squadronRoleGrid.setOpaque(false);

        JLabel headerLabel = PWCGLabelFactory.makePaperLabelLarge(roleCategory.getRoleCategoryDescription() + " Squadrons \n");
        squadronRoleGrid.add(headerLabel);

        List<Squadron> squadrons = getSquadronsForIntel(roleCategory, side);
        for (Squadron squadron : squadrons)
        {
            if (SquadronViability.isSquadronViable(squadron, campaign))
            {
                JButton squadronSelectButton = PWCGButtonFactory.makePaperButton(
                        squadron.determineDisplayName(campaign.getDate()), "SquadronSelected:" + squadron.getSquadronId(), "Detailed information for squadron", parent);
                squadronRoleGrid.add(squadronSelectButton);                
            }
            else
            {
                JButton squadronSelectButton = PWCGButtonFactory.makeRedPaperButton(
                        squadron.determineDisplayName(campaign.getDate()), "SquadronSelected:" + squadron.getSquadronId(), "Detailed information for squadron", parent);
                squadronRoleGrid.add(squadronSelectButton);
            }
        }
        
        int spacingRowsNeeded = 10 - squadrons.size();
        for(int i = 0;  i < spacingRowsNeeded; ++i)
        {
            squadronRoleGrid.add(PWCGLabelFactory.makeDummyLabel());
        }
        
        squadronRolePanel.add(squadronRoleGrid, BorderLayout.NORTH);
        return squadronRolePanel;
    }

    private List<Squadron> getSquadronsForIntel(PwcgRoleCategory roleCategory, Side side) throws PWCGException
    {
        List<Squadron> squadronsWithPrimaryRole = new ArrayList<>();

        List<Squadron> squadronsForMap = PWCGContext.getInstance().getSquadronManager().getActiveSquadronsForCurrentMap(campaign.getDate());
        for (Squadron squadron : squadronsForMap)
        {
            if (squadron.determineSquadronPrimaryRoleCategory(campaign.getDate()) == roleCategory)
            {
                if (squadron.determineSide() == side)
                {
                    squadronsWithPrimaryRole.add(squadron);
                }
            }
        }
        
        return squadronsWithPrimaryRole;
    }
}
