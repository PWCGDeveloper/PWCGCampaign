package pwcg.campaign.target.locator.targettype;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TargetRadius;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;

public class TargetTypeAvailabilityInputGenerator
{
    private TargetTypeAvailabilityInputs targetTypeAvailabilityInputs = new TargetTypeAvailabilityInputs();

    public TargetTypeAvailabilityInputs createTargetAvailabilityInputs(FlightInformation flightInformation) throws PWCGException
    {
        ICountry enemyCountry = flightInformation.getSquadron().determineEnemyCountry(flightInformation.getCampaign(), flightInformation.getCampaign().getDate());

        TargetRadius targetRadius = new TargetRadius();
        targetRadius.calculateTargetRadius(flightInformation.getFlightType(), flightInformation.getMission().getMissionBorders().getAreaRadius());

        targetTypeAvailabilityInputs.setPreferredDistance(new Double(targetRadius.getInitialTargetRadius()).intValue());
        targetTypeAvailabilityInputs.setMaxDistance(new Double(targetRadius.getMaxTargetRadius()).intValue());
        targetTypeAvailabilityInputs.setDate(flightInformation.getCampaign().getDate());
        targetTypeAvailabilityInputs.setTargetGeneralLocation(flightInformation.getTargetSearchStartLocation());
        targetTypeAvailabilityInputs.setSide(enemyCountry.getSide());
        
        return targetTypeAvailabilityInputs;
    }
}
