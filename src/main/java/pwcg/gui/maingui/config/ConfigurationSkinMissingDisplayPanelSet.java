package pwcg.gui.maingui.config;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MissingSkin;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePanelLayout;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.gui.utils.ToolTipManager;

public class ConfigurationSkinMissingDisplayPanelSet extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Map<String, List<MissingSkin>> selectedMissingSkins = new HashMap<String, List<MissingSkin>>();

	public ConfigurationSkinMissingDisplayPanelSet(Map<String, List<MissingSkin>> selectedMissingSkins) 
	{
        super();
	    this.selectedMissingSkins = selectedMissingSkins;
	    
		setLayout(new BorderLayout());
	}

    public void makeGUI()
    {
        try
        {
            this.add(BorderLayout.WEST, makeButtonPanel());
            this.add(BorderLayout.CENTER, makeCenterPanel());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

	public JPanel makeButtonPanel() throws PWCGException 
	{
        String imagePath = UiImageResolver.getImageMain("SkinAnalysisNav.jpg");

        ImageResizingPanel campaignButtonPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        campaignButtonPanel.setLayout(new BorderLayout());
        campaignButtonPanel.setOpaque(true);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        for (int i = 0; i < 2; ++i)
        {
            JLabel spacerLabel = new JLabel("     ");
            spacerLabel.setHorizontalAlignment(SwingConstants.LEFT);
            spacerLabel.setOpaque(false);
            
            buttonPanel.add(spacerLabel);
        }
        
        JButton reportButton = makePlainButton("      Return", "Return", "Return to missing skin main page");
        buttonPanel.add(reportButton);

        campaignButtonPanel.add (buttonPanel, BorderLayout.NORTH);
        
        return campaignButtonPanel;
 	}

    private JButton makePlainButton(String buttonText, String commandText, String toolTiptext) throws PWCGException
    {
        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, commandText, this);
        ToolTipManager.setToolTip(button, toolTiptext);

        return button;
    }

    public JPanel makeCenterPanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "paperFull.jpg";
        JPanel missingSkinDisplayPanel = new ImagePanelLayout(imagePath, new BorderLayout());

        String header = generateReportHeader();
        
        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.setOpaque(false);

        Font headerFont = PWCGMonitorFonts.getDecorativeFont();
        JLabel reportHeaderLabel = new JLabel(header);
        reportHeaderLabel.setOpaque(false);
        reportHeaderLabel.setFont(headerFont);
        reportPanel.add(reportHeaderLabel, BorderLayout.NORTH);
        
        JPanel reportBodyPanel = generateReportBody();
        reportPanel.add(reportBodyPanel, BorderLayout.CENTER);
        
        JScrollPane waypointScrollPane = ScrollBarWrapper.makeScrollPane(reportPanel);

        missingSkinDisplayPanel.add(waypointScrollPane, BorderLayout.CENTER);
        
        return missingSkinDisplayPanel;
    }

    private String generateReportHeader()
    {
        int numMissingSkins = 0;
                
        for (List<MissingSkin> missingSkinSet : selectedMissingSkins.values())
        {
            numMissingSkins += missingSkinSet.size();
        }

        return "Missing Skin Report: " + numMissingSkins + " skins are missing";
    }

    private JPanel generateReportBody() throws PWCGException
    {
        JPanel reportBodyPanel = new JPanel(new GridLayout(0,2));
        reportBodyPanel.setOpaque(false);
                
        for (String planeTypeDesc : selectedMissingSkins.keySet())
        {
            PlaneType plane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeTypeDesc);
            List<MissingSkin> missingSkinSet = selectedMissingSkins.get(planeTypeDesc);
            
            if (missingSkinSet.size() > 0)
            {
                // The plane
                JLabel reportBodyPlaneLabel = PWCGButtonFactory.makePaperLabelMedium("Plane: " + plane.getDisplayName());
                reportBodyPanel.add(reportBodyPlaneLabel);
                JLabel reportBodyPlaneDummy = PWCGButtonFactory.makePaperLabelMedium(" ");
                reportBodyPanel.add(reportBodyPlaneDummy);
    
                // The missing skins
                for (MissingSkin missingSkin : missingSkinSet)
                {
                    JLabel reportBodySkinLabel = PWCGButtonFactory.makePaperLabelMedium(missingSkin.getSkinName());
                    reportBodyPanel.add(reportBodySkinLabel);
                    JLabel reportBodyCategoryLabel = PWCGButtonFactory.makePaperLabelMedium(missingSkin.getCategory());
                    reportBodyPanel.add(reportBodyCategoryLabel);
                }
            }
        }
        
        return reportBodyPanel;
    }

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if (ae.getActionCommand().equalsIgnoreCase("Return"))
			{
                CampaignGuiContextManager.getInstance().popFromContextStack();
			}
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
}

