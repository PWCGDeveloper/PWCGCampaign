package pwcg.aar.ui.events.model;

import java.util.Date;

public class AircraftIntroducedEvent extends AAREvent
{
    private String aircraftType = "";

    public AircraftIntroducedEvent(String aircraftType, Date date, boolean isNewsWorthy)
    {
        super(date, isNewsWorthy);
        this.aircraftType = aircraftType;
    }

    public String getAircraft()
    {
        return aircraftType;
    }
}
