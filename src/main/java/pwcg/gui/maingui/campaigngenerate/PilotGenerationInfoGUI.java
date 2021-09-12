package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.scene.control.ButtonGroup;
import javax.swing.ImageIcon;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.RadioButton ;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.ToolTipManager;

public class PilotGenerationInfoGUI extends Pane implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
    private CampaignNewPilotScreen parent = null;
    private Campaign campaign = null;

	public PilotGenerationInfoGUI(CampaignNewPilotScreen parent, Campaign campaign)
	{
		super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.parent = parent;
        this.campaign = campaign;
	}

	public void makeServiceSelectionPanel() throws PWCGException
	{
	    Pane serviceMainPanel = new Pane(new BorderLayout());
		serviceMainPanel.setOpaque(false);

        Label chooseServiceLabel = ButtonFactory.makeMenuLabelLarge("Choose a service:");
        serviceMainPanel.add(chooseServiceLabel, BorderLayout.NORTH);

        Pane internalServicePanel = new Pane();
        internalServicePanel.setLayout(new BorderLayout());
        internalServicePanel.setOpaque(false);

        int numRows = 0;
		int numCols = 1;

        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
		if (monitorSize == MonitorSize.FRAME_VERY_SMALL || monitorSize == MonitorSize.FRAME_SMALL)
		{
	        numRows = 6;
	        numCols = 2;
		}
		
		Pane servicePanel = new Pane(new GridLayout(numRows, numCols));
		servicePanel.setOpaque(false);

		// Make a button for each service
        ButtonGroup serviceButtonGroup = new ButtonGroup();
		for (ArmedService service : ArmedServiceFinder.getArmedServicesForPilotCreation(campaign))
		{
	        String icon = service.getServiceIcon() + ".jpg";
	        RadioButton  serviceButton = makeRadioButton(service, icon);
	        servicePanel.add(serviceButton);
	        serviceButtonGroup.add(serviceButton);
		}

		internalServicePanel.add(servicePanel, BorderLayout.NORTH);
        
        serviceMainPanel.add(internalServicePanel, BorderLayout.CENTER);

		add(serviceMainPanel, BorderLayout.CENTER);
	}

	private RadioButton  makeRadioButton(ArmedService service, String imageName) throws PWCGException
	{
        Color buttonBG = ColorMap.WOOD_BACKGROUND;
        Color buttonFG = ColorMap.CHALK_FOREGROUND;
        
        Font font = PWCGMonitorFonts.getPrimaryFont();

        String imagePath = ContextSpecificImages.imagesNational();
		String selectedIconPath = imagePath + imageName;
		String notSelectedIconPath = imagePath + "No" + imageName;
		
		ImageIcon selectedIcon = new ImageIcon(selectedIconPath);
		ImageIcon notSelectedIcon = new ImageIcon(notSelectedIconPath);
	
		RadioButton  button= new RadioButton (service.getName());
		
		button.setIcon(notSelectedIcon);
		button.setSelectedIcon(selectedIcon);
		
		button.addActionListener(this);
		button.setBackground(buttonBG);
		button.setForeground(buttonFG);
		button.setFont(font);
		button.setOpaque(false);
		ToolTipManager.setToolTip(button, service.getName());

        String actionCommand = "" + service.getServiceId();
        button.setActionCommand(actionCommand);

		return button;
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			String action = ae.getActionCommand();
			ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(Integer.valueOf(action));
			parent.changeService(service);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
}

