package pwcg.gui.maingui.config;

import java.awt.BorderLayout;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePanelLayout;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;

public class PwcgSkinConfigurationAnalysisDisplayScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private Map<String, PlaneType> planeTypesToDisplay = null;

	public PwcgSkinConfigurationAnalysisDisplayScreen(Map<String, PlaneType> planeTypesToDisplay) 
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

	public Pane makeButtonPanel() throws PWCGException 
	{
        Pane campaignButtonPanel = new Pane(new BorderLayout());
        campaignButtonPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        for (int i = 0; i < 2; ++i)
        {
            Label spacerLabel = new Label("     ");
            spacerLabel.setAlignment(SwingConstants.LEFT);
            spacerLabel.setOpaque(false);
            
            buttonPanel.add(spacerLabel);
        }
        
        Button reportButton = ButtonFactory.makeTranslucentMenuButton("Return", "Return", "Return to skin analysis page", this);
        buttonPanel.add(reportButton);

        campaignButtonPanel.add (buttonPanel, BorderLayout.NORTH);
        
        return campaignButtonPanel;
 	}

    public Pane makeCenterPanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "paperFull.jpg";
        Pane missingSkinDisplayPanel = new ImagePanelLayout(imagePath, new BorderLayout());
        
        Pane reportPanel = new Pane(new BorderLayout());
        reportPanel.setOpaque(false);

        String header = generateReportHeader();
        Font headerFont = PWCGMonitorFonts.getDecorativeFont();
        Label reportHeaderLabel = new Label(header);
        reportHeaderLabel.setOpaque(false);
        reportHeaderLabel.setFont(headerFont);
        reportPanel.add(reportHeaderLabel, BorderLayout.NORTH);
        
        Pane reportBodyPanel = generateReportBody();
        reportPanel.add(reportBodyPanel, BorderLayout.CENTER);
        
        JScrollPane waypointScrollPane = ScrollBarWrapper.makeScrollPane(reportPanel);

        missingSkinDisplayPanel.add(waypointScrollPane, BorderLayout.CENTER);
        
        return missingSkinDisplayPanel;
    }

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

    private Pane generateReportBody() throws PWCGException
    {
        Pane reportBodyPanel = new Pane(new GridLayout(0,5));
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

    private void addSkinsForPlane(Pane reportBodyPanel, PlaneType plane, List<Skin> skinSet) throws PWCGException
    {
        Label reportBodyPlaneLabel = ButtonFactory.makePaperLabelMedium("Plane: " + plane.getDisplayName());
        reportBodyPanel.add(reportBodyPlaneLabel);
        
        Label reportBodyPlaneDummy = null;
        reportBodyPlaneDummy = ButtonFactory.makePaperLabelMedium(" ");
        reportBodyPanel.add(reportBodyPlaneDummy);
        reportBodyPlaneDummy = ButtonFactory.makePaperLabelMedium(" ");
        reportBodyPanel.add(reportBodyPlaneDummy);
        reportBodyPlaneDummy = ButtonFactory.makePaperLabelMedium(" ");
        reportBodyPanel.add(reportBodyPlaneDummy);
        reportBodyPlaneDummy = ButtonFactory.makePaperLabelMedium(" ");
        reportBodyPanel.add(reportBodyPlaneDummy);

        TreeMap<String, Skin> sortedSkins = new TreeMap<String, Skin>();
        for (Skin skin : skinSet)
        {
            String skinKey = skin.getSkinName() + skin.getSquadId();
            sortedSkins.put(skinKey, skin);
        }
        
        for (Skin skin : sortedSkins.values())
        {
            Label reportBodySkinLabel = ButtonFactory.makePaperLabelMedium(skin.getSkinName());
            reportBodyPanel.add(reportBodySkinLabel);
            
            Label reportBodyCategoryLabel = ButtonFactory.makePaperLabelMedium(skin.getCategory());
            reportBodyPanel.add(reportBodyCategoryLabel);
            
            Label reportBodyStartDateLabel = ButtonFactory.makePaperLabelMedium(DateUtils.getDateStringPretty(skin.getStartDate()));
            reportBodyPanel.add(reportBodyStartDateLabel);
            
            Label reportBodyEndDateLabel = ButtonFactory.makePaperLabelMedium(DateUtils.getDateStringPretty(skin.getEndDate()));
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
            Label reportBodySquadronLabel = ButtonFactory.makePaperLabelMedium(squadronName);
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

