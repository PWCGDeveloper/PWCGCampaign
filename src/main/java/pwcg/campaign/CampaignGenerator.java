package pwcg.campaign;

import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.MapForAirfieldFinder;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class CampaignGenerator 
{
    private CampaignGeneratorModel generatorModel;
    private Campaign campaign;
    
    public CampaignGenerator(CampaignGeneratorModel generatorModel)
    {
        this.generatorModel = generatorModel;
    }

    public Campaign generate() throws PWCGException
    {
        generatorModel.validateCampaignInputs();
        createCampaignBasis();
        staffSquadrons();
        staffReplacements();
        
        return campaign;
    }

    private void createCampaignBasis() throws PWCGException
    {
        setMapForNewCampaign();
        createCampaign();
        setCampaignAces();
    }

    private void createCampaign() throws PWCGException
	{
		campaign = new Campaign();
		campaign.initializeCampaignDirectories();
        campaign.initializeCampaignConfigs();
        campaign.setDate(generatorModel.getCampaignDate());
        campaign.getCampaignData().setName(generatorModel.getCampaignName());
        campaign.getCampaignData().setCampaignMode(generatorModel.getCampaignMode());
	}

    private void staffSquadrons() throws PWCGException
    {
        List<Squadron> activeSquadronsOnCampaignStartDate = PWCGContext.getInstance().getSquadronManager().getActiveSquadrons(generatorModel.getCampaignDate());
        for (Squadron squadron : activeSquadronsOnCampaignStartDate)
        {
            CampaignSquadronGenerator squadronGenerator = new CampaignSquadronGenerator(campaign, squadron);
            squadronGenerator.createSquadron(generatorModel);
        }
    }

    private void staffReplacements() throws PWCGException
    {
        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllActiveArmedServices(campaign.getDate());
        for (ArmedService armedService : armedServices)
        {
            CampaignEquipmentGenerator equipmentGenerator = new CampaignEquipmentGenerator(campaign, armedService);
            equipmentGenerator.createReplacements();
        }
    }

    private void setMapForNewCampaign() throws PWCGException
    {
        Squadron squad = PWCGContext.getInstance().getSquadronManager().getSquadronByName(generatorModel.getSquadronName(), generatorModel.getCampaignDate());
        IAirfield airfield = squad.determineCurrentAirfieldAnyMap(generatorModel.getCampaignDate());
        List<FrontMapIdentifier> airfieldMaps = MapForAirfieldFinder.getMapForAirfield(airfield.getName());
        FrontMapIdentifier initialAirfieldMap = airfieldMaps.get(0);

        PWCGContext.getInstance().changeContext(initialAirfieldMap);
    }

	private void setCampaignAces() throws PWCGException
	{
		CampaignAces aces =  PWCGContext.getInstance().getAceManager().loadFromHistoricalAces(generatorModel.getCampaignDate());
        campaign.getPersonnelManager().setCampaignAces(aces);
	}

}
