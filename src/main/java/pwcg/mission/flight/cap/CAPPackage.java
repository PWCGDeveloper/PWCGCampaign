package pwcg.mission.flight.cap;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;

public class CAPPackage implements IFlightPackage
{	
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;
    private FlightTypes flightType;
    private List<IFlight> packageFlights = new ArrayList<>();

    public CAPPackage(FlightTypes flightType)
    {
        this.flightType = flightType;
    }

    @Override
    public List<IFlight> createPackage (Mission mission, ICountry country) throws PWCGException 
    {
        if (flightType != FlightTypes.INTERCEPT && flightType != FlightTypes.LOW_ALT_CAP)
        {
            throw new PWCGException("Invalid intercept flight type " + flightType);
        }
        
        this.flightInformation = FlightInformationFactory.buildFlightInformation(mission, country, flightType, planeType);
        this.targetDefinition = buildTargetDefintion();

        CAPFlight interceptFlight = new CAPFlight (flightInformation, targetDefinition);
        interceptFlight.createFlight();
        
        packageFlights.add(interceptFlight);
        return packageFlights;
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
