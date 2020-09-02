/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package handling.channel.handler;

import client.MapleBeans;
import client.MapleCharacter;
import client.MapleClient;
import java.util.ArrayList;
import java.util.List;
import server.Randomizer;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;

/**
 *
 * @author wubin
 */
public class BeanGame {
    
    public static int frequency  = 0;
    
    public static int a = 0;
    public static int b = 0;
    public static int d = 0;
    public static int s = 0;
    public static int as = 0;
    public static int bs = 0;
    public static int ds= 0;
    
    public static final void BeanGame(LittleEndianAccessor slea, MapleClient c) {
        //System.out.println("豆豆出现包" +slea.toString());
        MapleCharacter chr = c.getPlayer();
        List<MapleBeans> beansInfo = new ArrayList<>();

        int type = slea.readByte();
        int intensity = 0;//力度
        int number = 0;//序号
        if(type == 0){
            intensity = slea.readShort();
            slea.readInt();
            chr.setBeansRange(intensity);
          //  slea.readInt();
            c.getSession().write(CWvsContext.enableActions());
        } else if (type == 1) { //点开始的时候 确认打豆豆的力度
            //01 E8 03
            intensity = slea.readShort();
            slea.readInt();
            chr.setBeansRange(intensity);
            //System.out.println("打豆豆力度1："+力度);
            c.getSession().write(CWvsContext.enableActions());
            /*} else if (type == 0) {  //没存在的必要
             力度 = slea.readShort();
             豆豆序號 = slea.readInt() + 1;
             chr.setBeansRange(力度);
             chr.setBeansNum(豆豆序號);
             if (豆豆序號 == 1) {
             chr.setCanSetBeansNum(false);
             }*/
        } else if (type == 2) { //没存在的必要
            //02 1B 00 00 00
            slea.readInt();
            /*
             豆豆序号 = slea.readInt(); //点暂停时显示的下个豆豆的序号 同时也与要扣除的豆数的值相同
             chr.gainBeans(豆豆序号 - chr.getBeansNum());
             chr.setBeansNum(豆豆序号);
             System.out.println("扣除的豆豆数量："+豆豆序号);
             System.out.println("type == 2 "+slea.toString());
             */

            /*if ((type == 11) || (type == 0)) {
             力度 = slea.readShort();
             豆豆序號 = slea.readInt() + 1;
             chr.setBeansRange(力度);
             chr.setBeansNum(豆豆序號);
             if (豆豆序號 == 1) {
             chr.setCanSetBeansNum(false);
             }
             }*/
            /*} else if (type == 6) {
             slea.skip(1);
             int 循環次數 = slea.readByte();
             if (循環次數 == 0) {
             return;
             }
             if (循環次數 != 1) {
             slea.skip((循環次數 - 1) * 8);
             }
             if (chr.isCanSetBeansNum()) {
             chr.setBeansNum(chr.getBeansNum() + 循環次數);
             }
             chr.gainBeans(-循環次數);
             chr.setCanSetBeansNum(true);*/
            
        } else if (type == 3) { //打豆豆进洞以后的数据
           /* slea.readInt();
            slea.readInt();
            slea.readInt();
            chr.gainddj(1);
            String note ="恭喜你打豆豆成功中奖！当前中奖次数："+chr.getddj();
            String notea ="恭喜你打豆豆成功中奖！当前中奖次数："+chr.getddj();
            c.getSession().write(UIPacket.getTopMsg(note));
            c.getSession().write(MaplePacketCreator.BeansGameMessage(0x01,0x01,notea));
            chr.dropMessage(6, "恭喜你打豆豆成功中奖！当前中奖次数："+chr.getddj()+"\r\n");*/
            /*E2 00 
             03 
             1D 01 00 00 //屏幕中间滚动的怪物图片ID数据？？？
             26 11 00 00 //屏幕中间滚动的怪物图片ID数据？？？
             1F 01 00 00 //屏幕中间滚动的怪物图片ID数据？？？  但是是发给什么包处理的呢？
            */
            seta(slea.readInt());
            setb(slea.readInt());
            setd(slea.readInt());
            boolean aa = getb() - geta() == 3;
            int sss = Randomizer.nextInt(9);
            if (aa) {
                setas(sss);
                setbs(sss);
            } else {
                if (geta() == 9) {
                    setas(sss);
                    setbs(sss - 1);
                } else {
                    setas(sss);
                    setbs(sss);
                }
            }
            setds(Randomizer.nextInt(9));
            gainFrequency(1);
            if (getFrequency() > 7) {
                setFrequency(7);
            }
           // c.getSession().write(MaplePacketCreator.BeansJDCS(get进洞次数()));
           // try {
              // Thread.sleep(20000L);//2ms
            //c.getSession().write(CField.BeansJDCS(getFrequency()));
           /* } catch (InterruptedException ex) {//捕获该线程异常
                System.out.println("线程锁开启失败");
            }*/
        }else if(type == 4){//记录进洞次数的黄色豆豆。最多只有7个。
            //for (int i = 0; i < get进洞次数(); i++) { 
            gainFrequency(-1);
            if (getas() == getbs()){
            //sets(Randomizer.nextInt(100) + 20);
            //c.getSession().write(CField.BeansJDXZ(getFrequency(), geta(), getb(), getd(), getas(), getbs(), getds()));
            } else {
            //c.getSession().write(CField.BeansJDXZ(getFrequency(), 0, 0, 0, getas(), getbs(), getds()));
            }
           // }
            
        }else if(type == 5){//移动以后三排一样的 这里应该是处理 打完以后出现的 看看是不是判断一排的代码
            if(getas() == getbs() && getas() == getds()){
            chr.gainBeans(gets());
            chr.gainExp(gets(), true, false, true);
           // c.getSession().write(MaplePacketCreator.BeansZJgeidd(0x05, gets()));
            String notea ="恭喜你打豆豆成功中奖！当前中奖获得豆豆："+gets()+"个！";
            //c.getSession().write(CField.BeansGameMessage(0x01,0x01,notea));
            }
        } else if ((type == 0x0B)) {
            //0B[11] - 点start/stop的时候获得start/stop时豆豆的力度和序号
            //0 - 刚打开界面的时候设置的力度
            //0B E8 03 00 00 00 00
            //00 88 13 1B 00 00 00
            /*力度 = slea.readShort();
             chr.setBeansRange(力度);
             byte size = (byte) (slea.readByte()+1);
             short Pos = slea.readShort();
             byte Type = (byte) (slea.readByte() + 1);
             */

            intensity = slea.readShort();
            number = slea.readInt() + 1;//这里获得的Int是最后一个豆豆的序号
            chr.setBeansRange(intensity);
            chr.setBeansNum(number);
            if (number == 1) {
                chr.setCanSetBeansNum(false);
            }
            /*
             System.out.println("混合类型："+type);
             System.out.println("豆豆序号1："+豆豆序号);
             System.out.println("打豆豆力度2："+力度);
             */
           // for (int i = 0; i < 5; i++) {
            // beansInfo.add(new Beans(chr.getBeansRange() + rand(-100, 100), getBeanType(), chr.getBeansNum() + i));
            // }
        } else if (type == 6) { //点暂停或者满5个豆豆后客户端发送的豆豆信息 最多5个豆豆
            /*
             * 06 00 
             * 01 
             * 1B 00 00 00 E8 03 00 4D
             * 
             * 06 01 
             * 05 
             * 01 00 00 00 E8 03 02 52 
             * 02 00 00 00 E8 03 02 52 
             * 03 00 00 00 E8 03 02 52 
             * 04 00 00 00 E8 03 02 52 
             * 05 00 00 00 E8 03 02 52
             */

            slea.skip(1);
            int 循环次数 = slea.readByte();
            if (循环次数 == 0) {
                return;
            } else if (循环次数 != 1) {
                slea.skip((循环次数 - 1) * 8);
            }
            //int 临时豆豆序号 = slea.readInt();
            //豆豆序号 = (临时豆豆序号 == 1 ? 0 : 临时豆豆序号) + (chr.getBeansNum()  == 临时豆豆序号 ? 1 : 0);
            if (chr.isCanSetBeansNum()) {
                chr.setBeansNum(chr.getBeansNum() + 循环次数);
                //System.out.println("豆豆序号2："+chr.getBeansNum());
            }
            chr.gainBeans(-循环次数);
            chr.setCanSetBeansNum(true);
            //System.out.println("扣除的豆豆数量："+循环次数);
            //System.out.println("type == 6 "+slea.toString());
        } else {
            System.out.println("未处理的类型【" + type + "】\n包" + slea.toString());
        }
        if (type == 0x0B || type == 6) {
            for (int i = 0; i < 5; i++) {
               // beansInfo.add(new Beans(chr.getBeansRange(), getBeanType(), chr.getBeansNum() + i));
                beansInfo.add(new MapleBeans(chr.getBeansRange() + rand(-100, 100), getBeanType(), chr.getBeansNum() + i));
            }
            //c.getSession().write(CField.showBeans(beansInfo));
        }
    }

