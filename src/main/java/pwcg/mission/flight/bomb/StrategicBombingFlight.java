package pwcg.mission.flight.bomb;

import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory.IngressWaypointPattern;
import pwcg.mission.target.TargetDefinition;

public class StrategicBombingFlight extends BombingFlight
{          
    protected IngressWaypointPattern ingressWaypointPosition;
    
    public StrategicBombingFlight(FlightInformation flightInformation, TargetDefinition targetDefinition, double distanceToIngress)
    {
        super(flightInformation, targetDefinition);
        this.ingressWaypointPosition = IngressWaypointPattern.INGRESS_AT_TARGET;
        this.distanceToIngress = distanceToIngress;
    }
}
