package tools.packet;

import client.*;
import client.MapleStat.Temp;
import client.inventory.*;
import constants.GameConstants;
import handling.SendPacketOpcode;
import handling.channel.DojoRankingsData;
import handling.channel.MapleGeneralRanking.CandyRankingInfo;
import handling.channel.MapleGuildRanking;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.World;
import handling.world.exped.MapleExpedition;
import handling.world.exped.PartySearch;
import handling.world.exped.PartySearchType;
import handling.world.family.MapleFamily;
import handling.world.family.MapleFamilyBuff;
import handling.world.family.MapleFamilyCharacter;
import handling.world.guild.*;
import java.awt.Point;
import java.util.*;
import java.util.Map.Entry;
import server.MapleItemInformationProvider;
import server.MapleStatEffect;
import server.StructFamiliar;
import server.life.PlayerNPC;
import server.stores.HiredMerchant;
import server.stores.MaplePlayerShopItem;
import tools.HexTool;
import tools.KoreanDateUtil;
import tools.Pair;
import tools.StringUtil;
import tools.data.MaplePacketLittleEndianWriter;

public class CWvsContext {

    public static byte[] enableActions() {
        return updatePlayerStats(new EnumMap<MapleStat, Integer>(MapleStat.class), true, null);
    }

    public static byte[] updatePlayerStats(Map<MapleStat, Integer> stats, MapleCharacter chr) {
        return updatePlayerStats(stats, false, chr);
    }

    public static byte[] updatePlayerStats(Map<MapleStat, Integer> mystats, boolean itemReaction, MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_STATS.getValue());
        mplew.write(itemReaction ? 1 : 0);
        int updateMask = 0;
        for (MapleStat statupdate : mystats.keySet()) {
            updateMask |= statupdate.getValue();
        }
        mplew.writeInt(updateMask);
        for (final Entry<MapleStat, Integer> statupdate : mystats.entrySet()) {
            switch (statupdate.getKey()) {
                case SKIN:
                case LEVEL:
                    mplew.write((statupdate.getValue()).byteValue());
                    break;
                case JOB:
                case STR:
                case DEX:
                case INT:
                case LUK:
                case HP:
                case MAXHP:
                case MP:
                case MAXMP:
                case AVAILABLEAP:
                case FAME:
                    mplew.writeShort((statupdate.getValue()).shortValue());
                    break;
                case AVAILABLESP:
                    mplew.writeShort(chr.getRemainingSp(0));
                    break;
                case EXP:
                case FACE:
                case HAIR:
                case MESO:
                    mplew.writeInt((statupdate.getValue()));
                    break;
                case PET:
                    mplew.writeLong((statupdate.getValue()).longValue());
                    mplew.writeLong((statupdate.getValue()).longValue());
                    mplew.writeLong((statupdate.getValue()).longValue());
                    break;
                default:
                    mplew.writeInt((statupdate.getValue()));
                    break;
            }
        }
        if ((updateMask == 0L) && (!itemReaction)) {
            mplew.write(1);
        }

        mplew.write(0);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] setTemporaryStats(short str, short dex, short _int, short luk, short watk, short matk, short acc, short avoid, short speed, short jump) {
        Map<Temp, Integer> stats = new EnumMap<>(MapleStat.Temp.class);

        stats.put(MapleStat.Temp.STR, Integer.valueOf(str));
        stats.put(MapleStat.Temp.DEX, Integer.valueOf(dex));
        stats.put(MapleStat.Temp.INT, Integer.valueOf(_int));
        stats.put(MapleStat.Temp.LUK, Integer.valueOf(luk));
        stats.put(MapleStat.Temp.WATK, Integer.valueOf(watk));
        stats.put(MapleStat.Temp.MATK, Integer.valueOf(matk));
        stats.put(MapleStat.Temp.ACC, Integer.valueOf(acc));
        stats.put(MapleStat.Temp.AVOID, Integer.valueOf(avoid));
        stats.put(MapleStat.Temp.SPEED, Integer.valueOf(speed));
        stats.put(MapleStat.Temp.JUMP, Integer.valueOf(jump));

        return temporaryStats(stats);
    }

    public static byte[] temporaryStats_Aran() {
        Map<Temp, Integer> stats = new EnumMap<>(MapleStat.Temp.class);

        stats.put(MapleStat.Temp.STR, 999);
        stats.put(MapleStat.Temp.DEX, 999);
        stats.put(MapleStat.Temp.INT, 999);
        stats.put(MapleStat.Temp.LUK, 999);
        stats.put(MapleStat.Temp.WATK, 255);
        stats.put(MapleStat.Temp.ACC, 999);
        stats.put(MapleStat.Temp.AVOID, 999);
        stats.put(MapleStat.Temp.SPEED, 140);
        stats.put(MapleStat.Temp.JUMP, 120);

        return temporaryStats(stats);
    }

    public static byte[] temporaryStats_Balrog(MapleCharacter chr) {
        Map<Temp, Integer> stats = new EnumMap<>(MapleStat.Temp.class);

        int offset = 1 + (chr.getLevel() - 90) / 20;
        stats.put(MapleStat.Temp.STR, chr.getStat().getTotalStr() / offset);
        stats.put(MapleStat.Temp.DEX, chr.getStat().getTotalDex() / offset);
        stats.put(MapleStat.Temp.INT, chr.getStat().getTotalInt() / offset);
        stats.put(MapleStat.Temp.LUK, chr.getStat().getTotalLuk() / offset);
        stats.put(MapleStat.Temp.WATK, chr.getStat().getTotalWatk() / offset);
        stats.put(MapleStat.Temp.MATK, chr.getStat().getTotalMagic() / offset);

        return temporaryStats(stats);
    }

    public static byte[] temporaryStats(Map<MapleStat.Temp, Integer> mystats) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TEMP_STATS.getValue());
        int updateMask = 0;
        for (MapleStat.Temp statupdate : mystats.keySet()) {
            updateMask |= statupdate.getValue();
        }
        mplew.writeInt(updateMask);
        for (final Entry<MapleStat.Temp, Integer> statupdate : mystats.entrySet()) {
            switch (statupdate.getKey()) {
                case SPEED:
                case JUMP:
                case UNKNOWN:
                    mplew.write((statupdate.getValue()).byteValue());
                    break;
                default:
                    mplew.writeShort((statupdate.getValue()).shortValue());
            }
        }

        return mplew.getPacket();
    }

    public static byte[] temporaryStats_Reset() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TEMP_STATS_RESET.getValue());

        return mplew.getPacket();
    }

    public static byte[] updateSkills(Map<Skill, SkillEntry> update, boolean hyper) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_SKILLS.getValue());
        mplew.write(1);//was0
        mplew.writeShort(update.size());
        for (Map.Entry z : update.entrySet()) {
            mplew.writeInt(((Skill) z.getKey()).getId());
            mplew.writeInt(((SkillEntry) z.getValue()).skillevel);
            mplew.writeInt(((SkillEntry) z.getValue()).masterlevel);
            //PacketHelper.addExpirationTime(mplew, ((SkillEntry) z.getValue()).expiration);
        }
        mplew.write(/*
                 * hyper ? 0x0C :
                 */4);

        return mplew.getPacket();
    }

    public static byte[] giveFameErrorResponse(int op) {
        return OnFameResult(op, null, true, 0);
    }

    public static byte[] OnFameResult(int op, String charname, boolean raise, int newFame) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FAME_RESPONSE.getValue());
        mplew.write(op);
        if ((op == 0) || (op == 5)) {
            mplew.writeMapleAsciiString(charname == null ? "" : charname);
            mplew.write(raise ? 1 : 0);
            if (op == 0) {
                mplew.writeInt(newFame);
            }
        }

        return mplew.getPacket();
    }

    public static byte[] fullClientDownload() {
        //Opens "http://maplestory.nexon.net/support/game-download"
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FULL_CLIENT_DOWNLOAD.getValue());

        return mplew.getPacket();
    }

    public static byte[] bombLieDetector(boolean error, int mapid, int channel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LIE_DETECTOR.getValue());
        mplew.write(error ? 2 : 1);
        mplew.writeInt(mapid);
        mplew.writeInt(channel);

        return mplew.getPacket();
    }

    public static byte[] sendLieDetector(byte[] image, int refresh) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LIE_DETECTOR.getValue());
        mplew.write(6); // 1 = not attacking, 2 = tested, 3 = going through 
        mplew.write(4); // 2 give invalid pointer (suppose to be admin macro) 
        mplew.write(refresh); //次数
        if (image == null) {
            mplew.writeInt(0);
            return mplew.getPacket();
        }
        mplew.writeInt(image.length);
        mplew.write(image);

        return mplew.getPacket();
    }

    public static byte[] LieDetectorResponse(final byte msg) {
        return LieDetectorResponse(msg, (byte) 0);
    }

    public static byte[] LieDetectorResponse(final byte msg, final byte msg2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LIE_DETECTOR.getValue());
        mplew.write(msg); // 1 = not attacking, 2 = tested, 3 = going through 
        mplew.write(msg2);

        return mplew.getPacket();
    }

    public static byte[] getLieDetector(byte type, String tester) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LIE_DETECTOR.getValue()); // 2A 00 01 00 00 00  
        mplew.write(type); // 1 = not attacking, 2 = tested, 3 = going through, 4 save screenshot 
        switch (type) {
            case 4: //save screen shot 
                mplew.write(0);
                mplew.writeMapleAsciiString(""); // file name 
                break;
            case 5:
                mplew.write(1); // 2 = save screen shot 
                mplew.writeMapleAsciiString(tester); // me or file name 
                break;
            case 6:
                mplew.write(4); // 2 or anything else, 2 = with maple admin picture, basicaly manager's skill? 
                mplew.write(1); // if > 0, then time = 60,000..maybe try < 0? 
                //mplew.writeInt(size);
                //mplew.write(byte); // bytes 
                break;
            case 7://send this if failed 
                // 2 = You have been appointed as a auto BOT program user and will be restrained. 
                mplew.write(4); // default 
                break;
            case 9:
                // 0 = passed lie detector test 
                // 1 = reward 5000 mesos for not botting. 
                // 2 = thank you for your cooperation with administrator. 
                mplew.write(0);
                break;
            case 8: // save screen shot.. it appears that you may be using a macro-assisted program
                mplew.write(0); // 2 or anything else , 2 = show msg, 0 = none 
                mplew.writeMapleAsciiString(""); // file name 
                break;
            case 10: // no save 
                mplew.write(0); // 2 or anything else, 2 = show msg 
                mplew.writeMapleAsciiString(""); // ?? // hi_You have passed the lie detector test 
                break;
            default:
                mplew.write(0);
                break;
        }
        return mplew.getPacket();
    }

    public static byte[] lieDetector(byte mode, byte action, byte[] image, String str1, String str2, String str3) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LIE_DETECTOR.getValue());
        mplew.write(mode);
        mplew.write(action); //2 = show msg/save screenshot/maple admin picture(mode 6)
        if (mode == 6) {
            mplew.write(1); //if true time is 60:00
            PacketHelper.addImageInfo(mplew, image);
        }
        if (mode == 7 || mode == 9) {
        }
        if (mode == 4) { //save screenshot
            mplew.writeMapleAsciiString(str1); //file name
        }
        if (mode != 5) {
            if (mode == 10) {
                mplew.writeMapleAsciiString(str2); //passed lie detector message
            } else {
                if (mode != 8) {
                }
                mplew.writeMapleAsciiString(str2); //failed lie detector, file name (for screenshot)
            }
        }
        mplew.writeMapleAsciiString(str3); //file name for screenshot

        return mplew.getPacket();
    }

    public static byte[] report(int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REPORT_RESPONSE.getValue());
        mplew.write(mode);
        if (mode == 2) {
            mplew.write(0);
            mplew.writeInt(1); //times left to report
        }

        return mplew.getPacket();
    }

    public static byte[] OnSetClaimSvrAvailableTime(int from, int to) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(4);

        mplew.writeShort(SendPacketOpcode.REPORT_TIME.getValue());
        mplew.write(from);
        mplew.write(to);

        return mplew.getPacket();
    }

    public static byte[] OnClaimSvrStatusChanged(boolean enable) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);

        mplew.writeShort(SendPacketOpcode.REPORT_STATUS.getValue());
        mplew.write(enable ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] updateMount(MapleCharacter chr, boolean levelup) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_MOUNT.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(chr.getMount().getLevel());
        mplew.writeInt(chr.getMount().getExp());
        mplew.writeInt(chr.getMount().getFatigue());
        mplew.write(levelup ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] showQuestCompletion(int id) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_QUEST_COMPLETION.getValue());
        mplew.writeShort(id);

        return mplew.getPacket();
    }

    public static byte[] useSkillBook(MapleCharacter chr, int skillid, int maxlevel, boolean canuse, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.USE_SKILL_BOOK.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(1);
        mplew.writeInt(skillid);
        mplew.writeInt(maxlevel);
        mplew.write(canuse ? 1 : 0);
        mplew.write(success ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] useAPSPReset(boolean spReset, int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(spReset ? SendPacketOpcode.SP_RESET.getValue() : SendPacketOpcode.AP_RESET.getValue());
        mplew.write(1);
        mplew.writeInt(cid);
        mplew.write(1);

        return mplew.getPacket();
    }

    public static byte[] expandCharacterSlots(int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.EXPAND_CHARACTER_SLOTS.getValue());
        mplew.writeInt(mode);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] finishedGather(int type) {
        return gatherSortItem(true, type);
    }

    public static byte[] finishedSort(int type) {
        return gatherSortItem(false, type);
    }

    public static byte[] gatherSortItem(boolean gather, int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(gather ? SendPacketOpcode.FINISH_GATHER.getValue() : SendPacketOpcode.FINISH_SORT.getValue());
        mplew.write(1);
        mplew.write(type);

        return mplew.getPacket();
    }

    public static byte[] updateExpPotion(int mode, int id, int itemId, boolean firstTime, int level, int potionDstLevel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.EXP_POTION.getValue());
        mplew.write(mode);
        mplew.write(1); //bool for get_update_time
        mplew.writeInt(id);
        if (id != 0) {
            mplew.write(1); //not even being read how rude of nexon
            if (mode == 1) {
                mplew.writeInt(0);
            }
            if (mode == 2) {
                mplew.write(firstTime ? 1 : 0); //1 on first time then it turns 0
                mplew.writeInt(itemId);
                if (itemId != 0) {
                    mplew.writeInt(level); //level, confirmed
                    mplew.writeInt(potionDstLevel); //max level with potion
                    mplew.writeLong(384); //random, more like potion id
                }
            }
        }

        return mplew.getPacket();
    }

    public static byte[] updateGender(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_GENDER.getValue());
        mplew.write(chr.getGender());

        return mplew.getPacket();
    }

    public static byte[] charInfo(MapleCharacter chr, boolean isSelf) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHAR_INFO.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(chr.getLevel());
        mplew.writeShort(chr.getJob());
        //mplew.writeShort(chr.getSubcategory());
        //mplew.write(chr.getStat().pvpRank);
        mplew.writeShort(chr.getFame());
        MapleMarriage marriage = chr.getMarriage();
        mplew.write(marriage != null && marriage.getId() != 0);
        //if (marriage != null && marriage.getId() != 0) {
        //      mplew.writeInt(marriage.getId()); //marriage id
        //      mplew.writeInt(marriage.getHusbandId()); //husband char id
        //       mplew.writeInt(marriage.getWifeId()); //wife char id
        //     mplew.writeShort(3); //msg type
        //       mplew.writeInt(chr.getMarriageItemId()); //ring id husband
        //        mplew.writeInt(chr.getMarriageItemId()); //ring id wife
        //       mplew.writeAsciiString(marriage.getHusbandName(), 13); //husband name
        //       mplew.writeAsciiString(marriage.getWifeName(), 13); //wife name
        // }
        //  List prof = chr.getProfessions();
        //mplew.write(prof.size());
        //   for (Iterator i$ = prof.iterator(); i$.hasNext();) {
        //        int i = ((Integer) i$.next());
        //        mplew.writeShort(i);
        //  }
        if (chr.getGuildId() <= 0) {
            mplew.writeMapleAsciiString("尚未加入家族");
            mplew.writeMapleAsciiString("尚未加入联盟");
        } else {
            MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
            if (gs != null) {
                mplew.writeMapleAsciiString(gs.getName());
                if (gs.getAllianceId() > 0) {
                    MapleGuildAlliance allianceName = World.Alliance.getAlliance(gs.getAllianceId());
                    if (allianceName != null) {
                        mplew.writeMapleAsciiString(allianceName.getName());
                    } else {
                        mplew.writeMapleAsciiString("");
                    }
                } else {
                    mplew.writeMapleAsciiString("");
                }
            } else {
                mplew.writeMapleAsciiString("尚未加入家族");
                mplew.writeMapleAsciiString("尚未加入联盟");
            }
        }
        byte index = 1;
        for (final MaplePet pet : chr.getSummonedPets()) {
            if (pet.getSummoned()) {
                mplew.write(pet.getSummonedValue());
                mplew.writeInt(pet.getPetItemId()); // 宠物道具代码
                mplew.writeMapleAsciiString(pet.getName());// 宠物名称
                mplew.write(pet.getLevel()); // 宠物等级
                mplew.writeShort(pet.getCloseness()); // 宠物亲密度
                mplew.write(pet.getFullness()); // 宠物饱食度
                mplew.writeShort(pet.getFlags()); // 寵物技能
                Item inv = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) (index == 2 ? -122 : index == 1 ? -114 : -138));
                mplew.writeInt(inv == null ? 0 : inv.getItemId());
                index++;
            }
        }
        mplew.write(0); // 宠物结束

        // 坐骑
        if (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -18) != null) {
            final int itemid = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -18).getItemId();
            final MapleMount mount = chr.getMount();
            final boolean canwear = MapleItemInformationProvider.getInstance().getReqLevel(itemid) <= chr.getLevel();
            mplew.write(canwear ? 1 : 0);
            if (canwear) {
                mplew.writeInt(mount.getLevel());
                mplew.writeInt(mount.getExp());
                mplew.writeInt(mount.getFatigue());
            }
        } else {
            mplew.write(0);
        }

        // 购物车
        int wishlistSize = chr.getWishlistSize();
        mplew.write(wishlistSize);
        if (wishlistSize > 0) {
            int[] wishlist = chr.getWishlist();
            for (int x = 0; x < wishlistSize; x++) {
                mplew.writeInt(wishlist[x]);
            }
        }

        // 怪物卡册
        chr.getMonsterBook().addCharInfoPacket(chr.getMonsterBookCover(), mplew);

        // 勋章?
        Item medal = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -49);
        mplew.writeInt(medal == null ? 0 : medal.getItemId());
        List<Pair<Integer, Long>> medalQuests = chr.getCompletedMedals();
        mplew.writeShort(medalQuests.size());
        for (Pair x : medalQuests) {
            mplew.writeShort(((Integer) x.left));
        }

        List chairs = new ArrayList();
        for (Item i : chr.getInventory(MapleInventoryType.SETUP).newList()) {
            if ((i.getItemId() / 10000 == 301) && (!chairs.contains(i.getItemId()))) {
                chairs.add(i.getItemId());
            }
        }
        mplew.writeInt(chairs.size());
        for (Iterator i$ = chairs.iterator(); i$.hasNext();) {
            int i = ((Integer) i$.next());
            mplew.writeInt(i);
        }

        List medals = new ArrayList();
        for (Item i : chr.getInventory(MapleInventoryType.EQUIP).list()) {
            if ((i.getItemId() >= 1142000) && (i.getItemId() < 1152000)) {
                medals.add(i.getItemId());
            }
        }
        mplew.writeInt(medals.size());
        for (Iterator i$ = medals.iterator(); i$.hasNext();) {
            int i = ((Integer) i$.next());
            mplew.writeInt(i);
        }
