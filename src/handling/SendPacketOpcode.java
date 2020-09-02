package handling;

import constants.ServerConfig;
import tools.FileoutputUtil;
import tools.StringUtil;

public enum SendPacketOpcode implements WritableIntValueHolder {

    //登录游戏
    LOGIN_STATUS((short) 0x00),
    //许可协议
    LICENSE_RESULT((short) 0x02),
    //选择性别
    CHOOSE_GENDER((short) 0x04),
    //性别选择返回
    GENDER_SET((short) 0x05),
    //服务器状态
    SERVERSTATUS((short) 0x06),
    //服务器列表
    SERVERLIST((short) 0x09),
    //人物列表
    CHARLIST((short) 0x0A),
    //服务器IP
    SERVER_IP((short) 0x0B),
    //检查人物名字
    CHAR_NAME_RESPONSE((short) 0x0C),
    //注册帐号窗口
    REGISTER_INFO((byte) 0x0D),
    //检查帐号名是否可用
    CHECK_ACCOUNT_INFO((byte) 0x0E),
    //注册帐号
    REGISTER_ACCOUNT((byte) 0x0F),
    //创建人物
    ADD_NEW_CHAR_ENTRY((short) 0x11),
    //删除角色
    DELETE_CHAR_RESPONSE((byte) 0x12),
    //更换频道
    CHANGE_CHANNEL((short) 0x13),
    //通讯包
    PING((short) 0x14),
    //商城未知
    CS_USE((short) 0x15),
    //道具栏信息
    INVENTORY_OPERATION((short) 0x20),
    //更新道具栏数量
    INVENTORY_GROW((short) 0x21),
    //刷新人物属性
    UPDATE_STATS((short) 0x22),
    //获得增益效果状态
    GIVE_BUFF((short) 0x23),
    //取消增益效果状态
    CANCEL_BUFF((short) 0x24),
    //临时能力值开始
    TEMP_STATS((short) 0x25),
    //临时能力值结束
    TEMP_STATS_RESET((short) 0x26),
    //刷新技能
    UPDATE_SKILLS((short) 0x27),
    //人气反馈
    FAME_RESPONSE((short) 0x29),
    //人物具体信息
    SHOW_STATUS_INFO((short) 0x2A),
    //小纸条
    SHOW_NOTES((short) 0x2B),
    //缩地石
    TROCK_LOCATIONS((short) 0x2C),
    //测谎仪
    LIE_DETECTOR((short) 0x2D),
    //刷新骑宠
    UPDATE_MOUNT((short) 0x32),
    //任务提示
    SHOW_QUEST_COMPLETION((short) 0x33),
    //雇佣商店
    SEND_TITLE_BOX((short) 0x34),
    //使用技能书
    USE_SKILL_BOOK((short) 0x35),
    //道具排序
    FINISH_SORT((short) 0x36),
    //道具集合
    FINISH_GATHER((short) 0x37),
    //角色信息
    CHAR_INFO((short) 0x3A),
    //开设组队
    PARTY_OPERATION((short) 0x3B),
    //好友列表
    BUDDYLIST((short) 0x3C),
    //家族操作
    GUILD_OPERATION((short) 0x3E),
    //家族联盟
    ALLIANCE_OPERATION((short) 0x3F),
    //召唤门
    SPAWN_PORTAL((short) 0x40),
    //服务器公告
    SERVERMESSAGE((short) 0x41),
    //花生机奖
    PIGMI_REWARD((short) 0x42),
    //商店搜索器
    OWL_OF_MINERVA((short) 0x43),
    //商店搜索传送
    OWL_RESULT((short) 0x44),
    //
    ENGAGE_REQUEST((short) 0x45),
    //
    ENGAGE_RESULT((short) 0x46),
    //红鸾宫结婚滚动特效条
    MARRAGE_EFFECT((short) 0x47),
    //黄色文字
    YELLOW_CHAT((short) 0x4E),
    //捕捉怪物文字
    CATCH_MOB((short) 0x50),
    //情景喇叭重置 -7
    AVATAR_MEGA_RESULT((short) 0x55),
    //情景喇叭 -7
    AVATAR_MEGA((short) 0x56),
    //情景喇叭删除 -7
    AVATAR_MEGA_REMOVE((short) 0x57),
    //建立玩家Npc -7
    MAKE_PLAYER_NPC((short) 0x58),
    //玩家NPC
    PLAYER_NPC((short) 0x59),
    GET_CARD((short) 0x5B),
    MONSTERBOOK_CHANGE_COVER((short) 0x5C),
    //道场 废弃的地铁台以及金字塔计数板
    SESSION_VALUE((short) 0x62),
    //打开校谱
    SEND_PEDIGREE((short) 0x63),
    //打开学院
    OPEN_FAMILY((short) 0x65),
    //学院返回信息
    FAMILY_MESSAGE((short) 0x66),
    //学院邀请
    FAMILY_INVITE((short) 0x67),
    //接受(拒绝)学院邀请 
    FAMILY_JUNIOR((short) 0x68),
    //学院信息
    SENIOR_MESSAGE((short) 0x69),
    //学院系统
    FAMILY((short) 0x6A),
    //改变学院点数
    REP_INCREASE((short) 0x6B),
    //学院玩家登录
    FAMILY_LOGGEDIN((short) 0x6C),
    //学院状态
    FAMILY_BUFF((short) 0x6D),
    //使用学院点
    FAMILY_USE_REQUEST((short) 0x6E),
    //学院等级提升信息
    LEVEL_UPDATE((short) 0x6F),
    //结婚提示
    MARRIAGE_UPDATE((short) 0x70),
    //转职提示
    JOB_UPDATE((short) 0x71),//9B

