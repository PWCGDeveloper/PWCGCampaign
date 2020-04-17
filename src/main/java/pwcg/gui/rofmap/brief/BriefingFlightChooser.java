package pwcg.gui.rofmap.brief;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class BriefingFlightChooser implements ActionListener
{
    private Mission mission;
    private JComboBox<String> cbFlightChooser;
    private IFlightChanged flightChanged;

    public BriefingFlightChooser(Mission mission, IFlightChanged flightChanged)
    {
        this.mission = mission;
        this.flightChanged = flightChanged;
    }
    
    public void makeComboBox() throws PWCGException
    {
        Color jComboBoxBackgroundColor = ColorMap.PAPER_BACKGROUND;
        Font font = MonitorSupport.getPrimaryFontLarge();

        cbFlightChooser = new JComboBox<String>();
        for (IFlight playerFlight : mission.getMissionFlightBuilder().getPlayerFlights())
        {
            Squadron squadron = playerFlight.getSquadron();
            cbFlightChooser.addItem(squadron.determineDisplayName(mission.getCampaign().getDate()));
        }

        cbFlightChooser.setOpaque(false);
        cbFlightChooser.setBackground(jComboBoxBackgroundColor);
        cbFlightChooser.setSelectedIndex(0);
        cbFlightChooser.setActionCommand("FlightChanged");
        cbFlightChooser.addActionListener(this);
        cbFlightChooser.setFont(font);
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            if (ae.getActionCommand().equalsIgnoreCase("FlightChanged"))
            {
                int squadronIndex = cbFlightChooser.getSelectedIndex();
                Squadron squadron = mission.getMissionFlightBuilder().getPlayerFlights().get(squadronIndex).getSquadron();
                flightChanged.flightChanged(squadron);
            }  
        }
        catch (PWCGException e)
        {
        }
    }

    public JComboBox<String> getCbFlightChooser()
    {
        return cbFlightChooser;
    }
}
