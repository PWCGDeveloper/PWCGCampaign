package pwcg.mission.target;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.target.locator.StrategicTargetLocator;

public class TargetDefinitionBuilderStrategic implements ITargetDefinitionBuilder
{
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition = new TargetDefinition();

    public TargetDefinitionBuilderStrategic (IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    @Override
    public TargetDefinition buildTargetDefinition () throws PWCGException
    {
        Coordinate missionCenter = flightInformation.getMission().getMissionBorders().getCenter();

        ICountry targetCountry = flightInformation.getSquadron().determineEnemyCountry(flightInformation.getCampaign(), flightInformation.getCampaign().getDate());
        StrategicTargetTypeGenerator strategicTargetTypeGenerator = new StrategicTargetTypeGenerator(targetCountry.getSide(), flightInformation.getCampaign().getDate(), missionCenter);
        TargetType targetType = strategicTargetTypeGenerator.createTargetType(flightInformation.getMission().getMissionBorders().getAreaRadius());

        TargetRadius targetRadius = new TargetRadius();
        targetRadius.calculateTargetRadius(flightInformation.getFlightType(), flightInformation.getMission().getMissionBorders().getAreaRadius());
        targetDefinition.setPreferredRadius(Double.valueOf(targetRadius.getInitialTargetRadius()).intValue());
        targetDefinition.setMaximumRadius(Double.valueOf(targetRadius.getMaxTargetRadius()).intValue());

        StrategicTargetLocator strategicTargetLocator = new StrategicTargetLocator(
                Double.valueOf(targetRadius.getInitialTargetRadius()).intValue(), 
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
    

    @Override
    public TargetDefinition buildScrambleOpposeTargetDefinition(FlightInformation scrambleFlightInformation, TargetType targetType) throws PWCGException
    {
        buildTargetDefinition();          
        Coordinate targetLocation = scrambleFlightInformation.getFlightHomePosition();
        targetDefinition.setTargetPosition(targetLocation);
        targetDefinition.setTargetOrientation(new Orientation());
        return targetDefinition;
    }
}