//        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] getMonsterBookInfo(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

//        mplew.writeShort(SendPacketOpcode.BOOK_INFO.getValue());
//        mplew.writeInt(chr.getId());
//        mplew.writeInt(chr.getLevel());
//        chr.getMonsterBook().writeCharInfoPacket(mplew);
        return mplew.getPacket();
    }

    public static byte[] spawnPortal(int townId, int targetId, int skillId, Point pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_PORTAL.getValue());
        mplew.writeInt(townId);
        mplew.writeInt(targetId);
        if ((townId != 999999999) && (targetId != 999999999)) {
            mplew.writeInt(skillId);
            mplew.writePos(pos);
        }

        return mplew.getPacket();
    }

    public static byte[] mechPortal(Point pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MECH_PORTAL.getValue());
        mplew.writePos(pos);

        return mplew.getPacket();
    }

    public static byte[] echoMegaphone(String name, String message) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ECHO_MESSAGE.getValue());
        mplew.write(0);
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.writeMapleAsciiString(name);
        mplew.writeMapleAsciiString(message);

        return mplew.getPacket();
    }

    public static byte[] showQuestMsg(String msg) {
        return broadcastMsg(5, msg);
    }

    public static byte[] Mulung_Pts(int recv, int total) {
        return showQuestMsg("你获得 " + recv + " 修炼点数, 目前累计了 " + total + " 点修炼点数");
    }

    public static byte[] serverNotice(int type, String message) {
        return broadcastMsg(type, message);
    }

    public static byte[] serverNotice(String message) {
        return broadcastMsg(message);
    }

    public static byte[] broadcastMsg(String message) {
        return broadcastMessage(4, 0, message, false);
    }

    public static byte[] broadcastMsg(int type, String message) {
        return broadcastMessage(type, 0, message, false);
    }

    public static byte[] broadcastMsg(int type, int channel, String message) {
        return broadcastMessage(type, channel, message, false);
    }

    public static byte[] broadcastMsg(int type, int channel, String message, boolean smegaEar) {
        return broadcastMessage(type, channel, message, smegaEar);
    }

    private static byte[] broadcastMessage(int type, int channel, String message, boolean megaEar) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        /*
         * 9 = 黄色背景道具广
         *
         */
        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(type);
        if (type == 4) {
            mplew.write(1);
        }
        if ((type != 23) && (type != 24)) {
            mplew.writeMapleAsciiString(message);
        }
        switch (type) {
            case 3:
            case 22:
            case 25:
            case 26:
                mplew.write(channel - 1);
                mplew.write(megaEar ? 1 : 0);
                break;
            case 9:
                mplew.write(channel - 1);
                break;
            case 12:
                mplew.writeInt(channel - 1);
                break;
            case 6:
            case 11:
            case 20:
                mplew.writeInt((channel >= 1000000) && (channel < 6000000) ? channel : 0);
                break;
            case 24:
                mplew.writeShort(0);
            case 4:
            case 5:
            case 7:
            case 8:
            case 10:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            case 23:
        }
        return mplew.getPacket();
    }

    public static byte[] getGachaponMega(String name, String message, Item item, byte rareness, String gacha) {
        return getGachaponMega(name, message, item, rareness, false, gacha);
    }

    public static byte[] getGachaponMega(String name, String message, Item item, byte rareness) {
        return getGachaponMega(name, message, item, rareness, true, "");
    }

    public static byte[] getGachaponMega(String name, String message, Item item, byte rareness, boolean dragon, String gacha) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(0x0F);
        mplew.writeMapleAsciiString(new StringBuilder().append(name).append(message).toString());
        mplew.writeInt(0);
        if (!dragon) {
            mplew.writeInt(item.getItemId());
        }
        //mplew.writeMapleAsciiString(gacha);
        PacketHelper.addItemInfo(mplew, item);

        return mplew.getPacket();
    }

    public static byte[] getEventEnvelope(int questID, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(23);
        mplew.writeShort(questID);
        mplew.writeInt(time);

        return mplew.getPacket();
    }

    public static byte[] tripleSmega(List<String> message, boolean ear, int channel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(10);
        if (message.get(0) != null) {
            mplew.writeMapleAsciiString(message.get(0));
        }
        mplew.write(message.size());
        for (int i = 1; i < message.size(); i++) {
            if (message.get(i) != null) {
                mplew.writeMapleAsciiString(message.get(i));
            }
        }
        mplew.write(channel - 1);
        mplew.write(ear ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] itemMegaphone(String msg, boolean whisper, int channel, Item item) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(8);
        mplew.writeMapleAsciiString(msg);
        mplew.write(channel - 1);
        mplew.write(whisper ? 1 : 0);
        PacketHelper.addItemPosition(mplew, item, true, false);
        if (item != null) {
            PacketHelper.addItemInfo(mplew, item);
        }

        return mplew.getPacket();
    }

    public static byte[] getPeanutResult(int itemId, short quantity, int itemId2, short quantity2, int ourItem) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PIGMI_REWARD.getValue());
        mplew.writeInt(itemId);
        mplew.writeShort(quantity);
        mplew.writeInt(ourItem);
        mplew.writeInt(itemId2);
        mplew.writeInt(quantity2);
        mplew.write(0);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] getOwlOpen() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.OWL_OF_MINERVA.getValue());
        mplew.write(9);
        mplew.write(GameConstants.owlItems.length);
        for (int i : GameConstants.owlItems) {
            mplew.writeInt(i);
        }

        return mplew.getPacket();
    }

    public static byte[] getOwlSearched(int itemSearch, List<HiredMerchant> hms) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.OWL_OF_MINERVA.getValue());
        mplew.write(8);
        mplew.writeInt(0);
        mplew.writeInt(itemSearch);
        int size = 0;

        for (HiredMerchant hm : hms) {
            size += hm.searchItem(itemSearch).size();
        }

        mplew.writeInt(size);
        for (HiredMerchant hm : hms) {
            for (Iterator<HiredMerchant> i = hms.iterator(); i.hasNext();) {
                hm = i.next();
                final List<MaplePlayerShopItem> items = hm.searchItem(itemSearch);
                for (MaplePlayerShopItem item : items) {
                    mplew.writeMapleAsciiString(hm.getOwnerName());
                    mplew.writeInt(hm.getMap().getId());
                    mplew.writeMapleAsciiString(hm.getDescription());
                    mplew.writeInt(item.item.getQuantity());
                    mplew.writeInt(item.bundles);
                    mplew.writeInt(item.price);
                    switch (2) {
                        case 0:
                            mplew.writeInt(hm.getOwnerId());
                            break;
                        case 1:
                            mplew.writeInt(hm.getStoreId());
                            break;
                        default:
                            mplew.writeInt(hm.getObjectId());
                    }

                    mplew.write(hm.getFreeSlot() == -1 ? 1 : 0);
                    mplew.write(GameConstants.getInventoryType(itemSearch).getType());
                    if (GameConstants.getInventoryType(itemSearch) == MapleInventoryType.EQUIP) {
                        PacketHelper.addItemInfo(mplew, item.item);
                    }
                }
            }
        }
        return mplew.getPacket();
    }

    public static byte[] getOwlMessage(int msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);

        mplew.writeShort(SendPacketOpcode.OWL_RESULT.getValue());
        mplew.write(msg);

        return mplew.getPacket();
    }

    public static byte[] sendEngagementRequest(String name, int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ENGAGE_REQUEST.getValue());

        mplew.write(0);
        mplew.writeMapleAsciiString(name);
        mplew.writeInt(cid);

        return mplew.getPacket();
    }

    public static byte[] sendEngagement(byte msg, int item, MapleCharacter male, MapleCharacter female) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ENGAGE_RESULT.getValue());
        mplew.write(msg);
        if (msg == 9 || msg >= 11 && msg <= 14) {
            mplew.writeInt(0);
            mplew.writeInt(male.getId());
            mplew.writeInt(female.getId());
            mplew.writeShort(1);
            mplew.writeInt(item);
            mplew.writeInt(item);
            mplew.writeAsciiString(male.getName(), 13);
            mplew.writeAsciiString(female.getName(), 13);
        } else if (msg == 10 || msg >= 15 && msg <= 16) {
            mplew.writeAsciiString("Male", 13);
            mplew.writeAsciiString("Female", 13);
            mplew.writeShort(0);
        }

        return mplew.getPacket();
    }

    public static byte[] sendWeddingGive() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(9);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] sendWeddingReceive() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(10);
        mplew.writeLong(-1L);
        mplew.writeInt(0);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] giveWeddingItem() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(11);
        mplew.write(0);
        mplew.writeLong(0L);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] receiveWeddingItem() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(15);
        mplew.writeLong(0L);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] sendCashPetFood(boolean success, byte index) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3 + (success ? 1 : 0));

        mplew.writeShort(SendPacketOpcode.USE_CASH_PET_FOOD.getValue());
        mplew.write(success ? 0 : 1);
        if (success) {
            mplew.write(index);
        }

        return mplew.getPacket();
    }

    public static byte[] yellowChat(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.YELLOW_CHAT.getValue());
        mplew.write(-1);
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] shopDiscount(int percent) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOP_DISCOUNT.getValue());
        mplew.write(percent);

        return mplew.getPacket();
    }

    public static byte[] catchMob(int mobid, int itemid, byte success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CATCH_MOB.getValue());
        mplew.write(success);
        mplew.writeInt(itemid);
        mplew.writeInt(mobid);

        return mplew.getPacket();
    }

    public static byte[] spawnPlayerNPC(PlayerNPC npc) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PLAYER_NPC.getValue());
        mplew.write(1);
        mplew.writeInt(npc.getId());
        mplew.writeMapleAsciiString(npc.getName());
        PacketHelper.addCharLook(mplew, npc, true, false);

        return mplew.getPacket();
    }

    public static byte[] disabledNPC(List<Integer> ids) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3 + ids.size() * 4);

        mplew.writeShort(SendPacketOpcode.DISABLE_NPC.getValue());
        mplew.write(ids.size());
        for (Integer i : ids) {
            mplew.writeInt(i);
        }

        return mplew.getPacket();
    }

    public static byte[] getCard(int itemid, int level) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GET_CARD.getValue());
        mplew.write(itemid > 0 ? 1 : 0);
        if (itemid > 0) {
            mplew.writeInt(itemid);
            mplew.writeInt(level);
        }
        return mplew.getPacket();
    }

    public static byte[] changeCardSet(int set) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CARD_SET.getValue());
        mplew.writeInt(set);

        return mplew.getPacket();
    }

    public static byte[] upgradeBook(Item book, MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BOOK_STATS.getValue());
        mplew.writeInt(book.getPosition());
        PacketHelper.addItemInfo(mplew, book, chr);

        return mplew.getPacket();
    }

    public static byte[] getCardDrops(int cardid, List<Integer> drops) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CARD_DROPS.getValue());
        mplew.writeInt(cardid);
        mplew.writeShort(drops == null ? 0 : drops.size());
        if (drops != null) {
            for (Integer de : drops) {
                mplew.writeInt(de);
            }
        }

        return mplew.getPacket();
    }

    public static byte[] getFamiliarInfo(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FAMILIAR_INFO.getValue());
        mplew.writeInt(chr.getFamiliars().size());
        for (MonsterFamiliar mf : chr.getFamiliars().values()) {
            mf.writeRegisterPacket(mplew, true);
        }
        List<Pair<Integer, Long>> size = new ArrayList<>();
        for (Item i : chr.getInventory(MapleInventoryType.USE).list()) {
            if (i.getItemId() / 10000 == 287) {
                StructFamiliar f = MapleItemInformationProvider.getInstance().getFamiliarByItem(i.getItemId());
                if (f != null) {
                    size.add(new Pair<>(f.familiar, i.getInventoryId()));
                }
            }
        }
        mplew.writeInt(size.size());
        for (Pair<?, ?> s : size) {
            mplew.writeInt(chr.getId());
            mplew.writeInt(((Integer) s.left));
            mplew.writeLong(((Long) s.right));
            mplew.write(0);
        }
        size.clear();

        return mplew.getPacket();
    }

    public static byte[] updateWebBoard(boolean result) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WEB_BOARD_UPDATE.getValue());
        mplew.writeBoolean(result);

        return mplew.getPacket();
    }

    public static byte[] MulungEnergy(int energy) {
        return sendPyramidEnergy("energy", String.valueOf(energy));
    }

    public static byte[] sendPyramidEnergy(String type, String amount) {
        return sendString(1, type, amount);
    }

    public static byte[] sendGhostPoint(String type, String amount) {
        return sendString(2, type, amount);
    }

    public static byte[] sendGhostStatus(String type, String amount) {
        return sendString(3, type, amount);
    }

    public static byte[] sendString(int type, String object, String amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        switch (type) {
            case 1:

                mplew.writeShort(SendPacketOpcode.SESSION_VALUE.getValue());
                break;
            case 2:
                mplew.writeShort(SendPacketOpcode.PARTY_VALUE.getValue());
                break;
            case 3:
                mplew.writeShort(SendPacketOpcode.MAP_VALUE.getValue());
        }

        mplew.writeMapleAsciiString(object);
        mplew.writeMapleAsciiString(amount);

        return mplew.getPacket();
    }

    public static byte[] fairyPendantMessage(int termStart, int incExpR) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(14);

        mplew.writeShort(SendPacketOpcode.EXP_BONUS.getValue());
        mplew.writeInt(17);
        mplew.writeInt(0);

        mplew.writeInt(incExpR);

        return mplew.getPacket();
    }

    public static byte[] potionDiscountMessage(int type, int potionDiscR) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(10);

        mplew.writeShort(SendPacketOpcode.POTION_BONUS.getValue());
        mplew.writeInt(type);
        mplew.writeInt(potionDiscR);

        return mplew.getPacket();
    }

    public static byte[] sendLevelup(boolean family, int level, String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LEVEL_UPDATE.getValue());
        mplew.write(family ? 1 : 2);
        mplew.writeInt(level);
        mplew.writeMapleAsciiString(name);

        return mplew.getPacket();
    }

    public static byte[] sendMarriage(boolean family, String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MARRIAGE_UPDATE.getValue());
        mplew.write(family ? 1 : 0);
        mplew.writeMapleAsciiString(name);

        return mplew.getPacket();
    }

    public static byte[] sendJobup(boolean family, int jobid, String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.JOB_UPDATE.getValue());
        mplew.write(family ? 1 : 0);
        mplew.writeInt(jobid);
        mplew.writeMapleAsciiString(new StringBuilder().append(!family ? "> " : "").append(name).toString());

        return mplew.getPacket();
    }

    public static byte[] getAvatarMega(MapleCharacter chr, int channel, int itemId, List<String> text, boolean ear) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.AVATAR_MEGA.getValue());
        mplew.writeInt(itemId);
        mplew.writeMapleAsciiString(chr.getName());
        for (String i : text) {
            mplew.writeMapleAsciiString(i);
        }
        mplew.writeInt(channel - 1);
        mplew.write(ear ? 1 : 0);
        PacketHelper.addCharLook(mplew, chr, true, false);

        return mplew.getPacket();
    }

    public static byte[] GMPoliceMessage(boolean dc) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);

        mplew.writeShort(SendPacketOpcode.GM_POLICE.getValue());
        mplew.write(dc ? 10 : 0);

        return mplew.getPacket();
    }

    public static byte[] GMPoliceMessage(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MAPLE_ADMIN_MSG.getValue());
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] pendantSlot(boolean p) { //slot -59
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SLOT_UPDATE.getValue());
        mplew.write(p ? 1 : 0);
        return mplew.getPacket();
    }

    public static byte[] followRequest(int chrid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FOLLOW_REQUEST.getValue());
        mplew.writeInt(chrid);

        return mplew.getPacket();
    }

    public static byte[] getTopMsg(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TOP_MSG.getValue());
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] getNewTopMsg() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.NEW_TOP_MSG.getValue());
        mplew.writeInt(3);
        mplew.writeInt(20);
        mplew.writeInt(15);
        mplew.writeInt(58);
        mplew.writeAsciiString("Welcome to Maple World. We're one big family!");

        return mplew.getPacket();
    }

    public static byte[] showMidMsg(String s, int l) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MID_MSG.getValue());
        mplew.write(l);
        mplew.writeMapleAsciiString(s);
        mplew.write(s.length() > 0 ? 0 : 1);

        return mplew.getPacket();
    }

    public static byte[] getMidMsg(String msg, boolean keep, int index) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MID_MSG.getValue());
        mplew.write(index);
        mplew.writeMapleAsciiString(msg);
        mplew.write(keep ? 0 : 1);

        return mplew.getPacket();
    }

    public static byte[] clearMidMsg() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CLEAR_MID_MSG.getValue());

        return mplew.getPacket();
    }

    public static byte[] getSpecialMsg(String msg, int type, boolean show) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPECIAL_MSG.getValue());
        mplew.writeMapleAsciiString(msg);
        mplew.writeInt(type);
        mplew.writeInt(show ? 0 : 1);

        return mplew.getPacket();
    }

    public static byte[] CakePieMsg() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CAKE_VS_PIE_MSG.getValue());

        return mplew.getPacket();
    }

    public static byte[] gmBoard(int increnement, String url) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GM_STORY_BOARD.getValue());
        mplew.writeInt(increnement); //Increnement number
        mplew.writeMapleAsciiString(url);

        return mplew.getPacket();
    }

    public static byte[] updateJaguar(MapleCharacter from) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_JAGUAR.getValue());
        PacketHelper.addJaguarInfo(mplew, from);

        return mplew.getPacket();
    }

    public static byte[] loadInformation(byte mode, int location, int birthday, int favoriteAction, int favoriteLocation, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.YOUR_INFORMATION.getValue());
        mplew.write(mode);
        if (mode == 2) {
            mplew.writeInt(location);
            mplew.writeInt(birthday);
            mplew.writeInt(favoriteAction);
            mplew.writeInt(favoriteLocation);
        } else if (mode == 4) {
            mplew.write(success ? 1 : 0);
        }

        return mplew.getPacket();
    }

    public static byte[] saveInformation(boolean fail) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.YOUR_INFORMATION.getValue());
        mplew.write(4);
        mplew.write(fail ? 0 : 1);

        return mplew.getPacket();
    }

    public static byte[] myInfoResult() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FIND_FRIEND.getValue());
        mplew.write(6);
        mplew.writeInt(0);
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] findFriendResult(byte mode, List<MapleCharacter> friends, int error, MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FIND_FRIEND.getValue());
        mplew.write(mode);
        switch (mode) {
            case 6:
                mplew.writeInt(0);
                mplew.writeInt(0);
                break;
            case 8:
                mplew.writeShort(friends.size());
                for (MapleCharacter mc : friends) {
                    mplew.writeInt(mc.getId());
                    mplew.writeMapleAsciiString(mc.getName());
                    //mplew.write(mc.getLevel());
                    //mplew.writeShort(mc.getJob());
                    //mplew.writeInt(0);
                    //mplew.writeInt(0);
                }
                break;
            case 9:
                mplew.write(error);
                break;
            case 11:
                mplew.writeInt(chr.getId());
                PacketHelper.addCharLook(mplew, chr, true, false);
                break;
        }

        return mplew.getPacket();
    }

    public static byte[] showBackgroundEffect(String eff, int value) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.VISITOR.getValue());
        mplew.writeMapleAsciiString(eff);
        mplew.write(value);

        return mplew.getPacket();
    }

    public static byte[] sendPinkBeanChoco() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PINKBEAN_CHOCO.getValue());
        mplew.writeInt(0);
        mplew.write(1);
        mplew.write(0);
        mplew.write(0);
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] changeChannelMsg(int channel, String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(8 + msg.length());

        mplew.writeShort(SendPacketOpcode.AUTO_CC_MSG.getValue());
        mplew.writeInt(channel);
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] pamSongUI() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PAM_SONG.getValue());
        return mplew.getPacket();
    }

    public static byte[] ultimateExplorer() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ULTIMATE_EXPLORER.getValue());

        return mplew.getPacket();
    }

    public static byte[] professionInfo(String skil, int level1, int level2, int chance) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPECIAL_STAT.getValue());
        mplew.writeMapleAsciiString(skil);
        mplew.writeInt(level1);
        mplew.writeInt(level2);
        mplew.write(1);
        mplew.writeInt((skil.startsWith("9200")) || (skil.startsWith("9201")) ? 100 : chance);

        return mplew.getPacket();
    }

    public static byte[] updateAzwanFame(int level, int fame, boolean levelup) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_HONOUR.getValue());
        mplew.writeInt(level);
        mplew.writeInt(fame);
        mplew.write(levelup ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] showAzwanKilled() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.AZWAN_KILLED.getValue());

        return mplew.getPacket();
    }

    public static byte[] showSilentCrusadeMsg(byte type, short chapter) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SILENT_CRUSADE_MSG.getValue());
        mplew.write(type);
        mplew.writeShort(chapter - 1);

        /*
         * type: 0 - open ui (short is chapter) 2 - not enough inventory space 3
         * - failed due to unknown error
         */
        return mplew.getPacket();
    }

    public static byte[] getSilentCrusadeMsg(byte type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SILENT_CRUSADE_SHOP.getValue());
        mplew.write(type);

        return mplew.getPacket();
    }

    public static byte[] showSCShopMsg(byte type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SILENT_CRUSADE_SHOP.getValue());
        mplew.write(type);

        return mplew.getPacket();
    }

    public static byte[] updateImpTime() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_IMP_TIME.getValue());
        mplew.writeInt(0);
        mplew.writeLong(0L);

        return mplew.getPacket();
    }

    public static byte[] updateImp(MapleImp imp, int mask, int index, boolean login) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ITEM_POT.getValue());
        mplew.write(login ? 0 : 1);
        mplew.writeInt(index + 1);
        mplew.writeInt(mask);
        if ((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) {
            Pair<?, ?> i = MapleItemInformationProvider.getInstance().getPot(imp.getItemId());
            if (i == null) {
                return enableActions();
            }
            mplew.writeInt(((Integer) i.left));
            mplew.write(imp.getLevel());
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.STATE.getValue()) != 0)) {
            mplew.write(imp.getState());
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.FULLNESS.getValue()) != 0)) {
            mplew.writeInt(imp.getFullness());
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.CLOSENESS.getValue()) != 0)) {
            mplew.writeInt(imp.getCloseness());
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.CLOSENESS_LEFT.getValue()) != 0)) {
            mplew.writeInt(1);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MINUTES_LEFT.getValue()) != 0)) {
            mplew.writeInt(0);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.LEVEL.getValue()) != 0)) {
            mplew.write(1);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.FULLNESS_2.getValue()) != 0)) {
            mplew.writeInt(imp.getFullness());
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.UPDATE_TIME.getValue()) != 0)) {
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.CREATE_TIME.getValue()) != 0)) {
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.AWAKE_TIME.getValue()) != 0)) {
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.SLEEP_TIME.getValue()) != 0)) {
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_CLOSENESS.getValue()) != 0)) {
            mplew.writeInt(100);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_DELAY.getValue()) != 0)) {
            mplew.writeInt(1000);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_FULLNESS.getValue()) != 0)) {
            mplew.writeInt(1000);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_ALIVE.getValue()) != 0)) {
            mplew.writeInt(1);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_MINUTES.getValue()) != 0)) {
            mplew.writeInt(10);
        }
        mplew.write(0);

        return mplew.getPacket();
    }

