package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pwcg.campaign.CampaignMode;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.campaign.config.CampaignConfigurationSimpleGUIController;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.PWCGButtonFactory;

public class CampaignGeneratorProfileGUI extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
    private static final Color textBoxBackgroundColor = ColorMap.CHALK_FOREGROUND;
    
	private static Font font = null;
	
    private ButtonGroup coopGroup = new ButtonGroup();
    private ButtonModel singlePlayerButtonModel = null;
    private ButtonModel coopCooperativeButtonModel = null;
    
    private JTextField campaignNameTextBox;
     
    private JLabel lCampaignType;
    private JLabel lCampaignName;

    private CampaignGeneratorScreen parent;

    private CampaignMode campaignMode = CampaignMode.CAMPAIGN_MODE_NONE;
    private String campaignName = "";

	public CampaignGeneratorProfileGUI(CampaignGeneratorScreen parent) 
	{
        super();
        this.setOpaque(false);
        this.setLayout(new BorderLayout());

        this.parent = parent;       
	}
	

	public void makePanels() throws PWCGException 
	{
	    font = PWCGMonitorFonts.getPrimaryFontLarge();

		try
		{			
			JPanel campaignGeneratePanel = new JPanel(new BorderLayout());
			campaignGeneratePanel.setOpaque(false);
	        
			JPanel campaignNamePanel = createCampaignNameWidget();
			JPanel campaignModePanel = createCampaignModeWidget();
		    JPanel campaignChooseServiceGUI = makeServicePanel();
		    
		    
            campaignGeneratePanel.add(campaignModePanel, BorderLayout.NORTH);
            campaignGeneratePanel.add(campaignChooseServiceGUI, BorderLayout.CENTER);
            campaignGeneratePanel.add(campaignNamePanel, BorderLayout.SOUTH);
 			
			this.add(campaignGeneratePanel, BorderLayout.CENTER);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private JPanel createCampaignModeWidget() throws PWCGException
    {
        JPanel shapePanel = new JPanel(new BorderLayout());
        shapePanel.setOpaque(false);

        JPanel coopButtonPanelGrid = new JPanel(new GridLayout(0,1));
        coopButtonPanelGrid.setOpaque(false);
        
        lCampaignType = makeCoopLabel(CampaignConfigurationSimpleGUIController.CAMPAIGN_TYPE + ":");      
        coopButtonPanelGrid.add(lCampaignType);

        JRadioButton singlePlayerButton = PWCGButtonFactory.makeRadioButton(
                "Single Player Mode", 
                "Mission Mode: Single Player", 
                "Select single player mode for generated missions", 
                false, 
                this,
                ColorMap.CHALK_FOREGROUND);       
        coopButtonPanelGrid.add(singlePlayerButton);
        singlePlayerButtonModel = singlePlayerButton.getModel();
        coopGroup.add(singlePlayerButton);

        JRadioButton coopCooperativeButton = PWCGButtonFactory.makeRadioButton(
                "Coop Cooperative Mode", 
                "Mission Mode: Coop Cooperative", 
                "Select coop player mode for generated missions", 
                false, 
                this,
                ColorMap.CHALK_FOREGROUND);       
              
        coopButtonPanelGrid.add(coopCooperativeButton);
        coopCooperativeButtonModel = coopCooperativeButton.getModel();
        coopGroup.add(coopCooperativeButton);
        
        shapePanel.add(coopButtonPanelGrid, BorderLayout.NORTH);
        
        JPanel topSpacingGrid = new JPanel(new GridLayout(0,1));
        topSpacingGrid.setOpaque(false);
        topSpacingGrid.add(PWCGButtonFactory.makeDummy());
        topSpacingGrid.add(PWCGButtonFactory.makeDummy());
        
        JPanel bottomSpacingGrid = new JPanel(new GridLayout(0,1));
        bottomSpacingGrid.setOpaque(false);
        bottomSpacingGrid.add(PWCGButtonFactory.makeDummy());
        bottomSpacingGrid.add(PWCGButtonFactory.makeDummy());

        JPanel coopButtonPanel = new JPanel(new BorderLayout());
        coopButtonPanel.setOpaque(false);
        coopButtonPanel.add(topSpacingGrid, BorderLayout.NORTH);
        coopButtonPanel.add(shapePanel, BorderLayout.CENTER);
        coopButtonPanel.add(bottomSpacingGrid, BorderLayout.SOUTH);

        return coopButtonPanel;
    }

    private JPanel createCampaignNameWidget() throws PWCGException
    {
        lCampaignName = createCampaignGenMenuLabel("Campaign Name:");

        campaignNameTextBox = new JTextField(50);
        campaignNameTextBox.setFont(font);
        campaignNameTextBox.setBackground(textBoxBackgroundColor);
        campaignNameTextBox.setMaximumSize( campaignNameTextBox.getPreferredSize() );
        
        createTextDocumentListener();

        JPanel campaignNameContainerGrid = new JPanel(new GridLayout(0,1));
        campaignNameContainerGrid.setOpaque(false);
        
        JPanel campaignNameContainerPanel = new JPanel();
        campaignNameContainerPanel.setLayout(new BoxLayout(campaignNameContainerPanel, BoxLayout.LINE_AXIS));
        campaignNameContainerPanel.setOpaque(false);
        campaignNameContainerPanel.add(lCampaignName, BorderLayout.WEST);
        campaignNameContainerPanel.add(campaignNameTextBox, BorderLayout.CENTER);
        
        campaignNameContainerGrid.add(PWCGButtonFactory.makeDummy());
        campaignNameContainerGrid.add(campaignNameContainerPanel);
        campaignNameContainerGrid.add(PWCGButtonFactory.makeDummy());
        campaignNameContainerGrid.add(PWCGButtonFactory.makeDummy());
        campaignNameContainerGrid.add(PWCGButtonFactory.makeDummy());

        JPanel campaignNamePanel = new JPanel(new BorderLayout());
        campaignNamePanel.setOpaque(false);
        campaignNamePanel.add(campaignNameContainerGrid, BorderLayout.CENTER);

        return campaignNamePanel;
    }

    private void createTextDocumentListener()
    {
        DocumentListener campaignNameTextBoxListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFieldState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFieldState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFieldState();
            }

            protected void updateFieldState() {
                campaignName = campaignNameTextBox.getText();
                setCampaignProfileData();
            }
        };
        
        campaignNameTextBox.getDocument().addDocumentListener(campaignNameTextBoxListener);
    }
    
    private JPanel makeServicePanel() throws PWCGException
    {
        CampaignGeneratorChooseServiceGUI campaignChooseServiceGUI = new CampaignGeneratorChooseServiceGUI(parent);
        campaignChooseServiceGUI.makeServiceSelectionPanel();
        return campaignChooseServiceGUI;
    }

    private JLabel createCampaignGenMenuLabel(String labelText) throws PWCGException
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        
        JLabel menuLabel = new JLabel(labelText, JLabel.RIGHT);
        menuLabel.setFont(font);
        menuLabel.setForeground(fgColor);
        menuLabel.setOpaque(false);
        
        return menuLabel;
    }

    private JLabel makeCoopLabel(String buttonName) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        JLabel button= new JLabel(buttonName);
        button.setOpaque(false);
        button.setFont(font);

        return button;
    }

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
	        campaignMode = CampaignMode.CAMPAIGN_MODE_NONE;
	        if (ae.getActionCommand().contains("Single"))
	        {
	            campaignMode = CampaignMode.CAMPAIGN_MODE_SINGLE;;
	            coopGroup.setSelected(singlePlayerButtonModel, true);
	        }
	        else if (ae.getActionCommand().contains("Coop Cooperative"))
	        {
	            campaignMode = CampaignMode.CAMPAIGN_MODE_COOP;;
	            coopGroup.setSelected(coopCooperativeButtonModel, true);
	        }

	        setCampaignProfileData();
            
            revalidate();
            repaint();
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}


    private void setCampaignProfileData()
    {
        parent.setCampaignProfileParameters(campaignMode, campaignName);
    }
}
