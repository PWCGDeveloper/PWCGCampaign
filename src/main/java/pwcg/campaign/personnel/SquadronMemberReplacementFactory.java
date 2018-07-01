package pwcg.campaign.personnel;

import java.util.HashMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadmember.PilotNames;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;

public class SquadronMemberReplacementFactory
{
    private Campaign campaign;
    private ArmedService service;

    public SquadronMemberReplacementFactory (Campaign campaign, ArmedService service)
    {
        this.campaign = campaign;
        this.service = service;
    }

    public SquadronMember createAIReplacementPilot() throws PWCGException 
    {
        SquadronMember replacementPilot = new SquadronMember();
        
        HashMap<String, String> namesUsed = new HashMap<>();
        
        String rank = getLowestRankForService();
        
        String squaddieName = PilotNames.getInstance().getName(service.getCountry(), namesUsed);
        replacementPilot.setName(squaddieName);
        replacementPilot.setRank(rank);
        replacementPilot.setCountry(service.getCountry().getCountry());
        replacementPilot.setSquadronId(Squadron.REPLACEMENT);
        replacementPilot.setSerialNumber(campaign.getSerialNumber().getNextPilotSerialNumber());
        replacementPilot.setMissionFlown(0);        
        replacementPilot.setAiSkillLevel(AiSkillLevel.NOVICE);
        makePilotPicture(replacementPilot);
        
        return replacementPilot;
    }

    private String getLowestRankForService()
    {
        IRankHelper rankObj = RankFactory.createRankHelper();
        int lowestRankPos = rankObj.getLowestRankPosForService(service);
        String rank = rankObj.getRankByService(lowestRankPos, service);
        return rank;
    }

    private void makePilotPicture(SquadronMember newPilot) throws PWCGException
    {
        PilotPictureBuilder pilotPictureBuilder = new PilotPictureBuilder(service, new HashMap<Integer, SquadronMember>());
        String picPath = pilotPictureBuilder.assignPilotPicture();
        newPilot.setPicName(picPath);
    }

    public static SquadronMember createBlankSquadronMember()
    {
        SquadronMember squadronMember = new SquadronMember();
        return squadronMember;
    }

}
