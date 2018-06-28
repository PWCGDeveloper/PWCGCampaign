package pwcg.campaign;

import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;


public class TransferHandler 
{
    private Campaign campaign = null;
    
    public TransferHandler (Campaign campaign)
    {
        this.campaign = campaign;
    }

	public TransferEvent transferPlayer(ArmedService newService, String newSquadName) throws PWCGException 
	{
	    int leaveTimeForTransfer = transferleaveTime();
	
        changeInRankForServiceTransfer(newService);

        Squadron oldSquad =  PWCGContextManager.getInstance().getCampaign().determineSquadron();
        Squadron newSquad = setNewSquadron(newService, newSquadName);
                
        TransferEvent transferEvent = createTransferEvent(leaveTimeForTransfer, oldSquad, newSquad);
		
        return transferEvent;
	}

	public void transferAI(SquadronMember squadronMember) throws PWCGException 
    {
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        int newSquadronId = squadronManager.chooseSquadronForTransfer(campaign, squadronMember);
        squadronMember.setSquadronId(newSquadronId);
    }

	private TransferEvent createTransferEvent(int leaveTimeForTransfer, Squadron oldSquad, Squadron newSquad) throws PWCGException
	{
		TransferEvent transferEvent = new TransferEvent();
        transferEvent.setPilot(campaign.getPlayer());
        transferEvent.setTransferIn(true);
        transferEvent.setDate(campaign.getDate());
        transferEvent.setSquadron(newSquad.determineDisplayName(campaign.getDate()));
        transferEvent.setTransferTo(newSquad.getSquadronId());
        transferEvent.setTransferFrom(oldSquad.getSquadronId());
        transferEvent.setLeaveTime(leaveTimeForTransfer);
		return transferEvent;
	}

	private Squadron setNewSquadron(ArmedService newService, String newSquadName)
	        throws PWCGException
	{
		SquadronMember player = campaign.getPlayer();
		Squadron newSquad =  PWCGContextManager.getInstance().getSquadronManager().getSquadronByNameAndCountry(newSquadName, newService.getCountry(), campaign.getDate());
        
		campaign.setSquadId(newSquad.getSquadronId());
		player.setSquadronId(newSquad.getSquadronId());
		
		return newSquad;
	}

	private int transferleaveTime() throws PWCGException 
	{
        int leaveTimeForTransfer = 5 + RandomNumberGenerator.getRandom(10);
        return leaveTimeForTransfer;
	}

    private void changeInRankForServiceTransfer(ArmedService newService) throws PWCGException 
    {
        SquadronMember player = campaign.getPlayer();
        IRankHelper iRank = RankFactory.createRankHelper();        
        int rankPos = iRank.getRankPosByService(player.getRank(), campaign.determineSquadron().determineServiceForSquadron(campaign.getDate()));
        
        int lowestRankPos = iRank.getLowestRankPosForService(newService);
        if (rankPos > lowestRankPos)
        {
            rankPos = lowestRankPos;
        }
        
        String newRank = iRank.getRankByService(rankPos, newService);
        player.setRank(newRank);
    }
}
