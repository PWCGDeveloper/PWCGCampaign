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
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyViability;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignIntelligenceCompanyListPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private CampaignIntelligenceReportScreen parent;
    private Side side;

	public CampaignIntelligenceCompanyListPanel(Campaign campaign, CampaignIntelligenceReportScreen parent, Side side)
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

        JPanel companyListPanel = makeCompanyListPanel();
        this.add(companyListPanel, BorderLayout.CENTER);
        
        ImageToDisplaySizer.setDocumentSize(this);
    }

    private JPanel makeCompanyListPanel() throws PWCGException 
    {
        ImageResizingPanel companyListPanel = new ImageResizingPanel("");
        companyListPanel.setOpaque(false);
        companyListPanel.setLayout(new BorderLayout());
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        companyListPanel.setImageFromName(imagePath);
        companyListPanel.setBorder(PwcgBorderFactory.createStandardDocumentBorder());
 
        JPanel companyListGrid = makeCompanyGrid(); 
        companyListPanel.add(companyListGrid, BorderLayout.NORTH);
        
        return companyListPanel;
    }

    private JPanel makeCompanyGrid() throws PWCGException
    {
        JPanel companyListGrid = new JPanel(new GridLayout(0,2));
        companyListGrid.setOpaque(false);

        JPanel companyRoleGrid = formCompanyDescForRole(PwcgRoleCategory.MAIN_BATTLE_TANK);
        companyListGrid.add(companyRoleGrid);
                
        companyRoleGrid = formCompanyDescForRole(PwcgRoleCategory.TANK_DESTROYER);
        companyListGrid.add(companyRoleGrid);
        
        companyRoleGrid = formCompanyDescForRole(PwcgRoleCategory.SELF_PROPELLED_GUN);
        companyListGrid.add(companyRoleGrid);
                
        companyRoleGrid = formCompanyDescForRole(PwcgRoleCategory.SELF_PROPELLED_AAA);
        companyListGrid.add(companyRoleGrid);
                
        return companyListGrid;
    }
    

    private JPanel formCompanyDescForRole(PwcgRoleCategory roleCategory) throws PWCGException
    {
        JPanel companyRolePanel = new JPanel(new BorderLayout());
        companyRolePanel.setOpaque(false);

        JPanel companyRoleGrid = new JPanel(new GridLayout(0,1));
        companyRoleGrid.setOpaque(false);

        JLabel headerLabel = PWCGLabelFactory.makePaperLabelLarge(roleCategory.getRoleCategoryDescription() + " Companies \n");
        companyRoleGrid.add(headerLabel);

        List<Company> companies = getCompaniesForIntel(roleCategory);
        for (Company company : companies)
        {
            if (CompanyViability.isCompanyViable(company, campaign))
            {
                JButton companySelectButton = PWCGButtonFactory.makePaperButton(
                        company.determineDisplayName(campaign.getDate()), "CompanySelected:" + company.getCompanyId(), "Detailed information for company", parent);
                companyRoleGrid.add(companySelectButton);                
            }
            else
            {
                JButton companySelectButton = PWCGButtonFactory.makeRedPaperButton(
                        company.determineDisplayName(campaign.getDate()), "CompanySelected:" + company.getCompanyId(), "Detailed information for company", parent);
                companyRoleGrid.add(companySelectButton);
            }
        }
        
        int spacingRowsNeeded = 10 - companies.size();
        for(int i = 0;  i < spacingRowsNeeded; ++i)
        {
            companyRoleGrid.add(PWCGLabelFactory.makeDummyLabel());
        }
        
        companyRolePanel.add(companyRoleGrid, BorderLayout.NORTH);
        return companyRolePanel;
    }

    private List<Company> getCompaniesForIntel(PwcgRoleCategory roleCategory) throws PWCGException
    {
        List<Company> companiesWithPrimaryRole = new ArrayList<>();

        List<Company> companiesForMap = PWCGContext.getInstance().getCompanyManager().getActiveCompaniesForCurrentMap(campaign.getDate());
        for (Company company : companiesForMap)
        {
            if (includeCompany(company, roleCategory))
            {
                companiesWithPrimaryRole.add(company);
            }
        }
        
        return companiesWithPrimaryRole;
    }
    
    private boolean includeCompany(Company company, PwcgRoleCategory roleCategory) throws PWCGException
    {
        if (company.determineSquadronPrimaryRoleCategory(campaign.getDate()) != roleCategory)
        {
            return false;
        }
        
        if (company.determineSide() != side)
        {
            return false;
        }
        
        return true;
    }
}
