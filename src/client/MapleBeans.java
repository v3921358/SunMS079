/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

/**
 *
 * @author wubin
 */
public class MapleBeans {
    private final int number;
    private final int type;
    private final int pos;

    public MapleBeans(int pos, int type, int number) {
        this.pos = pos;
        this.number = number;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public int getPos() {
        return pos;
    }
    
}
