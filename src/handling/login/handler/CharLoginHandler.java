package handling.login.handler;

import client.LoginCrypto;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.PartTimeJob;
import client.Skill;
import client.SkillEntry;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.JobConstants;
import constants.ServerConfig;
import constants.ServerConstants;
import constants.WorldConstants;
import constants.WorldConstants.TespiaWorldOption;
import constants.WorldConstants.WorldOption;
import database.DBConPool;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginInformationProvider;
import handling.login.LoginInformationProvider.JobType;
import handling.login.LoginServer;
import handling.login.LoginWorker;
import handling.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import server.MapleItemInformationProvider;
import server.quest.MapleQuest;
import tools.FileoutputUtil;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.LoginPacket;
import tools.packet.PacketHelper;

public class CharLoginHandler {

    private static boolean loginFailCount(final MapleClient c) {
        c.loginAttempt++;
        return c.loginAttempt > 4;
    }

    public static void handleAuthRequest(final LittleEndianAccessor slea, final MapleClient c) {
        //System.out.println("Sending response to client.");
        int request = slea.readInt();
        int response;

        response = ((request >> 5) << 5) + (((((request & 0x1F) >> 3) ^ 2) << 3) + (7 - (request & 7)));
        response |= ((request >> 7) << 7);
        response -= 1; //-1 again on v143

        c.getSession().write(LoginPacket.sendAuthResponse(response));
    }

    public static void CheckAccount(final LittleEndianAccessor slea, final MapleClient c) {
        String accountName = slea.readMapleAsciiString();
        c.getSession().write(LoginPacket.CheckAccount(accountName, c.isAccountNameUsed(accountName)));
    }

