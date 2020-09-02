/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Maple
 */
import java.io.Serializable;

public class MapleKeyBinding implements Serializable {

    private static final long serialVersionUID = 91795419538569L;
    private final int type, action;

    public MapleKeyBinding(int type, int action) {
        super();
        this.type = type;
        this.action = action;
    }

    public int getType() {
        return type;
    }

    public int getAction() {
        return action;
    }
}