//    public static byte[] getMulungRanking(MapleClient c, List<DojoRankingInfo> all) {
//        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
//        mplew.writeShort(SendPacketOpcode.MULUNG_DOJO_RANKING.getValue());
//        MapleDojoRanking data = MapleDojoRanking.getInstance();
//        mplew.writeInt(all.size()); // size
//        for (DojoRankingInfo info : all) {
//            mplew.writeShort(info.getRank());
//            mplew.writeMapleAsciiString(info.getName());
//            mplew.writeLong(info.getTime());
//        }
//        return mplew.getPacket();
//    }
    public static byte[] getMulungRanking() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MULUNG_DOJO_RANKING.getValue());
        DojoRankingsData data = DojoRankingsData.loadLeaderboard();
        mplew.writeInt(data.totalCharacters); // size
        for (int i = 0; i < data.totalCharacters; i++) {
            mplew.writeShort(data.ranks[i]); // rank
            mplew.writeMapleAsciiString(data.names[i]); // Character name
            mplew.writeLong(data.times[i]); // time in seconds
        }
        return mplew.getPacket();
    }

    public static byte[] getMulungMessage(boolean dc, String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MULUNG_MESSAGE.getValue());
        mplew.write(dc ? 1 : 0);
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] getCandyRanking(MapleClient c, List<CandyRankingInfo> all) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(10);

        mplew.writeShort(SendPacketOpcode.CANDY_RANKING.getValue());
        mplew.writeInt(all.size());
        for (CandyRankingInfo info : all) {
            mplew.writeShort(info.getRank());
            mplew.writeMapleAsciiString(info.getName());
        }
        return mplew.getPacket();
    }

    public static class AlliancePacket {

        public static byte[] getAllianceInfo(MapleGuildAlliance alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(12);
            mplew.write(alliance == null ? 0 : 1);
            if (alliance != null) {
                addAllianceInfo(mplew, alliance);
            }

            return mplew.getPacket();
        }

        private static void addAllianceInfo(MaplePacketLittleEndianWriter mplew, MapleGuildAlliance alliance) {
            mplew.writeInt(alliance.getId());
            mplew.writeMapleAsciiString(alliance.getName());
            for (int i = 1; i <= 5; i++) {
                mplew.writeMapleAsciiString(alliance.getRank(i));
            }
            mplew.write(alliance.getNoGuilds());
            for (int i = 0; i < alliance.getNoGuilds(); i++) {
                mplew.writeInt(alliance.getGuildId(i));
            }
            mplew.writeInt(alliance.getCapacity());
            mplew.writeMapleAsciiString(alliance.getNotice());
        }

        public static byte[] getGuildAlliance(MapleGuildAlliance alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(13);
            if (alliance == null) {
                mplew.writeInt(0);
                return mplew.getPacket();
            }
            int noGuilds = alliance.getNoGuilds();
            MapleGuild[] g = new MapleGuild[noGuilds];
            for (int i = 0; i < alliance.getNoGuilds(); i++) {
                g[i] = World.Guild.getGuild(alliance.getGuildId(i));
                if (g[i] == null) {
                    return CWvsContext.enableActions();
                }
            }
            mplew.writeInt(noGuilds);
            for (MapleGuild gg : g) {
                CWvsContext.GuildPacket.getGuildInfo(mplew, gg);
            }
            return mplew.getPacket();
        }

        public static byte[] allianceMemberOnline(int alliance, int gid, int id, boolean online) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(14);
            mplew.writeInt(alliance);
            mplew.writeInt(gid);
            mplew.writeInt(id);
            mplew.write(online ? 1 : 0);

            return mplew.getPacket();
        }

        public static byte[] removeGuildFromAlliance(MapleGuildAlliance alliance, MapleGuild expelledGuild, boolean expelled) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(16);
            addAllianceInfo(mplew, alliance);
            CWvsContext.GuildPacket.getGuildInfo(mplew, expelledGuild);
            mplew.write(expelled ? 1 : 0);

            return mplew.getPacket();
        }

        public static byte[] addGuildToAlliance(MapleGuildAlliance alliance, MapleGuild newGuild) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(18);
            addAllianceInfo(mplew, alliance);
            mplew.writeInt(newGuild.getId());
            CWvsContext.GuildPacket.getGuildInfo(mplew, newGuild);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] sendAllianceInvite(String allianceName, MapleCharacter inviter) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(3);
            mplew.writeInt(inviter.getGuildId());
            mplew.writeMapleAsciiString(inviter.getName());
            mplew.writeMapleAsciiString(allianceName);

            return mplew.getPacket();
        }

        public static byte[] getAllianceUpdate(MapleGuildAlliance alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(23);
            addAllianceInfo(mplew, alliance);

            return mplew.getPacket();
        }

        public static byte[] createGuildAlliance(MapleGuildAlliance alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(15);
            addAllianceInfo(mplew, alliance);
            int noGuilds = alliance.getNoGuilds();
            MapleGuild[] g = new MapleGuild[noGuilds];
            for (int i = 0; i < alliance.getNoGuilds(); i++) {
                g[i] = World.Guild.getGuild(alliance.getGuildId(i));
                if (g[i] == null) {
                    return CWvsContext.enableActions();
                }
            }
            for (MapleGuild gg : g) {
                CWvsContext.GuildPacket.getGuildInfo(mplew, gg);
            }
            return mplew.getPacket();
        }

        public static byte[] updateAlliance(MapleGuildCharacter mgc, int allianceid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(24);
            mplew.writeInt(allianceid);
            mplew.writeInt(mgc.getGuildId());
            mplew.writeInt(mgc.getId());
            mplew.writeInt(mgc.getLevel());
            mplew.writeInt(mgc.getJobId());

            return mplew.getPacket();
        }

        public static byte[] updateAllianceLeader(int allianceid, int newLeader, int oldLeader) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(25);
            mplew.writeInt(allianceid);
            mplew.writeInt(oldLeader);
            mplew.writeInt(newLeader);

            return mplew.getPacket();
        }

        public static byte[] allianceRankChange(int aid, String[] ranks) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(26);
            mplew.writeInt(aid);
            for (String r : ranks) {
                mplew.writeMapleAsciiString(r);
            }

            return mplew.getPacket();
        }

        public static byte[] updateAllianceRank(MapleGuildCharacter mgc) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(27);
            mplew.writeInt(mgc.getId());
            mplew.write(mgc.getAllianceRank());

            return mplew.getPacket();
        }

        public static byte[] changeAllianceNotice(int allianceid, String notice) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(28);
            mplew.writeInt(allianceid);
            mplew.writeMapleAsciiString(notice);

            return mplew.getPacket();
        }

        public static byte[] disbandAlliance(int alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(29);
            mplew.writeInt(alliance);

            return mplew.getPacket();
        }

        public static byte[] changeAlliance(MapleGuildAlliance alliance, boolean in) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(1);
            mplew.write(in ? 1 : 0);
            mplew.writeInt(in ? alliance.getId() : 0);
            int noGuilds = alliance.getNoGuilds();
            MapleGuild[] g = new MapleGuild[noGuilds];
            for (int i = 0; i < noGuilds; i++) {
                g[i] = World.Guild.getGuild(alliance.getGuildId(i));
                if (g[i] == null) {
                    return CWvsContext.enableActions();
                }
            }
            mplew.write(noGuilds);
            for (int i = 0; i < noGuilds; i++) {
                mplew.writeInt(g[i].getId());

                Collection<MapleGuildCharacter> members = g[i].getMembers();
                mplew.writeInt(members.size());
                for (MapleGuildCharacter mgc : members) {
                    mplew.writeInt(mgc.getId());
                    mplew.write(in ? mgc.getAllianceRank() : 0);
                }
            }

            return mplew.getPacket();
        }

        public static byte[] changeAllianceLeader(int allianceid, int newLeader, int oldLeader) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(2);
            mplew.writeInt(allianceid);
            mplew.writeInt(oldLeader);
            mplew.writeInt(newLeader);

            return mplew.getPacket();
        }

        public static byte[] changeGuildInAlliance(MapleGuildAlliance alliance, MapleGuild guild, boolean add) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(4);
            mplew.writeInt(add ? alliance.getId() : 0);
            mplew.writeInt(guild.getId());
            Collection<MapleGuildCharacter> members = guild.getMembers();
            mplew.writeInt(members.size());
            for (MapleGuildCharacter mgc : members) {
                mplew.writeInt(mgc.getId());
                mplew.write(add ? mgc.getAllianceRank() : 0);
            }

            return mplew.getPacket();
        }

        public static byte[] changeAllianceRank(int allianceid, MapleGuildCharacter player) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(5);
            mplew.writeInt(allianceid);
            mplew.writeInt(player.getId());
            mplew.writeInt(player.getAllianceRank());

            return mplew.getPacket();
        }
    }

    public static class FamilyPacket {

        public static byte[] getFamilyData() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.FAMILY.getValue());
            MapleFamilyBuff[] entries = MapleFamilyBuff.values();
            mplew.writeInt(entries.length);

            for (MapleFamilyBuff entry : entries) {
                mplew.write(entry.type);
                mplew.writeInt(entry.rep);
                mplew.writeInt(1);
                mplew.writeMapleAsciiString(entry.name);
                mplew.writeMapleAsciiString(entry.desc);
            }
            return mplew.getPacket();
        }

        public static byte[] getFamilyInfo(MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.OPEN_FAMILY.getValue());
            mplew.writeInt(chr.getCurrentRep());
            mplew.writeInt(chr.getTotalRep());
            mplew.writeInt(chr.getTotalRep());
            mplew.writeShort(chr.getNoJuniors());
            mplew.writeShort(2);
            mplew.writeShort(chr.getNoJuniors());
            MapleFamily family = World.Family.getFamily(chr.getFamilyId());
            if (family != null) {
                mplew.writeInt(family.getLeaderId());
                mplew.writeMapleAsciiString(family.getLeaderName());
                mplew.writeMapleAsciiString(family.getNotice());
            } else {
                mplew.writeLong(0L);
            }
            List<?> b = chr.usedBuffs();
            mplew.writeInt(b.size());
            for (Iterator<?> i$ = b.iterator(); i$.hasNext();) {
                int ii = ((Integer) i$.next());
                mplew.writeInt(ii);
                mplew.writeInt(1);
            }

            return mplew.getPacket();
        }

        public static void addFamilyCharInfo(MapleFamilyCharacter ldr, MaplePacketLittleEndianWriter mplew) {
            mplew.writeInt(ldr.getId());
            mplew.writeInt(ldr.getSeniorId());
            mplew.writeShort(ldr.getJobId());
            mplew.writeShort(0);
            mplew.write(ldr.getLevel());
            mplew.write(ldr.isOnline() ? 1 : 0);
            mplew.writeInt(ldr.getCurrentRep());
            mplew.writeInt(ldr.getTotalRep());
            mplew.writeInt(ldr.getTotalRep());
            mplew.writeInt(ldr.getTotalRep());
            mplew.writeInt(Math.max(ldr.getChannel(), 0));
            mplew.writeInt(0);
            mplew.writeMapleAsciiString(ldr.getName());
        }

        public static byte[] getFamilyPedigree(MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SEND_PEDIGREE.getValue());
            mplew.writeInt(chr.getId());
            MapleFamily family = World.Family.getFamily(chr.getFamilyId());

            int descendants = 2;
            int gens = 0;
            int generations = 0;
            if (family == null) {
                mplew.writeInt(2);
                addFamilyCharInfo(new MapleFamilyCharacter(chr, 0, 0, 0, 0), mplew);
            } else {
                mplew.writeInt(family.getMFC(chr.getId()).getPedigree().size() + 1);
                addFamilyCharInfo(family.getMFC(family.getLeaderId()), mplew);

                if (chr.getSeniorId() > 0) {
                    MapleFamilyCharacter senior = family.getMFC(chr.getSeniorId());
                    if (senior != null) {
                        if (senior.getSeniorId() > 0) {
                            addFamilyCharInfo(family.getMFC(senior.getSeniorId()), mplew);
                        }
                        addFamilyCharInfo(senior, mplew);
                    }
                }
            }
            addFamilyCharInfo(chr.getMFC() == null ? new MapleFamilyCharacter(chr, 0, 0, 0, 0) : chr.getMFC(), mplew);
            if (family != null) {
                if (chr.getSeniorId() > 0) {
                    MapleFamilyCharacter senior = family.getMFC(chr.getSeniorId());
                    if (senior != null) {
                        if ((senior.getJunior1() > 0) && (senior.getJunior1() != chr.getId())) {
                            addFamilyCharInfo(family.getMFC(senior.getJunior1()), mplew);
                        } else if ((senior.getJunior2() > 0) && (senior.getJunior2() != chr.getId())) {
                            addFamilyCharInfo(family.getMFC(senior.getJunior2()), mplew);
                        }

                    }

                }

                if (chr.getJunior1() > 0) {
                    MapleFamilyCharacter junior = family.getMFC(chr.getJunior1());
                    if (junior != null) {
                        addFamilyCharInfo(junior, mplew);
                    }
                }
                if (chr.getJunior2() > 0) {
                    MapleFamilyCharacter junior = family.getMFC(chr.getJunior2());
                    if (junior != null) {
                        addFamilyCharInfo(junior, mplew);
                    }
                }
                if (chr.getJunior1() > 0) {
                    MapleFamilyCharacter junior = family.getMFC(chr.getJunior1());
                    if (junior != null) {
                        if ((junior.getJunior1() > 0) && (family.getMFC(junior.getJunior1()) != null)) {
                            gens++;
                            addFamilyCharInfo(family.getMFC(junior.getJunior1()), mplew);
                        }
                        if ((junior.getJunior2() > 0) && (family.getMFC(junior.getJunior2()) != null)) {
                            gens++;
                            addFamilyCharInfo(family.getMFC(junior.getJunior2()), mplew);
                        }
                    }
                }
                if (chr.getJunior2() > 0) {
                    MapleFamilyCharacter junior = family.getMFC(chr.getJunior2());
                    if (junior != null) {
                        if ((junior.getJunior1() > 0) && (family.getMFC(junior.getJunior1()) != null)) {
                            gens++;
                            addFamilyCharInfo(family.getMFC(junior.getJunior1()), mplew);
                        }
                        if ((junior.getJunior2() > 0) && (family.getMFC(junior.getJunior2()) != null)) {
                            gens++;
                            addFamilyCharInfo(family.getMFC(junior.getJunior2()), mplew);
                        }
                    }
                }
                generations = family.getMemberSize();
            }
            mplew.writeLong(gens);
            mplew.writeInt(0);
            mplew.writeInt(-1);
            mplew.writeInt(generations);

            if (family != null) {
                if (chr.getJunior1() > 0) {
                    MapleFamilyCharacter junior = family.getMFC(chr.getJunior1());
                    if (junior != null) {
                        if ((junior.getJunior1() > 0) && (family.getMFC(junior.getJunior1()) != null)) {
                            mplew.writeInt(junior.getJunior1());
                            mplew.writeInt(family.getMFC(junior.getJunior1()).getDescendants());
                        } else {
                            mplew.writeInt(0);
                        }
                        if ((junior.getJunior2() > 0) && (family.getMFC(junior.getJunior2()) != null)) {
                            mplew.writeInt(junior.getJunior2());
                            mplew.writeInt(family.getMFC(junior.getJunior2()).getDescendants());
                        } else {
                            mplew.writeInt(0);
                        }
                    }
                }
                if (chr.getJunior2() > 0) {
                    MapleFamilyCharacter junior = family.getMFC(chr.getJunior2());
                    if (junior != null) {
                        if ((junior.getJunior1() > 0) && (family.getMFC(junior.getJunior1()) != null)) {
                            mplew.writeInt(junior.getJunior1());
                            mplew.writeInt(family.getMFC(junior.getJunior1()).getDescendants());
                        } else {
                            mplew.writeInt(0);
                        }
                        if ((junior.getJunior2() > 0) && (family.getMFC(junior.getJunior2()) != null)) {
                            mplew.writeInt(junior.getJunior2());
                            mplew.writeInt(family.getMFC(junior.getJunior2()).getDescendants());
                        } else {
                            mplew.writeInt(0);
                        }
                    }
                }
            }

            List<?> b = chr.usedBuffs();
            mplew.writeInt(b.size());
            for (Iterator<?> i$ = b.iterator(); i$.hasNext();) {
                int ii = ((Integer) i$.next());
                mplew.writeInt(ii);
                mplew.writeInt(1);
            }
            mplew.writeShort(2);

            return mplew.getPacket();
        }

        public static byte[] getFamilyMsg(byte type, int meso) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.FAMILY_MESSAGE.getValue());
            mplew.writeInt(type);
            mplew.writeInt(meso);

            return mplew.getPacket();
        }

        public static byte[] sendFamilyInvite(int cid, int otherLevel, int otherJob, String inviter) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.FAMILY_INVITE.getValue());
            mplew.writeInt(cid);
            mplew.writeInt(otherLevel);
            mplew.writeInt(otherJob);
            mplew.writeInt(0);
            mplew.writeMapleAsciiString(inviter);
            return mplew.getPacket();
        }

        public static byte[] sendFamilyJoinResponse(boolean accepted, String added) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.FAMILY_JUNIOR.getValue());
            mplew.write(accepted ? 1 : 0);
            mplew.writeMapleAsciiString(added);

            return mplew.getPacket();
        }

        public static byte[] getSeniorMessage(String name) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SENIOR_MESSAGE.getValue());
            mplew.writeMapleAsciiString(name);

            return mplew.getPacket();
        }

        public static byte[] changeRep(int r, String name) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.REP_INCREASE.getValue());
            mplew.writeInt(r);
            mplew.writeMapleAsciiString(name);

            return mplew.getPacket();
        }

        public static byte[] familyLoggedIn(boolean online, String name) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.FAMILY_LOGGEDIN.getValue());
            mplew.write(online ? 1 : 0);
            mplew.writeMapleAsciiString(name);

            return mplew.getPacket();
        }

        public static byte[] familyBuff(int type, int buffnr, int amount, int time) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.FAMILY_BUFF.getValue());
            mplew.write(type);
            if ((type >= 2) && (type <= 4)) {
                mplew.writeInt(buffnr);

                mplew.writeInt(type == 3 ? 0 : amount);
                mplew.writeInt(type == 2 ? 0 : amount);
                mplew.write(0);
                mplew.writeInt(time);
            }
            return mplew.getPacket();
        }

        public static byte[] cancelFamilyBuff() {
            return familyBuff(0, 0, 0, 0);
        }

        public static byte[] familySummonRequest(String name, String mapname) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.FAMILY_USE_REQUEST.getValue());
            mplew.writeMapleAsciiString(name);
            mplew.writeMapleAsciiString(mapname);

            return mplew.getPacket();
        }
    }

    public static class BuddylistPacket {

        public static byte[] updateBuddylist(Collection<BuddylistEntry> buddylist) {
            return updateBuddylist(buddylist, 7);
        }

        public static byte[] updateBuddylist(Collection<BuddylistEntry> buddylist, int deleted) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            mplew.write(deleted);
            mplew.write(buddylist.size());
            for (BuddylistEntry buddy : buddylist) {
                mplew.writeInt(buddy.getCharacterId());
                mplew.writeAsciiString(buddy.getName(), 13);
                mplew.write(buddy.isVisible() ? 0 : 1);//if adding = 2
                mplew.writeInt(buddy.getChannel() == -1 ? -1 : buddy.getChannel());
                mplew.writeAsciiString(buddy.getGroup(), 17);
            }
            for (int x = 0; x < buddylist.size(); x++) {
                mplew.writeInt(0);
            }

            return mplew.getPacket();
        }

        public static byte[] updateBuddyChannel(int characterid, int channel) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            mplew.write(0x14);
            mplew.writeInt(characterid);
            mplew.write(0);
            mplew.writeInt(channel);

            return mplew.getPacket();
        }

        public static byte[] requestBuddylistAdd(int cidFrom, String nameFrom, int levelFrom, int jobFrom) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            mplew.write(0x9);
            mplew.writeInt(cidFrom);
            mplew.writeMapleAsciiString(nameFrom);
            //mplew.writeInt(levelFrom);
            //mplew.writeInt(jobFrom);
            // mplew.writeInt(0);//v115
            mplew.writeInt(cidFrom);
            mplew.writeAsciiString(nameFrom, 13);
            mplew.write(1);
            mplew.writeInt(0);
            mplew.writeAsciiString("群未定", 17);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] updateBuddyCapacity(int capacity) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            mplew.write(0x15);
            mplew.write(capacity);

            return mplew.getPacket();
        }

        /*
         * 0x0B 好友目录已满 0x0C 对方的好友目录已满 0x0D 已经是好友 0x0E 不能把管理员添加为好友 0x0F 没有登录的角色
         * 0x10 因为发生未知错误，你的请求无法处理 0x11 因为发生未知错误，你的请求无法处理 0x13 因为发生未知错误，你的请求无法处理
         * 0x16 因为发生未知错误，你的请求无法处理
         */
        public static byte[] buddylistMessage(byte message) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            mplew.write(message);

            return mplew.getPacket();
        }
    }

    public static byte[] giveKilling(int x) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
        PacketHelper.writeSingleMask(mplew, MapleBuffStat.KILL_COUNT);
