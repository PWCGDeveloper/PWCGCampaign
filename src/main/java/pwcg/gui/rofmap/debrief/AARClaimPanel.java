package pwcg.gui.rofmap.debrief;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.aar.AARCoordinator;
import pwcg.aar.inmission.phase3.reconcile.victories.PlayerDeclarations;
import pwcg.aar.inmission.phase3.reconcile.victories.PlayerVictoryDeclaration;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.PWCGButtonFactory;

public class AARClaimPanel extends AARPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

    private JComboBox<String> cbVictoriesClaimedBoxes = new JComboBox<String>();
	private List<JComboBox<String>> cbPlaneBoxes = new ArrayList<JComboBox<String>>();
    private JPanel victoriesClaimedPanel = null;
	
	private int numVictories = 0;

	private List<String> planeTypesInMission = new ArrayList<String>();

	public AARClaimPanel() throws PWCGException 
	{
	    super();
		this.setOpaque(false);
	}

	public void makePanel() throws PWCGException 
	{
		JPanel selectedPanel = makeSelectPanel();
		this.add(selectedPanel, BorderLayout.NORTH);

		victoriesClaimedPanel = makeClaimsPanel();
		this.add(victoriesClaimedPanel, BorderLayout.CENTER);
	}

	public JPanel makeSelectPanel() throws PWCGException 
	{
		Color bgColor = ColorMap.PAPER_BACKGROUND;
		
		Font font = MonitorSupport.getPrimaryFont();
		
		JPanel selectedPanel = new JPanel (new BorderLayout());
		selectedPanel.setOpaque(false);
		
		cbVictoriesClaimedBoxes.setOpaque(false);
		cbVictoriesClaimedBoxes.setBackground(bgColor);
		cbVictoriesClaimedBoxes.setSize(300, 40);		
		cbVictoriesClaimedBoxes.addActionListener(this);
		cbVictoriesClaimedBoxes.setFont(font);

		for (int i = 0 ; i < 10; ++i)
		{
			cbVictoriesClaimedBoxes.addItem("Victories Claimed: " + i);
		}

		JPanel victoriesClaimedPanel = new JPanel (new GridLayout(0,4));
		victoriesClaimedPanel.setOpaque(false);
		
		victoriesClaimedPanel.add(PWCGButtonFactory.makeDummy());

		JLabel lVictories = new JLabel("Air to Air Claims: ", JLabel.RIGHT);
		lVictories.setBackground(bgColor);
		lVictories.setFont(font);
		lVictories.setOpaque(false);
		victoriesClaimedPanel.add(lVictories);
		victoriesClaimedPanel.add(cbVictoriesClaimedBoxes);
		victoriesClaimedPanel.setBackground(bgColor);

		victoriesClaimedPanel.add(PWCGButtonFactory.makeDummy());
		
		selectedPanel.add (victoriesClaimedPanel, BorderLayout.NORTH);
		
		return selectedPanel;
	}
	
	private JPanel makeClaimsPanel() throws PWCGException 
	{
		Color bgColor = ColorMap.PAPER_BACKGROUND;
		
		Font font = MonitorSupport.getPrimaryFont();

		victoriesClaimedPanel = new JPanel (new BorderLayout());
		victoriesClaimedPanel.setOpaque(false);
		victoriesClaimedPanel.setFont(font);

		JPanel victoriesClaimedMainGridPanel = new JPanel (new GridLayout(0,1));
		victoriesClaimedMainGridPanel.setOpaque(false);

		for (int i = 0; i < numVictories; ++i)
		{
			JPanel victoryPanel = new JPanel(new GridLayout(0,4));
			victoryPanel.setOpaque(false);

			JLabel lVictories = new JLabel("Victory Report: ", JLabel.RIGHT);
			lVictories.setOpaque(false);
			lVictories.setFont(font);

			// The plane drop down
			JComboBox<String> cbPlane = new JComboBox<String>();
			cbPlane.setOpaque(false);
			cbPlane.setBackground(bgColor);
			cbPlane.setSize(300, 40);		
			cbPlane.setFont(font);

			// Get plane list for the other side
            planeTypesInMission = AARCoordinator.getInstance().getAarContext().getPreliminaryData().getClaimPanelData().getEnemyPlaneTypesInMission();                   
    			
			for (String planeName : planeTypesInMission)
			{
                String planeDisplayName = planeName;
                PlaneType plane = PWCGContextManager.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeName);
                if (plane != null)
                {
                    planeDisplayName = plane.getDisplayName();
                }
                cbPlane.addItem(planeDisplayName);
			}
			cbPlaneBoxes.add(cbPlane);

			victoryPanel.add(PWCGButtonFactory.makeDummy());
			victoryPanel.add(lVictories);
			victoryPanel.add(cbPlane);
			victoryPanel.add(PWCGButtonFactory.makeDummy());
			
			victoriesClaimedMainGridPanel.add (victoryPanel);
		}
		
		victoriesClaimedPanel.add(victoriesClaimedMainGridPanel, BorderLayout.NORTH);
		
		return victoriesClaimedPanel;
	}

	public Map<Integer, PlayerDeclarations> getPlayerDeclarations() throws PWCGException 
	{
        Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
	    PlayerDeclarations playerDeclarationForOnePlayer = new PlayerDeclarations();
		for (int i = 0; i < numVictories; ++i)
		{
			PlayerVictoryDeclaration declaration = new PlayerVictoryDeclaration();
			String planeTypeDesc = (String)cbPlaneBoxes.get(i).getSelectedItem();
				
			if (planeTypeDesc.equals(PlaneType.BALLOON))
			{
			    declaration.setAircraftType(PlaneType.BALLOON);
	            playerDeclarationForOnePlayer.addDeclaration(declaration);
			}
			else
			{
			    PlaneType planeType = PWCGContextManager.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeTypeDesc);
			    if (planeType != null)
			    {
    			    if (planeType.getType().equalsIgnoreCase(planeType.getType()))
    			    {
    			        declaration.setAircraftType(planeType.getType());
    		            playerDeclarationForOnePlayer.addDeclaration(declaration);
    			    }
			    }
			    else
			    {
			        Logger.log(LogLevel.ERROR, "Declared plane type not found: " + planeTypeDesc);
			    }
			}
		}
		
		// TODO Need to specify particula plazyer after tabs are in place
        playerDeclarations.put(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, playerDeclarationForOnePlayer);
		return playerDeclarations;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		String victoriesString = (String)cbVictoriesClaimedBoxes.getSelectedItem();
		int beginIndex = victoriesString.indexOf(":");
		++beginIndex;
		String numberString = victoriesString.substring(beginIndex).trim();
		numVictories = new Integer(numberString).intValue();
		
		try
		{
		    setMapForDebrief();
		    
			if (victoriesClaimedPanel != null)
			{
				JPanel victoriesClaimedPanel = makeClaimsPanel();
				BorderLayout layout = (BorderLayout)this.getLayout();
				this.remove(layout.getLayoutComponent(BorderLayout.CENTER));
				this.add(victoriesClaimedPanel, BorderLayout.CENTER);
				
				this.setVisible(false);
				this.setVisible(true);
			}
		}
		catch (Exception e)
		{
			Logger.logException(e);
		}
	}

	private void setMapForDebrief() throws PWCGException
	{	    
        PWCGContextManager.getInstance().changeContext(AARCoordinator.getInstance().getAarContext().getPreliminaryData().getClaimPanelData().getMapId());
	}
}
