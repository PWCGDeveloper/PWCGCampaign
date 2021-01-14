package pwcg.mission.flight.contactpatrol;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetBuilder;

public class ContactPatrolPackage implements IFlightPackage
{
    public ContactPatrolPackage()
    {
    }


    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {        
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.CONTACT_PATROL);
        TargetDefinition targetDefinition = buildTargetDefinition(flightInformation);
        
        ContactPatrolFlight contactPatrol = new ContactPatrolFlight (flightInformation, targetDefinition);
        contactPatrol.createFlight();
        return contactPatrol;
    }
    
    private TargetDefinition buildTargetDefinition(FlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetBuilder(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