//        mplew.writeInt(0);
//        mplew.write(0);
//        mplew.writeInt(x);
//        mplew.writeZeroBytes(6);
        mplew.writeShort(0);
        mplew.write(0);
        mplew.writeInt(x);
        return mplew.getPacket();
    }

    public static class ExpeditionPacket {

        public static byte[] expeditionStatus(MapleExpedition me, boolean created, boolean silent) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(created ? 86 : silent ? 72 : 76);//74
            mplew.writeInt(me.getType().exped);
            mplew.writeInt(0);
            for (int i = 0; i < 6; i++) {
                if (i < me.getParties().size()) {
                    MapleParty party = World.Party.getParty((me.getParties().get(i)).intValue());

                    CWvsContext.PartyPacket.addPartyStatus(-1, party, mplew, false, true);
                } else {
                    CWvsContext.PartyPacket.addPartyStatus(-1, null, mplew, false, true);
                }

            }

            return mplew.getPacket();
        }

        public static byte[] expeditionError(int errcode, String name) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(100);//88
            mplew.writeInt(errcode);
            mplew.writeMapleAsciiString(name);

            return mplew.getPacket();
        }

        public static byte[] expeditionMessage(int code) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(code);

            return mplew.getPacket();
        }

        public static byte[] expeditionJoined(String name) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(87);//75
            mplew.writeMapleAsciiString(name);

            return mplew.getPacket();
        }

        public static byte[] expeditionLeft(String name) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(91);//79
            mplew.writeMapleAsciiString(name);

            return mplew.getPacket();
        }

        public static byte[] expeditionLeaderChanged(int newLeader) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(96);//84
            mplew.writeInt(newLeader);

            return mplew.getPacket();
        }

        public static byte[] expeditionUpdate(int partyIndex, MapleParty party) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(97);//85
            mplew.writeInt(0);
            mplew.writeInt(partyIndex);

            CWvsContext.PartyPacket.addPartyStatus(-1, party, mplew, false, true);

            return mplew.getPacket();
        }

        public static byte[] expeditionInvite(MapleCharacter from, int exped) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(99);//87
            mplew.writeInt(from.getLevel());
            mplew.writeInt(from.getJob());
            mplew.writeInt(0);
            mplew.writeMapleAsciiString(from.getName());
            mplew.writeInt(exped);

            return mplew.getPacket();
        }
    }

    public static class PartyPacket {

        public static byte[] partyCreated(int partyid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(0x8);
            mplew.writeInt(partyid);
            mplew.writeInt(999999999);
            mplew.writeInt(999999999);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] partyInvite(MapleCharacter from) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(4);
            mplew.writeInt(from.getParty() == null ? 0 : from.getParty().getId());
            mplew.writeMapleAsciiString(from.getName());
            //mplew.writeInt(from.getLevel());
            // mplew.writeInt(from.getJob());
            mplew.write(0);
            //mplew.writeInt(0);
            return mplew.getPacket();
        }

        public static byte[] partyRequestInvite(MapleCharacter from) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(7);
            mplew.writeInt(from.getId());
            mplew.writeMapleAsciiString(from.getName());
            mplew.writeInt(from.getLevel());
            mplew.writeInt(from.getJob());

            return mplew.getPacket();
        }

        /*
         * 0x09 已加入其他组队 0x0A 新手不能开启组队 0x0D 没有参加的组队 0x10 已加入其他组 0x11 组队成员已满 0x13
         * 在这个频道找不到角色 0x15 + MapleAsciiString 玩家处于拒绝组队状态 0x16 + MapleAsciiString
         * 玩家正在做别的事情 0x17 + MapleAsciiString玩家拒绝了组队招待 0x1B 能转让给同一个场地的组队成员 0x1C
         * 没有与组队长同一地图的组队成员 0x1D 只能向相同频道内的组队成员出售 0x1F 管理员不能开组队
         */
        public static byte[] partyStatusMessage(int message, String charname) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(message);
            if ((message == 0x15) || (message == 0x16) || (message == 0x17)) {
                mplew.writeMapleAsciiString(charname);
            }
            return mplew.getPacket();
        }

        public static void addPartyStatus(int forchannel, MapleParty party, MaplePacketLittleEndianWriter lew, boolean leaving) {
            addPartyStatus(forchannel, party, lew, leaving, false);
        }

        public static void addPartyStatus(int forchannel, MapleParty party, MaplePacketLittleEndianWriter lew, boolean leaving, boolean exped) {
            List<MaplePartyCharacter> partymembers;
            if (party == null) {
                partymembers = new ArrayList();
            } else {
                partymembers = new ArrayList(party.getMembers());
            }
            while (partymembers.size() < 6) {
                partymembers.add(new MaplePartyCharacter());
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.getId());
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeAsciiString(partychar.getName(), 13);
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.getJobId());
            }
            //for (MaplePartyCharacter partychar : partymembers) {
            //     lew.writeInt(0);
            // }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.getLevel());
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.isOnline() ? partychar.getChannel() - 1 : -2);
            }
            //for (MaplePartyCharacter partychar : partymembers) {
            //    lew.writeInt(0);
            // }

            lew.writeInt(party == null ? 0 : party.getLeader().getId());
            if (exped) {
                return;
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.getChannel() == forchannel ? partychar.getMapid() : 999999999);
            }
            for (MaplePartyCharacter partychar : partymembers) {
                if ((partychar.getChannel() == forchannel) && (!leaving)) {
                    lew.writeInt(partychar.getDoorTown());
                    lew.writeInt(partychar.getDoorTarget());
                    //lew.writeInt(partychar.getDoorSkill());
                    lew.writeInt(partychar.getDoorPosition().x);
                    lew.writeInt(partychar.getDoorPosition().y);
                } else {
                    lew.writeInt(leaving ? 999999999 : 0);
                    lew.writeInt(leaving ? 999999999 : 0);
                    lew.writeLong(leaving ? -1 : 0);
                }
            }
        }

        public static byte[] updateParty(int forChannel, MapleParty party, PartyOperation op, MaplePartyCharacter target) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            switch (op) {
                case DISBAND:
                case EXPEL:
                case LEAVE:
                    mplew.write(0xC);
                    mplew.writeInt(party.getId());
                    mplew.writeInt(target.getId());
                    if (op == PartyOperation.DISBAND) {
                        mplew.write(0);
                        mplew.writeInt(party.getId());
                    } else {
                        mplew.write(1);
                        if (op == PartyOperation.EXPEL) {
                            mplew.write(1);
                        } else {
                            mplew.write(0);
                        }
                        mplew.writeMapleAsciiString(target.getName());
                        addPartyStatus(forChannel, party, mplew, op == PartyOperation.LEAVE);
                    }
                    break;

                case JOIN:
                    mplew.write(0xF);
                    mplew.writeInt(party.getId());
                    mplew.writeMapleAsciiString(target.getName());
                    addPartyStatus(forChannel, party, mplew, false);
                    break;
                case SILENT_UPDATE:
                case LOG_ONOFF:
                    mplew.write(0x7);
                    mplew.writeInt(party.getId());
                    addPartyStatus(forChannel, party, mplew, op == PartyOperation.LOG_ONOFF);
                    break;
                case CHANGE_LEADER:
                case CHANGE_LEADER_DC:
                    mplew.write(0x1B);
                    mplew.writeInt(target.getId());
                    mplew.write(op == PartyOperation.CHANGE_LEADER_DC ? 1 : 0);
            }
            return mplew.getPacket();
        }

        public static byte[] partyPortal(int townId, int targetId, int skillId, Point position, boolean animation) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(0x23);
            mplew.write(animation ? 0 : 1);
            mplew.writeInt(townId);
            mplew.writeInt(targetId);
            mplew.writePos(position);

            return mplew.getPacket();
        }

        public static byte[] getPartyListing(PartySearchType pst) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(GameConstants.GMS ? 147 : 77);
            mplew.writeInt(pst.id);
            final List<PartySearch> parties = World.Party.searchParty(pst);
            mplew.writeInt(parties.size());
            for (PartySearch party : parties) {
                mplew.writeInt(0);
                mplew.writeInt(2);
                if (pst.exped) {
                    MapleExpedition me = World.Party.getExped(party.getId());
                    mplew.writeInt(me.getType().maxMembers);
                    mplew.writeInt(party.getId());
                    mplew.writeAsciiString(party.getName(), 48);
                    for (int i = 0; i < 5; i++) {
                        if (i < me.getParties().size()) {
                            MapleParty part = World.Party.getParty((me.getParties().get(i)).intValue());
                            if (part != null) {
                                addPartyStatus(-1, part, mplew, false, true);
                            } else {
                                mplew.writeZeroBytes(202);
                            }
                        } else {
                            mplew.writeZeroBytes(202);
                        }
                    }
                } else {
                    mplew.writeInt(0);
                    mplew.writeInt(party.getId());
                    mplew.writeAsciiString(party.getName(), 48);
                    addPartyStatus(-1, World.Party.getParty(party.getId()), mplew, false, true);
                }

                mplew.writeShort(0);
            }

            return mplew.getPacket();
        }

        public static byte[] partyListingAdded(PartySearch ps) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(93);
            mplew.writeInt(ps.getType().id);
            mplew.writeInt(0);
            mplew.writeInt(1);
            if (ps.getType().exped) {
                MapleExpedition me = World.Party.getExped(ps.getId());
                mplew.writeInt(me.getType().maxMembers);
                mplew.writeInt(ps.getId());
                mplew.writeAsciiString(ps.getName(), 48);
                for (int i = 0; i < 5; i++) {
                    if (i < me.getParties().size()) {
                        MapleParty party = World.Party.getParty((me.getParties().get(i)).intValue());
                        if (party != null) {
                            addPartyStatus(-1, party, mplew, false, true);
                        } else {
                            mplew.writeZeroBytes(202);
                        }
                    } else {
                        mplew.writeZeroBytes(202);
                    }
                }
            } else {
                mplew.writeInt(0);
                mplew.writeInt(ps.getId());
                mplew.writeAsciiString(ps.getName(), 48);
                addPartyStatus(-1, World.Party.getParty(ps.getId()), mplew, false, true);
            }
            mplew.writeShort(0);

            return mplew.getPacket();
        }

        public static byte[] showMemberSearch(List<MapleCharacter> chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.MEMBER_SEARCH.getValue());
            mplew.write(chr.size());
            for (MapleCharacter c : chr) {
                mplew.writeInt(c.getId());
                mplew.writeMapleAsciiString(c.getName());
                mplew.writeShort(c.getJob());
                mplew.write(c.getLevel());
            }
            return mplew.getPacket();
        }

        public static byte[] showPartySearch(List<MapleParty> chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PARTY_SEARCH.getValue());
            mplew.write(chr.size());
            for (MapleParty c : chr) {
                mplew.writeInt(c.getId());
                mplew.writeMapleAsciiString(c.getLeader().getName());
                mplew.write(c.getLeader().getLevel());
                mplew.write(c.getLeader().isOnline() ? 1 : 0);
                mplew.write(c.getMembers().size());
                for (MaplePartyCharacter ch : c.getMembers()) {
                    mplew.writeInt(ch.getId());
                    mplew.writeMapleAsciiString(ch.getName());
                    mplew.writeShort(ch.getJobId());
                    mplew.write(ch.getLevel());
                    mplew.write(ch.isOnline() ? 1 : 0);
                }
            }
            return mplew.getPacket();
        }
    }

    public static class GuildPacket {

        public static byte[] guildInvite(int gid, String charName, int levelFrom, int jobFrom) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x05);
            mplew.writeInt(gid);
            mplew.writeMapleAsciiString(charName);
            return mplew.getPacket();
        }

        public static byte[] showGuildInfo(MapleCharacter c) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x1A);
            if ((c == null) || (c.getMGC() == null)) {
                mplew.write(0);
                return mplew.getPacket();
            }
            MapleGuild g = World.Guild.getGuild(c.getGuildId());
            if (g == null) {
                mplew.write(0);
                return mplew.getPacket();
            }
            mplew.write(1);
            getGuildInfo(mplew, g);

            return mplew.getPacket();
        }

        public static void getGuildInfo(MaplePacketLittleEndianWriter mplew, MapleGuild guild) {
            mplew.writeInt(guild.getId());
            mplew.writeMapleAsciiString(guild.getName());
            for (int i = 1; i <= 5; i++) {
                mplew.writeMapleAsciiString(guild.getRankTitle(i));
            }
            guild.addMemberData(mplew);
            mplew.writeInt(guild.getCapacity());
            mplew.writeShort(guild.getLogoBG());
            mplew.write(guild.getLogoBGColor());
            mplew.writeShort(guild.getLogo());
            mplew.write(guild.getLogoColor());
            mplew.writeMapleAsciiString(guild.getNotice());
            mplew.writeInt(guild.getGP());
            mplew.writeInt(guild.getAllianceId() > 0 ? guild.getAllianceId() : 0);
        }

        public static byte[] newGuildInfo(MapleCharacter c) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(38);
            if ((c == null) || (c.getMGC() == null)) {
                return genericGuildMessage((byte) 37);
            }
            MapleGuild g = World.Guild.getGuild(c.getGuildId());
            if (g == null) {
                return genericGuildMessage((byte) 37);
            }
            getGuildInfo(mplew, g);

            return mplew.getPacket();
        }

        public static byte[] newGuildMember(MapleGuildCharacter mgc) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x27);
            mplew.writeInt(mgc.getGuildId());
            mplew.writeInt(mgc.getId());
            mplew.writeAsciiString(mgc.getName(), 13);
            mplew.writeInt(mgc.getJobId());
            mplew.writeInt(mgc.getLevel());
            mplew.writeInt(mgc.getGuildRank());
            mplew.writeInt(mgc.isOnline() ? 1 : 0);
            mplew.writeInt(mgc.getAllianceRank());
            mplew.writeInt(mgc.getGuildContribution());

            return mplew.getPacket();
        }

        public static byte[] memberLeft(MapleGuildCharacter mgc, boolean bExpelled) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(bExpelled ? 0x2F : 0x2C);
            mplew.writeInt(mgc.getGuildId());
            mplew.writeInt(mgc.getId());
            mplew.writeMapleAsciiString(mgc.getName());

            return mplew.getPacket();
        }

        public static byte[] guildDisband(int gid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x32);
            mplew.writeInt(gid);
            mplew.write(1);

            return mplew.getPacket();
        }

        public static byte[] guildCapacityChange(int gid, int capacity) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x3A);
            mplew.writeInt(gid);
            mplew.write(capacity);

            return mplew.getPacket();
        }

        public static byte[] guildContribution(int gid, int cid, int c) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(72);
            mplew.writeInt(gid);
            mplew.writeInt(cid);
            mplew.writeInt(c);

            return mplew.getPacket();
        }

        public static byte[] changeRank(MapleGuildCharacter mgc) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x40);
            mplew.writeInt(mgc.getGuildId());
            mplew.writeInt(mgc.getId());
            mplew.write(mgc.getGuildRank());

            return mplew.getPacket();
        }

        public static byte[] rankTitleChange(int gid, String[] ranks) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x3E);
            mplew.writeInt(gid);
            for (String r : ranks) {
                mplew.writeMapleAsciiString(r);
            }

            return mplew.getPacket();
        }

        public static byte[] guildEmblemChange(int gid, short bg, byte bgcolor, short logo, byte logocolor) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x42);
            mplew.writeInt(gid);
            mplew.writeShort(bg);
            mplew.write(bgcolor);
            mplew.writeShort(logo);
            mplew.write(logocolor);

            return mplew.getPacket();
        }

        public static byte[] updateGP(int gid, int GP, int glevel) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x48);
            mplew.writeInt(gid);
            mplew.writeInt(GP);
            //mplew.writeInt(glevel);

            return mplew.getPacket();
        }

        public static byte[] guildNotice(int gid, String notice) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x44);
            mplew.writeInt(gid);
            mplew.writeMapleAsciiString(notice);

            return mplew.getPacket();
        }

        public static byte[] guildMemberLevelJobUpdate(MapleGuildCharacter mgc) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x3C);
            mplew.writeInt(mgc.getGuildId());
            mplew.writeInt(mgc.getId());
            mplew.writeInt(mgc.getLevel());
            mplew.writeInt(mgc.getJobId());

            return mplew.getPacket();
        }

        public static byte[] guildMemberOnline(int gid, int cid, boolean bOnline) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x3D);
            mplew.writeInt(gid);
            mplew.writeInt(cid);
            mplew.write(bOnline ? 1 : 0);

            return mplew.getPacket();
        }

        public static byte[] showGuildRanks(int npcid, List<MapleGuildRanking.GuildRankingInfo> all) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x49);
            mplew.writeInt(npcid);
            mplew.writeInt(all.size());
            for (MapleGuildRanking.GuildRankingInfo info : all) {
                mplew.writeMapleAsciiString(info.getName());
                mplew.writeInt(info.getGP());
                mplew.writeInt(info.getLogo());
                mplew.writeInt(info.getLogoColor());
                mplew.writeInt(info.getLogoBg());
                mplew.writeInt(info.getLogoBgColor());
            }

            return mplew.getPacket();
        }

        public static byte[] guildSkillPurchased(int gid, int sid, int level, long expiration, String purchase, String activate) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(85);
            mplew.writeInt(gid);
            mplew.writeInt(sid);
            mplew.writeShort(level);
            mplew.writeLong(PacketHelper.getTime(expiration));
            mplew.writeMapleAsciiString(purchase);
            mplew.writeMapleAsciiString(activate);

            return mplew.getPacket();
        }

        public static byte[] guildLeaderChanged(int gid, int oldLeader, int newLeader, int allianceId) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(89);
            mplew.writeInt(gid);
            mplew.writeInt(oldLeader);
            mplew.writeInt(newLeader);
            mplew.write(1);
            mplew.writeInt(allianceId);

            return mplew.getPacket();
        }

        public static byte[] denyGuildInvitation(String charname) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x37);
            mplew.writeMapleAsciiString(charname);

            return mplew.getPacket();
        }

        public static byte[] genericGuildMessage(byte code) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(code);//30 = cant find in ch

            return mplew.getPacket();
        }

        public static byte[] BBSThreadList(List<MapleBBSThread> bbs, int start) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BBS_OPERATION.getValue());
            mplew.write(6);
            if (bbs == null) {
                mplew.write(0);
                mplew.writeLong(0L);
                return mplew.getPacket();
            }
            int threadCount = bbs.size();
            MapleBBSThread notice = null;
            for (MapleBBSThread b : bbs) {
                if (b.isNotice()) {
                    notice = b;
                    break;
                }
            }
            mplew.write(notice == null ? 0 : 1);
            if (notice != null) {
                addThread(mplew, notice);
            }
            if (threadCount < start) {
                start = 0;
            }
            mplew.writeInt(threadCount);
            int pages = Math.min(10, threadCount - start);
            mplew.writeInt(pages);
            for (int i = 0; i < pages; i++) {
                addThread(mplew, bbs.get(start + i));
            }

            return mplew.getPacket();
        }

        private static void addThread(MaplePacketLittleEndianWriter mplew, MapleBBSThread rs) {
            mplew.writeInt(rs.localthreadID);
            mplew.writeInt(rs.ownerID);
            mplew.writeMapleAsciiString(rs.name);
            mplew.writeLong(PacketHelper.getKoreanTimestamp(rs.timestamp));
            mplew.writeInt(rs.icon);
            mplew.writeInt(rs.getReplyCount());
        }

        public static byte[] showThread(MapleBBSThread thread) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BBS_OPERATION.getValue());
            mplew.write(7);
            mplew.writeInt(thread.localthreadID);
            mplew.writeInt(thread.ownerID);
            mplew.writeLong(PacketHelper.getKoreanTimestamp(thread.timestamp));
            mplew.writeMapleAsciiString(thread.name);
            mplew.writeMapleAsciiString(thread.text);
            mplew.writeInt(thread.icon);
            mplew.writeInt(thread.getReplyCount());
            for (MapleBBSThread.MapleBBSReply reply : thread.replies.values()) {
                mplew.writeInt(reply.replyid);
                mplew.writeInt(reply.ownerID);
                mplew.writeLong(PacketHelper.getKoreanTimestamp(reply.timestamp));
                mplew.writeMapleAsciiString(reply.content);
            }

            return mplew.getPacket();
        }
    }

    public static class InfoPacket {

        public static byte[] showMesoGain(int gain, boolean inChat) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            if (!inChat) {
                mplew.write(0);
                mplew.write(1);
                mplew.write(0);
                mplew.writeInt(gain);
                mplew.writeShort(0);
            } else {
                mplew.write(5);
                mplew.writeInt(gain);
                mplew.writeShort(0);
            }

            return mplew.getPacket();
        }

        public static byte[] getShowInventoryStatus(int mode) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(0);
            mplew.write(mode);
            mplew.writeInt(0);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] getShowItemGain(int itemId, short quantity) {
            return getShowItemGain(itemId, quantity, false);
        }

        public static byte[] getShowItemGain(int itemId, short quantity, boolean inChat) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            if (inChat) {
                mplew.writeShort(SendPacketOpcode.SHOW_SPECIAL_EFFECT.getValue());
                mplew.write(3);
                mplew.write(1);
                mplew.writeInt(itemId);
                mplew.writeInt(quantity);
            } else {
                mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
                mplew.writeShort(0);
                mplew.writeInt(itemId);
                mplew.writeInt(quantity);
            }

            return mplew.getPacket();
        }

        public static byte[] updateQuest(MapleQuestStatus quest) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(1);
            mplew.writeShort(quest.getQuest().getId());
            mplew.write(quest.getStatus());
            switch (quest.getStatus()) {
                case 0:
                    mplew.write(0);
                    break;
                case 1:
                    mplew.writeMapleAsciiString(quest.getCustomData() != null ? quest.getCustomData() : "");
                    break;
                case 2:
                    mplew.writeLong(KoreanDateUtil.getFileTimestamp(System.currentTimeMillis()));
            }

            return mplew.getPacket();
        }

        public static byte[] updateQuestMobKills(MapleQuestStatus status) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(1);
            mplew.writeShort(status.getQuest().getId());
            mplew.write(1);
            StringBuilder sb = new StringBuilder();
            for (Iterator<?> i$ = status.getMobKills().values().iterator(); i$.hasNext();) {
                int kills = ((Integer) i$.next());
                sb.append(StringUtil.getLeftPaddedStr(String.valueOf(kills), '0', 3));
            }
            mplew.writeMapleAsciiString(sb.toString());
            //mplew.writeLong(0L);//removed v146

            return mplew.getPacket();
        }

        public static byte[] itemExpired(int itemid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(2);
            mplew.writeInt(itemid);

            return mplew.getPacket();
        }

        public static byte[] GainEXP_Monster(int gain, boolean white, int partyinc, int Class_Bonus_EXP, int Equipment_Bonus_EXP, int Premium_Bonus_EXP, int Marriage_Bonus_EXP, int Team_Bonus_Rate, int Activity_Bonus_EXP) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(3);// 类别：经验
            mplew.write(white ? 1 : 0);
            mplew.writeInt(gain);// 获得经验
            byte v2 = 0;
            mplew.write(v2);//是否为任务
            mplew.writeInt(Activity_Bonus_EXP);//活动奖励
            byte v6 = 0;
            mplew.write(0);//事件百分比
            mplew.write(Team_Bonus_Rate);//组队经验百分比
            mplew.writeInt(Marriage_Bonus_EXP);//婚礼额外经验

            if (v6 > 0) {
                mplew.write(0);
            }
            if (v2 > 0) {
                byte v2a = 0;
                mplew.write(v2a);
                if (v2a > 0) {
                    byte v2aa = 0;
                    mplew.write(v2aa);
                }

            }

            mplew.write(0);//派对奖金事件率
            
            mplew.writeInt(partyinc);// 组队经验
            mplew.writeInt(Equipment_Bonus_EXP);// 道具佩戴经验
            mplew.writeInt(0);//网吧经验

            return mplew.getPacket();
        }

        public static byte[] GainEXP_Others(int gain, boolean inChat, boolean white) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(3);
            mplew.write(white ? 1 : 0);
            mplew.writeInt(gain);
            byte v2 = 0;
            mplew.write(v2);//是否为任务
            mplew.writeInt(0);//活动奖励
            byte v6 = 0;
            mplew.write(0);//事件百分比
            mplew.write(0);//组队经验百分比
            mplew.writeInt(0);//婚礼额外经验

            if (v6 > 0) {
                mplew.write(0);
            }
            if (v2 > 0) {
                byte v2a = 0;
                mplew.write(v2a);
                if (v2a > 0) {
                    byte v2aa = 0;
                    mplew.write(v2aa);
                }

            }

            mplew.write(0);//派对奖金事件率
            mplew.writeInt(0);
            mplew.writeInt(/*
                     * partyinc
                     */0);

            mplew.writeInt(0);//网吧经验
            mplew.writeInt(0);//彩虹周奖励经验

            return mplew.getPacket();
        }

        public static byte[] getSPMsg(byte sp, short job) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(4);
            mplew.writeShort(job);
            mplew.write(sp);

            return mplew.getPacket();
        }

        public static byte[] getShowFameGain(int gain) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(4);
            mplew.writeInt(gain);

            return mplew.getPacket();
        }

        public static byte[] getGPMsg(int itemid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(7);
            mplew.writeInt(itemid);

            return mplew.getPacket();
        }

        public static byte[] getGPContribution(int itemid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(6);
            mplew.writeInt(itemid);

            return mplew.getPacket();
        }

        public static byte[] getStatusMsg(int itemid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(7);
            mplew.writeInt(itemid);

            return mplew.getPacket();
        }

        public static byte[] showInfo(String info) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(9);
            mplew.writeMapleAsciiString(info);

            return mplew.getPacket();
        }

        public static byte[] updateInfoQuest(int quest, String data) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(10);
            mplew.writeShort(quest);
            mplew.writeMapleAsciiString(data);

            //System.err.println("infoquest " + mplew.toString());
            return mplew.getPacket();
        }

        public static byte[] showItemReplaceMessage(List<String> message) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(12);
            mplew.write(message.size());
            for (String x : message) {
                mplew.writeMapleAsciiString(x);
            }

            return mplew.getPacket();
        }

        public static byte[] showTraitGain(MapleTrait.MapleTraitType trait, int amount) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(14);
            mplew.writeLong(trait.getStat().getValue());
            mplew.writeInt(amount);

            return mplew.getPacket();
        }

        public static byte[] showTraitMaxed(MapleTrait.MapleTraitType trait) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(15);
            mplew.writeLong(trait.getStat().getValue());

            return mplew.getPacket();
        }

        public static byte[] getBPMsg(int amount) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(19);
            mplew.writeInt(amount);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] showExpireMessage(byte type, List<Integer> item) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(4 + item.size() * 4);

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(type);
            mplew.write(item.size());
            for (Integer it : item) {
                mplew.writeInt(it);
            }

            return mplew.getPacket();
        }

        public static byte[] showStatusMessage(int mode, String info, String data) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(mode);
            if (mode == 22) {
                mplew.writeMapleAsciiString(info);
                mplew.writeMapleAsciiString(data);
            }

            return mplew.getPacket();
        }

        public static byte[] showReturnStone(int act) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(21);
            mplew.write(act);

            return mplew.getPacket();
        }

        public static byte[] showStatus(int IDK) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());

            mplew.write(3);
            mplew.write(1);
            mplew.writeInt(IDK);
            mplew.writeLong(0);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] showItemBox() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(1);
            mplew.write(0x7A);
            mplew.write(0x1C);
            mplew.write(1);
            mplew.write(1);
            mplew.write(0);
            mplew.write(0x30);
            return mplew.getPacket();
        }

        public static byte[] getShowCoreGain(int core, int quantity) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(28);
            mplew.write(22);
            mplew.writeInt(core);
            mplew.writeInt(quantity);

            return mplew.getPacket();
        }
    }

    public static class BuffPacket {

        public static byte[] giveDice(int buffid, int skillid, int duration, Map<MapleBuffStat, Integer> statups) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeBuffMask(mplew, statups);

            mplew.writeShort(Math.max(buffid / 100, Math.max(buffid / 10, buffid % 10))); // 1-6

            mplew.writeInt(skillid); // skillid
            mplew.writeInt(duration);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.writeInt(GameConstants.getDiceStat(buffid, 3));
            mplew.writeInt(GameConstants.getDiceStat(buffid, 3));
            mplew.writeInt(GameConstants.getDiceStat(buffid, 4));
            mplew.writeZeroBytes(20); //idk
            mplew.writeInt(GameConstants.getDiceStat(buffid, 2));
            mplew.writeZeroBytes(12); //idk
            mplew.writeInt(GameConstants.getDiceStat(buffid, 5));
            mplew.writeZeroBytes(16); //idk
            mplew.writeInt(GameConstants.getDiceStat(buffid, 6));
            mplew.writeZeroBytes(16);
            mplew.writeZeroBytes(6);//new 143
            mplew.writeInt(1000);//new 143
            mplew.write(1);
            mplew.writeInt(0);//new143
//            mplew.write(4); // Total buffed times
//            mplew.write(0);//v112
            return mplew.getPacket();
        }

        public static byte[] giveHoming(int skillid, int mobid, int x) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.HOMING_BEACON);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.writeInt(1);
            mplew.writeLong(skillid);
            mplew.write(0);
            mplew.writeLong(mobid);
            mplew.writeShort(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(0);//v112
            return mplew.getPacket();
        }

        public static byte[] giveMount(int buffid, int skillid, Map<MapleBuffStat, Integer> statups) {
            return showMonsterRiding(-1, statups, buffid, skillid);
        }

        public static byte[] showMonsterRiding(int cid, Map<MapleBuffStat, Integer> statups, int buffid, int skillId) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            if (cid == -1) {
                mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            } else {
                mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
                mplew.writeInt(cid);
            }
            PacketHelper.writeBuffMask(mplew, statups);
            mplew.writeShort(0);
            mplew.writeInt(buffid);
            mplew.writeInt(skillId);
            mplew.writeInt(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(2);
            return mplew.getPacket();
        }

        public static byte[] givePirate(Map<MapleBuffStat, Integer> statups, int duration, int skillid) {
            return giveForeignPirate(statups, duration, -1, skillid);
        }

        public static byte[] giveForeignPirate(Map<MapleBuffStat, Integer> statups, int duration, int cid, int skillid) {
            final boolean infusion = skillid == 5121009 || skillid == 15111005;
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            if (cid == -1) {
                mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            } else {
                mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
                mplew.writeInt(cid);
            }
            PacketHelper.writeBuffMask(mplew, statups);
            mplew.writeShort(0);
            //   mplew.write(0);
            for (Integer stat : statups.values()) {
                mplew.writeInt(stat);
                mplew.writeLong(skillid);
                mplew.writeZeroBytes(infusion ? 6 : 1);
                mplew.writeShort(duration);//duration... seconds
            }
            mplew.writeShort(0);
            mplew.writeShort(0);
            mplew.write(1);
            mplew.write(1);
            return mplew.getPacket();
        }

        public static byte[] giveArcane(int skillid, Map<Integer, Integer> statups) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.MANY_USES);
            mplew.writeShort(statups.size());
            mplew.writeInt(skillid);
            mplew.writeInt(5000);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(0);
            mplew.writeZeroBytes(9);
            return mplew.getPacket();
        }

        public static byte[] giveEnergyChargeTest(int bar, int bufflength) {
            return giveEnergyChargeTest(-1, bar, bufflength);
        }

        public static byte[] giveEnergyChargeTest(int cid, int bar, int bufflength) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            if (cid == -1) {
                mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            } else {
                mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
                mplew.writeInt(cid);
            }
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.ENERGY_CHARGE);

            mplew.writeShort(0);
            mplew.writeInt(Math.min(bar, 10000));
            mplew.writeLong(0);
            mplew.write(0);
            mplew.writeInt(bar >= 10000 ? bufflength : 0);
            return mplew.getPacket();
        }

        public static byte[] giveBuff(int buffid, int bufflength, Map<MapleBuffStat, Integer> statups, MapleStatEffect effect) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeBuffMask(mplew, statups);//48
            boolean stacked = false;
            boolean isAura = false;
            for (Map.Entry<MapleBuffStat, Integer> stat : statups.entrySet()) {
                isAura = GameConstants.isAuraBuff(stat.getKey());
                if (!stat.getKey().canStack()) {
                    boolean specialBuff = GameConstants.isSpecialBuff(stat.getKey());
                    if (specialBuff) {
                        mplew.writeInt(stat.getValue());
                    } else {
                        mplew.writeShort(stat.getValue());
                    }
                    mplew.writeInt(buffid);
                    mplew.writeInt(bufflength);
                    if (stat.getKey() == MapleBuffStat.HOLY_SHIELD) {
                        mplew.writeInt(0);
                    }
                }
            }
            for (Map.Entry<MapleBuffStat, Integer> stat : statups.entrySet()) {
                if (stat.getKey().canStack()) {
                    if (!stacked) {
                        mplew.writeZeroBytes(5);
                        mplew.writeZeroBytes(4); //new v143?
                        if (GameConstants.isSpecialStackBuff(stat.getKey())) {
                            mplew.writeZeroBytes(1); //not sure where this part comes
                        }
                        stacked = true;
                    }

                    mplew.writeInt(1); //amount of the same buffstat
                    //for each of the same buffstats:
                    mplew.writeInt(buffid);
                    mplew.writeInt(stat.getValue());
                    mplew.writeInt(Integer.MAX_VALUE); //some kind of time
                    mplew.writeInt(0);
                    mplew.writeInt(bufflength);
                }
            }
            if (!isAura) {
                mplew.writeShort(0);
                if (effect != null) {
                    if (effect.isDivineShield()) {
                        mplew.writeInt(effect.getEnhancedWatk());
                    } else if (effect.getCharColor() > 0) {
                        mplew.writeInt(effect.getCharColor());
                    } else if (effect.isInflation()) {
                        mplew.writeInt(effect.getInflation());
                    }
                }
            }
            mplew.writeShort(0);

            mplew.writeZeroBytes(69); //make sure no dc incase not enough length

            //new v142
            mplew.writeShort(1);
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] giveBuff2(int buffid, int bufflength, Map<MapleBuffStat, Integer> statups, MapleStatEffect effect) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeBuffMask(mplew, statups);//48
            boolean stacked = false;
            boolean isAura = false;
            boolean isCombo = false;
            int stackLength = 0;
            for (Map.Entry<MapleBuffStat, Integer> stat : statups.entrySet()) {
                if ((stat.getKey()).canStack()) {
                    if (stat.getKey() != MapleBuffStat.DAMAGE_CAP_INCREASE) {
                        stackLength++;
                    }
                }
            }
            for (Map.Entry<MapleBuffStat, Integer> stat : statups.entrySet()) {
                isAura = GameConstants.isAuraBuff(stat.getKey());
                if (stat.getKey() == MapleBuffStat.COMBO) {
                    isCombo = true;
                }
                if ((stat.getKey()).canStack()) {
                    if (!stacked) {
                        mplew.writeZeroBytes(4);
                        mplew.write(0);
                        boolean specialStackBuff = GameConstants.isSpecialStackBuff(stat.getKey());
                        if (specialStackBuff) {
                            mplew.writeZeroBytes(1); //not sure where this part comes
                        }
                        if (stackLength > 0) {
                            //mplew.writeInt(stackLength); //stack length
                        }
                        stacked = true;
                    }
                    //if (stat.getKey() == MapleBuffStat.DAMAGE_CAP_INCREASE) {
                    mplew.writeInt(1);
                    //}
                    mplew.writeInt(buffid);
                    if (buffid != 23111004) {
                        mplew.writeInt(bufflength);
                    }
                    if (!GameConstants.isValuelessBuff(buffid)) {
                        if (buffid == 2321005) {//advanced bless
                            mplew.writeInt(stat.getValue());
                            mplew.writeInt(240000);
                        } else {
                            mplew.writeInt(stat.getValue());
                            mplew.writeInt(262643804); //unknown 
                        }
                    }
                    if (stat.getKey() == MapleBuffStat.DAMAGE_CAP_INCREASE) {
                        mplew.writeInt(1000);
                    }
                } else {
                    boolean specialBuff = GameConstants.isSpecialBuff(stat.getKey());
                    if (specialBuff) {
                        mplew.writeInt(stat.getValue());
                    } else {
                        if (buffid == 61101002 || buffid == 61120007) {
                            mplew.writeShort(stat.getValue() % 10);
                        } else {
                            mplew.writeShort(stat.getValue());
                        }
                    }
                    mplew.writeInt(buffid);
                    mplew.writeInt(bufflength);
                }
                if (buffid == 31011001) {// Overload Release?? idk
                    mplew.writeInt(effect.getDuration());
                }
                if (statups.containsKey(MapleBuffStat.TEMPEST_BLADES)) {
                    mplew.writeZeroBytes(5);
                    mplew.writeInt(buffid == 61101002 ? 1 : 2);
                    mplew.writeInt(buffid == 61101002 ? 3 : 5);
                    mplew.writeInt(stat.getValue() / 100); //weapon
                    mplew.writeInt(buffid == 61101002 ? 3 : 5);
                    if (buffid == 61120007) {
                        mplew.writeZeroBytes(8);
                    }
                }
            }
            if (buffid == 24121004) {// Priere D'Aria
                mplew.writeZeroBytes(3);
                mplew.writeShort(0);
                mplew.write(0);
            }
            if (!isAura) {
                if (buffid == 23111004) {//ignis roar
                    mplew.write(0);
                    mplew.writeShort(750);
                } else {
                    mplew.writeShort(0);
                    if (effect != null) {
                        if (effect.isDivineShield()) {
                            mplew.writeInt(effect.getEnhancedWatk());
                        } else if (effect.getCharColor() > 0) {
                            mplew.writeInt(effect.getCharColor());
                        } else if (effect.isInflation()) {
                            mplew.writeInt(effect.getInflation());
                        }
                    }
                }
            }
            if (buffid == 23111004) {//ignis roar
                mplew.write(0);
                mplew.writeShort(1000);
            } else {
                mplew.writeShort(0);

            }

            if (statups.containsKey(MapleBuffStat.MAPLE_WARRIOR)) {
                mplew.write(HexTool.getByteArrayFromHexString("00 E8 03 00 00 00 13 00 00 00 00"));
                return mplew.getPacket();
            }

            if (statups.containsKey(MapleBuffStat.TEMPEST_BLADES)) { //this too
                mplew.writeShort(0);
            }
            if (statups.containsKey(MapleBuffStat.KAISER_COMBO)) { //this too
                mplew.writeZeroBytes(8);
            }

            if (true) { //this part doesnt always come
                if (buffid != 23111004) {//ignis roar 
                    mplew.writeShort(0);
                }
                mplew.write(0);
                mplew.write(0);
                if (buffid == 2311007 || buffid == 2111007 || buffid == 2211007 || buffid == 23111004 || buffid == 1201007) {//teleport mastery
                    mplew.write(1);
                } else {
                    mplew.write(effect != null && effect.isShadow());
                }
                if (isAura || statups.containsKey(MapleBuffStat.TEMPEST_BLADES)) {
                    mplew.writeInt(0);
                }
            }

            //new v142
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);
            mplew.writeZeroBytes(30);