    TOP_MSG((short) 0x73),
    //钓鱼
    FISHING_BOARD_UPDATE((short) 0x75),
    //打开网页
    OPEN_WEB((short) 0x76),
    //显示抵用卷
    CHAR_CASH((short) 0x7D),
    //技能宏 -12
    SKILL_MACRO((short) 0x80),
    //传送到地图 -12
    WARP_TO_MAP((short) 0x81),
    //打开拍卖
    MTS_OPEN((short) 0x82),
    //打开商城 -12
    CS_OPEN((short) 0x83),
    //地图错误
    MAP_BLOCKED((short) 0x84),
    //服务器错误
    SERVER_BLOCKED((short) 0x85),
    //组队提示错误
    PARTY_BLOCKED((short) 0x86),
    //显示装备效果
    SHOW_EQUIP_EFFECT((short) 0x89),
    //组队家族聊天 -13
    MULTICHAT((short) 0x8A),
    //悄悄话
    WHISPER((short) 0x8B),
    //配偶聊天
    SPOUSE_CHAT((short) 0x8C),
    //BOSS血条
    BOSS_ENV((short) 0x8D),
    //
    MOVE_ENV((short) 0x8E),
    //更新BOSS血条
    UPDATE_ENV((short) 0x8F),
    //地图效果 
    MAP_EFFECT((short) 0x91),
    //音乐盒 5100000
    CASH_SONG((short) 0x92),
    //GM效果 
    GM_EFFECT((short) 0x93),
    //0X问答活动 
    OX_QUIZ((short) 0x94),
    //0X问答提示 
    GMEVENT_INSTRUCTIONS((short) 0x95),
    //时钟 
    CLOCK((short) 0x96),
    //船只特效
    BOAT_EFFECT((short) 0x98),
    //船只移动
    BOAT_MOVE((short) 0x99),
    //船只状态
    BOAT_STATE((short) 0x9A),
    //
    SET_OBJECT_STATE((short) 0x9B),
    //时间停止
    STOP_CLOCK((short) 0x9C),
    //阿里安特记分板
    ARIANT_SCOREBOARD((short) 0x9E),//12F
    //
    MOVE_PLATFORM((short) 0x9F),//153
    //废气的地铁台以及金字塔
    PYRAMID_UPDATE((short) 0xA0),//131
    PYRAMID_RESULT((short) 0xA1),//132

