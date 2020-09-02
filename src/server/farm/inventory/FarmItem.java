/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.farm.inventory;

/**
 *
 * @author Itzik
 */
public class FarmItem {

    private final int id;
    private short quantity;

    public FarmItem(final int id, final short quantity) {
        super();
        this.id = id;
        this.quantity = quantity;
    }

    public short getQuantity() {
        return quantity;
    }

    public void setQuantity(final short quantity) {
        this.quantity = quantity;
    }

    public final int getItemId() {
        return id;
    }

    public byte getType() {
        return 2; //1 building 2 decoration 3 consumable item 4 house
    }
}
