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

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePanelLayout;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.gui.utils.ToolTipManager;

public class ConfigurationSkinConfigDisplayPanelSet extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private Map<String, PlaneType> planeTypesToDisplay = null;

	public ConfigurationSkinConfigDisplayPanelSet(Map<String, PlaneType> planeTypesToDisplay) 
	{
	    this.planeTypesToDisplay = planeTypesToDisplay;
	    
		setLayout(new BorderLayout());
	}

    public void makeGUI()
    {
        try
        {
            setLeftPanel(makeButtonPanel());
            setCenterPanel(makeCenterPanel());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

	public JPanel makeButtonPanel() throws PWCGException 
	{
        String imagePath = getSideImageMain("SkinAnalysisNav.jpg");

        ImageResizingPanel campaignButtonPanel = new ImageResizingPanel(imagePath);
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
        
        JButton reportButton = makePlainButton("      Return", "Return", "Return to skin analysis page");
        buttonPanel.add(reportButton);

        campaignButtonPanel.add (buttonPanel, BorderLayout.NORTH);
        
        return campaignButtonPanel;
 	}

    
    /**
     * @param imageName
     * @return
     * @throws PWCGException 
     * @
     */
    private JButton makePlainButton(String buttonText, String commandText, String toolTiptext) throws PWCGException
    {
        JButton button = PWCGButtonFactory.makeMenuButton(buttonText, commandText, this);
        ToolTipManager.setToolTip(button, toolTiptext);

        return button;
    }



    /**
     * @return
     * @throws PWCGException
     */
    public JPanel makeCenterPanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "paperFull.jpg";
        JPanel missingSkinDisplayPanel = new ImagePanelLayout(imagePath, new BorderLayout());

        // The  award panel
        
        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.setOpaque(false);

        String header = generateReportHeader();
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


    /**
     * @return
     */
    String generateReportHeader()
    {
        Map<String, List<Skin>> allSkinsInPWCG = PWCGContext.getInstance().getSkinManager().getAllSkinsByPlane();
        int numSkinsInPWCG = 0;
        for (List<Skin> skinSet : allSkinsInPWCG.values())
        {
            numSkinsInPWCG += skinSet.size();
        }
        
        return "Skin Report: " + numSkinsInPWCG + " skin configurations exist in PWCG";
    }


    /**
     * @return
     * @throws PWCGException 
     */
    private JPanel generateReportBody() throws PWCGException
    {
        JPanel reportBodyPanel = new JPanel(new GridLayout(0,5));
        reportBodyPanel.setOpaque(false);
                
        Map<String, List<Skin>> allSkinsInPWCG = PWCGContext.getInstance().getSkinManager().getAllSkinsByPlane();
        
        for (String planeTypeDesc : planeTypesToDisplay.keySet())
        {
            PlaneType plane = planeTypesToDisplay.get(planeTypeDesc);
            List<Skin> skinSet = allSkinsInPWCG.get(planeTypeDesc);
            
            addSkinsForPlane(reportBodyPanel, plane, skinSet);
        }
        
        return reportBodyPanel;
    }

    private void addSkinsForPlane(JPanel reportBodyPanel, PlaneType plane, List<Skin> skinSet) throws PWCGException
    {
        // The plane
        JLabel reportBodyPlaneLabel = PWCGButtonFactory.makePaperLabelMedium("Plane: " + plane.getDisplayName());
        reportBodyPanel.add(reportBodyPlaneLabel);
        
        JLabel reportBodyPlaneDummy = null;
        reportBodyPlaneDummy = PWCGButtonFactory.makePaperLabelMedium(" ");
        reportBodyPanel.add(reportBodyPlaneDummy);
        reportBodyPlaneDummy = PWCGButtonFactory.makePaperLabelMedium(" ");
        reportBodyPanel.add(reportBodyPlaneDummy);
        reportBodyPlaneDummy = PWCGButtonFactory.makePaperLabelMedium(" ");
        reportBodyPanel.add(reportBodyPlaneDummy);
        reportBodyPlaneDummy = PWCGButtonFactory.makePaperLabelMedium(" ");
        reportBodyPanel.add(reportBodyPlaneDummy);

        // The missing skins
        TreeMap<String, Skin> sortedSkins = new TreeMap<String, Skin>();
        for (Skin skin : skinSet)
        {
            String skinKey = skin.getSkinName() + skin.getSquadId();
            sortedSkins.put(skinKey, skin);
        }
        
        // The missing skins
        for (Skin skin : sortedSkins.values())
        {
            JLabel reportBodySkinLabel = PWCGButtonFactory.makePaperLabelMedium(skin.getSkinName());
            reportBodyPanel.add(reportBodySkinLabel);
            
            JLabel reportBodyCategoryLabel = PWCGButtonFactory.makePaperLabelMedium(skin.getCategory());
            reportBodyPanel.add(reportBodyCategoryLabel);
            
            JLabel reportBodyStartDateLabel = PWCGButtonFactory.makePaperLabelMedium(DateUtils.getDateStringPretty(skin.getStartDate()));
            reportBodyPanel.add(reportBodyStartDateLabel);
            
            JLabel reportBodyEndDateLabel = PWCGButtonFactory.makePaperLabelMedium(DateUtils.getDateStringPretty(skin.getEndDate()));
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
                Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(skin.getSquadId());
                if (squadron != null)
                {
                    squadronName = "" + squadron.getSquadronId();
                }
                else
                {
                    squadronName = "Not Defined";
                }
            }
            JLabel reportBodySquadronLabel = PWCGButtonFactory.makePaperLabelMedium(squadronName);
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

