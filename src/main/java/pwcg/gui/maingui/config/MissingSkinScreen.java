package pwcg.gui.maingui.config;

import java.awt.BorderLayout;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MissingSkin;
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

	public Pane makeButtonPanel() throws PWCGException 
	{
        Pane navButtonPanel = new Pane(new BorderLayout());
        navButtonPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        for (int i = 0; i < 2; ++i)
        {
            Label spacerLabel = new Label("     ");
            spacerLabel.setAlignment(SwingConstants.LEFT);
            spacerLabel.setOpaque(false);
            
            buttonPanel.add(spacerLabel);
        }
        
        Button reportButton = makePlainButton("      Return", "Return", "Return to missing skin main page");
        buttonPanel.add(reportButton);

        navButtonPanel.add (buttonPanel, BorderLayout.NORTH);
        
        return navButtonPanel;
 	}

    private Button makePlainButton(String buttonText, String commandText, String toolTiptext) throws PWCGException
    {
        Button button = ButtonFactory.makeTranslucentMenuButton(buttonText, commandText, toolTiptext, this);
        ToolTipManager.setToolTip(button, toolTiptext);

        return button;
    }

    public Pane makeCenterPanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "paperFull.jpg";
        Pane missingSkinDisplayPanel = new ImagePanelLayout(imagePath, new BorderLayout());

        String header = generateReportHeader();
        
        Pane reportPanel = new Pane(new BorderLayout());
        reportPanel.setOpaque(false);

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

    private String generateReportHeader()
    {
        int numMissingSkins = 0;
                
        for (List<MissingSkin> missingSkinSet : selectedMissingSkins.values())
        {
            numMissingSkins += missingSkinSet.size();
        }

        return "Missing Skin Report: " + numMissingSkins + " skins are missing";
    }

    private Pane generateReportBody() throws PWCGException
    {
        Pane reportBodyPanel = new Pane(new GridLayout(0,2));
        reportBodyPanel.setOpaque(false);
                
        for (String planeTypeDesc : selectedMissingSkins.keySet())
        {
            PlaneType plane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeTypeDesc);
            List<MissingSkin> missingSkinSet = selectedMissingSkins.get(planeTypeDesc);
            
            if (missingSkinSet.size() > 0)
            {
                Label reportBodyPlaneLabel = ButtonFactory.makePaperLabelMedium("Plane: " + plane.getDisplayName());
                reportBodyPanel.add(reportBodyPlaneLabel);
                Label reportBodyPlaneDummy = ButtonFactory.makePaperLabelMedium(" ");
                reportBodyPanel.add(reportBodyPlaneDummy);
    
                for (MissingSkin missingSkin : missingSkinSet)
                {
                    Label reportBodySkinLabel = ButtonFactory.makePaperLabelMedium(missingSkin.getSkinName());
                    reportBodyPanel.add(reportBodySkinLabel);
                    Label reportBodyCategoryLabel = ButtonFactory.makePaperLabelMedium(missingSkin.getCategory());
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