//            System.err.println("\u001B[34m buff " + mplew.toString());
            return mplew.getPacket();
        }

        public static byte[] giveDebuff(MapleDisease statups, int x, int skillid, int level, int duration) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeSingleMask(mplew, statups);
            mplew.writeShort(x);
            mplew.writeShort(skillid);
            mplew.writeShort(level);
            mplew.writeInt(duration);
            mplew.write(0);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.writeZeroBytes(30);
            return mplew.getPacket();
        }

        public static byte[] cancelBuff(List<MapleBuffStat> statups) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_BUFF.getValue());

            PacketHelper.writeMask(mplew, statups);
            for (MapleBuffStat z : statups) {
                if (z.canStack()) {
                    mplew.writeInt(0); //amount of buffs still in the stack? dunno mans
                }
            }
            mplew.write(3);
            mplew.write(1);
            mplew.writeLong(0);
            mplew.writeLong(0);
            mplew.writeLong(0);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] cancelDebuff(MapleDisease mask) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_BUFF.getValue());

            PacketHelper.writeSingleMask(mplew, mask);
            mplew.write(3);
            mplew.write(1);
            mplew.writeLong(0);
            mplew.write(0);//v112
            return mplew.getPacket();
        }

        public static byte[] cancelHoming() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_BUFF.getValue());

            PacketHelper.writeSingleMask(mplew, MapleBuffStat.HOMING_BEACON);
            mplew.write(0);//v112

            return mplew.getPacket();
        }

