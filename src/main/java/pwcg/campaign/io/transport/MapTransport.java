package pwcg.campaign.io.transport;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.location.Coordinate;

public class MapTransport
{
    private List<List<Coordinate>> transportRoutes = new ArrayList<>();

    public void addTransportLocations(List<Coordinate> transportRoute)
    {
        this.transportRoutes.add(transportRoute);
    }
}