    //召唤玩家 -18
    SPAWN_PLAYER((short) 0xA2),
    //召唤玩家 -18
    REMOVE_PLAYER_FROM_MAP((short) 0xA3),
    //聊天信息
    CHATTEXT((short) 0xA4),
    //小黑板
    CHALKBOARD((short) 0xA6),
    //更新玩家
    UPDATE_CHAR_BOX((short) 0xA7),
    //消費效果
    SHOW_CONSUME_EFFECT((short) 0xA8),
    //砸卷效果
    SHOW_SCROLL_EFFECT((short) 0xA9),
    //钓鱼
    FISHING_CAUGHT((short) 0xAB),
    //召唤宠物
    SPAWN_PET((short) 0xAD),
    //宠物移动
    MOVE_PET((short) 0xAF),
    //PET_CHAT
    PET_CHAT((short) 0xB0),
    //宠物名变更
    PET_NAMECHANGE((short) 0xB1),
    //宠物例外道具
    PET_EXCEPTION_LIST((short) 0xB2),
    //宠物命令
    PET_COMMAND((short) 0xB3),
    //召唤兽召唤
    SPAWN_SUMMON((short) 0xB4),
    //召唤兽移除
    REMOVE_SUMMON((short) 0xB5),
    //召唤兽移动
    MOVE_SUMMON((short) 0xB6),
    //召唤兽攻击
    SUMMON_ATTACK((short) 0xB7),
    //召唤兽技能
    SUMMON_SKILL((short) 0xB8),
    //召唤兽受到伤害
    DAMAGE_SUMMON((short) 0xB9),
    //人物移动
    MOVE_PLAYER((short) 0xBB),
    //近距离攻击 
    CLOSE_RANGE_ATTACK((short) 0xBC),
    //远距离攻击 
    RANGED_ATTACK((short) 0xBD),
    //魔法攻击 
    MAGIC_ATTACK((short) 0xBE),
    //被动攻击 
    ENERGY_ATTACK((short) 0xBF),
    //技能能效果
    SKILL_EFFECT((short) 0xC0),
    //取消技能效果
    CANCEL_SKILL_EFFECT((short) 0xC1),
    //人物伤害
    DAMAGE_PLAYER((short) 0xC2),
    //人物面部表情
    FACIAL_EXPRESSION((short) 0xC3),
    //显示物品效果
    SHOW_EFFECT((short) 0xC5),
    //椅子效果
    SHOW_CHAIR((short) 0xC6),
    //更新玩家外观
    UPDATE_CHAR_LOOK((short) 0xC7),
    //玩家外观效果
    SHOW_FOREIGN_EFFECT((short) 0xC8),
    //获取异常状态
    GIVE_FOREIGN_BUFF((short) 0xC9),
    //取消异常状态
    CANCEL_FOREIGN_BUFF((short) 0xCA),
    //更新组队HP显示
    UPDATE_PARTYMEMBER_HP((short) 0xCB),
    //加载家族名字 
    LOAD_GUILD_NAME((short) 0xCC),
    //加载家族图标 
    LOAD_GUILD_ICON((short) 0xCD),
    //加载团队
    LOAD_TEAM((short) 0xCE),
    //取消椅子
    CANCEL_CHAIR((short) 0xCF),
    //显示物品效果
    SHOW_SPECIAL_EFFECT((short) 0xD0),
    //角色地图瞬移 武林道场会出现
    CURRENT_MAP_WARP((short) 0xD1),
    //使用金币包成功
    MESOBAG_SUCCESS((short) 0xD4),
    //使用金币包失败 
    MESOBAG_FAILURE((short) 0xD5),
    //刷新任务
    UPDATE_QUEST_INFO((short) 0xD6),
    //未知
    HP_DECREASE((short) 0xD7),
    //增加(取消)宠物技能 
    PET_FLAG_CHANGE((short) 0xD8),
    //玩家提示
    PLAYER_HINT((short) 0xD9),
    INTRO_DISABLE_UI((short) 0xE3),
    INTRO_LOCK((short) 0xE4),
    SUMMON_HINT((short) 0xE5),
    SUMMON_HINT_MSG((short) 0xE6),
    //连击效果
    ARAN_COMBO((short) 0xE7),
    //技能冷却
    COOLDOWN((short) 0xEC),
    //召唤怪物 -1F
    SPAWN_MONSTER((short) 0xEE),
    //杀死怪物 -1F
    KILL_MONSTER((short) 0xEF),
    //召唤怪物控制 -1F
    SPAWN_MONSTER_CONTROL((short) 0xF0),
    //怪物移动 -1F
    MOVE_MONSTER((short) 0xF1),
    //移动怪物回应 -1F
    MOVE_MONSTER_RESPONSE((short) 0xF2),
    //添加怪物状态
    APPLY_MONSTER_STATUS((short) 0xF4),
    //取消怪物状态
    CANCEL_MONSTER_STATUS((short) 0xF5),
    //怪物攻击怪物
    MOB_TO_MOB_DAMAGE((short) 0xF7),
    //怪物伤害
    DAMAGE_MONSTER((short) 0xF8),
    //显示怪物HP -1F
    SHOW_MONSTER_HP((short) 0xFC),
    //显示磁铁(显示蓝色CATCH)
    SHOW_MAGNET((short) 0xFD),
    //抓怪物(显示红色CATCH)
    CATCH_MONSTER((short) 0xFF),
    //更新到这里

    //召唤NPC -21
    SPAWN_NPC((short) 0x104),
    //移除NPC -21
    REMOVE_NPC((short) 0x105),
    //唤NPC 请求控制权 -21
    SPAWN_NPC_REQUEST_CONTROLLER((short) 0x106),
    //NPC说话
    NPC_ACTION((short) 0x107),
    //召唤雇佣商店
    SPAWN_HIRED_MERCHANT((short) 0x10D),
    //取消雇佣商店 
    DESTROY_HIRED_MERCHANT((short) 0x10E),
    //更新雇佣商店
    UPDATE_HIRED_MERCHANT((short) 0x10F),
    //掉落物品在地图上 -21
    DROP_ITEM_FROM_MAPOBJECT((short) 0x110),
    //从地图上删除物品 -21
    REMOVE_ITEM_FROM_MAP((short) 0x111),
    //召唤LOVE错误
    SPAWN_KITE_ERROR((short) 0x112),
    //召唤LOVE
    SPAWN_KITE((short) 0x113),
    //取消LOVE
    DESTROY_KITE((short) 0x114),
    //召唤烟雾
    SPAWN_MIST((short) 0x115),
    //取消烟雾
    REMOVE_MIST((short) 0x116),
    //召唤门
    SPAWN_DOOR((short) 0x117),
    //取消门
    REMOVE_DOOR((short) 0x118),
    //攻击反应堆
    REACTOR_HIT((short) 0x11C),
    //反应堆移动
    REACTOR_MOVE((short) 0x11D),
    //召唤反应堆
    REACTOR_SPAWN((short) 0x11E),
    //移除反应堆
    REACTOR_DESTROY((short) 0x11F),
    // 雪球活动
    ROLL_SNOWBALL((short) 0x120),//2C7
    // 雪球活动
    HIT_SNOWBALL((short) 0x121),
    // 雪球活动
    SNOWBALL_MESSAGE((short) 0x122),
    // 雪球活动
    LEFT_KNOCK_BACK((short) 0x123),
    // 椰子活动
    HIT_COCONUT((short) 0x124),
    // 椰子活动分数
    COCONUT_SCORE((short) 0x125),
    //怪物嘉年华
    MONSTER_CARNIVAL_START((short) 0x129),//2C9
    MONSTER_CARNIVAL_OBTAINED_CP((short) 0x12A),//2CA
    MONSTER_CARNIVAL_PARTY_CP((short) 0x12B),
    MONSTER_CARNIVAL_SUMMON((short) 0x12C),//2CD

