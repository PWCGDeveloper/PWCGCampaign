package pwcg.campaign;

import java.util.List;

import pwcg.campaign.context.MapFinderForCampaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.squadmember.SquadronMember;
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

    public Campaign generate(PWCGProduct product) throws PWCGException
    {
        generatorModel.validateCampaignInputs();
        createCampaignBasis(product);
        staffSquadrons();
        createPersonnelReplacements();
        createEquipmentReplacements();
        return campaign;
    }

    private void createCampaignBasis(PWCGProduct product) throws PWCGException
    {
        createCampaign(product);
        setCampaignAces();
    }

    private void createCampaign(PWCGProduct product) throws PWCGException
	{
		campaign = new Campaign(product);
        campaign.initializeCampaignConfigs();
        campaign.getCampaignData().setInitialMap(MapFinderForCampaign.findMapForSquadeonAndDate(generatorModel.getCampaignSquadron(), generatorModel.getCampaignDate()));
        campaign.setDate(generatorModel.getCampaignDate());
        campaign.getCampaignData().setName(generatorModel.getCampaignName());
        campaign.getCampaignData().setCampaignMode(generatorModel.getCampaignMode());
	}

    private void staffSquadrons() throws PWCGException
    {
        List<Squadron> activeSquadronsOnCampaignStartDate = PWCGContext.getInstance(campaign.getProduct()).getSquadronManager().getActiveSquadrons(generatorModel.getCampaignDate());
        for (Squadron squadron : activeSquadronsOnCampaignStartDate)
        {
            CampaignSquadronGenerator squadronGenerator = new CampaignSquadronGenerator(campaign, squadron);
            squadronGenerator.createSquadron(generatorModel);
        }
        
        useCampaignPlayerToSetReferencePlayer();
    }

    private void useCampaignPlayerToSetReferencePlayer() throws PWCGException
    {
        SquadronMember referencePlayer = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList().get(0);
        campaign.getCampaignData().setReferencePlayerSerialNumber(referencePlayer.getSerialNumber());
    }

    private void createPersonnelReplacements() throws PWCGException
    {
        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllActiveArmedServices(campaign.getDate());
        for (ArmedService armedService : armedServices)
        {
            CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
            personnelManager.createPersonnelReplacements(armedService);
        }
    }

    private void createEquipmentReplacements() throws PWCGException
    {
        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllActiveArmedServices(campaign.getDate());
        for (ArmedService armedService : armedServices)
        {
            CampaignEquipmentManager equipmentGenerator = campaign.getEquipmentManager();
            equipmentGenerator.createEquipmentDepot(armedService);
        }
    }

	private void setCampaignAces() throws PWCGException
	{
		CampaignAces aces =  PWCGContext.getInstance().getAceManager().loadFromHistoricalAces(generatorModel.getCampaignDate());
        campaign.getPersonnelManager().setCampaignAces(aces);
	}
}