    public static void RegisterAccount(LittleEndianAccessor slea, MapleClient c) {
        /*
         0C 
         07 00 61 31 32 33 31 32 33 用户名
         07 00 61 31 32 33 31 32 33 密码
         05 00 77 75 62 69 6E 姓名
         0A 00 31 39 39 35 2F 30 36 2F 30 37 生日
         00 00 电话号码
         09 00 31 31 31 31 31 31 31 31 31 问题1
         09 00 31 31 31 31 31 31 31 31 31 答案1
         09 00 31 31 31 31 31 31 31 31 31 问题2
         09 00 31 31 31 31 31 31 31 31 31 答案2
         0F 00 36 35 33 34 33 33 37 31 40 71 71 2E 63 6F 6D 邮箱
         12 00 33 35 30 35 38 32 31 39 39 35 30 36 30 37 32 30 35 36 身份证
         00 00 手机号码
         00 00 
         00 00 
         00 00
         *
         */
        String accountName = slea.readMapleAsciiString();
        String password = slea.readMapleAsciiString();
        String realName = slea.readMapleAsciiString();
        String birthDay = slea.readMapleAsciiString();
        String homeNo = slea.readMapleAsciiString();
        String questionOne = slea.readMapleAsciiString();
        String answerOne = slea.readMapleAsciiString();
        String questionTwo = slea.readMapleAsciiString();
        String answerTwo = slea.readMapleAsciiString();
        String email = slea.readMapleAsciiString();
        String IDCard = slea.readMapleAsciiString();
        String telNo = slea.readMapleAsciiString();
        byte gender = slea.readByte();
        int nxCredit = 0;

        boolean result = false;
        if (!c.isAccountNameUsed(accountName)) {
            try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
                try (PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (`name`, password, birthday,email,gender) VALUES (?, ?, ?, ?, ?)")) {
                    ps.setString(1, accountName);
                    ps.setString(2, LoginCrypto.hexSha1(password));
                    ps.setString(3, birthDay);
                    ps.setString(4, email);
                    ps.setByte(5, gender);
                    ps.executeUpdate();
                    ps.close();
                    result = true;
                }
            } catch (SQLException ex) {
                System.err.println("RegisterAccount" + ex);
                FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
            }
        }
        c.getSession().write(LoginPacket.RegisterAccount(result));
    }

    public static void setGender(final LittleEndianAccessor slea, final MapleClient c) {
        byte gender = slea.readByte();
        String username = slea.readMapleAsciiString();
        if (c.getAccountName().equals(username)) {
            c.setGender(gender);
            c.getSession().write(LoginPacket.genderChanged(c));
            c.getSession().write(CWvsContext.broadcastMsg(1, "设置性别成功请重新登录。"));
        } else {
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
        }
    }

    /**
     * 输入账号密码，登入账号。
     * @param slea
     * @param c
     */
    public static final void login(final LittleEndianAccessor slea, final MapleClient c) {
        String login = slea.readMapleAsciiString();
        String pwd = slea.readMapleAsciiString();

        final boolean ipBan = c.hasBannedIP();
        final boolean macBan = c.hasBannedMac();

        int loginok = c.login(login, pwd, ipBan || macBan);
        final Calendar tempbannedTill = c.getTempBanCalendar();

        if (loginok == 0 && (ipBan || macBan) && !c.isGm()) {
            loginok = 3;
            if (macBan) {
                // this is only an ipban o.O" - maybe we should refactor this a bit so it's more readable
                MapleCharacter.ban(c.getSession().getRemoteAddress().toString().split(":")[0], "Enforcing account ban, account " + login, false, 4, false);
            }
        }
        if (ServerConstants.getAutoReg()) {
            if (loginok == 5) {
                //账号不存在
                c.getSession().write(LoginPacket.RegisterInfo(true));
                loginok = 1;
            }
        }
        /*if (loginok == 7) {
         if (ServerConfig.AutoDisconnect) {
         for (int i = 0; i < 125; i++) {
         if (WorldOption.isExists(i) && WorldOption.getById(i).isAvailable()) {
         for (MapleCharacter chr : c.loadCharacters(i)) {
         for (ChannelServer cs : ChannelServer.getAllInstances()) {
         MapleCharacter victim = cs.getPlayerStorage().getCharacterById(chr.getId());
         if (victim != null) {
         victim.getClient().getSession().close();
         victim.getClient().disconnect(true, false);
         FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
         }
         }
         MapleCharacter victim = CashShopServer.getPlayerStorage().getCharacterById(chr.getId());
         if (victim != null) {
         victim.getClient().getSession().close();
         victim.getClient().disconnect(true, false);
         FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
         }
         c.updateLoginState(MapleClient.LOGIN_NOTLOGGEDIN, c.getSessionIPAddress());
         loginok = c.login(login, pwd, ipBan || macBan);
         }
         }
         }
         c.updateLoginState(MapleClient.LOGIN_NOTLOGGEDIN, c.getSessionIPAddress());
         }
         }*/

        if (loginok != 0) {
            if (!loginFailCount(c)) {
                c.clearInformation();
                c.getSession().write(LoginPacket.getLoginFailed(loginok));
            } else {
                c.getSession().close();
//                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

            }
        } else if (tempbannedTill.getTimeInMillis() != 0) {
            if (!loginFailCount(c)) {
                c.clearInformation();
                c.getSession().write(LoginPacket.getTempBan(PacketHelper.getTime(tempbannedTill.getTimeInMillis()), c.getBanReason()));
            } else {
                c.getSession().close();
                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
        } else if (c.getGender() == 10) {
            c.getSession().write(LoginPacket.genderNeeded(c));
        } else {
            c.loginAttempt = 0;
            LoginWorker.registerClient(c);
        }
    }

    public static void ServerListRequest(final MapleClient c) {
        LoginServer.forceRemoveAccounts(c, false);
        if (ServerConstants.TESPIA) {
            for (TespiaWorldOption tespiaservers : TespiaWorldOption.values()) {
                if (TespiaWorldOption.getById(tespiaservers.getWorld()).show() && TespiaWorldOption.getById(tespiaservers.getWorld()) != null) {
                    c.getSession().write(LoginPacket.getServerList(Integer.parseInt(tespiaservers.getWorld().replace("t", "")), LoginServer.getLoad()));
                }
            }
        } else {
            for (WorldOption servers : WorldOption.values()) {
                if (WorldOption.getById(servers.getWorld()).show() && servers != null) {
                    c.getSession().write(LoginPacket.getServerList(servers.getWorld(), LoginServer.getLoad()));
                }
            }
        }
        c.getSession().write(LoginPacket.getEndOfServerList());
        boolean hasCharacters = false;
        for (int world = 0; world < WorldOption.values().length; world++) {
            final List<MapleCharacter> chars = c.loadCharacters(world);
            if (chars != null) {
                hasCharacters = true;
                break;
            }
        }
        if (ServerConstants.TESPIA) {
            for (TespiaWorldOption value : TespiaWorldOption.values()) {
                String world = value.getWorld();
            }
        }
        if (!hasCharacters) {

        }
        if (WorldOption.recommended >= 0) {

        }
    }

    public static void ServerStatusRequest(final MapleClient c) {
        LoginServer.forceRemoveAccounts(c, false);
        // 0 = Select world normally
        // 1 = "Since there are many users, you may encounter some..."
        // 2 = "The concurrent users in this world have reached the max"
        final int numPlayer = LoginServer.getUsersOn();
        final int userLimit = LoginServer.getUserLimit();
        if (numPlayer >= userLimit) {
            c.getSession().write(LoginPacket.getServerStatus(2));
        } else if (numPlayer * 2 >= userLimit) {
            c.getSession().write(LoginPacket.getServerStatus(1));
        } else {
            c.getSession().write(LoginPacket.getServerStatus(0));
        }
    }

    public static void CharlistRequest(final LittleEndianAccessor slea, final MapleClient c) {
        LoginServer.forceRemoveAccounts(c, false);
        if (!c.isLoggedIn()) {
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }
        final int server = slea.readByte();
        final int channel = slea.readByte() + 1;
        if (!World.isChannelAvailable(channel, server) || !WorldOption.isExists(server)) {
            c.getSession().write(LoginPacket.getLoginFailed(10)); //cannot process so many
            return;
        }

        if (!WorldOption.getById(server).isAvailable() && !(c.isGm() && server == WorldConstants.gmserver)) {
            c.getSession().write(CWvsContext.broadcastMsg(1, "目前世界 " + WorldConstants.getNameById(server) + " 不开放. \r\n请选择其他世界."));
            c.getSession().write(LoginPacket.getLoginFailed(1)); //Shows no message, but it is used to unstuck
            return;
        }

        //System.out.println("Client " + c.getSession().getRemoteAddress().toString().split(":")[0] + " is connecting to server " + server + " channel " + channel + "");
        final List<MapleCharacter> chars = c.loadCharacters(server);
        if (chars != null && ChannelServer.getInstance(channel) != null) {
            c.setWorld(server);
            c.setChannel(channel);
            c.getSession().write(LoginPacket.getCharList(c.getSecondPassword(), chars, c.getCharacterSlots()));
        } else {
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

        }
    }

    public static void updateCCards(final LittleEndianAccessor slea, final MapleClient c) {
        if (slea.available() != 36 || !c.isLoggedIn()) {
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

            return;
        }
        final Map<Integer, Integer> cids = new LinkedHashMap<>();
        for (int i = 1; i <= 6; i++) { // 6 chars
            final int charId = slea.readInt();
            if ((!c.login_Auth(charId) && charId != 0) || ChannelServer.getInstance(c.getChannel()) == null || !WorldOption.isExists(c.getWorld())) {
                c.getSession().close();
                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

                return;
            }
            cids.put(i, charId);
        }
        c.updateCharacterCards(cids);
    }

    public static void CheckCharName(final String name, final MapleClient c) {
        LoginInformationProvider li = LoginInformationProvider.getInstance();
        boolean nameUsed = true;
        if (MapleCharacterUtil.canCreateChar(name, c.isGm())) {
            nameUsed = false;
        }
        if (li.isForbiddenName(name) && !c.isGm()) {
            nameUsed = false;
        }
        c.getSession().write(LoginPacket.charNameResponse(name, nameUsed));
    }

    /**
     * 创建角色
     * @param slea
     * @param c
     */
    public static void CreateChar(final LittleEndianAccessor slea, final MapleClient c) {
        String name;
        byte gender, skin, unk;
        short subcategory;
        int face, hair, hairColor = -1, hat = -1, top, bottom = -1, shoes, weapon, cape = -1, faceMark = -1, ears = -1, tail = -1, shield = -1;
        JobType job;
        name = slea.readMapleAsciiString();
        if (!MapleCharacterUtil.canCreateChar(name, false)) {
            System.out.println("char name hack: " + name);
            return;
        }
        int job_type = slea.readInt();
        if (!ServerConfig.koqst && job_type == 0) {
            c.getSession().write(CWvsContext.broadcastMsg(1, "暂不开放骑士团职业,请联系管理员。"));
            return;
        }
        if (!ServerConfig.kozs && job_type == 2) {
            c.getSession().write(CWvsContext.broadcastMsg(1, "暂不开放战神职业,请联系管理员。"));
            return;
        }
        job = JobType.getByType(job_type);
        if (job == null) {
            System.out.println("New job type found: " + 1);
            return;
        }
        for (JobConstants.LoginJob j : JobConstants.LoginJob.values()) {
            if (j.getJobType() == 1) {
                if (j.getFlag() != JobConstants.LoginJob.JobFlag.ENABLED.getFlag()) {
                    System.out.println("job was tried to be created while not enabled");
                    return;
                }
            }
        }

        gender = c.getGender();
        face = slea.readInt();
        hair = slea.readInt();
        top = slea.readInt();
        bottom = slea.readInt();
        shoes = slea.readInt();
        weapon = slea.readInt();

        MapleCharacter newchar = MapleCharacter.getDefault(c, job);
        newchar.setWorld((byte) c.getWorld());
        newchar.setFace(face);
        newchar.setHair(hair);
        newchar.setGender(gender);
        newchar.setName(name);

        //if (job_type == 0) {
        //    c.getSession().write(CWvsContext.broadcastMsg(1, "暂不开放骑士团职业,请联系管理员。"));
        //     return;
        // }
        if (!CheckCreate(gender, face, hair, weapon, top, bottom, shoes, job_type)) {
            System.out.println("无法创建角色: " + gender + " | " + 0 + " | " + job.type + " | ");
            c.getSession().write(CWvsContext.broadcastMsg(1, "创建角色异常，请不要使用非法程序。"));
            return;
        }

        final MapleItemInformationProvider li = MapleItemInformationProvider.getInstance();
        final MapleInventory equip = newchar.getInventory(MapleInventoryType.EQUIPPED);
        Item item;
        //-1 Hat | -2 Face | -3 Eye acc | -4 Ear acc | -5 Topwear 
        //-6 Bottom | -7 Shoes | -9 Cape | -10 Shield | -11 Weapon
        //todo check zero's beta weapon slot
        int[][] equips = new int[][]{{hat, -1}, {top, -5}, {bottom, -6}, {cape, -9}, {shoes, -7}, {weapon, -11}, {shield, -10}};
        for (int[] i : equips) {
            if (i[0] > 0) {
                item = li.getEquipById(i[0]);
                item.setPosition((byte) i[1]);
                item.setGMLog("Character Creation");
                equip.addFromDB(item);
            }
        }

        //newchar.getInventory(MapleInventoryType.USE).addItem(new Item(2000013, (byte) 0, (short) 200, (byte) 0), 1);
        //newchar.getInventory(MapleInventoryType.USE).addItem(new Item(2000014, (byte) 0, (short) 200, (byte) 0), 2);
        newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161001, (byte) 0, (short) 1, (byte) 0), 1);

        if (MapleCharacterUtil.canCreateChar(name, c.isGm()) && (!LoginInformationProvider.getInstance().isForbiddenName(name) || c.isGm()) && (c.isGm() || c.canMakeCharacter(c.getWorld()))) {
            MapleCharacter.saveNewCharToDB(newchar, job, (short) 0);
            c.getSession().write(LoginPacket.addNewCharEntry(newchar, true));
            c.createdChar(newchar.getId());
        } else {
            c.getSession().write(LoginPacket.addNewCharEntry(newchar, false));
        }
    }

    public static void CreateUltimate(final LittleEndianAccessor slea, final MapleClient c) {
        if (!c.getPlayer().isGM() && (!c.isLoggedIn() || c.getPlayer() == null || c.getPlayer().getLevel() < 120 || c.getPlayer().getMapId() != 130000000 || c.getPlayer().getQuestStatus(20734) != 0 || c.getPlayer().getQuestStatus(20616) != 2 || !GameConstants.isKOC(c.getPlayer().getJob()) || !c.canMakeCharacter(c.getPlayer().getWorld()))) {
            c.getSession().write(CField.createUltimate(2));
            //Character slots are full. Please purchase another slot from the Cash Shop.
            return;
        }
        //System.out.println(slea.toString());
        final String name = slea.readMapleAsciiString();
        final int job = slea.readInt(); //job ID

        final int face = slea.readInt();
        final int hair = slea.readInt();

        //No idea what are these used for:
        final int hat = slea.readInt();
        final int top = slea.readInt();
        final int glove = slea.readInt();
        final int shoes = slea.readInt();
        final int weapon = slea.readInt();

        final byte gender = c.getPlayer().getGender();

//        JobType errorCheck = JobType.Adventurer;
//        if (!LoginInformationProvider.getInstance().isEligibleItem(gender, 0, errorCheck.type, face)) {
//            System.out.println("無法創立角色: " + gender + " | " + index + " | " + job.type + " | " + i);
//            c.getSession().write(CWvsContext.enableActions());
//            return;
//        }
        JobType jobType = JobType.UltimateAdventurer;

        MapleCharacter newchar = MapleCharacter.getDefault(c, jobType);
        newchar.setJob(job);
        newchar.setWorld(c.getPlayer().getWorld());
        newchar.setFace(face);
        newchar.setHair(hair);
        newchar.setGender(gender);
        newchar.setName(name);
        newchar.setSkinColor((byte) 3); //troll
        newchar.setLevel((short) 50);
        newchar.getStat().str = (short) 4;
        newchar.getStat().dex = (short) 4;
        newchar.getStat().int_ = (short) 4;
        newchar.getStat().luk = (short) 4;
        newchar.setRemainingAp((short) 254); //49*5 + 25 - 16
        newchar.setRemainingSp(job / 100 == 2 ? 128 : 122); //2 from job advancements. 120 from leveling. (mages get +6)
        newchar.getStat().maxhp += 150; //Beginner 10 levels
        newchar.getStat().maxmp += 125;
        switch (job) {
            case 110:
            case 120:
            case 130:
                newchar.getStat().maxhp += 600; //Job Advancement
                newchar.getStat().maxhp += 2000; //Levelup 40 times
                newchar.getStat().maxmp += 200;
                break;
            case 210:
            case 220:
            case 230:
                newchar.getStat().maxmp += 600;
                newchar.getStat().maxhp += 500; //Levelup 40 times
                newchar.getStat().maxmp += 2000;
                break;
            case 310:
            case 320:
            case 410:
            case 420:
            case 520:
                newchar.getStat().maxhp += 500;
                newchar.getStat().maxmp += 250;
                newchar.getStat().maxhp += 900; //Levelup 40 times
                newchar.getStat().maxmp += 600;
                break;
            case 510:
                newchar.getStat().maxhp += 500;
                newchar.getStat().maxmp += 250;
                newchar.getStat().maxhp += 450; //Levelup 20 times
                newchar.getStat().maxmp += 300;
                newchar.getStat().maxhp += 800; //Levelup 20 times
                newchar.getStat().maxmp += 400;
                break;
            default:
                return;
        }

        final Map<Skill, SkillEntry> ss = new HashMap<>();
        ss.put(SkillFactory.getSkill(1074 + (job / 100)), new SkillEntry((byte) 5, (byte) 5, -1));
        ss.put(SkillFactory.getSkill(80), new SkillEntry((byte) 1, (byte) 1, -1));
        newchar.changeSkillLevel_Skip(ss, false);
        final MapleItemInformationProvider li = MapleItemInformationProvider.getInstance();

        //TODO: Make this GMS - Like
        int[] items = new int[]{1142257, hat, top, shoes, glove, weapon, hat + 1, top + 1, shoes + 1, glove + 1, weapon + 1}; //brilliant = fine+1
        for (byte i = 0; i < items.length; i++) {
            Item item = li.getEquipById(items[i]);
            item.setPosition((byte) (i + 1));
            newchar.getInventory(MapleInventoryType.EQUIP).addFromDB(item);
        }

        newchar.getInventory(MapleInventoryType.USE).addItem(new Item(2000004, (byte) 0, (short) 200, (byte) 0));
        if (MapleCharacterUtil.canCreateChar(name, c.isGm()) && (!LoginInformationProvider.getInstance().isForbiddenName(name) || c.isGm())) {
            MapleCharacter.saveNewCharToDB(newchar, jobType, (short) 0);
            MapleQuest.getInstance(20734).forceComplete(c.getPlayer(), 1101000);
            c.getSession().write(CField.createUltimate(0));
        } else if (!LoginInformationProvider.getInstance().isForbiddenName(name) || c.isGm()) {
            c.getSession().write(CField.createUltimate(3)); //"You cannot use this name."
        } else {
            c.getSession().write(CField.createUltimate(1));
        }
    }

    public static void DeleteChar(final LittleEndianAccessor slea, final MapleClient c) {
        String Secondpw_Client = GameConstants.GMS ? slea.readMapleAsciiString() : null;
        if (Secondpw_Client == null) {
            if (slea.readByte() > 0) { // Specific if user have second password or not
                Secondpw_Client = slea.readMapleAsciiString();
            }
            slea.readMapleAsciiString();
        }

        final int Character_ID = slea.readInt();

        if (!c.login_Auth(Character_ID) || !c.isLoggedIn() || loginFailCount(c)) {
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

            return; // Attempting to delete other character
        }
        byte state = 0;

        if (c.getSecondPassword() != null) { // On the server, there's a second password
            if (Secondpw_Client == null) { // Client's hacking
                c.getSession().close();
                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

                return;
            } else if (!c.CheckSecondPassword(Secondpw_Client)) { // Wrong Password
                state = 20;
            }
        }

        if (state == 0) {
            state = (byte) c.deleteCharacter(Character_ID);
        }
        c.getSession().write(LoginPacket.deleteCharResponse(Character_ID, state));
    }

    public static void Character_WithoutSecondPassword(final LittleEndianAccessor slea, final MapleClient c, final boolean haspic, final boolean view) {
        if (LoginServer.CheckSelectChar(c.getAccID(), System.currentTimeMillis())) {
            final int charId = slea.readInt();
            /*if (view) {
             c.setChannel(1);
             c.setWorld(slea.readInt());
             }*/

            // 快速登入发二次包 第二次判断到这 由于 !c.isLoggedIn() 及 !c.login_Auth(charId) 而被踢下线
            if (!c.isLoggedIn() || loginFailCount(c) || !c.login_Auth(charId) || ChannelServer.getInstance(c.getChannel()) == null || !WorldOption.isExists(c.getWorld())) {
                c.getSession().close();
                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

                return;
            }
            if (c.getIdleTask() != null) {
                c.getIdleTask().cancel(true);
            }
            final String s = c.getSessionIPAddress();
            LoginServer.putLoginAuth(charId, s.substring(s.indexOf('/') + 1, s.length()), c.getTempIP(), c.getChannel());
            c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, s);
            c.getSession().write(CField.getServerIP(c, Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1]), charId));
        } else {
            c.getSession().write(CWvsContext.broadcastMsg(1, "登入操作过快，请重新操作。"));
        }
    }

    public static void Character_WithSecondPassword(final LittleEndianAccessor slea, final MapleClient c, final boolean view) {
        final String password = slea.readMapleAsciiString();
        final int charId = slea.readInt();
        if (view) {
            c.setChannel(1);
            c.setWorld(slea.readInt());
        }
        if (!c.isLoggedIn() || loginFailCount(c) || c.getSecondPassword() == null || !c.login_Auth(charId) || ChannelServer.getInstance(c.getChannel()) == null || !WorldOption.isExists(c.getWorld())) {
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

            return;
        }
        c.updateMacs(slea.readMapleAsciiString());

        if (c.CheckSecondPassword(password) && password.length() >= 6 && password.length() <= 16 || c.isGm()) {

            if (c.getIdleTask() != null) {
                c.getIdleTask().cancel(true);
            }

            final String s = c.getSessionIPAddress();
            LoginServer.putLoginAuth(charId, s.substring(s.indexOf('/') + 1, s.length()), c.getTempIP(), c.getChannel());
            c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, s);
            c.getSession().write(CField.getServerIP(c, Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1]), charId));
        } else {
            c.getSession().write(LoginPacket.secondPwError((byte) 0x14));
        }
    }

    public static void partTimeJob(final LittleEndianAccessor slea, final MapleClient c) {
        System.out.println("[Part Time Job] data: " + slea);
        byte mode = slea.readByte(); //1 = start 2 = end
        int cid = slea.readInt(); //character id
        byte job = slea.readByte(); //part time job
        if (mode == 0) {
            LoginPacket.partTimeJob(cid, (byte) 0, System.currentTimeMillis());
        } else if (mode == 1) {
            LoginPacket.partTimeJob(cid, job, System.currentTimeMillis());
        }
    }

    public static void PartJob(LittleEndianAccessor slea, MapleClient c) {
        if (c.getPlayer() != null || !c.isLoggedIn()) {
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

            return;
        }
        final byte mode = slea.readByte();
        final int cid = slea.readInt();
        if (mode == 1) {
            final PartTimeJob partTime = MapleCharacter.getPartTime(cid);
            final byte job = slea.readByte();
            if (/*chr.getLevel() < 30 || */job < 0 || job > 5 || partTime.getReward() > 0
                    || (partTime.getJob() > 0 && partTime.getJob() <= 5)) {
                c.getSession().close();
                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

                return;
            }
            partTime.setTime(System.currentTimeMillis());
            partTime.setJob(job);
            c.getSession().write(LoginPacket.updatePartTimeJob(partTime));
            MapleCharacter.removePartTime(cid);
            MapleCharacter.addPartTime(partTime);
        } else if (mode == 2) {
            final PartTimeJob partTime = MapleCharacter.getPartTime(cid);
            if (/*chr.getLevel() < 30 || */partTime.getReward() > 0
                    || partTime.getJob() < 0 || partTime.getJob() > 5) {
                c.getSession().close();
                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

                return;
            }
            final long distance = (System.currentTimeMillis() - partTime.getTime()) / (60 * 60 * 1000L);
            if (distance > 1) {
                partTime.setReward((int) (((partTime.getJob() + 1) * 1000L) + distance));
            } else {
                partTime.setJob((byte) 0);
                partTime.setReward(0);
            }
            partTime.setTime(System.currentTimeMillis());
            MapleCharacter.removePartTime(cid);
            MapleCharacter.addPartTime(partTime);
            c.getSession().write(LoginPacket.updatePartTimeJob(partTime));
        }
    }

    public static boolean CheckCreate(byte gender, int face, int hair, int weapon, int top, int bottom, int shoes, int job_type) {
        boolean pass = true;
        if (job_type == 1) {
            if (gender == 0) {
                if (face != 20100 && face != 20401 && face != 20402) {
                    pass = false;
                } else if (hair != 30000 && hair != 30027 && hair != 30030) {
                    pass = false;
                } else if (top != 1040002 && top != 1040006 && top != 1040010) {
                    pass = false;
                } else if (bottom != 1060002 && bottom != 1060006) {
                    pass = false;
                }
            } else if (gender == 1) {
                if (face != 21700 && face != 21201 && face != 21002) {
                    pass = false;
                } else if (hair != 31002 && hair != 31047 && hair != 31057) {
                    pass = false;
                } else if (top != 1041002 && top != 1041006 && top != 1041010 && top != 1041011) {
                    pass = false;
                } else if (bottom != 1061002 && bottom != 1061008) {
                    pass = false;
                }
            }
            if (shoes != 1072001 && shoes != 1072005 && shoes != 1072037 && shoes != 1072038) {
                pass = false;
            } else if (weapon != 1302000 && weapon != 1312004 && weapon != 1322005) {
                pass = false;
            }
        } else if (job_type == 2) {
            if (gender == 0) {
                if (face != 20100 && face != 20401 && face != 20402) {
                    pass = false;
                } else if (hair != 30000 && hair != 30027 && hair != 30030) {
                    pass = false;
                } else if (top != 1042167) {
                    pass = false;
                } else if (bottom != 1062115) {
                    pass = false;
                }
            } else if (gender == 1) {
                if (face != 21700 && face != 21201 && face != 21002) {
                    pass = false;
                } else if (hair != 31002 && hair != 31047 && hair != 31057) {
                    pass = false;
                } else if (top != 1042167) {
                    pass = false;
                } else if (bottom != 1062115) {
                    pass = false;
                }
            }
            if (shoes != 1072383) {
                pass = false;
            } else if (weapon != 1442079) {
                pass = false;
            }
        }
        return pass;
    }
}