    MONSTER_CARNIVAL_DIED((short) 0x12E),//2CF
    MONSTER_CARNIVAL_LEAVE((short) 0x12F),//2D0
    ENGLISH_QUIZ((short) 0x130),
    MONSTER_CARNIVAL_RESULT((short) 0x131),//2D1
    //阿里安特排行更新
    ARIANT_SCORE_UPDATE((short) 0x132),
    //NPC交谈 -0x25
    NPC_TALK((short) 0x145),
    //打开NPC商店
    OPEN_NPC_SHOP((short) 0x146),
    //NPC商店确认
    CONFIRM_SHOP_TRANSACTION((short) 0x147),
    //打开仓库
    OPEN_STORAGE((short) 0x14A),
    //聊天招待
    MERCH_ITEM_MSG((short) 0x14B),
    //弗兰德里
    MERCH_ITEM_STORE((short) 0x14C),
    //剪刀石头布
    RPS_GAME((short) 0x14D),
    //聊天招待
    MESSENGER((short) 0x14E),
    //玩家互动
    PLAYER_INTERACTION((short) 0x14F),
    //打开豆豆机界面
    OPEN_BEANS((short) 0x15B),
    //送货员 -0x26
    PACKAGE_OPERATION((short) 0x15F),
    //商城刷新点卷
    CS_UPDATE((short) 0x161),
    //商城操作 
    CS_OPERATION((short) 0x162),
    //键盘排序 -0x2A
    KEYMAP((short) 0x16F),
    //宠物自动补红
    PET_AUTO_HP((short) 0x170),
    //宠物自动补蓝
    PET_AUTO_MP((short) 0x171),
    //金锤子
    VICIOUS_HAMMER((short) 0x182),
    // General

    AUTH_RESPONSE((short) 0x16),
    // Login

    MAPLE_POINT((short) 0x999),
    SEND_LINK((short) 0x01),
    LOGIN_SECOND((short) 0x02),
    PIN_OPERATION((short) 0x06),
    PIN_ASSIGNED((short) 0x07),
    ALL_CHARLIST((short) 0x08),
    RELOG_RESPONSE((short) 0x17),
    REGISTER_PIC_RESPONSE((short) 0x1A),
    ENABLE_RECOMMENDED((short) 0x1D),
    SEND_RECOMMENDED((short) 0x1E),
    PART_TIME((short) 0x1F),
    CHANNEL_SELECTED((short) 0x21),
    EXTRA_CHAR_INFO((short) 0x22),//23
    SPECIAL_CREATION((short) 0x23),//24
    SECONDPW_ERROR((short) 0x24),//25
    CHANGE_BACKGROUND((short) 0x13C),//v148
    // Channel

    REMOVE_BG_LAYER((short) 0x999),
    SET_MAP_OBJECT_VISIBLE((short) 0x999),
    UPDATE_STOLEN_SKILLS((short) 0x2D),//2E
    TARGET_SKILL((short) 0x2E),//2F
    DIRECTION_FACIAL_EXPRESSION((short) 0x1F8),//1E7
    MOVE_SCREEN((short) 0x1F9),//1E8
    FULL_CLIENT_DOWNLOAD((short) 0x35),
    REPORT_RESPONSE((short) 0x3A),//39
    REPORT_TIME((short) 0x3B),//3A
    REPORT_STATUS((short) 0x3C),//3B

    SP_RESET((short) 0x42),//41
    AP_RESET((short) 0x43),//42
    DISTRIBUTE_ITEM((short) 0x44),//43
    EXPAND_CHARACTER_SLOTS((short) 0x45),//44

    EXP_POTION((short) 0x43),
    REPORT_RESULT((short) 0x4E),//v145
    TRADE_LIMIT((short) 0x50),//v145
    UPDATE_GENDER((short) 0x51),//50
    BBS_OPERATION((short) 0x52),//51

    MEMBER_SEARCH((short) 0x57),//5A
    PARTY_SEARCH((short) 0x58),//5A
    BOOK_INFO((short) 0x5A),//5A
    CODEX_INFO_RESPONSE((short) 0x5B),//5C
    EXPEDITION_OPERATION((short) 0x5C),//5D

    MECH_PORTAL((short) 0x62),//63
    ECHO_MESSAGE((short) 0x63),//64

    ITEM_OBTAIN((short) 0x6A),//68

    WEDDING_GIFT((short) 0x73),//71
    WEDDING_MAP_TRANSFER((short) 0x74),//72
    USE_CASH_PET_FOOD((short) 0x76),//74

    SHOP_DISCOUNT((short) 0x79),//76

    DISABLE_NPC((short) 0x7D),//7A
    CARD_UNK((short) 0x7F),//new143
    CARD_SET((short) 0x80),//7D
    BOOK_STATS((short) 0x81),//7E
    UPDATE_CODEX((short) 0x82),//7F
    CARD_DROPS((short) 0x83),//80
    FAMILIAR_INFO((short) 0x84),//81
    CHANGE_HOUR((short) 0x83),//83
    RESET_MINIMAP((short) 0x84),//87
    CONSULT_UPDATE((short) 0x85),//88
    CLASS_UPDATE((short) 0x86),//89
    WEB_BOARD_UPDATE((short) 0x87),//8A