//public static byte[] giveForeignBuff(int cid, Map<MapleBuffStat, Integer> statups, MapleStatEffect effect) {
        //MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
// mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
        //mplew.writeInt(cid);
// PacketHelper.writeBuffMask(mplew, statups);
        //for (Map.Entry statup : statups.entrySet()) {
// if ((statup.getKey() == MapleBuffStat.SHADOWPARTNER) || (statup.getKey() == MapleBuffStat.MECH_CHANGE) || (statup.getKey() == MapleBuffStat.DARK_AURA) || (statup.getKey() == MapleBuffStat.YELLOW_AURA) || (statup.getKey() == MapleBuffStat.BLUE_AURA) || (statup.getKey() == MapleBuffStat.GIANT_POTION) || (statup.getKey() == MapleBuffStat.SPIRIT_LINK) || (statup.getKey() == MapleBuffStat.PYRAMID_PQ) || (statup.getKey() == MapleBuffStat.WK_CHARGE) || (statup.getKey() == MapleBuffStat.SPIRIT_SURGE) || (statup.getKey() == MapleBuffStat.MORPH) || (statup.getKey() == MapleBuffStat.DARK_METAMORPHOSIS)) {
        //mplew.writeShort(((Integer)statup.getValue()).shortValue());
        //mplew.writeInt(effect.isSkill() ? effect.getSourceId() : -effect.getSourceId());
