/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.farm.inventory;

/**
 *
 * @author Itzik
 */
public class FarmBuilding extends FarmItem {

    private int id;
    private short quantity = 1;
    private int waru = 0;
    private int position = -1;

    private FarmBuilding(int id, short quantity, int position) {
        super(id, quantity);
        this.position = position;
    }

    @Override
    public short getQuantity() {
        return quantity;
    }

    public int getPosition() {
        return position;
    }

    public int getWaru() {
        return waru;
    }

    @Override
    public byte getType() {
        return 1; //1 building 2 decoration 3 consumable item 4 house
    }
}
