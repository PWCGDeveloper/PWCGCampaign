package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ButtonGroup;
import javax.swing.ImageIcon;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.RadioButton ;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceFinder;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.ToolTipManager;

public class CampaignGeneratorChooseServiceGUI extends Pane implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	private CampaignGeneratorScreen parent = null;

	public CampaignGeneratorChooseServiceGUI(CampaignGeneratorScreen parent)
	{
        this.parent = parent;
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
	}

	public void makeServiceSelectionPanel() throws PWCGException
	{
		Pane serviceMainPanel = new Pane(new BorderLayout());
		serviceMainPanel.setLayout(new BorderLayout());
		serviceMainPanel.setOpaque(false);

        Label chooseServiceLabel = ButtonFactory.makeMenuLabelLarge("Choose a service:");
        serviceMainPanel.add(chooseServiceLabel, BorderLayout.NORTH);
        

        Pane internalServicePanel = new Pane(new BorderLayout());
        internalServicePanel.setLayout(new BorderLayout());
        internalServicePanel.setOpaque(false);

        ButtonGroup serviceButtonGroup = new ButtonGroup();
        
        Pane servicePanel = new Pane(new GridLayout(0, 2));
        servicePanel.setOpaque(false);
        
        Pane alliedServicePanel = new Pane(new GridLayout(0, 1));
        alliedServicePanel.setOpaque(false);
        
        Pane axisServicePanel = new Pane(new GridLayout(0, 1));
        axisServicePanel.setOpaque(false);

        servicePanel.add(alliedServicePanel);
        servicePanel.add(axisServicePanel);
        
        List<ArmedService> alliedArmedServices = getArmedServicesForSide(Side.ALLIED);
		for (ArmedService service : alliedArmedServices)
		{
	        String icon = service.getServiceIcon() + ".jpg";
	        RadioButton  serviceButton = makeRadioButton(service, icon);
	        alliedServicePanel.add(serviceButton);
	        serviceButtonGroup.add(serviceButton);
		}
		alliedServicePanel.add(makeBlankLabel());

        List<ArmedService> axisArmedServices = getArmedServicesForSide(Side.AXIS);
        for (ArmedService service : axisArmedServices)
        {
            String icon = service.getServiceIcon() + ".jpg";
            RadioButton  serviceButton = makeRadioButton(service, icon);
            axisServicePanel.add(serviceButton);
            serviceButtonGroup.add(serviceButton);
        }

        int numBlanksRequired = alliedArmedServices.size() - axisArmedServices.size();
        for (int i = 0; i < numBlanksRequired; ++i)
        {
            axisServicePanel.add(makeBlankLabel());
        }
        axisServicePanel.add(makeBlankLabel());

		internalServicePanel.add(servicePanel, BorderLayout.NORTH);
        
        serviceMainPanel.add(internalServicePanel, BorderLayout.CENTER);

		add(serviceMainPanel, BorderLayout.CENTER);
	}

    private Label makeBlankLabel()
    {
        Label blankLabel = new Label("   ");
        blankLabel.setOpaque(false);
        return blankLabel;
    }

	List<ArmedService> getArmedServicesForSide(Side side) throws PWCGException   
	{
	    List<ArmedService> armedServicesForSide = new ArrayList<>();
	    for (ArmedService armedService : ArmedServiceFinder.getArmedServicesAllSides())
	    {
	        if (armedService.getCountry().getSide() == side)
	        {
	            armedServicesForSide.add(armedService);
	        }
	    }
        return armedServicesForSide;
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

