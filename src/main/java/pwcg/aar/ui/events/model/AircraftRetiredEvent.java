package pwcg.aar.ui.events.model;

import java.util.Date;

public class AircraftRetiredEvent  extends AAREvent
{
    private String aircraftType = "";

    public AircraftRetiredEvent(String aircraftType, Date date, boolean isNewsWorthy)
    {
        super(date, isNewsWorthy);
        this.aircraftType = aircraftType;
    }

    public String getAircraft()
    {
        return aircraftType;
    }
}
