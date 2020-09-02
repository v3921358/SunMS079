/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Itzik
 */
public class CashItem {

    private final int category, subcategory, parent, sn, itemid, flag, price, discountPrice, quantity, expire, gender, likes;
    private final String image;

    public CashItem(int category, int subcategory, int parent, String image, int sn, int itemid, int flag, int price, int discountPrice, int quantity, int expire, int gender, int likes) {
        this.category = category;
        this.subcategory = subcategory;
        this.parent = parent;
        this.image = image;
        this.sn = sn;
        this.itemid = itemid;
        this.flag = flag;
        this.price = price;
        this.discountPrice = discountPrice;
        this.quantity = quantity;
        this.expire = expire;
        this.gender = gender;
        this.likes = likes;
    }

    public int getCategory() {
        return category;
    }

    public int getSubCategory() {
        return subcategory;
    }

    public int getParent() {
        return parent;
    }

    public String getImage() {
        return image;
    }

    public int getSN() {
        return sn;
    }

    public int getItemId() {
        return itemid;
    }

    public int getFlag() {
        return flag;
    }

    public int getPrice() {
        return price;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getExpire() {
        return expire;
    }

    public int getGender() {
        return gender;
    }

    public int getLikes() {
        return likes;
    }

}
