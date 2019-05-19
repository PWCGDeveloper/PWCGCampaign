package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pwcg.campaign.ArmedService;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ToolTipManager;

public class CampaignGeneratorChooseServiceGUI extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	private CampaignGeneratorPanelSet parent = null;

	public CampaignGeneratorChooseServiceGUI(CampaignGeneratorPanelSet parent)
	{
		super();
        this.setLayout(new BorderLayout());

		this.parent = parent;
	}

	public void makeServiceSelectionPanel(String imagePath) throws PWCGException
	{
		ImageResizingPanel serviceMainPanel = new ImageResizingPanel(imagePath);
		serviceMainPanel.setLayout(new BorderLayout());
		serviceMainPanel.setOpaque(false);

        JLabel chooseServiceLabel = PWCGButtonFactory.makeMenuLabelLarge("Choose a service:");
        serviceMainPanel.add(chooseServiceLabel, BorderLayout.NORTH);
        

        JPanel intrenalServicePanel = new ImageResizingPanel(imagePath);
        intrenalServicePanel.setLayout(new BorderLayout());
        intrenalServicePanel.setOpaque(false);

        int numRows = 0;
		int numCols = 1;

		Dimension screenSize = MonitorSupport.getPWCGFrameSize();
		if (screenSize.height < 900)
		{
	        numRows = 6;
	        numCols = 2;
		}
		
		JPanel servicePanel = new JPanel(new GridLayout(numRows, numCols));
		servicePanel.setOpaque(false);

		// Make a button for each service
        ButtonGroup serviceButtonGroup = new ButtonGroup();
		List<ArmedService> allServices = ArmedServiceFactory.createServiceManager().getAllArmedServices();
		for (ArmedService service : allServices)
		{
	        String icon = service.getServiceIcon() + ".jpg";
	        JRadioButton serviceButton = makeRadioButton(service, icon);
	        servicePanel.add(serviceButton);
	        serviceButtonGroup.add(serviceButton);
		}

		intrenalServicePanel.add(servicePanel, BorderLayout.NORTH);
        
        serviceMainPanel.add(intrenalServicePanel, BorderLayout.CENTER);

		add(serviceMainPanel, BorderLayout.CENTER);
	}

	private JRadioButton makeRadioButton(ArmedService service, String imageName) throws PWCGException
	{
        Color buttonBG = ColorMap.WOOD_BACKGROUND;
        Color buttonFG = ColorMap.CHALK_FOREGROUND;
        
        Font font = MonitorSupport.getPrimaryFont();

        String imagePath = ContextSpecificImages.imagesNational();
		String selectedIconPath = imagePath + imageName;
		String notSelectedIconPath = imagePath + "No" + imageName;
		
		ImageIcon selectedIcon = new ImageIcon(selectedIconPath);
		ImageIcon notSelectedIcon = new ImageIcon(notSelectedIconPath);
	
		JRadioButton button= new JRadioButton(service.getName());
		
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
			ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(new Integer(action));
			parent.changeService(service);
		}
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
}

