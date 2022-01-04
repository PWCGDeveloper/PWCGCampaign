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
import pwcg.campaign.tank.TankType;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MissingSkin;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePanelLayout;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.gui.utils.ToolTipManager;

public class MissingSkinScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private Map<String, List<MissingSkin>> selectedMissingSkins = new HashMap<String, List<MissingSkin>>();

	public MissingSkinScreen(Map<String, List<MissingSkin>> selectedMissingSkins) 
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

	    this.selectedMissingSkins = selectedMissingSkins;
	    
		setLayout(new BorderLayout());
	}

    public void makeGUI()
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.PwcgSkinConfigurationAnalysisScreen);
            this.setImageFromName(imagePath);

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
        JPanel navButtonPanel = new JPanel(new BorderLayout());
        navButtonPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        for (int i = 0; i < 2; ++i)
        {
            buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        }
        
        JButton reportButton = makePlainButton("      Return", "Return", "Return to missing skin main page");
        buttonPanel.add(reportButton);

        navButtonPanel.add (buttonPanel, BorderLayout.NORTH);
        
        return navButtonPanel;
 	}

    private JButton makePlainButton(String buttonText, String commandText, String toolTiptext) throws PWCGException
    {
        JButton button = PWCGButtonFactory.makeTranslucentMenuButton(buttonText, commandText, toolTiptext, this);
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
        
        JLabel reportHeaderLabel = PWCGLabelFactory.makeTransparentLabel(header, ColorMap.PAPER_FOREGROUND, headerFont, SwingConstants.LEFT);
        reportPanel.add(reportHeaderLabel, BorderLayout.NORTH);
        
        JPanel reportBodyPanel = generateReportBody();
        reportPanel.add(reportBodyPanel, BorderLayout.CENTER);
        
        JScrollPane waypointScrollPane = ScrollBarWrapper.makeScrollPane(reportPanel);

        missingSkinDisplayPanel.add(waypointScrollPane, BorderLayout.CENTER);
        
        return missingSkinDisplayPanel;
    }

    private String generateReportHeader() throws PWCGException
    {
        int numMissingSkins = 0;
                
        for (List<MissingSkin> missingSkinSet : selectedMissingSkins.values())
        {
            numMissingSkins += missingSkinSet.size();
        }

        return (InternationalizationManager.getTranslation("Missing Skin Report") + ": " + numMissingSkins + " " + InternationalizationManager.getTranslation("skins are missing"));
    }

    private JPanel generateReportBody() throws PWCGException
    {
        JPanel reportBodyPanel = new JPanel(new GridLayout(0,2));
        reportBodyPanel.setOpaque(false);
                
        for (String planeTypeDesc : selectedMissingSkins.keySet())
        {
            TankType plane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(planeTypeDesc);
            List<MissingSkin> missingSkinSet = selectedMissingSkins.get(planeTypeDesc);
            
            if (missingSkinSet.size() > 0)
            {
                JLabel reportBodyPlaneLabel = PWCGLabelFactory.makePaperLabelMedium("Plane: " + plane.getDisplayName());
                reportBodyPanel.add(reportBodyPlaneLabel);
                JLabel reportBodyPlaneDummy = PWCGLabelFactory.makePaperLabelMedium(" ");
                reportBodyPanel.add(reportBodyPlaneDummy);
    
                for (MissingSkin missingSkin : missingSkinSet)
                {
                    JLabel reportBodySkinLabel = PWCGLabelFactory.makePaperLabelMedium(missingSkin.getSkinName());
                    reportBodyPanel.add(reportBodySkinLabel);
                    JLabel reportBodyCategoryLabel = PWCGLabelFactory.makePaperLabelMedium(missingSkin.getCategory());
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

