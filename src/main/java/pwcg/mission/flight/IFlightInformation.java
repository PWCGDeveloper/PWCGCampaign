package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.plane.PlaneMcu;

public interface IFlightInformation
{

    List<SquadronMember> getParticipatingPlayersForFlight();

    Mission getMission();

    Squadron getSquadron();

    FlightTypes getFlightType();

    boolean isPlayerFlight();

    boolean isEscortedByPlayerFlight();

    boolean isEscortForPlayerFlight();

    boolean isPlayerRelatedFlight();

    List<SquadronMember> getFlightParticipatingPlayers();

    boolean isVirtual();

    boolean isAirStart() throws PWCGException;

    boolean isParkedStart() throws PWCGException;

    IAirfield getDepartureAirfield() throws PWCGException;

    List<PlaneMcu> getPlanes();
    
    Campaign getCampaign();

    Coordinate getTargetSearchStartLocation();

    int getAltitude();

    void setAltitude(int altitude);

    void calculateAltitude() throws PWCGException;

    boolean isFighterMission();

    ICountry getCountry();

    Coordinate getFlightHomePosition() throws PWCGException;

    String getAirfieldName();

    IAirfield getAirfield();

    int getFlightId();

    int getFlightCruisingSpeed();

    void setCruisingSpeed(int cruisingSpeed);
        
    int getFormationType();
}