    MAP_VALUE((short) 0x8C),//v145
    EXP_BONUS((short) 0x8D),//v145
    PARTY_VALUE((short) 0x999),//v145
    POTION_BONUS((short) 0x999),//8D

    MAPLE_TV_MSG((short) 0x999),
    LUCKY_LUCKY_MONSTORY((short) 0x999),//new v147

    POPUP2((short) 0x9D),
    CANCEL_NAME_CHANGE((short) 0x9E),
    CANCEL_WORLD_TRANSFER((short) 0x9F),
    CLOSE_HIRED_MERCHANT((short) 0xA3),//A0
    GM_POLICE((short) 0x999),//A1
    TREASURE_BOX((short) 0xA5),//A2
    NEW_YEAR_CARD((short) 0xA6),//A3
    RANDOM_MORPH((short) 0xA7),//A4
    CANCEL_NAME_CHANGE_2((short) 0x999),//A9
    SLOT_UPDATE((short) 0xA9),//AC
    FOLLOW_REQUEST((short) 0xAB),//AD

    NEW_TOP_MSG((short) 0xB1),//new148
    MID_MSG((short) 0xB2),
    CLEAR_MID_MSG((short) 0xB3),
    SPECIAL_MSG((short) 0xB4),
    MAPLE_ADMIN_MSG((short) 0xB5),
    CAKE_VS_PIE_MSG((short) 0x999),
    GM_STORY_BOARD((short) 0xB7),
    INVENTORY_FULL((short) 0xB6),//v145
    ZERO_STATS((short) 0xB8),
    UPDATE_JAGUAR((short) 0xB9),
    YOUR_INFORMATION((short) 0xB9),
    FIND_FRIEND((short) 0xBA),
    VISITOR((short) 0xBB),
    PINKBEAN_CHOCO((short) 0xBC),
    PAM_SONG((short) 0xBD),
    AUTO_CC_MSG((short) 0xBE),
    DISALLOW_DELIVERY_QUEST((short) 0xC1),//bb
    ULTIMATE_EXPLORER((short) 0xC2),//BC
    SPECIAL_STAT((short) 0xC3), //also profession_info //BD
    UPDATE_IMP_TIME((short) 0xC5),//BE
    ITEM_POT((short) 0xC6),//BF
    MULUNG_MESSAGE((short) 0xC9),//C2
    GIVE_CHARACTER_SKILL((short) 0xCA),//C3
    MULUNG_DOJO_RANKING((short) 0xCF),//C8
    UPDATE_INNER_ABILITY((short) 0xD4),//CD
    EQUIP_STOLEN_SKILL((short) 0xD5),//CE
    REPLACE_SKILLS((short) 0xD5),//CE
    INNER_ABILITY_MSG((short) 0xD6),//CF
    ENABLE_INNER_ABILITY((short) 0xD7),//D0
    DISABLE_INNER_ABILITY((short) 0xD8),//D1
    UPDATE_HONOUR((short) 0xD9),//D2
    AZWAN_UNKNOWN((short) 0xDA),//D3 //probably circulator shit?
    AZWAN_RESULT((short) 0xDB),//D4
    AZWAN_KILLED((short) 0xDC),//D5
    CIRCULATOR_ON_LEVEL((short) 0xDD),//D6
    SILENT_CRUSADE_MSG((short) 0xDE),//D7
    SILENT_CRUSADE_SHOP((short) 0xDF),//D8
    CASSANDRAS_COLLECTION((short) 0xEA),//new v145

    POPUP((short) 0xF0),//E9
    MINIMAP_ARROW((short) 0xF4),//ED
    UNLOCK_CHARGE_SKILL((short) 0xFA),//F2
    LOCK_CHARGE_SKILL((short) 0xFB),//F3
    CANDY_RANKING((short) 0xFF),//F8
    ATTENDANCE((short) 0x10A),//102
    MESSENGER_OPEN((short) 0x10B),//103

    EVENT_CROWN((short) 0x111),//10D
    RANDOM_RESPONSE((short) 0x121),
    MAGIC_WHEEL((short) 0x134),//125
    REWARD((short) 0x135),//126

    FARM_OPEN((short) 0x138),//129

    QUICK_SLOT((short) 0x999),//153

    PVP_INFO((short) 0x157),//154
    PYRAMID_KILL_COUNT((short) 0x158),//155
    DIRECTION_STATUS((short) 0x168),//159
    GAIN_FORCE((short) 0x169),//15A
    ACHIEVEMENT_RATIO((short) 0x16A),//15B
    QUICK_MOVE((short) 0x999),//15C

    CHATTEXT_1((short) 0x17B),//16A

    SHOW_MAGNIFYING_EFFECT((short) 0x170),//16E
    SHOW_POTENTIAL_RESET((short) 0x171),//16F
    SHOW_FIREWORKS_EFFECT((short) 0x172),//170
    SHOW_NEBULITE_EFFECT((short) 0x173),//171
    SHOW_FUSION_EFFECT((short) 0x174),//172
    PVP_ATTACK((short) 0x140),
    PVP_MIST((short) 0x141),
    PVP_COOL((short) 0x142),
    TESLA_TRIANGLE((short) 0x999),//0x15C
    FOLLOW_EFFECT((short) 0x15D),
    SHOW_PQ_REWARD((short) 0x15E),
    CRAFT_EFFECT((short) 0x182),//15F
    CRAFT_COMPLETE((short) 0x183),//160
    HARVESTED((short) 0x185),//161
    PLAYER_DAMAGED((short) 0x165),
    NETT_PYRAMID((short) 0x166),
    SET_PHASE((short) 0x167),
    PAMS_SONG((short) 0x168),
    SPAWN_PET_2((short) 0x192),//16D

