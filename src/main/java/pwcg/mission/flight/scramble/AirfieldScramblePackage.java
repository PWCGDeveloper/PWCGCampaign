package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderFactory;

public class AirfieldScramblePackage
{
    private Mission mission;
    private Campaign campaign;
    private Side side;
    private Coordinate scrambleCoordinates;
    
    public AirfieldScramblePackage (Mission mission, Campaign campaign, Side side, Coordinate scrambleCoordinates)
    {
        this.campaign = campaign;
        this.mission = mission;
        this.side = side;
        this.scrambleCoordinates = scrambleCoordinates;
    }
    
    public IFlight createEnemyScramble() throws PWCGException
    {
        AirfieldScrambleFlight scramble = null;
        List<Squadron> residentSquadrons = getResidentSquadrons();
        if (residentSquadrons != null && residentSquadrons.size() > 0)
        {
            List<Squadron> fighterSquadrons = getFighterSquadrons(residentSquadrons);
            if (fighterSquadrons != null && fighterSquadrons.size() > 0)
            {
                scramble = createScrambleForResidentSquadron(fighterSquadrons);
            }
        }
        return scramble;
    }

    private List<Squadron> getResidentSquadrons() throws PWCGException
    {
        SquadronManager squadronManager =  PWCGContext.getInstance().getSquadronManager();

        List<Squadron> squadrons = null;
        squadrons = squadronManager.getNearestSquadronsBySide(
                campaign, 
                scrambleCoordinates, 
                0, 10000.0, 
                side,
                campaign.getDate());
        
        return squadrons;
    }

    private List<Squadron> getFighterSquadrons(List<Squadron> squadrons) throws PWCGException
    {
        List<Squadron> fighterSquadrons = new ArrayList<>();
        for (Squadron squadron : squadrons)
        {
            if (squadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_FIGHTER))
            {
                fighterSquadrons.add(squadron);
            }
        }
        
        return fighterSquadrons;
    }

    private AirfieldScrambleFlight createScrambleForResidentSquadron(List<Squadron> fighterSquadrons) throws PWCGException
    {
        Squadron scrambleSquadron = fighterSquadrons.get(0);
        boolean isPlayerFlight = false;
        FlightBuildInformation airfieldScrambleFlightBuildInformation = new FlightBuildInformation(mission, scrambleSquadron, isPlayerFlight);
        IFlightInformation airfieldScrambleFlightInformation = FlightInformationFactory.buildFlightInformation(airfieldScrambleFlightBuildInformation, FlightTypes.SCRAMBLE);
        TargetDefinition targetDefinition = buildAirfieldScrambleTargetDefintion(airfieldScrambleFlightInformation, scrambleSquadron);

        AirfieldScrambleFlight scramble =  new AirfieldScrambleFlight(airfieldScrambleFlightInformation, targetDefinition);
        scramble.createFlight();
        return scramble;
    }


    private TargetDefinition buildAirfieldScrambleTargetDefintion(IFlightInformation airfieldScrambleFlightBuildInformation, Squadron squadron) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = TargetDefinitionBuilderFactory.createFlightTargetDefinitionBuilder(airfieldScrambleFlightBuildInformation);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinition();
        
        targetDefinition.setPosition(squadron.determineCurrentPosition(airfieldScrambleFlightBuildInformation.getCampaign().getDate()));
        return targetDefinition;
        
    }
}
