package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class BriefingPlayerFlightChooser implements ActionListener
{
    private Mission mission;
    private IFlightChanged playerFlightChangedCallback;
    private JPanel playerFlightChooserPanel;

    private ButtonGroup playerFlightChooserButtonGroup = new ButtonGroup();
    private Map<Integer, ButtonModel> playerFlightChooserButtonModels = new HashMap<>();

    public BriefingPlayerFlightChooser(Mission mission, IFlightChanged playerFlightChangedCallback)
    {
        this.mission = mission;
        this.playerFlightChangedCallback = playerFlightChangedCallback;
    }
    
    public void createBriefingPlayerSquadronSelectPanel() throws PWCGException
    {        
        JPanel flightChooserButtonPanelGrid = new JPanel(new GridLayout(0,1));
        flightChooserButtonPanelGrid.setOpaque(false);

        JLabel spacerLabel1 = PWCGLabelFactory.makeDummyLabel();        
        flightChooserButtonPanelGrid.add(spacerLabel1);

        Font font = PWCGMonitorFonts.getPrimaryFont();
        JLabel playerFlightSelectLabel = PWCGLabelFactory.makeTransparentLabel("Active Player Flight", ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        flightChooserButtonPanelGrid.add(playerFlightSelectLabel);

        Map<Integer, Squadron> playerSquadronsInMission = new HashMap<>();
        for (IFlight playerFlight : mission.getFlights().getPlayerFlights())
        {
            Squadron squadron = playerFlight.getSquadron();
            playerSquadronsInMission.put(squadron.getSquadronId(), squadron);
        }

        for (Squadron squadron : playerSquadronsInMission.values())
        {
            JRadioButton airLowDensity = PWCGButtonFactory.makeRadioButton(
                    squadron.determineDisplayName(mission.getCampaign().getDate()), 
                    "FlightChanged:" + squadron.getSquadronId(),
                    "Select squadron to change context", 
                    null, 
                    ColorMap.CHALK_FOREGROUND,
                    false, this);       
            flightChooserButtonPanelGrid.add(airLowDensity);
            ButtonModel model = airLowDensity.getModel();
            playerFlightChooserButtonGroup.add(airLowDensity);
            playerFlightChooserButtonModels.put(squadron.getSquadronId(), model);
        }

        playerFlightChooserPanel = new JPanel(new BorderLayout());
        playerFlightChooserPanel.setOpaque(false);
        playerFlightChooserPanel.add(flightChooserButtonPanelGrid, BorderLayout.SOUTH);

        JPanel shapePanel = new JPanel(new BorderLayout());
        shapePanel.setOpaque(false);

        shapePanel.add(flightChooserButtonPanelGrid, BorderLayout.NORTH);
        playerFlightChooserPanel.add(shapePanel, BorderLayout.CENTER);
    }

    public void setSelectedButton(int squadronId)
    {
        ButtonModel model = playerFlightChooserButtonModels.get(squadronId);
        playerFlightChooserButtonGroup.setSelected(model, true);
    }

    public JPanel getFlightChooserPanel()
    {
        return playerFlightChooserPanel;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            int index = action.indexOf(":");
            String selectedSquadronId = action.substring(index + 1);
            int squadronId = Integer.valueOf(selectedSquadronId);
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
            
            setSelectedButton(squadronId);

            playerFlightChangedCallback.flightChanged(squadron);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }

    }
}
