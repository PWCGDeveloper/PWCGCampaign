package pwcg.mission.target;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.locator.targettype.StrategicTargetTypeGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.target.locator.StrategicTargetLocator;

public class TargetDefinitionBuilderStrategic implements ITargetDefinitionBuilder
{
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition = new TargetDefinition();

    public TargetDefinitionBuilderStrategic (FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public TargetDefinition buildTargetDefinition () throws PWCGException
    {
        Coordinate missionCenter = flightInformation.getMission().getMissionBorders().getCenter();

        ICountry targetCountry = flightInformation.getSquadron().determineEnemyCountry(flightInformation.getCampaign(), flightInformation.getCampaign().getDate());
        StrategicTargetTypeGenerator strategicTargetTypeGenerator = new StrategicTargetTypeGenerator(targetCountry.getSide(), flightInformation.getCampaign().getDate(), missionCenter);
        TacticalTarget targetType = strategicTargetTypeGenerator.createTargetType(flightInformation.getMission().getMissionBorders().getAreaRadius());

        TargetRadius targetRadius = new TargetRadius();
        targetRadius.calculateTargetRadius(flightInformation.getFlightType(), flightInformation.getMission().getMissionBorders().getAreaRadius());
        targetDefinition.setPreferredRadius(new Double(targetRadius.getInitialTargetRadius()).intValue());
        targetDefinition.setMaximumRadius(new Double(targetRadius.getMaxTargetRadius()).intValue());

        StrategicTargetLocator strategicTargetLocator = new StrategicTargetLocator(
                new Double(targetRadius.getInitialTargetRadius()).intValue(), 
                targetCountry.getSide(), 
                flightInformation.getCampaign().getDate(), 
                missionCenter);
        IFixedPosition place = strategicTargetLocator.getStrategicTargetLocation(targetType);

        targetDefinition.setTargetType(targetType);
        targetDefinition.setAttackingSquadron(flightInformation.getSquadron());
        targetDefinition.setTargetName(TargetDefinitionBuilderUtils.buildTargetName(targetCountry, targetType));

        targetDefinition.setAttackingCountry(flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()));
        targetDefinition.setTargetCountry(targetCountry);
        targetDefinition.setDate(flightInformation.getCampaign().getDate());
        targetDefinition.setPlayerTarget((Squadron.isPlayerSquadron(flightInformation.getCampaign(), flightInformation.getSquadron().getSquadronId())));

        targetDefinition.setTargetPosition(place.getPosition());
        targetDefinition.setTargetOrientation(new Orientation());

        return targetDefinition;
    }
}
