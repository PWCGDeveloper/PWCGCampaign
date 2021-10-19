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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.squadron.Squadron;
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
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;

public class IntelMapGUI extends MapGUI implements ActionListener
{
    private static String MAP_DELIMITER = "Map: ";
	private static final long serialVersionUID = 1L;

    private ButtonGroup mapButtonGroup = new ButtonGroup();
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
        mapPanel.setMapBackground(100);

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

        JPanel mapCheckBoxesPanel = makeMapCheckBoxes();
        rightPanel.add(mapCheckBoxesPanel, BorderLayout.NORTH);

        JPanel squadronInfoPanel = makeInfoPanel(squadId);
        rightPanel.add(squadronInfoPanel, BorderLayout.CENTER);
        
        return rightPanel;
    }

	private JPanel makeInfoPanel(int squadId) throws PWCGException 
	{
		JPanel squadDescriptionPanel = new JPanel(new BorderLayout());
		squadDescriptionPanel.setOpaque(false);
		
		JPanel descriptionGrid = makeSquadronDescriptionGrid();
		squadDescriptionPanel.add(descriptionGrid, BorderLayout.NORTH);
		
		JTextPane squadDesc = makeIntelSquadronDescription(squadId);
		squadDescriptionPanel.add(squadDesc, BorderLayout.CENTER);
		
		return squadDescriptionPanel;
	}

	private JPanel makeSquadronDescriptionGrid() throws PWCGException
	{
		Font fontMain = PWCGMonitorFonts.getPrimaryFont();

        JPanel descriptionGrid = new JPanel(new GridLayout(0,1));
        descriptionGrid.setOpaque(false);

        descriptionGrid.add(PWCGLabelFactory.makeDummyLabel());
        
        JLabel header = new JLabel("Squadron Information");
        header.setFont(fontMain);
        header.setOpaque(false);
        descriptionGrid.add(header);
		return descriptionGrid;
	}

	private JTextPane makeIntelSquadronDescription(int squadId) throws PWCGException
	{
		Font font = PWCGMonitorFonts.getTypewriterFont();

		String squadronText = "";
		Squadron squadron =  PWCGContext.getInstance().getSquadronManager().getSquadron(squadId);
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

    private JPanel makeMapCheckBoxes() throws PWCGException
    {
        JPanel mapPanel = new JPanel(new BorderLayout());
        mapPanel.setOpaque(false);
                
        JPanel mapGrid = new JPanel( new GridLayout(0,1));
        mapGrid.setOpaque(false);
        
        mapPanel.add(mapGrid, BorderLayout.NORTH);

        JLabel mapLabel = PWCGLabelFactory.makeMenuLabelLarge("Choose Map");
        mapGrid.add(mapLabel);
        
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            PWCGMap moscowMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.MOSCOW_MAP);
            if (moscowMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.MOSCOW_MAP);
            }
            
            PWCGMap stalingradMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.STALINGRAD_MAP);
            if (stalingradMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.STALINGRAD_MAP);
            }
            
            PWCGMap kubanMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.KUBAN_MAP);
            if (kubanMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.KUBAN_MAP);
            }
            
            PWCGMap east1944Map = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.EAST1944_MAP);
            if (east1944Map.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.EAST1944_MAP);
            }
            
            PWCGMap east1945Map = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.EAST1945_MAP);
            if (east1945Map.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.EAST1945_MAP);
            }
            
            PWCGMap bodenplatteMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.BODENPLATTE_MAP);
            if (bodenplatteMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.BODENPLATTE_MAP);
            }
        }
        else if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            PWCGMap arrasMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.ARRAS_MAP);
            if (arrasMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.ARRAS_MAP);
            }
        }
        else
        {
            throw new PWCGException("No valid product selected");
        }
        
        return mapPanel;
    }

    private void addToMapGrid(JPanel mapGrid, FrontMapIdentifier mapIdentifier) throws PWCGException
    {
        mapGrid.add(makeRadioButton(mapIdentifier.getMapName(), MAP_DELIMITER + mapIdentifier.getMapName(), mapButtonGroup));
    }

    private JRadioButton makeRadioButton(String buttonText, String commandString, ButtonGroup buttonGroup) throws PWCGException 
    {
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        JRadioButton button = new JRadioButton(buttonText);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFont(font);
        button.setActionCommand(commandString);

        buttonGroup.add(button);

        return button;
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