// } else if (statup.getKey() == MapleBuffStat.FAMILIAR_SHADOW) {
// mplew.writeInt(((Integer)statup.getValue()).intValue());
/// mplew.writeInt(effect.getCharColor());
// } else {
        //mplew.writeShort(((Integer)statup.getValue()).shortValue());
// }
// /}
// mplew.writeShort(0);
        //mplew.writeShort(0);
        //mplew.write(1);
        //mplew.write(1);
        //return mplew.getPacket();
// }
        public static byte[] giveAriaBuff(Map<MapleBuffStat, Integer> statups, int bufflevel, int buffid, int bufflength) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeBuffMask(mplew, statups);
            // mplew.write(HexTool.getByteArrayFromHexString("00 00 00 00 00 00 00 80 00 00 00 00 00 00 00 00 00 00 40 00 00 00 00 00 00 00 00 00 00 00 00 00"));         
            for (Map.Entry stat : statups.entrySet()) {
                mplew.writeShort(((Integer) stat.getValue()));
                mplew.writeInt(buffid);
                mplew.writeInt(bufflength);
            }
            mplew.writeZeroBytes(3);
            mplew.writeShort(0); // not sure..
            mplew.write(0);
            mplew.writeShort(0);
            return mplew.getPacket();
        }

        public static byte[] giveForeignBuff(int cid, Map<MapleBuffStat, Integer> statups, MapleStatEffect effect) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
            mplew.writeInt(cid);

            PacketHelper.writeBuffMask(mplew, statups);
            for (Entry<MapleBuffStat, Integer> statup : statups.entrySet()) {
                if (statup.getKey() == MapleBuffStat.SHADOWPARTNER || statup.getKey() == MapleBuffStat.MECH_CHANGE || statup.getKey() == MapleBuffStat.DARK_AURA || statup.getKey() == MapleBuffStat.YELLOW_AURA || statup.getKey() == MapleBuffStat.BLUE_AURA || statup.getKey() == MapleBuffStat.GIANT_POTION || statup.getKey() == MapleBuffStat.SPIRIT_LINK || statup.getKey() == MapleBuffStat.PYRAMID_PQ || statup.getKey() == MapleBuffStat.WK_CHARGE || statup.getKey() == MapleBuffStat.DAMAGE_R || statup.getKey() == MapleBuffStat.MORPH || statup.getKey() == MapleBuffStat.WATER_SHIELD || statup.getKey() == MapleBuffStat.DARK_METAMORPHOSIS) {
                    mplew.writeShort(statup.getValue().shortValue());
                    mplew.writeInt(effect.isSkill() ? effect.getSourceId() : -effect.getSourceId());
                } else if (statup.getKey() == MapleBuffStat.FAMILIAR_SHADOW) {
                    mplew.writeInt(statup.getValue());
                    mplew.writeInt(effect.getCharColor());
                } else {
                    mplew.writeShort(statup.getValue().shortValue());
                }
            }
            mplew.writeShort(1);
            mplew.write(0);
            mplew.writeInt(2);
            mplew.writeZeroBytes(13);
            mplew.writeShort(600);

            mplew.writeZeroBytes(20);
            /*
             * mplew.writeShort(0); mplew.writeShort(0); mplew.write(1);
             * mplew.write(1);
             mplew.write(0);
             *///v140
            return mplew.getPacket();
        }

        public static byte[] giveForeignDebuff(int cid, final MapleDisease statups, int skillid, int level, int x) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
            mplew.writeInt(cid);

            PacketHelper.writeSingleMask(mplew, statups);
            if (skillid == 125) {
                mplew.writeShort(0);
                mplew.write(0); //todo test
            }
            mplew.writeShort(x);
            mplew.writeShort(skillid);
            mplew.writeShort(level);
            mplew.writeShort(0); // same as give_buff
            mplew.writeShort(0); //Delay
            mplew.write(1);
            mplew.write(1);
            mplew.write(0);//v112
            mplew.writeZeroBytes(20);
            return mplew.getPacket();
        }

        public static byte[] cancelForeignBuff(int cid, List<MapleBuffStat> statups) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_FOREIGN_BUFF.getValue());
            mplew.writeInt(cid);
            PacketHelper.writeMask(mplew, statups);//36 bytes
//            mplew.write(3);
            mplew.write(1);
