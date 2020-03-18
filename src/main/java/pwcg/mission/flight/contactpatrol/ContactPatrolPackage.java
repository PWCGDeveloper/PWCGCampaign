package pwcg.mission.flight.contactpatrol;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.factory.TargetFactory;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class ContactPatrolPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public ContactPatrolPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public IFlight createPackage () throws PWCGException 
	{
        IGroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        ContactPatrolFlight contactPatrol = createFlight(groundUnitCollection);
		return contactPatrol;
	}

    private ContactPatrolFlight createFlight(IGroundUnitCollection groundUnitCollection) throws PWCGException
    {
        ContactPatrolFlight contactPatrol = new ContactPatrolFlight (flightInformation);
        contactPatrol.createFlight();
        contactPatrol.addLinkedGroundUnit(groundUnitCollection);
        return contactPatrol;
    }

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetFactory targetBuilder = new TargetFactory(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
