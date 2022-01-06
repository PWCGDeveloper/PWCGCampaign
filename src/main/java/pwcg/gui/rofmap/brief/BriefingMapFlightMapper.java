package pwcg.gui.rofmap.brief;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class BriefingMapFlightMapper
{
    private BriefingFlight briefingMissionHandler;
    private BriefingMapPanel mapPanel;

    public BriefingMapFlightMapper(BriefingFlight briefingMissionHandler, BriefingMapPanel mapPanel) throws PWCGException
    {
        this.briefingMissionHandler = briefingMissionHandler;
        this.mapPanel = mapPanel;
    }

    public void mapRequestedFlights() throws PWCGException
    {
        mapFlights();
        mapFlightBox();
    }

    private void mapFlights() throws PWCGException
    {
        Mission mission = briefingMissionHandler.getMission();
        mapPanel.clearVirtualPoints();

        for (IFlight playerFlight : mission.getFlights().getPlayerUnits())
        {
            mapPanel.makeMapPanelVirtualPoints(playerFlight);
        }

        for (IFlight flight : mission.getFlights().getAiFlights())
        {
            mapFlightAndLinkedFlights(flight);
        }
    }

    private void mapFlightAndLinkedFlights(IFlight flight) throws PWCGException
    {
        mapPanel.makeMapPanelVirtualPoints(flight);
    }

    private void mapFlightBox() throws PWCGException
    {
        CoordinateBox briefingBorders = CoordinateBox.copy(briefingMissionHandler.getMission().getMissionBorders());
        briefingBorders.expandBox(5000);
        mapPanel.setMissionBorders(briefingBorders);
    }
}