    private static int getBeanType() {
        int random = rand(1, 100);
        int beanType = 0;
        //3 - �?, 2 - ???, 1 - �?, 0 - ?��?�?
        switch (random) {
            case 2:
                beanType = 1;
                break;
            case 49:
                beanType = 2;
                break;
            case 99:
                beanType = 3;
                break;
        }
        return beanType;
    }

    public static final int getFrequency(){
        return frequency;
    }
    
    public static final void gainFrequency(int a){
        frequency  += a;
    }
    public static final void setFrequency(int a){
        frequency  = a;
    }
    public static final int geta(){
        return a;
    }
    public static final void seta(int s){
        a = s;
    }
    public static final int getb(){
        return b;
    }
    public static final void setb(int a){
        b = a;
    }
    public static final int getd(){
        return d;
    }
    public static final void setd(int a){
        d = a;
    }
    public static final int gets(){
        return s;
    }
    public static final void sets(int a){
        s = a;
    }
    public static final int getas(){
        return as;
    }
    public static final void setas(int s){
        as = s;
    }
    public static final int getbs(){
        return bs;
    }
    public static final void setbs(int a){
        bs = a;
    }
    public static final int getds(){
        return ds;
    }
    public static final void setds(int a){
        ds = a;
    }
    
    private static int rand(int lbound, int ubound) {
        return (int) ((Math.random() * (ubound - lbound + 1)) + lbound);
    }

    public static final void updateBeans(LittleEndianAccessor slea, MapleClient c) {
        //c.getSession().write(CField.updateBeans(c.getPlayer().getId(), c.getPlayer().getBeans()));
        c.getSession().write(CWvsContext.enableActions());
    }
}