//            mplew.write(0);//v112

            return mplew.getPacket();
        }

        public static byte[] cancelForeignDebuff(int cid, MapleDisease mask) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_FOREIGN_BUFF.getValue());
            mplew.writeInt(cid);

            PacketHelper.writeSingleMask(mplew, mask);//48 bytes
            //mplew.write(3);
            mplew.write(1);
            //mplew.write(0);//v112
            return mplew.getPacket();
        }

        public static byte[] giveCard(int cid, int oid, int skillid) {
            MaplePacketLittleEndianWriter writer = new MaplePacketLittleEndianWriter();
            writer.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
            writer.write(0);
            writer.writeInt(cid);
            writer.writeInt(1);
            writer.writeInt(oid);
            writer.writeInt(skillid);
            writer.write(1);
            writer.writeInt(2);
            writer.writeInt(1);
            writer.writeInt(21);
            writer.writeInt(8);
            writer.writeInt(8);
            writer.write(0);
            return writer.getPacket();
        }
    }

    public static class InventoryPacket {

        public static byte[] addInventorySlot(MapleInventoryType type, Item item) {
            return addInventorySlot(type, item, false);
        }

        public static byte[] addInventorySlot(MapleInventoryType type, Item item, boolean fromDrop) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(fromDrop ? 1 : 0);
            mplew.write(1);
            mplew.write(0);
            mplew.write(type.getType());
            mplew.writeShort(item.getPosition());
            PacketHelper.addItemInfo(mplew, item);
            return mplew.getPacket();
        }

        public static byte[] updateInventorySlot(MapleInventoryType type, Item item, boolean fromDrop) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(fromDrop ? 1 : 0);
            mplew.write(1);
            mplew.write(1);
            mplew.write(type.getType());
            mplew.writeShort(item.getPosition());
            mplew.writeShort(item.getQuantity());

            return mplew.getPacket();
        }

        public static byte[] moveInventoryItem(MapleInventoryType type, short src, short dst, boolean bag, boolean bothBag) {
            return moveInventoryItem(type, src, dst, (byte) -1, bag, bothBag);
        }

        public static byte[] moveInventoryItem(MapleInventoryType type, short src, short dst, short equipIndicator, boolean bag, boolean bothBag) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(1);
            mplew.write(2);
            mplew.write(type.getType());
            mplew.writeShort(src);
            mplew.writeShort(dst);
            if (equipIndicator != -1) {
                mplew.write(equipIndicator);
            }

            return mplew.getPacket();
        }

        public static byte[] moveAndMergeInventoryItem(MapleInventoryType type, short src, short dst, short total, boolean bag, boolean switchSrcDst, boolean bothBag) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(2);
            mplew.write(3);
            mplew.write(type.getType());
            mplew.writeShort(src);

            mplew.write(1);
            mplew.write(type.getType());
            mplew.writeShort(dst);
            mplew.writeShort(total);

            return mplew.getPacket();
        }

        public static byte[] moveAndMergeWithRestInventoryItem(MapleInventoryType type, short src, short dst, short srcQ, short dstQ, boolean bag, boolean switchSrcDst, boolean bothBag) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(2);
            mplew.write(1);
            mplew.write(type.getType());
            mplew.writeShort(src);
            mplew.writeShort(srcQ);
            mplew.write(1);
            mplew.write(type.getType());
            mplew.writeShort(dst);
            mplew.writeShort(dstQ);

            return mplew.getPacket();
        }

        public static byte[] clearInventoryItem(MapleInventoryType type, short slot, boolean fromDrop) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(fromDrop ? 1 : 0);
            mplew.write(1);
            mplew.write((slot > 100) && (type == MapleInventoryType.ETC) ? 7 : 3);
            mplew.write(type.getType());
            mplew.writeShort(slot);

            return mplew.getPacket();
        }

        public static byte[] updateSpecialItemUse(Item item, byte invType, MapleCharacter chr) {
            return updateSpecialItemUse(item, invType, item.getPosition(), false, chr);
        }

        public static byte[] updateSpecialItemUse(Item item, byte invType, short pos, boolean theShort, MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(0);
            mplew.write(2);
            //mplew.write(0);

            mplew.write(GameConstants.isInBag(pos, invType) ? 7 : 3);
            mplew.write(invType);
            mplew.writeShort(pos);

            mplew.write(0);
            mplew.write(invType);
            if ((item.getType() == 1) || (theShort)) {
                mplew.writeShort(pos);
            } else {
                mplew.write(pos);
            }
            PacketHelper.addItemInfo(mplew, item, chr);
            if (pos < 0) {
                mplew.write(2);
            }

            return mplew.getPacket();
        }

        public static byte[] updateSpecialItemUse_(Item item, byte invType, MapleCharacter chr) {
            return updateSpecialItemUse_(item, invType, item.getPosition(), chr);
        }

        public static byte[] updateSpecialItemUse_(Item item, byte invType, short pos, MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(0);
            mplew.write(1);
            //mplew.write(0);

            mplew.write(0);
            mplew.write(invType);
            if (item.getType() == 1) {
                mplew.writeShort(pos);
            } else {
                mplew.write(pos);
            }
            PacketHelper.addItemInfo(mplew, item, chr);
            if (pos < 0) {
                mplew.write(1);
            }

            return mplew.getPacket();
        }

        public static byte[] updateEquippedItem(MapleCharacter chr, Equip eq, short pos) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(0);
            mplew.write(1);
            //mplew.write(0);

            mplew.write(0);
            mplew.write(1);
            mplew.writeShort(pos);
            PacketHelper.addItemInfo(mplew, eq, chr);

            return mplew.getPacket();
        }

        public static byte[] scrolledItem(Item scroll, MapleInventoryType inv, Item item, boolean destroyed, boolean potential, boolean equipped) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(destroyed ? 2 : 3);
            //mplew.write(0);

            mplew.write(scroll.getQuantity() > 0 ? 1 : 3);
            mplew.write(GameConstants.getInventoryType(scroll.getItemId()).getType());
            mplew.writeShort(scroll.getPosition());
            if (scroll.getQuantity() > 0) {
                mplew.writeShort(scroll.getQuantity());
            }

            mplew.write(3);
            mplew.write(inv.getType());
            mplew.writeShort(item.getPosition());
            if (!destroyed) {
                mplew.write(0);
                mplew.write(inv.getType());
                mplew.writeShort(item.getPosition());
                PacketHelper.addItemInfo(mplew, item);
            }
            if (!potential) {
                mplew.write(1);
            }
            if (equipped) {
                mplew.write(8);
            }

            return mplew.getPacket();
        }

        public static byte[] moveAndUpgradeItem(MapleInventoryType type, Item item, short oldpos, short newpos, MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(3);
            //mplew.write(0);

            // mplew.write(GameConstants.isInBag(newpos, type.getType()) ? 7 : 3);
            mplew.write(type.getType());
            mplew.writeShort(oldpos);

            mplew.write(0);
            mplew.write(1);
            mplew.writeShort(oldpos);
            PacketHelper.addItemInfo(mplew, item, chr);

            mplew.write(2);
            mplew.write(type.getType());
            mplew.writeShort(oldpos);
            mplew.writeShort(newpos);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] dropInventoryItem(MapleInventoryType type, short src) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(1);
            //mplew.write(0);

            mplew.write(3);
            mplew.write(type.getType());
            mplew.writeShort(src);
            if (src < 0) {
                mplew.write(1);
            }

            return mplew.getPacket();
        }

        public static byte[] dropInventoryItemUpdate(MapleInventoryType type, Item item) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(1);
            //mplew.write(0);

            mplew.write(1);
            mplew.write(type.getType());
            mplew.writeShort(item.getPosition());
            mplew.writeShort(item.getQuantity());

            return mplew.getPacket();
        }

        public static byte[] getInventoryFull() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(0);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] getInventoryStatus() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(0);
            mplew.write(0);
            //mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] getSlotUpdate(byte invType, byte newSlots) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_GROW.getValue());
            mplew.write(invType);
            mplew.write(newSlots);

            return mplew.getPacket();
        }

        public static byte[] getShowInventoryFull() {
            return CWvsContext.InfoPacket.getShowInventoryStatus(255);
        }

        public static byte[] showItemUnavailable() {
            return CWvsContext.InfoPacket.getShowInventoryStatus(254);
        }
    }

    public static byte[] updateHyperSp(int mode, int remainSp) {
        return updateSpecialStat("hyper", 0x1C, mode, remainSp);
    }

    public static byte[] updateSpecialStat(String stat, int array, int mode, int amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPECIAL_STAT.getValue());
        mplew.writeMapleAsciiString(stat);
        mplew.writeInt(array);
        mplew.writeInt(mode);
        mplew.write(1);
        mplew.writeInt(amount);

        return mplew.getPacket();
    }

    public static byte[] updateMaplePoint(int mp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MAPLE_POINT.getValue());
        mplew.writeInt(mp);

        return mplew.getPacket();
    }

    public static byte[] updateCrowns(int[] titles) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.EVENT_CROWN.getValue());
        for (int i = 0; i < 5; i++) {
            mplew.writeMapleAsciiString("");
            if (titles.length < i + 1) {
                mplew.write(-1);
            } else {
                mplew.write(titles[i]);
            }
        }

        return mplew.getPacket();
    }

    public static byte[] magicWheel(int type, List<Integer> items, String data, int endSlot) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MAGIC_WHEEL.getValue());
        mplew.write(type);
        switch (type) {
            case 3:
                mplew.write(items.size());
                for (int item : items) {
                    mplew.writeInt(item);
                }
                mplew.writeMapleAsciiString(data); // nexon encrypt the item and then send the string
                mplew.write(endSlot);
                break;
            case 5:
                //<Character Name> got <Item Name>.
                break;
            case 6:
                //You don't have a Magic Gachapon Wheel in your Inventory.
                break;
            case 7:
                //You don't have any Inventory Space.\r\n You must have 2 or more slots available\r\n in each of your tabs.
                break;
            case 8:
                //Please try this again later.
                break;
            case 9:
                //Failed to delete Magic Gachapon Wheel item.
                break;
            case 0xA:
                //Failed to receive Magic Gachapon Wheel item.
                break;
            case 0xB:
                //You cannot move while Magic Wheel window is open.
                break;
        }

        return mplew.getPacket();
    }

    public static class Reward {

        public static byte[] receiveReward(int id, byte mode, int quantity) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.REWARD.getValue());
            mplew.write(mode); // mode
            switch (mode) { // mode
                case 9:
                    mplew.writeInt(0);
                    break;
                case 0x0B:
                    mplew.writeInt(id);
                    mplew.writeInt(quantity); //quantity
                    //Popup: You have received the Maple Points.\r\n( %d maple point )
                    break;
                case 0x0C:
                    mplew.writeInt(id);
                    //Popup You have received the Game item.
                    break;
                case 0x0E:
                    mplew.writeInt(id);
                    mplew.writeInt(quantity); //quantity
                    //Popup: You have received the Mesos.\r\n( %d meso )
                    break;
                case 0x0F:
                    mplew.writeInt(id);
                    mplew.writeInt(quantity); //quantity
                    //Popup: You have received the Exp.\r\n( %d exp )
                    break;
                case 0x14:
                    //Popup: Failed to receive the Maple Points.
                    break;
                case 0x15:
                    mplew.write(0);
                    //Popup: Failed to receive the Game Item.
                    break;
                case 0x16:
                    mplew.write(0);
                    //Popup: Failed to receive the Game Item.
                    break;
                case 0x17:
                    //Popup: Failed to receive the Mesos.
                    break;
                case 0x18:
                    //Popup: Failed to receive the Exp.
                    break;
                case 0x21:
                    mplew.write(0); //66
                    //No inventory space
                    break;
            }

            return mplew.getPacket();
        }

        public static byte[] updateReward(int id, byte mode, List<MapleReward> rewards, int option) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.REWARD.getValue());
            mplew.write(mode); // mode
            switch (mode) { // mode
                case 9:
                    mplew.writeInt(rewards.size());
                    if (rewards.size() > 0) {
                        for (MapleReward reward : rewards) {
                            boolean empty = reward.getId() < 1;
                            mplew.writeInt(empty ? 0 : reward.getId()); // 0 = blank 1+ = gift
                            if (!empty) {
                                if ((option & 1) != 0) {
                                    mplew.writeLong(reward.getReceiveDate()); //start time
                                    mplew.writeLong(reward.getExpireDate()); //end time
                                    mplew.writeLong(reward.getReceiveDate()); //start time
                                    mplew.writeLong(reward.getExpireDate()); //end time
                                }
                                if ((option & 2) != 0) { //nexon do here a3 & 2 when a3 is 9
                                    mplew.writeInt(0);
                                    mplew.writeInt(0);
                                    mplew.writeInt(0);
                                    mplew.writeInt(0);
                                    mplew.writeInt(0);
                                    mplew.writeInt(0);
                                    mplew.writeMapleAsciiString("");
                                    mplew.writeMapleAsciiString("");
                                    mplew.writeMapleAsciiString("");
                                }
                                mplew.writeInt(reward.getType()); //type 3 = maple point 4 = mesos 5 = exp
                                mplew.writeInt(reward.getItem()); // item id
                                mplew.writeInt(/*
                                         * itemQ
                                         */reward.getItem() > 0 ? 1 : 0); // item quantity (?)
                                mplew.writeInt(0);
                                mplew.writeLong(0L);
                                mplew.writeInt(0);
                                mplew.writeInt(reward.getMaplePoints()); // maple point amount
                                mplew.writeInt(reward.getMeso()); // mesos amount
                                mplew.writeInt(reward.getExp()); // exp amount
                                mplew.writeInt(0);
                                mplew.writeInt(0);
                                mplew.writeMapleAsciiString("");
                                mplew.writeMapleAsciiString("");
                                mplew.writeMapleAsciiString("");
                                mplew.writeMapleAsciiString(reward.getDesc());
                            }
                        }
                    }
                    break;
                case 0x0B:
                    mplew.writeInt(id);
                    mplew.writeInt(0); //quantity
                    //Popup: You have received the Maple Points.\r\n( %d maple point )
                    break;
                case 0x0C:
                    mplew.writeInt(id);
                    //Popup You have received the Game item.
                    break;
                case 0x0E:
                    mplew.writeInt(id);
                    mplew.writeInt(0); //quantity
                    //Popup: You have received the Mesos.\r\n( %d meso )
                    break;
                case 0x0F:
                    mplew.writeInt(id);
                    mplew.writeInt(0); //quantity
                    //Popup: You have received the Exp.\r\n( %d exp )
                    break;
                case 0x14:
                    //Popup: Failed to receive the Maple Points.
                    break;
                case 0x15:
                    mplew.write(0);
                    //Popup: Failed to receive the Game Item.
                    break;
                case 0x16:
                    mplew.write(0);
                    //Popup: Failed to receive the Game Item.
                    break;
                case 0x17:
                    //Popup: Failed to receive the Mesos.
                    break;
                case 0x18:
                    //Popup: Failed to receive the Exp.
                    break;
                case 0x21:
                    mplew.write(0); //66
                    //No inventory space
                    break;
            }

            return mplew.getPacket();
        }
    }

    public static byte[] englishQuizMsg(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ENGLISH_QUIZ.getValue());
        mplew.writeInt(20); //?
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] openWeb(String web) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.OPEN_WEB.getValue());
        mplew.writeMapleAsciiString(web);

        return mplew.getPacket();
    }

    public static byte[] showCharCash(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHAR_CASH.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(chr.getCSPoints(2));

        return mplew.getPacket();
    }

    public static byte[] fishingUpdate(byte type, int id) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FISHING_BOARD_UPDATE.getValue());
        mplew.write(type);
        mplew.writeInt(id);

        return mplew.getPacket();
    }

    public static byte[] fishingCaught(int chrid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FISHING_CAUGHT.getValue());
        mplew.writeInt(chrid);

        return mplew.getPacket();
    }

}
