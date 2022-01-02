package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

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
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.ToolTipManager;

public class CrewMemberGenerationInfoGUI extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
    private CampaignNewCrewMemberScreen parent = null;
    private Campaign campaign = null;

	public CrewMemberGenerationInfoGUI(CampaignNewCrewMemberScreen parent, Campaign campaign)
	{
		super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.parent = parent;
        this.campaign = campaign;
	}

	public void makeServiceSelectionPanel() throws PWCGException
	{
	    JPanel serviceMainPanel = new JPanel(new BorderLayout());
		serviceMainPanel.setOpaque(false);

        JLabel chooseServiceLabel = PWCGLabelFactory.makeMenuLabelLarge("Choose a service:");
        serviceMainPanel.add(chooseServiceLabel, BorderLayout.NORTH);

        JPanel internalServicePanel = new JPanel();
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
		
		JPanel servicePanel = new JPanel(new GridLayout(numRows, numCols));
		servicePanel.setOpaque(false);

		// Make a button for each service
        ButtonGroup serviceButtonGroup = new ButtonGroup();
		for (ArmedService service : ArmedServiceFinder.getArmedServicesForCrewMemberCreation(campaign))
		{
	        String icon = service.getServiceIcon() + ".jpg";
	        JRadioButton serviceButton = makeRadioButton(service, icon);
	        servicePanel.add(serviceButton);
	        serviceButtonGroup.add(serviceButton);
		}

		internalServicePanel.add(servicePanel, BorderLayout.NORTH);
        
        serviceMainPanel.add(internalServicePanel, BorderLayout.CENTER);

		add(serviceMainPanel, BorderLayout.CENTER);
	}

	private JRadioButton makeRadioButton(ArmedService service, String imageName) throws PWCGException
	{	
        String commandText = "" + service.getServiceId();
		Color fgColor = ColorMap.CHALK_FOREGROUND;
		Font font = PWCGMonitorFonts.getPrimaryFont();
		JRadioButton button = PWCGButtonFactory.makeRadioButton(service.getName(), commandText, "", font, fgColor, false, this);

        String imagePath = ContextSpecificImages.imagesNational();
        String selectedIconPath = imagePath + imageName;
        String notSelectedIconPath = imagePath + "No" + imageName;
        
        ImageIcon selectedIcon = new ImageIcon(selectedIconPath);
        ImageIcon notSelectedIcon = new ImageIcon(notSelectedIconPath);
		button.setIcon(notSelectedIcon);
		button.setSelectedIcon(selectedIcon);
		
		ToolTipManager.setToolTip(button, service.getName());

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

