package pwcg.gui.rofmap.brief;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.gui.helper.BriefingMissionFlight;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.escort.VirtualEscortFlight;

public class BriefingMapFlightMapper
{
    private BriefingMissionFlight briefingMissionHandler;
    private BriefingMapPanel mapPanel;

    public BriefingMapFlightMapper(BriefingMissionFlight briefingMissionHandler, BriefingMapPanel mapPanel) throws PWCGException
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

        for (IFlight playerFlight : mission.getMissionFlightBuilder().getPlayerFlights())
        {
            mapPanel.makeMapPanelVirtualPoints(playerFlight);
        }

        for (IFlight flight : mission.getMissionFlightBuilder().getAiFlights())
        {
            mapFlightAndLinkedFlights(flight);
        }
    }

    private void mapFlightAndLinkedFlights(IFlight flight) throws PWCGException
    {
        mapPanel.makeMapPanelVirtualPoints(flight);

        for (IFlight linkedFlight : flight.getLinkedFlights().getLinkedFlights())
        {
            if (!(linkedFlight instanceof VirtualEscortFlight))
            {
                mapFlightAndLinkedFlights(linkedFlight);
            }
        }
    }

    private void mapFlightBox() throws PWCGException
    {
        CoordinateBox missionBorders = briefingMissionHandler.getMission().getMissionBorders().expandBox(5000);
        mapPanel.setMissionBorders(missionBorders);
    }
}
