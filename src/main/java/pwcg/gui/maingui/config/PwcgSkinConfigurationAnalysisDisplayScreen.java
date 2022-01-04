package pwcg.gui.maingui.config;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.tank.TankType;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
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

public class PwcgSkinConfigurationAnalysisDisplayScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private Map<String, TankType> planeTypesToDisplay = null;

	public PwcgSkinConfigurationAnalysisDisplayScreen(Map<String, TankType> planeTypesToDisplay) 
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.planeTypesToDisplay = planeTypesToDisplay;
	    
		setLayout(new BorderLayout());
	}

    public void makeGUI()
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.PwcgSkinConfigurationAnalysisDisplayScreen);
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
        JPanel campaignButtonPanel = new JPanel(new BorderLayout());
        campaignButtonPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        for (int i = 0; i < 2; ++i)
        {
            buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        }
        
        JButton reportButton = PWCGButtonFactory.makeTranslucentMenuButton("Return", "Return", "Return to skin analysis page", this);
        buttonPanel.add(reportButton);

        campaignButtonPanel.add (buttonPanel, BorderLayout.NORTH);
        
        return campaignButtonPanel;
 	}

    public JPanel makeCenterPanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "paperFull.jpg";
        JPanel missingSkinDisplayPanel = new ImagePanelLayout(imagePath, new BorderLayout());
        
        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.setOpaque(false);

        String header = generateReportHeader();
        Font headerFont = PWCGMonitorFonts.getDecorativeFont();
        JLabel reportHeaderLabel = PWCGLabelFactory.makeTransparentLabel(header, ColorMap.PAPER_FOREGROUND, headerFont, SwingConstants.LEFT);
        reportPanel.add(reportHeaderLabel, BorderLayout.NORTH);
        
        JPanel reportBodyPanel = generateReportBody();
        reportPanel.add(reportBodyPanel, BorderLayout.CENTER);
        
        JScrollPane waypointScrollPane = ScrollBarWrapper.makeScrollPane(reportPanel);

        missingSkinDisplayPanel.add(waypointScrollPane, BorderLayout.CENTER);
        
        return missingSkinDisplayPanel;
    }

    String generateReportHeader() throws PWCGException
    {
        Map<String, List<Skin>> allSkinsInPWCG = PWCGContext.getInstance().getSkinManager().getAllSkinsByPlane();
        int numSkinsInPWCG = 0;
        for (List<Skin> skinSet : allSkinsInPWCG.values())
        {
            numSkinsInPWCG += skinSet.size();
        }
        
        return (InternationalizationManager.getTranslation("Skin Report") + ": " + numSkinsInPWCG + " " + InternationalizationManager.getTranslation("skin configurations exist in PWCG"));
    }

    private JPanel generateReportBody() throws PWCGException
    {
        JPanel reportBodyPanel = new JPanel(new GridLayout(0,5));
        reportBodyPanel.setOpaque(false);
                
        Map<String, List<Skin>> allSkinsInPWCG = PWCGContext.getInstance().getSkinManager().getAllSkinsByPlane();
        
        for (String planeTypeDesc : planeTypesToDisplay.keySet())
        {
            TankType plane = planeTypesToDisplay.get(planeTypeDesc);
            List<Skin> skinSet = allSkinsInPWCG.get(planeTypeDesc);
            
            addSkinsForPlane(reportBodyPanel, plane, skinSet);
        }
        
        return reportBodyPanel;
    }

    private void addSkinsForPlane(JPanel reportBodyPanel, TankType plane, List<Skin> skinSet) throws PWCGException
    {
        JLabel reportBodyPlaneLabel = PWCGLabelFactory.makePaperLabelMedium("Plane: " + plane.getDisplayName());
        reportBodyPanel.add(reportBodyPlaneLabel);
        
        JLabel reportBodyPlaneDummy = null;
        reportBodyPlaneDummy = PWCGLabelFactory.makePaperLabelMedium(" ");
        reportBodyPanel.add(reportBodyPlaneDummy);
        reportBodyPlaneDummy = PWCGLabelFactory.makePaperLabelMedium(" ");
        reportBodyPanel.add(reportBodyPlaneDummy);
        reportBodyPlaneDummy = PWCGLabelFactory.makePaperLabelMedium(" ");
        reportBodyPanel.add(reportBodyPlaneDummy);
        reportBodyPlaneDummy = PWCGLabelFactory.makePaperLabelMedium(" ");
        reportBodyPanel.add(reportBodyPlaneDummy);

        TreeMap<String, Skin> sortedSkins = new TreeMap<String, Skin>();
        for (Skin skin : skinSet)
        {
            String skinKey = skin.getSkinName() + skin.getSquadId();
            sortedSkins.put(skinKey, skin);
        }
        
        for (Skin skin : sortedSkins.values())
        {
            JLabel reportBodySkinLabel = PWCGLabelFactory.makePaperLabelMedium(skin.getSkinName());
            reportBodyPanel.add(reportBodySkinLabel);
            
            JLabel reportBodyCategoryLabel = PWCGLabelFactory.makePaperLabelMedium(skin.getCategory());
            reportBodyPanel.add(reportBodyCategoryLabel);
            
            JLabel reportBodyStartDateLabel = PWCGLabelFactory.makePaperLabelMedium(DateUtils.getDateStringPretty(skin.getStartDate()));
            reportBodyPanel.add(reportBodyStartDateLabel);
            
            JLabel reportBodyEndDateLabel = PWCGLabelFactory.makePaperLabelMedium(DateUtils.getDateStringPretty(skin.getEndDate()));
            reportBodyPanel.add(reportBodyEndDateLabel);
            
            String squadronName = "Not Defined";
            if (skin.getSquadId() == Skin.FACTORY_GENERIC)
            {
                squadronName = "Factory";
            }
            else if (skin.getSquadId() == Skin.PERSONAL_SKIN)
            {
                squadronName = "Unassociated Personal";
            }
            else
            {
                Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(skin.getSquadId());
                if (squadron != null)
                {
                    squadronName = "" + squadron.getCompanyId();
                }
                else
                {
                    squadronName = "Not Defined";
                }
            }
            JLabel reportBodySquadronLabel = PWCGLabelFactory.makePaperLabelMedium(squadronName);
            reportBodyPanel.add(reportBodySquadronLabel);
        }
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

