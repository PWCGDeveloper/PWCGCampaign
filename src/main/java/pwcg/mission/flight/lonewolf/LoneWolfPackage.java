package pwcg.mission.flight.lonewolf;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;

public class LoneWolfPackage implements IFlightPackage
{
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    public LoneWolfPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.LONE_WOLF);
        this.targetDefinition = buildTargetDefintion();

        LoneWolfFlight patrolFlight = new LoneWolfFlight (flightInformation, targetDefinition);
        patrolFlight.createFlight();
        return patrolFlight;
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
