package pwcg.mission.flight.bomb;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.builder.AAAUnitBuilder;
import pwcg.mission.ground.builder.SearchLightBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TargetDefinition;

public class StrategicBombingPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public StrategicBombingPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public IFlight createPackage() throws PWCGException
    {
        BombingFlight bombingFlight = createBombingFlight();

        createAAA(flightInformation.getTargetDefinition(), bombingFlight);
        createSearchlight(flightInformation.getTargetDefinition(), bombingFlight);
        return bombingFlight;
    }

    private BombingFlight createBombingFlight() throws PWCGException
    {
        BombingFlight bombingFlight = new BombingFlight(flightInformation);
        bombingFlight.createFlight();
        return bombingFlight;
    }

    private void createAAA(TargetDefinition targetDefinition, BombingFlight bombingFlight) throws PWCGException
    {
        AAAUnitBuilder groundUnitBuilder = new AAAUnitBuilder(flightInformation.getCampaign(), targetDefinition.getTargetCountry(), targetDefinition.getTargetPosition());
        IGroundUnitCollection aaaArty = groundUnitBuilder.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_HIGH);
        bombingFlight.addLinkedGroundUnit(aaaArty);
    }

    private void createSearchlight(TargetDefinition targetDefinition, BombingFlight bombingFlight) throws PWCGException
    {
        SearchLightBuilder groundUnitBuilder =  new SearchLightBuilder(flightInformation.getCampaign());
        if (flightInformation.getMission().isNightMission())
        {
            IGroundUnitCollection searchLightGroup = groundUnitBuilder.createSearchLightGroup(targetDefinition);
            bombingFlight.addLinkedGroundUnit(searchLightGroup);
        }
    }
}