    PET_COLOR((short) 0x197),//172
    PET_SIZE((short) 0x198),//173

    DRAGON_SPAWN((short) 0x19A),//175
    INNER_ABILITY_RESET_MSG((short) 0x999),//173
    DRAGON_MOVE((short) 0x19B),//176
    DRAGON_REMOVE((short) 0x19C),//177
    ANDROID_SPAWN((short) 0x19D),//178
    ANDROID_MOVE((short) 0x19E),//179
    ANDROID_EMOTION((short) 0x19F),//17A
    ANDROID_UPDATE((short) 0x1A0),//17B
    ANDROID_DEACTIVATED((short) 0x1A1), //17C 
    SPAWN_FAMILIAR((short) 0x1A8),//183
    MOVE_FAMILIAR((short) 0x1A9),//184
    TOUCH_FAMILIAR((short) 0x1AA),//185
    ATTACK_FAMILIAR((short) 0x1AB),//186
    RENAME_FAMILIAR((short) 0x1AC),//187
    SPAWN_FAMILIAR_2((short) 0x1AD),//188
    UPDATE_FAMILIAR((short) 0x1AE),//189
    HAKU_CHANGE_1((short) 0x1A2),//18A
    HAKU_CHANGE_0((short) 0x1A5),//18B
    HAKU_MOVE((short) 0x1B0),//18B
    HAKU_UNK((short) 0x1B1),//18C
    HAKU_CHANGE((short) 0x1B2),//18D
    SPAWN_HAKU((short) 0x1B5),//190

    MOVE_ATTACK((short) 0x1BF),//19A

    ANGELIC_CHANGE((short) 0x1C7),//1A2

    SHOW_HARVEST((short) 0x2BA),//1AE
    PVP_HP((short) 0x1D7),//1B0

    R_MESOBAG_SUCCESS((short) 0x1EE),//1EB
    R_MESOBAG_FAILURE((short) 0x1EF),//1EC
    MAP_FADE((short) 0x201),//1F0
    MAP_FADE_FORCE((short) 0x202),//1F1

    PLAY_EVENT_SOUND((short) 0x208),//1F6
    PLAY_MINIGAME_SOUND((short) 0x209),//1F7
    MAKER_SKILL((short) 0x20A),//1F8
    OPEN_UI((short) 0x20C),//1FB
    OPEN_UI_OPTION((short) 0x20E),//1FD

    INTRO_ENABLE_UI((short) 0x210),//1FF

    ARAN_COMBO_RECHARGE((short) 0x215),//204
    RANDOM_EMOTION((short) 0x216),//205
    RADIO_SCHEDULE((short) 0x217),//206
    OPEN_SKILL_GUIDE((short) 0x218),//207
    GAME_MSG((short) 0x21A),//209
    GAME_MESSAGE((short) 0x21B),//20A
    BUFF_ZONE_EFFECT((short) 0x21D),//20C
    GO_CASHSHOP_SN((short) 0x21E),//20D
    DAMAGE_METER((short) 0x21F),//20E
    TIME_BOMB_ATTACK((short) 0x999),//20F
    FOLLOW_MOVE((short) 0x999),//20D
    FOLLOW_MSG((short) 0x999),//211
    AP_SP_EVENT((short) 0x999),//215
    QUEST_GUIDE_NPC((short) 0x999),//214
    REGISTER_FAMILIAR((short) 0x999),//218
    FAMILIAR_MESSAGE((short) 0x999),//219
    CREATE_ULTIMATE((short) 0x999),//21A
    HARVEST_MESSAGE((short) 0x999),//21C
    SHOW_MAP_NAME((short) 0x999),
    OPEN_BAG((short) 0x999),//21D
    DRAGON_BLINK((short) 0x999),//21E
    PVP_ICEGAGE((short) 0x999),//21F
    DIRECTION_INFO((short) 0x234),//223
    REISSUE_MEDAL((short) 0x235),//224
    PLAY_MOVIE((short) 0x238),//227
    CAKE_VS_PIE((short) 0x228),//225
    PHANTOM_CARD((short) 0x229),//226
    LUMINOUS_COMBO((short) 0x22A),//229
    MOVE_SCREEN_X((short) 0x199),//199
    MOVE_SCREEN_DOWN((short) 0x19A),//19A
    CAKE_PIE_INSTRUMENTS((short) 0x19B),//19B
    SEALED_BOX((short) 0x218),//212

    PVP_SUMMON((short) 0x26F),//269

    SUMMON_SKILL_2((short) 0x272),
    SUMMON_DELAY((short) 0x273),
    MONSTER_SKILL((short) 0x298),//281

