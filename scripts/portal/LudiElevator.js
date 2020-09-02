/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var status;function start(){status=-1;action(1,1,0)}function action(a,b,c){0>a||(1==a?status++:status--,0==status&&(222020200==cm.getMapId()?(cm.TimeMoveMap(222020210,222020100,13),cm.getPlayer().dropMessage(6,"\u6863\u9992\u4e14 \u6478\u7bee \u79cb\u5e9c\u5777\u80f6 \u556a 2\u6478, \u9152\u9600\u4ed8\u9611\u635e \u4e50\u7ef0 \u6478\u6d9d\u806a\u4fc3.")):(cm.TimeMoveMap(222020110,222020200,13),cm.getPlayer().dropMessage(6,"\u6863\u9992\u4e14 \u6478\u7bee \u79cb\u5e9c\u5777\u80f6 \u556a 99\u6478, \u98ce\u53fc\u5b8f\u5e9c\u51b3\u635e \u4e50\u7ef0 \u6478\u6d9d\u806a\u4fc3."))));cm.dispose()};