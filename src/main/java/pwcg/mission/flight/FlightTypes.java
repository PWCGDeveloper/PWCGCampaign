package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

public enum FlightTypes
{
    ANY,
    PATROL,
    OFFENSIVE,
    INTERCEPT,
    ESCORT,
    SCRAMBLE,
    SCRAMBLE_OPPOSE,
    HOME_DEFENSE,
    LONE_WOLF,
    BALLOON_BUST,
    BALLOON_DEFENSE,
    SEA_PATROL,
    LOW_ALT_CAP,
    LOW_ALT_PATROL,

    GROUND_ATTACK,
    BOMB,
    LOW_ALT_BOMB,
    DIVE_BOMB,
    ANTI_SHIPPING,
    STRATEGIC_BOMB,
    
    TRANSPORT,
    RECON,
    PORT_RECON,
    CONTACT_PATROL,
    ARTILLERY_SPOT,
    PARATROOP_DROP,
    CARGO_DROP,
    SPY_EXTRACT,
    
    FERRY,
    GROUND_FORCES;
    
    public static List<FlightTypes> getFighterFlightTypes()
    {
        List<FlightTypes> fighterFlightTypes = new ArrayList<>();
        fighterFlightTypes.add(PATROL);
        fighterFlightTypes.add(OFFENSIVE);
        fighterFlightTypes.add(INTERCEPT);
        fighterFlightTypes.add(LONE_WOLF);
        fighterFlightTypes.add(BALLOON_BUST);
        fighterFlightTypes.add(BALLOON_DEFENSE);
        fighterFlightTypes.add(LOW_ALT_CAP);
        fighterFlightTypes.add(LOW_ALT_PATROL);
        return fighterFlightTypes;
    }
}