    SKILL_EFFECT_MOB((short) 0x29A),//283
    TELE_MONSTER((short) 0x999),
    MONSTER_CRC_CHANGE((short) 0x29C),//285
    SHOW_TITLE((short) 0x999),
    ITEM_EFFECT_MOB((short) 0x29F),//288
    MONSTER_PROPERTIES((short) 0x1BF),
    REMOVE_TALK_MONSTER((short) 0x1C0),
    TALK_MONSTER((short) 0x28A),
    CYGNUS_ATTACK((short) 0x999),
    MONSTER_RESIST((short) 0x999),
    AZWAN_MOB_TO_MOB_DAMAGE((short) 0x999),
    AZWAN_SPAWN_MONSTER((short) 0x999),
    AZWAN_KILL_MONSTER((short) 0x999),
    AZWAN_SPAWN_MONSTER_CONTROL((short) 0x999),
    NPC_TOGGLE_VISIBLE((short) 0x2C2),//2AA
    INITIAL_QUIZ((short) 0x2C4),//2AC
    NPC_UPDATE_LIMITED_INFO((short) 0x2C5),//2AD
    NPC_SET_SPECIAL_ACTION((short) 0x2C6),//2AE
    NPC_SCRIPTABLE((short) 0x2C7),//2AF
    RED_LEAF_HIGH((short) 0x2C8),//2B0

    MECH_DOOR_SPAWN((short) 0x2BE),
    MECH_DOOR_REMOVE((short) 0x2BF),
    SPAWN_EXTRACTOR((short) 0x2DD),//2C5
    REMOVE_EXTRACTOR((short) 0x2DE),//2C6

    MOVE_HEALER((short) 0x2CD),
    PULLEY_STATE((short) 0x2CE),
    MONSTER_CARNIVAL_MESSAGE((short) 0x2D4),//2CE

    MONSTER_CARNIVAL_RANKING((short) 0x999),//2D8
    SHEEP_RANCH_INFO((short) 0x301),
    SHEEP_RANCH_CLOTHES((short) 0x999),//0x302
    WITCH_TOWER((short) 0x999),//0x303
    EXPEDITION_CHALLENGE((short) 0x999),//0x304
    ZAKUM_SHRINE((short) 0x305),
    CHAOS_ZAKUM_SHRINE((short) 0x306),
    PVP_TYPE((short) 0x307),
    PVP_TRANSFORM((short) 0x308),
    PVP_DETAILS((short) 0x309),
    PVP_ENABLED((short) 0x30A),
    PVP_SCORE((short) 0x30B),
    PVP_RESULT((short) 0x30C),
    PVP_TEAM((short) 0x30D),
    PVP_SCOREBOARD((short) 0x30E),
    PVP_POINTS((short) 0x310),
    PVP_KILLED((short) 0x311),
    PVP_MODE((short) 0x312),
    PVP_ICEKNIGHT((short) 0x313),//
    HORNTAIL_SHRINE((short) 0x2E1),
    CAPTURE_FLAGS((short) 0x2E2),
    CAPTURE_POSITION((short) 0x2E3),
    CAPTURE_RESET((short) 0x2E4),
    PINK_ZAKUM_SHRINE((short) 0x2E5),
    LOGOUT_GIFT((short) 0x2FB),
    TOURNAMENT((short) 0x236),
    TOURNAMENT_MATCH_TABLE((short) 0x237),
    TOURNAMENT_SET_PRIZE((short) 0x238),
    TOURNAMENT_UEW((short) 0x239),
    TOURNAMENT_CHARACTERS((short) 0x23A),
    WEDDING_PROGRESS((short) 0x236),
    WEDDING_CEREMONY_END((short) 0x237),
    CS_CHARGE_CASH((short) 0x2CA),
    CS_EXP_PURCHASE((short) 0x23B),
    GIFT_RESULT((short) 0x23C),
    CHANGE_NAME_CHECK((short) 0x23D),
    CHANGE_NAME_RESPONSE((short) 0x23E),
    CS_MESO_UPDATE((short) 0x35F),//35F
    //0x314 int itemid int sn
    CASH_SHOP((short) 0x3A2),//372
    CASH_SHOP_UPDATE((short) 0x3A3),//373
    GACHAPON_STAMPS((short) 0x253),
    FREE_CASH_ITEM((short) 0x254),
    CS_SURPRISE((short) 0x255),
    XMAS_SURPRISE((short) 0x256),
    ONE_A_DAY((short) 0x258),
    NX_SPEND_GIFT((short) 0x999),
    RECEIVE_GIFT((short) 0x25A),//new v145
    RANDOM_CHECK((short) 0x274),//25E

