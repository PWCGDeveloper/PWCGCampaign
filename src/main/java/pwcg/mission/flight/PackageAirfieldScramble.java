package pwcg.mission.flight;

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
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.plot.FlightInformationFactory;
import pwcg.mission.flight.scramble.ScrambleFlight;

public class PackageAirfieldScramble
{
    private Mission mission;
    private Campaign campaign;
    private Side side;
    private Coordinate scrambleCoordinates;
    
    public PackageAirfieldScramble (Mission mission, Campaign campaign, Side side, Coordinate scrambleCoordinates)
    {
        this.campaign = campaign;
        this.mission = mission;
        this.side = side;
        this.scrambleCoordinates = scrambleCoordinates;
    }
    
    public Flight createEnemyScramble() throws PWCGException
    {
        ScrambleFlight scramble = null;
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

    private ScrambleFlight createScrambleForResidentSquadron(List<Squadron> fighterSquadrons) throws PWCGException
    {
        Squadron scrambleSquad = fighterSquadrons.get(0);

        Coordinate scrambleCoords = scrambleSquad.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(scrambleCoords.copy());

        FlightInformation opposingFlightInformation = FlightInformationFactory.buildAiFlightInformation(scrambleSquad, mission, FlightTypes.SCRAMBLE);
        ScrambleFlight scramble =  new ScrambleFlight(opposingFlightInformation, missionBeginUnit);
        scramble.createUnitMission();
        return scramble;
    }

}
