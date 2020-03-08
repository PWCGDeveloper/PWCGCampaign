package pwcg.aar.ui.display.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.PromotionEvent;

public class AARPromotionPanelData
{
    private List<PromotionEvent> promotionEventsDuringElapsedTime = new ArrayList<>();

    public List<PromotionEvent> getPromotionEventsDuringElapsedTime()
    {
        return promotionEventsDuringElapsedTime;
    }

    public void setPromotionEventsDuringElapsedTime(List<PromotionEvent> promotionEventsDuringElapsedTime)
    {
        this.promotionEventsDuringElapsedTime = promotionEventsDuringElapsedTime;
    }

    public void merge(AARPromotionPanelData promotionPanelData)
    {
        promotionEventsDuringElapsedTime.addAll(promotionPanelData.getPromotionEventsDuringElapsedTime());        
    }
}
