package pwcg.mission.flight.recon;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;

public class ReconPackage implements IFlightPackage
{
    private List<IFlight> packageFlights = new ArrayList<>();

    @Override
    public List<IFlight> createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.RECON);
        TargetDefinition targetDefinition = buildTargetDefintion(flightInformation);

        if (flightBuildInformation.isPlayerFlight() && !flightBuildInformation.getMission().getCampaign().isCoop())
        {
        	PlayerReconFlight reconFlight = new PlayerReconFlight (flightInformation, targetDefinition);
        	reconFlight.createFlight();
            packageFlights.add(reconFlight);        	
        }
        else
        {
        	ReconFlight reconFlight = new ReconFlight (flightInformation, targetDefinition);
        	reconFlight.createFlight();
            packageFlights.add(reconFlight);
        }
        
        return packageFlights;
    }

    private TargetDefinition buildTargetDefintion(FlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
