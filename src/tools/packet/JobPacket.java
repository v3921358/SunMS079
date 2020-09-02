/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.packet;

import client.MapleBuffStat;
import client.MapleCharacter;
import handling.SendPacketOpcode;
import java.awt.Point;
import java.util.EnumMap;
import java.util.Map;
import server.MapleStatInfo;
import server.Randomizer;
import tools.data.MaplePacketLittleEndianWriter;

/**
 *
 * @author Itzik
 */
public class JobPacket {

    public static class PhantomPacket {

        public static byte[] addStolenSkill(int jobNum, int index, int skill, int level) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.UPDATE_STOLEN_SKILLS.getValue());
            mplew.write(1);
            mplew.write(0);
            mplew.writeInt(jobNum);
            mplew.writeInt(index);
            mplew.writeInt(skill);
            mplew.writeInt(level);
            mplew.writeInt(0);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] removeStolenSkill(int jobNum, int index) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.UPDATE_STOLEN_SKILLS.getValue());
            mplew.write(1);
            mplew.write(3);
            mplew.writeInt(jobNum);
            mplew.writeInt(index);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] replaceStolenSkill(int base, int skill) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.REPLACE_SKILLS.getValue());
            mplew.write(1);
            mplew.write(skill > 0 ? 1 : 0);
            mplew.writeInt(base);
            mplew.writeInt(skill);

            return mplew.getPacket();
        }

        public static byte[] gainCardStack(int oid, int runningId, int color, int skillid, int damage, int times) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
            mplew.write(0);
            mplew.writeInt(oid);
            mplew.writeInt(1);
            mplew.writeInt(damage);
            mplew.writeInt(skillid);
            for (int i = 0; i < times; i++) {
                mplew.write(1);
                mplew.writeInt(damage == 0 ? runningId + i : runningId);
                mplew.writeInt(color);
                mplew.writeInt(Randomizer.rand(15, 29));
                mplew.writeInt(Randomizer.rand(7, 11));
                mplew.writeInt(Randomizer.rand(0, 9));
            }
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] updateCardStack(final int total) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PHANTOM_CARD.getValue());
            mplew.write(total);

            return mplew.getPacket();
        }

        public static byte[] getCarteAnimation(int cid, int oid, int job, int total, int numDisplay) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
            mplew.write(0);
            mplew.writeInt(cid);
            mplew.writeInt(1);

            mplew.writeInt(oid);
            mplew.writeInt(job == 2412 ? 24120002 : 24100003);
            mplew.write(1);
            for (int i = 1; i <= numDisplay; i++) {
                mplew.writeInt(total - (numDisplay - i));
                mplew.writeInt(job == 2412 ? 2 : 0);

                mplew.writeInt(15 + Randomizer.nextInt(15));
                mplew.writeInt(7 + Randomizer.nextInt(5));
                mplew.writeInt(Randomizer.nextInt(4));

                mplew.write(i == numDisplay ? 0 : 1);
            }

            return mplew.getPacket();
        }

        public static byte[] giveAriaBuff(int bufflevel, int buffid, int bufflength) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);
            statups.put(MapleBuffStat.DAMAGE_RATE, 0);
            statups.put(MapleBuffStat.DAMAGE_PERCENT, 0);
            PacketHelper.writeBuffMask(mplew, statups);
            for (int i = 0; i < 2; i++) {
                mplew.writeShort(bufflevel);
                mplew.writeInt(buffid);
                mplew.writeInt(bufflength);
            }
            mplew.writeZeroBytes(3);
            mplew.writeShort(0);
            mplew.write(0);
            return mplew.getPacket();
        }
    }

    public static class AngelicPacket {

        public static byte[] showRechargeEffect() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SHOW_SPECIAL_EFFECT.getValue());
            mplew.write(0x2D);
            return mplew.getPacket();
        }

        public static byte[] RechargeEffect() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SHOW_SPECIAL_EFFECT.getValue());
            mplew.write(0x2D);
            return mplew.getPacket();
        }

        public static byte[] DressUpTime(byte type) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(type);
            mplew.writeShort(7707);
            mplew.write(2);
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
            return mplew.getPacket();
        }

        public static byte[] updateDress(int transform, MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.ANGELIC_CHANGE.getValue());
            mplew.writeInt(chr.getId());
            mplew.writeInt(transform);
            return mplew.getPacket();
        }

        public static byte[] lockSkill(int skillid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.LOCK_CHARGE_SKILL.getValue());
            mplew.writeInt(skillid);
            return mplew.getPacket();
        }

        public static byte[] unlockSkill() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.UNLOCK_CHARGE_SKILL.getValue());
            mplew.writeInt(0);
            return mplew.getPacket();
        }

        public static byte[] absorbingSoulSeeker(int characterid, int size, Point essence1, Point essence2, int skillid, boolean creation) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
            mplew.write(!creation ? 0 : 1);
            mplew.writeInt(characterid);
            if (!creation) {
                // false
                mplew.writeInt(3);
                mplew.write(1);
                mplew.write(size);
                mplew.writeZeroBytes(3);
                mplew.writeShort(essence1.x);
                mplew.writeShort(essence1.y);
                mplew.writeShort(essence2.y);
                mplew.writeShort(essence2.x);
            } else {
                // true
                mplew.writeShort(essence1.x);
                mplew.writeShort(essence1.y);
                mplew.writeInt(4);
                mplew.write(1);
                mplew.writeShort(essence1.y);
                mplew.writeShort(essence1.x);
            }
            mplew.writeInt(skillid);
            if (!creation) {
                for (int i = 0; i < 2; i++) {
                    mplew.write(1);
                    mplew.writeInt(Randomizer.rand(19, 20));
                    mplew.writeInt(1);
                    mplew.writeInt(Randomizer.rand(18, 19));
                    mplew.writeInt(Randomizer.rand(20, 23));
                    mplew.writeInt(Randomizer.rand(36, 55));
                    mplew.writeInt(540);
                    mplew.writeShort(0);//new 142
                    mplew.writeZeroBytes(6);//new 143
                }
            } else {
                mplew.write(1);
                mplew.writeInt(Randomizer.rand(6, 21));
                mplew.writeInt(1);
                mplew.writeInt(Randomizer.rand(42, 45));
                mplew.writeInt(Randomizer.rand(4, 7));
                mplew.writeInt(Randomizer.rand(267, 100));
                mplew.writeInt(0);//540
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] SoulSeekerRegen(MapleCharacter chr, int sn) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
            mplew.write(1);
            mplew.writeInt(chr.getId());
            mplew.writeInt(sn);
            mplew.writeInt(4);
            mplew.write(1);
            mplew.writeInt(sn);
            mplew.writeInt(65111007); // hide skills
            mplew.write(1);
            mplew.writeInt(Randomizer.rand(0x06, 0x10));
            mplew.writeInt(1);
            mplew.writeInt(Randomizer.rand(0x28, 0x2B));
            mplew.writeInt(Randomizer.rand(0x03, 0x04));
            mplew.writeInt(Randomizer.rand(0xFA, 0x49));
            mplew.writeInt(0);
            mplew.writeLong(0);
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] SoulSeeker(MapleCharacter chr, int skillid, int sn, int sc1, int sc2) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
            mplew.write(0);
            mplew.writeInt(chr.getId());
            mplew.writeInt(3);
            mplew.write(1);
            mplew.writeInt(sn);
            if (sn >= 1) {
                mplew.writeInt(sc1);//SHOW_ITEM_GAIN_INCHAT
                if (sn == 2) {
                    mplew.writeInt(sc2);
                }
            }
            mplew.writeInt(65111007); // hide skills
            for (int i = 0; i < 2; i++) {
                mplew.write(1);
                mplew.writeInt(i + 2);
                mplew.writeInt(1);
                mplew.writeInt(Randomizer.rand(0x0F, 0x10));
                mplew.writeInt(Randomizer.rand(0x1B, 0x22));
                mplew.writeInt(Randomizer.rand(0x1F, 0x24));
                mplew.writeInt(540);
                mplew.writeInt(0);//wasshort new143
                mplew.writeInt(0);//new143
            }
            mplew.write(0);
            return mplew.getPacket();
        }
    }

    public static class LuminousPacket {

        public static byte[] updateLuminousGauge(int darktotal, int lighttotal, int darktype, int lighttype) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.LUMINOUS_COMBO.getValue());
            mplew.writeInt(darktotal);
            mplew.writeInt(lighttotal);
            mplew.writeInt(darktype);
            mplew.writeInt(lighttype);
            mplew.writeInt(281874974);//1210382225

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }

        public static byte[] giveLuminousState(int skill, int light, int dark, int duration) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());

            PacketHelper.writeSingleMask(mplew, MapleBuffStat.LUMINOUS_GAUGE);

            mplew.writeShort(1);
            mplew.writeInt(skill); //20040217
            mplew.writeInt(200000000);
            mplew.writeZeroBytes(5);
            mplew.writeInt(skill); //20040217
            mplew.writeInt(483195070);
            mplew.writeZeroBytes(8);
            mplew.writeInt(Math.max(light, -1)); //light gauge
            mplew.writeInt(Math.max(dark, -1)); //dark gauge
            mplew.writeInt(1);
            mplew.writeInt(1);//was2
            mplew.writeInt(283183599);
            mplew.writeInt(0);
            mplew.writeInt(0);//new143
            mplew.writeInt(1);
            mplew.write(0);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }
    }

    public static class XenonPacket {

        public static byte[] giveXenonSupply(short amount) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.SUPPLY_SURPLUS);

            mplew.writeShort(amount);
            mplew.writeInt(30020232); //skill id
            mplew.writeInt(-1); //duration
            mplew.writeZeroBytes(18);

            return mplew.getPacket();
        }

        public static byte[] giveAmaranthGenerator() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);
            statups.put(MapleBuffStat.SUPPLY_SURPLUS, 0);
            statups.put(MapleBuffStat.AMARANTH_GENERATOR, 0);
            PacketHelper.writeBuffMask(mplew, statups);

            mplew.writeShort(20); //gauge fill
            mplew.writeInt(30020232); //skill id
            mplew.writeInt(-1); //duration

            mplew.writeShort(1);
            mplew.writeInt(36121054); //skill id
            mplew.writeInt(10000); //duration

            mplew.writeZeroBytes(5);
            mplew.writeInt(1000);
            mplew.writeInt(1);
            mplew.writeZeroBytes(1);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }
    }

    public static class AvengerPacket {

        public static byte[] giveAvengerHpBuff(int hp) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());

            PacketHelper.writeSingleMask(mplew, MapleBuffStat.LUNAR_TIDE);
            mplew.writeShort(3);
            mplew.writeInt(0);
            mplew.writeInt(2100000000);
            mplew.writeZeroBytes(5);
            mplew.writeInt(hp);
            mplew.writeZeroBytes(9);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }

        public static byte[] giveExceed(short amount) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.EXCEED);

            mplew.writeShort(amount);
            mplew.writeInt(30010230); //skill id
            mplew.writeInt(-1); //duration
            mplew.writeZeroBytes(14);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }

        public static byte[] giveExceedAttack(int skill, short amount) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.EXCEED_ATTACK);

            mplew.writeShort(amount);
            mplew.writeInt(skill); //skill id
            mplew.writeInt(15000); //duration
            mplew.writeZeroBytes(14);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }

        public static byte[] cancelExceed() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_BUFF.getValue());

            Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);
            statups.put(MapleBuffStat.EXCEED, 0);
            statups.put(MapleBuffStat.EXCEED_ATTACK, 0);
            PacketHelper.writeBuffMask(mplew, statups);

            return mplew.getPacket();
        }
    }

    public static class DawnWarriorPacket {

        public static byte[] giveMoonfallStance(int level) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);
            statups.put(MapleBuffStat.CRITICAL_PERCENT_UP, 0);
            statups.put(MapleBuffStat.MOON_STANCE2, 0);
            statups.put(MapleBuffStat.WARRIOR_STANCE, 0);
            PacketHelper.writeBuffMask(mplew, statups);

            mplew.writeShort(level);
            mplew.writeInt(11101022);
            mplew.writeInt(Integer.MAX_VALUE);
            mplew.writeShort(1);
            mplew.writeInt(11101022);
            mplew.writeInt(Integer.MAX_VALUE);
            mplew.writeInt(0);
            mplew.write(5);
            mplew.write(1);
            mplew.writeInt(1);
            mplew.writeInt(11101022);
            mplew.writeInt(level);
            mplew.writeInt(Integer.MAX_VALUE);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.write(1);
            mplew.writeInt(0);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }

        public static byte[] giveSunriseStance(int level) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);
            statups.put(MapleBuffStat.ATTACK_SPEED, 0);
            statups.put(MapleBuffStat.DAMAGE_PERCENT, 0);
            statups.put(MapleBuffStat.WARRIOR_STANCE, 0);
            PacketHelper.writeBuffMask(mplew, statups);

            mplew.writeShort(level);
            mplew.writeInt(11111022);
            mplew.writeInt(Integer.MAX_VALUE);
            mplew.writeInt(0);
            mplew.write(5);
            mplew.write(1);
            mplew.writeInt(1);
            mplew.writeInt(11111022);
            mplew.writeInt(-1);
            mplew.writeInt(Integer.MAX_VALUE);
            mplew.writeInt(0);
            mplew.writeInt(1);
            mplew.writeInt(11111022);
            mplew.writeInt(0x19);
            mplew.writeInt(Integer.MAX_VALUE);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.write(1);
            mplew.writeInt(0);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }

        public static byte[] giveEquinox_Moon(int level, int duration) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);
            statups.put(MapleBuffStat.CRITICAL_PERCENT_UP, 0);
            statups.put(MapleBuffStat.MOON_STANCE2, 0);
            statups.put(MapleBuffStat.EQUINOX_STANCE, 0);
            PacketHelper.writeBuffMask(mplew, statups);

            mplew.writeShort(level);
            mplew.writeInt(11121005);
            mplew.writeInt(duration);
            mplew.writeShort(1);
            mplew.writeInt(11121005);
            mplew.writeInt(duration);
            mplew.writeInt(0);
            mplew.write(5);
            mplew.writeInt(1);
            mplew.writeInt(11121005);
            mplew.writeInt(level);
            mplew.writeInt(duration);
            mplew.writeInt(duration);
            mplew.writeInt(0);
            mplew.write(1);
            mplew.writeInt(0);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }

        public static byte[] giveEquinox_Sun(int level, int duration) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);
            statups.put(MapleBuffStat.ATTACK_SPEED, 0);
            statups.put(MapleBuffStat.DAMAGE_PERCENT, 0);
            statups.put(MapleBuffStat.EQUINOX_STANCE, 0);
            PacketHelper.writeBuffMask(mplew, statups);

            mplew.writeShort(level);
            mplew.writeInt(11121005);
            mplew.writeInt(duration);
            mplew.writeInt(0);
            mplew.write(5);
            mplew.writeInt(1);
            mplew.writeInt(11121005);
            mplew.writeInt(-1);
            mplew.writeInt(duration);
            mplew.writeInt(duration);
            mplew.writeInt(1);
            mplew.writeInt(11121005);
            mplew.writeInt(0x19);
            mplew.writeInt(duration);
            mplew.writeInt(duration);
            mplew.writeInt(0);
            mplew.write(1);
            mplew.writeInt(0);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }
    }

    public static class BeastTamerPacket {

        public static byte[] ModeCancel() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_BUFF.getValue());

            Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);
            statups.put(MapleBuffStat.ANIMAL_SELECT, 0);
            PacketHelper.writeBuffMask(mplew, statups);

            return mplew.getPacket();
        }

        public static byte[] BearMode() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());

            Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);
            statups.put(MapleBuffStat.ANIMAL_SELECT, 0);
            PacketHelper.writeBuffMask(mplew, statups);
            mplew.writeShort(1);
            mplew.writeInt(110001501);
            mplew.writeInt(-419268850);
            mplew.writeLong(0);
            mplew.writeInt(0);
            mplew.write(0);
            mplew.write(1);
            mplew.writeInt(0);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }

        public static byte[] LeopardMode() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());

            Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);
            statups.put(MapleBuffStat.ANIMAL_SELECT, 0);
            PacketHelper.writeBuffMask(mplew, statups);
            mplew.writeShort(2);
            mplew.writeInt(110001502);
            mplew.writeInt(-419263978);
            mplew.writeLong(0);
            mplew.writeInt(0);
            mplew.write(0);
            mplew.write(1);
            mplew.writeInt(0);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }

        public static byte[] HawkMode() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());

            Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);
            statups.put(MapleBuffStat.ANIMAL_SELECT, 0);
            PacketHelper.writeBuffMask(mplew, statups);
            mplew.writeShort(3);
            mplew.writeInt(110001503);
            mplew.writeInt(-419263978);
            mplew.writeLong(0);
            mplew.writeInt(0);
            mplew.write(0);
            mplew.write(1);
            mplew.writeInt(0);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }

        public static byte[] CatMode() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());

            Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);
            statups.put(MapleBuffStat.ANIMAL_SELECT, 0);
            PacketHelper.writeBuffMask(mplew, statups);
            mplew.writeShort(4);
            mplew.writeInt(110001504);
            mplew.writeInt(-419263978);
            mplew.writeLong(0);
            mplew.writeInt(0);
            mplew.write(0);
            mplew.write(1);
            mplew.writeInt(0);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }

        public static byte[] LeopardRoar() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());

            Map<MapleBuffStat, Integer> statups = new EnumMap<>(MapleBuffStat.class);
            statups.put(MapleBuffStat.DAMAGE_CAP_INCREASE, statups.get(MapleStatInfo.indieDamR));
            statups.put(MapleBuffStat.DAMAGE_PERCENT, statups.get(MapleStatInfo.indieMaxDamageOver));
            PacketHelper.writeBuffMask(mplew, statups);
            mplew.writeShort(4);
            mplew.writeInt(110001504);
            mplew.writeInt(-419263978);
            mplew.writeLong(0);
            mplew.writeInt(0);
            mplew.write(0);
            mplew.write(1);
            mplew.writeInt(0);

            mplew.writeZeroBytes(69); //for no dc

            return mplew.getPacket();
        }
    }
}
