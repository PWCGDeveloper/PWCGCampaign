package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.PWCGLabelFactory;

public class CampaignGeneratorProfileInfoGUI extends JPanel
{
	private static final long serialVersionUID = 1L;

    private CampaignGeneratorScreen parent;

	public CampaignGeneratorProfileInfoGUI(CampaignGeneratorScreen parent, String imagePath) 
	{
        super();
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        
        this.parent = parent;       
	}
	

	public void makePanels() throws PWCGException 
	{
		try
		{			
			JPanel campaignProfileInfoPanel = new JPanel(new BorderLayout());
			campaignProfileInfoPanel.setOpaque(false);
			
			JPanel campaignProfileInfoGridPanel = createPorfileInfoPanel(); 
			campaignProfileInfoPanel.add(campaignProfileInfoGridPanel, BorderLayout.NORTH);

            this.add(createSpacingPanel(), BorderLayout.WEST);
			this.add(campaignProfileInfoPanel, BorderLayout.CENTER);
            this.add(createSpacingPanel(), BorderLayout.EAST);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private JPanel createSpacingPanel() throws PWCGException
    {
        JPanel spacingGridPanel = new JPanel(new GridLayout(1, 1));
        spacingGridPanel.setOpaque(false);

        spacingGridPanel.add(PWCGLabelFactory.makeTransparentLabel(
                "                      ", ColorMap.CHALK_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT)); 

        return spacingGridPanel;
    }

    private JPanel createPorfileInfoPanel() throws PWCGException
    {
        JPanel campaignProfileInfoGridPanel = new JPanel(new GridLayout(4, 1));
        campaignProfileInfoGridPanel.setOpaque(false);

        campaignProfileInfoGridPanel.add(PWCGLabelFactory.makeTransparentLabel(
                "     ", ColorMap.CHALK_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT)); 

        String campaignNameText = InternationalizationManager.getTranslation("Campaign Name");
        campaignNameText += ": " + parent.getCampaignGeneratorDO().getCampaignName();
        JLabel campaignNameLabel = PWCGLabelFactory.makeTransparentLabel(
                campaignNameText, ColorMap.CHALK_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT);
        campaignProfileInfoGridPanel.add(campaignNameLabel); 
        
        String campaignModeText = InternationalizationManager.getTranslation("Campaign Mode");
        campaignModeText += ": " + parent.getCampaignGeneratorDO().getCampaignMode().getCampaignModeName();
        JLabel campaignModeLabel = PWCGLabelFactory.makeTransparentLabel(
                campaignModeText, ColorMap.CHALK_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT);
        campaignProfileInfoGridPanel.add(campaignModeLabel); 

        String serviceText = InternationalizationManager.getTranslation("Service");
        serviceText += ": " + parent.getCampaignGeneratorDO().getService().getName();
        JLabel serviceLabel = PWCGLabelFactory.makeTransparentLabel(
                serviceText, ColorMap.CHALK_FOREGROUND, PWCGMonitorFonts.getPrimaryFont(), SwingConstants.LEFT);
        campaignProfileInfoGridPanel.add(serviceLabel); 

        return campaignProfileInfoGridPanel;
    }
}
