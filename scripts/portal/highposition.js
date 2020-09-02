/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(15>a.getPlayer().getLevel()||a.isQuestFinished(29004))return!1;a.isQuestActive(29004)||(a.forceStartQuest(29004),a.updateInfoQuest(27017,"enter\x3d00000"),a.forceStartQuest(27018,"0"));for(var e=a.getInfoQuest(27017),c=parseInt(a.getQuestRecord(27018).getCustomData()),d="enter\x3d",f=[12E7,103E6,101020300,102E6,200080100],g=!1,b=0;b<f.length;b++){var h=!1;a.getPlayer().getMapId()==f[b]&&e.substring(b+6,b+7).equals("0")&&(d+="1",g=h=!0);h||(d+=e.substring(b+6,b+7))}g&&(a.updateInfoQuest(27017,d),a.forceStartQuest(27018,c+1,!0),a.getPlayer().dropMessage(-1,c+1+"/5 \u5b8c\u6210"),a.getPlayer().dropMessage(-1,"\u76ee\u524d\u6b63\u5728\u91c7\u53d6\u7684\u6807\u9898 - \u4e00\u4e2a\u8c01\u7ad9\u5728\u4e0a\u9762"),a.showQuestMsg("\u76ee\u524d\u6b63\u5728\u91c7\u53d6\u7684\u6807\u9898 - \u4e00\u4e2a\u8c01\u7ad9\u5728\u4e0a\u9762 "+(c+1)+"/5 \u5b8c\u6210"))};