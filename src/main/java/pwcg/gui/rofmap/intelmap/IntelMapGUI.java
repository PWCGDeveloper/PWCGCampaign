package pwcg.gui.rofmap.intelmap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapScroll;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePanelLayout;
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;

public class IntelMapGUI extends MapGUI implements ActionListener
{
    private static String MAP_DELIMITER = "Map: ";
	private static final long serialVersionUID = 1L;

    private Campaign campaign = null;
    private JPanel rightPanel;

	public IntelMapGUI(Date mapDate) throws PWCGException  
	{
		super(mapDate);
		setLayout(new BorderLayout());
		this.campaign = PWCGContext.getInstance().getCampaign();
	}

	public void makePanels() 
	{
		try
		{
			Color bg = ColorMap.MAP_BACKGROUND;
			setSize(200, 200);
			setOpaque(false);
			setBackground(bg);

	        List<FrontMapIdentifier> airfieldMaps = AirfieldManager.getMapIdForAirfield(campaign.findReferencePlayer().determineSquadron().determineCurrentAirfieldName(campaign.getDate()));
            PWCGContext.getInstance().changeContext(airfieldMaps.get(0));
								
			this.add(BorderLayout.EAST, makeRightPanel(-1));
			this.add(BorderLayout.CENTER, createMapPanel());
			this.add(BorderLayout.WEST, makeNavigationPanel());		
			
	        centerMapAt(null);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError (e.getMessage());
		}
	}

    private JPanel createMapPanel() throws PWCGException, PWCGException
    {
        JPanel intelMapCenterPanel = new JPanel(new BorderLayout());

        IntelMapPanel mapPanel = new IntelMapPanel(this, campaign);
        mapScroll = new MapScroll(mapPanel);  
        mapPanel.setData();

        Campaign campaign = PWCGContext.getInstance().getCampaign();
        makeMapPanelPoints(campaign.getDate());
        
        centerIntelMap();
        
        intelMapCenterPanel.add(mapScroll.getMapScrollPane());
        return intelMapCenterPanel;
    }

    private JPanel makeNavigationPanel() throws PWCGException  
    {
        JPanel intelNavPanel = new JPanel();
        intelNavPanel.setLayout(new BorderLayout());
        intelNavPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finished = PWCGButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished reading intel map", this);
        buttonPanel.add(finished);
        
        JLabel spacer1 = PWCGLabelFactory.makeMenuLabelLarge("");
        buttonPanel.add(spacer1);

        JPanel radioButtonPanel = new JPanel( new GridLayout(0,1));
        radioButtonPanel.setOpaque(false);
        
        JLabel spacer2 = PWCGLabelFactory.makeMenuLabelLarge("");
        buttonPanel.add(spacer2);

        intelNavPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return intelNavPanel;
    }
    
	private void makeMapPanelPoints(Date date) throws PWCGException  
	{
	    IntelMapPanel mapPanel = (IntelMapPanel)mapScroll.getMapPanel();
	    mapPanel.setData();
	}

	private void centerIntelMap()
	{
        IntelMapPanel mapPanel = (IntelMapPanel)mapScroll.getMapPanel();
        
		Point initialPosition = null;
	    for (IntelSquadronMapPoint mapPoint : mapPanel.getSquadronPoints().values())
	    {
	    	if (mapPoint.isPlayerSquadron)
	    	{
				initialPosition = mapPanel.coordinateToPoint(mapPoint.coord);
	    	}
	    }
	    
		centerMapAt(initialPosition);

	}

    public void updateInfoPanel(int squadId) throws PWCGException 
    {
        if (rightPanel != null)
        {
            this.remove(rightPanel);
        }

        makeRightPanel(squadId);
        this.add(BorderLayout.EAST, rightPanel);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(this);
        
        this.revalidate();
        this.repaint();
    }

    private JPanel makeRightPanel(int squadId) throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "PaperPart.jpg";
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        rightPanel = new ImagePanelLayout(imagePath, new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBackground(bgColor); 

        JPanel squadronInfoPanel = makeInfoPanel(squadId);
        rightPanel.add(squadronInfoPanel, BorderLayout.CENTER);
        
        Dimension imagePanelDimensions = ImageToDisplaySizer.getTextAreaDimensionsForScreen(450);
        rightPanel.setPreferredSize(new Dimension(imagePanelDimensions.width, imagePanelDimensions.height));

        return rightPanel;
    }

	private JPanel makeInfoPanel(int squadId) throws PWCGException 
	{
		JPanel squadDescriptionPanel = new JPanel(new BorderLayout());
		squadDescriptionPanel.setOpaque(false);
		

		JTextPane squadDesc = makeIntelSquadronDescription(squadId);
		squadDescriptionPanel.add(squadDesc, BorderLayout.CENTER);
		
		return squadDescriptionPanel;
	}

	private JTextPane makeIntelSquadronDescription(int squadId) throws PWCGException
	{
		Font font = PWCGMonitorFonts.getTypewriterFont();

		String squadronText = "";
		Company squadron =  PWCGContext.getInstance().getCompanyManager().getCompany(squadId);
		if (squadron != null)
		{
			squadronText = squadron.determineSquadronDescription(mapDate);
		}

		Dimension screenSize = PWCGMonitorSupport.getPWCGFrameSize();
		int preferredPanelLength = screenSize.height - 100;

		JTextPane squadDesc = new JTextPane();
		squadDesc.setFont(font);
		squadDesc.setOpaque(false);
		squadDesc.setText(squadronText);
		squadDesc.setPreferredSize(new Dimension(300, preferredPanelLength));
        return squadDesc;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{		
		try
		{
			String action = arg0.getActionCommand();
			if (action.contains("Finished"))
			{				
                CampaignGuiContextManager.getInstance().popFromContextStack();
			}	
            else if (action.startsWith(MAP_DELIMITER))
            {
                int indexOfMapName = MAP_DELIMITER.length();
                String mapName = action.substring(indexOfMapName);
                FrontMapIdentifier mapIdentifier = FrontMapIdentifier.getFrontMapIdentifierForName(mapName);
                PWCGContext.getInstance().changeContext(mapIdentifier);
                
                JPanel mapCenterPanel = createMapPanel();
                this.add(BorderLayout.CENTER, mapCenterPanel); 

                centerMapAt(null);
            }
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			
			try
			{
                CampaignGuiContextManager.getInstance().popFromContextStack();
			}
			catch (Exception e2)
			{
	            PWCGLogger.logException(e2);
			}
		}
	}
}
