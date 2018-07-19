package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.scramble.ScrambleFlight;

public class PackageAirfieldScramble
{
    private Mission mission;
    private Campaign campaign;
    
    public PackageAirfieldScramble (Mission mission, Campaign campaign)
    {
        this.campaign = campaign;
        this.mission = mission;
    }
    
    public Flight createEnemyScramble(Flight flight, Coordinate scrambleCoordinates) throws PWCGException
    {
        Flight enemyScramble = null;
        if (flight.isPlayerFlight())
        {
            enemyScramble = createEnemyScrambleForAirfieldAttack(scrambleCoordinates);
        }
        
        return enemyScramble;
    }

    protected Flight createEnemyScrambleForAirfieldAttack(Coordinate scrambleCoordinates) throws PWCGException 
    {
        ScrambleFlight scramble = null;
        List<Squadron> residentSquadrons = getResidentSquadrons(scrambleCoordinates);
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

    private List<Squadron> getResidentSquadrons(Coordinate scrambleCoordinates) throws PWCGException
    {
        SquadronManager squadronManager =  PWCGContextManager.getInstance().getSquadronManager();

        List<Squadron> squadrons = null;
        squadrons = squadronManager.getNearestSquadronsBySide(
                scrambleCoordinates, 
                0, 10000.0, 
                campaign.determineCountry().getSide().getOppositeSide(),
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

    private ScrambleFlight createScrambleForResidentSquadron(List<Squadron> fighterSquadrons) throws PWCGException
    {
        Squadron scrambleSquad = fighterSquadrons.get(0);

        Coordinate scrambleCoords = scrambleSquad.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(scrambleCoords.copy());

        FlightInformation opposingFlightInformation = FlightInformationFactory.buildAiFlightInformation(scrambleSquad, mission, FlightTypes.SCRAMBLE, scrambleCoords.copy());
        ScrambleFlight scramble =  new ScrambleFlight(opposingFlightInformation, missionBeginUnit);
        scramble.createUnitMission();
        return scramble;
    }

}
