package pwcg.campaign.io.transport;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.location.Coordinate;

public class MapTransport
{
    private List<List<Coordinate>> transportRoutes = new ArrayList<>();
    private List<Coordinate> allTransportCoordinates = new ArrayList<>();

    public void addTransportLocations(List<Coordinate> transportRoute)
    {
        this.transportRoutes.add(transportRoute);
        this.allTransportCoordinates.addAll(transportRoute);
    }

    public List<List<Coordinate>> getTransportRoutes()
    {
        return transportRoutes;
    }

    public List<Coordinate> getAllTransportCoordinates()
    {
        return allTransportCoordinates;
    }
    
}
