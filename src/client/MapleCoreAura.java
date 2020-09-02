/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Itzik
 */
public class MapleCoreAura {

    private int id, str, dex, int_, luk, att, magic, total, expire = 0;

    public MapleCoreAura(int id, int expire) {
        this.id = id;
        this.expire = expire;
    }

    public int getId() {
        return id;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getStr() {
        return str;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public int getDex() {
        return dex;
    }

    public void setInt(int int_) {
        this.int_ = int_;
    }

    public int getInt() {
        return int_;
    }

    public void setLuk(int luk) {
        this.luk = luk;
    }

    public int getLuk() {
        return luk;
    }

    public void setAtt(int att) {
        this.att = att;
    }

    public int getAtt() {
        return att;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getMagic() {
        return magic;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getExpire() {
        return expire;
    }
}
