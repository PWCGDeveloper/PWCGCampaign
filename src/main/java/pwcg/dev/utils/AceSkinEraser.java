package pwcg.dev.utils;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.HistoricalAce;

public class AceSkinEraser
{
    public static void main (String[] args)
    {
        try
        {
            AceSkinEraser aceSkinEraser = new AceSkinEraser();
            aceSkinEraser.eraseAceSkins(PWCGProduct.FC);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void eraseAceSkins(PWCGProduct product) throws Exception
    {
        PWCGContext.setProduct(product);
        List<HistoricalAce> aces =  PWCGContext.getInstance().getAceManager().getHistoricalAces();
        for (HistoricalAce ace : aces) 
        {
            ace.setSkins(new ArrayList<>());
            //HistoricalAceIOJson.writeJson(ace);
        }
    }
 }
