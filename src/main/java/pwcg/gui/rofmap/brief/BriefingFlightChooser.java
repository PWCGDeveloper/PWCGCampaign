package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.ButtonGroup;
import javafx.scene.control.ButtonModel;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.RadioButton ;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ButtonFactory;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class BriefingFlightChooser implements ActionListener
{
    private Mission mission;
    private IFlightChanged flightChanged;
    private Pane flightChooserPanel;

    private ButtonGroup flightChooserButtonGroup = new ButtonGroup();
    private Map<Integer, ButtonModel> flightChooserButtonModels = new HashMap<>();

    public BriefingFlightChooser(Mission mission, IFlightChanged flightChanged)
    {
        this.mission = mission;
        this.flightChanged = flightChanged;
    }
    
    public void createBriefingSquadronSelectPanel() throws PWCGException
    {        
        Pane flightChooserButtonPanelGrid = new Pane(new GridLayout(0,1));
        flightChooserButtonPanelGrid.setOpaque(false);

        Label spacerLabel1 = ButtonFactory.makeDummy();        
        flightChooserButtonPanelGrid.add(spacerLabel1);

        Label spacerLabel2 = ButtonFactory.makeDummy();        
        flightChooserButtonPanelGrid.add(spacerLabel2);

        Label spacerLabel3 = ButtonFactory.makeDummy();        
        flightChooserButtonPanelGrid.add(spacerLabel3);

        Map<Integer, Squadron> playerSquadronsInMission = new HashMap<>();
        for (IFlight playerFlight : mission.getMissionFlights().getPlayerFlights())
        {
            Squadron squadron = playerFlight.getSquadron();
            playerSquadronsInMission.put(squadron.getSquadronId(), squadron);
        }

        for (Squadron squadron : playerSquadronsInMission.values())
        {
            RadioButton  airLowDensity = ButtonFactory.makeRadioButton(
                    squadron.determineDisplayName(mission.getCampaign().getDate()), 
                    "FlightChanged:" + squadron.getSquadronId(),
                    "Select squadron to change context", 
                    false, 
                    this,
                    ColorMap.CHALK_FOREGROUND);       
            flightChooserButtonPanelGrid.add(airLowDensity);
            ButtonModel model = airLowDensity.getModel();
            flightChooserButtonGroup.add(airLowDensity);
            flightChooserButtonModels.put(squadron.getSquadronId(), model);
        }

        flightChooserPanel = new Pane(new BorderLayout());
        flightChooserPanel.setOpaque(false);
        flightChooserPanel.add(flightChooserButtonPanelGrid, BorderLayout.SOUTH);

        Pane shapePanel = new Pane(new BorderLayout());
        shapePanel.setOpaque(false);

        shapePanel.add(flightChooserButtonPanelGrid, BorderLayout.NORTH);
        flightChooserPanel.add(shapePanel, BorderLayout.CENTER);
    }

    public void setSelectedButton(int squadronId)
    {
        ButtonModel model = flightChooserButtonModels.get(squadronId);
        flightChooserButtonGroup.setSelected(model, true);
    }

    public Pane getFlightChooserPanel()
    {
        return flightChooserPanel;
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

            flightChanged.flightChanged(squadron);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }

    }
}