    PET_AUTO_CURE((short) 0x37F),//379
    START_TV((short) 0x380),//37A
    REMOVE_TV((short) 0x381),//37B
    ENABLE_TV((short) 0x37C),//37C
    GM_ERROR((short) 0x26D),
    ALIEN_SOCKET_CREATOR((short) 0x341),
    GOLDEN_HAMMER((short) 0x279),
    BATTLE_RECORD_DAMAGE_INFO((short) 0x27A),
    CALCULATE_REQUEST_RESULT((short) 0x27B),
    BOOSTER_PACK((short) 0x999),
    BOOSTER_FAMILIAR((short) 0x999),
    BLOCK_PORTAL((short) 0x999),
    NPC_CONFIRM((short) 0x999),
    RSA_KEY((short) 0x999),
    LOGIN_AUTH((short) 0x999),
    BUFF_BAR((short) 0x999),
    GAME_POLL_REPLY((short) 0x999),
    GAME_POLL_QUESTION((short) 0x999),
    SIDEKICK_OPERATION((short) 0x999),
    FARM_PACKET1((short) 0x35C),
    FARM_ITEM_PURCHASED((short) 0x35D),
    FARM_ITEM_GAIN((short) 0x358),
    HARVEST_WARU((short) 0x35A),
    FARM_MONSTER_GAIN((short) 0x999),
    FARM_INFO((short) 0x999),
    FARM_MONSTER_INFO((short) 0x369),
    FARM_QUEST_DATA((short) 0x36A),
    FARM_QUEST_INFO((short) 0x36B),
    FARM_MESSAGE((short) 0x999),//36C
    UPDATE_MONSTER((short) 0x36D),
    AESTHETIC_POINT((short) 0x36E),
    UPDATE_WARU((short) 0x36F),
    FARM_EXP((short) 0x374),
    FARM_PACKET4((short) 0x375),
    QUEST_ALERT((short) 0x377),
    FARM_PACKET8((short) 0x378),
    FARM_FRIENDS_BUDDY_REQUEST((short) 0x37B),
    FARM_FRIENDS((short) 0x37C),
    FARM_USER_INFO((short) 0x3F0),//388
    FARM_AVATAR((short) 0x3F2),//38A
    FRIEND_INFO((short) 0x3F5),//38D
    FARM_RANKING((short) 0x3F7),//38F
    SPAWN_FARM_MONSTER1((short) 0x3FB),//393
    SPAWN_FARM_MONSTER2((short) 0x3FC),//394
    RENAME_MONSTER((short) 0x3FD),//395
    STRENGTHEN_UI((short) 0x408),//402
    //Unplaced:
    DEATH_COUNT((short) 0x206),
    REDIRECTOR_COMMAND((short) 0x1337),
    
    RESET_SCREEN((short) 0x999),
    UNKNOWN;

    private short code = -2;
    public static boolean record = false;

    private SendPacketOpcode() {
    }

    private SendPacketOpcode(short code) {
        this.code = code;
    }

    @Override
    public void setValue(short code) {
        this.code = code;
    }

    @Override
    public short getValue() {
        return getValue(true);
    }

    public short getValue(boolean show) {
        if (show && ServerConfig.logPackets) {
            if (isRecordHeader(this)) {
                record = true;
                String tab = "";
                for (int i = 4; i > name().length() / 8; i--) {
                    tab += "\t";
                }
                FileoutputUtil.log("logs/数据包_编写.txt", "[发送]\t" + name() + tab + "\t包头:0x" + StringUtil.getLeftPaddedStr(String.valueOf(code), '0', 4) + "\r\n");
            } else {
                record = false;
            }
        }
        return code;
    }

    public String getType(short code) {
        String type = null;
        if (code >= 0 && code < 0xE || code >= 0x17 && code < 0x21) {
            type = "CLogin";
        } else if (code >= 0xE && code < 0x17) {
            type = "LoginSecure";
        } else if (code >= 0x21 && code < 0xCB) {
            type = "CWvsContext";
        } else if (code >= 0xD2) {
            type = "CField";
        }
        return type;
    }

    public static String nameOf(int value) {
        for (SendPacketOpcode opcode : SendPacketOpcode.values()) {
            if (opcode.getValue(false) == value) {
                return opcode.name();
            }
        }
        return "UNKNOWN";
    }

    public static boolean isRecordHeader(SendPacketOpcode opcode) {
        switch (opcode) {
//            case WARP_TO_MAP:
//            case GUILD_OPERATION:
//            case PARTY_OPERATION:
//            case GIVE_BUFF:
//            case SPAWN_PLAYER:
//            case DROP_ITEM_FROM_MAPOBJECT:
//            case INVENTORY_OPERATION:
            case UNKNOWN:
                return true;
            default:
                return false;
        }
    }

    public static boolean isSpamHeader(SendPacketOpcode opcode) {
        switch (opcode) {
            case PING:
  //          case NPC_ACTION:
//            case AUTH_RESPONSE:
//            case SERVERLIST:
//            case UPDATE_STATS:
      //      case MOVE_PLAYER:
     //       case SPAWN_NPC:
    //        case SPAWN_NPC_REQUEST_CONTROLLER:
     //       case REMOVE_NPC:
     //       case MOVE_MONSTER:
     //       case MOVE_MONSTER_RESPONSE:
     //       case SPAWN_MONSTER:
     //       case SPAWN_MONSTER_CONTROL:
//            case HAKU_MOVE:
//            case MOVE_SUMMON:
//            case MOVE_FAMILIAR:
//            case ANDROID_MOVE:
//            case INVENTORY_OPERATION:
    //       case MOVE_PET:
//            case SHOW_SPECIAL_EFFECT:
//            case DROP_ITEM_FROM_MAPOBJECT:
//            case REMOVE_ITEM_FROM_MAP:
//            case UPDATE_PARTYMEMBER_HP:
//            case DAMAGE_PLAYER:
//            case SHOW_MONSTER_HP:
//            case CLOSE_RANGE_ATTACK:
//            case RANGED_ATTACK:
//            case ARAN_COMBO:
//            case REMOVE_BG_LAYER:
//            case SPECIAL_STAT:
//            case TOP_MSG:
//            case ANGELIC_CHANGE:
//            case UPDATE_CHAR_LOOK:
    //        case KILL_MONSTER:
                return true;
            default:
                return false;
        }
    }
}
