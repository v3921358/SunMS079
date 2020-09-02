/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */importPackage(Packages.tools);function act(){rm.spawnMonster(9300061,1,0,0);rm.getClient().getMap().startMapEffect("\u4fdd\u62a4\u6708\u4eae\u5154\u5b50\uff0c\u6363\u788e\u78e8\u574a\uff0c\u6536\u96c610\u4e2a\u6708\u4eae\u5154\u5b50\u7684\u7c73\u7cd5!",5120016,7E3);rm.getClient().getMap().broadcastMessage(MaplePacketCreator.bunnyPacket());rm.getClient().getMap().broadcastMessage(MaplePacketCreator.showHPQMoon());rm.getClient().getMap().showAllMonsters()};