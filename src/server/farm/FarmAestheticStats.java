/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.farm;

/**
 *
 * @author Itzik
 */
public class FarmAestheticStats {

    private MapleFarm farm;
    private int space;
    private int shopDiscountR;
    private int specialMerchantR;

    public FarmAestheticStats(MapleFarm farm) {
        this.farm = farm;
    }

    public void setFarm(MapleFarm farm) {
        this.farm = farm;
    }

    public void setSpaceAddition(int space) {
        this.space = space;
    }

    public void setShopDiscountR(int shopDiscountR) {
        this.shopDiscountR = shopDiscountR;
    }

    public void setSpecialMerchantR(int specialMerchantR) {
        this.specialMerchantR = specialMerchantR;
    }

    public MapleFarm getFarm() {
        return farm;
    }

    public int getSpaceAddition() {
        return space;
    }

    public int getShopDiscountR() {
        return shopDiscountR;
    }

    public int getSpecialMerchantR() {
        return specialMerchantR;
    }
}
