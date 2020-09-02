package scripting;

import client.MapleClient;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import server.life.MapleLifeFactory;
import server.life.MapleNPC;
import server.quest.MapleQuest;
import tools.FileoutputUtil;

public class NPCScriptManager extends AbstractScriptManager {

    private final Map<MapleClient, NPCConversationManager> cms = new WeakHashMap<>();
    private static final NPCScriptManager instance = new NPCScriptManager();

    public static final NPCScriptManager getInstance() {
        return instance;
    }

    public final boolean hasScript(final MapleClient c, final int npc, String script) {
        Invocable iv = getInvocable("npc/" + npc + ".js", c, true);
        if (script != null && !script.isEmpty()) {
            iv = getInvocable("npc/" + script + ".js", c, true);
        }
        return iv != null;
    }

    public void start(MapleClient c, int npcId) {
        start(c, npcId, null);
    }

    public final void start(final MapleClient c, final int npc, String script) {
        start(c, npc, 0, script);
    }

    public final void start(final MapleClient c, final int npc, final int mode, String script) {
        final Lock lock = c.getNPCLock();
        lock.lock();
        try {
            if (c.getPlayer().isAdmin()) {
                c.getPlayer().dropMessage(5, "对话NPC：" + npc + " 模式：" + script + "　小字號：" + mode);
            }
            if (!cms.containsKey(c) && c.canClickNPC()) {
                Invocable iv = getInvocable("npc/" + npc + ".js", c, true); //safe disposal
                if (script == null) {
                    if (mode != 0) {
                        iv = getInvocable("npc/" + npc + "_" + mode + ".js", c, true); //safe disposal
                    } else {
                        iv = getInvocable("npc/" + npc + ".js", c, true); //safe disposal
                    }
                } else {
                    iv = getInvocable("special/" + script + ".js", c, true); //safe disposal
                }
                if (iv == null) {
                    iv = getInvocable("special/notcoded.js", c, true); //safe disposal
                    if (iv == null) {
                        if (iv == null) {
                            System.out.println("[NPC脚本] 找不到NPC脚本(ID:" + npc + "), 特殊模式(" + script + "),所在地图(ID:" + c.getPlayer().getMapId() + ")");
                        }
                        dispose(c);
                        return;
                    }
                }
                final ScriptEngine scriptengine = (ScriptEngine) iv;
                final NPCConversationManager cm = new NPCConversationManager(c, npc, -1, script, (byte) -1, iv, mode);
                cms.put(c, cm);
                scriptengine.put("cm", cm);

                c.getPlayer().setConversation(1);
                c.setClickedNPC();
                //String npcmsg = "Started NPC ID: " + npc;
                //String scriptmsg = "Started NPC ID: " + npc + " with script: " + script;
                //System.out.println(script != null ? scriptmsg : npcmsg);
                try {
                    iv.invokeFunction("start"); // Temporary until I've removed all of start
                } catch (NoSuchMethodException nsme) {
                    iv.invokeFunction("action", (byte) 1, (byte) 0, 0);
                }
            }
        } catch (final ScriptException | NoSuchMethodException e) {
            System.err.println("[NPC脚本] NPC脚本出错（ID : " + npc + "）模式：" + script + " \r\n错误内容: " + e);
            FileoutputUtil.log("logs/异常/脚本异常.log", "NPC脚本出错（ID : " + npc + "）模式" + script + ".\r\n错误信息：" + e);
            dispose(c);
        } finally {
            lock.unlock();
        }
    }

    public final void action(final MapleClient c, final byte mode, final byte type, final int selection) {
        if (mode != -1) {
            final NPCConversationManager cm = cms.get(c);
            if (cm == null || cm.getLastMsg() > -1) {
                return;
            }
            final Lock lock = c.getNPCLock();
            lock.lock();
            try {
                if (cm.pendingDisposal) {
                    dispose(c);
                } else {
                    c.setClickedNPC();
//                    System.err.println("mode " + mode);
//                    System.err.println("type " + type );
//                    System.err.println("sel "+ selection);
                    cm.getIv().invokeFunction("action", mode, type, selection);
                }
            } catch (final ScriptException | NoSuchMethodException e) {
                int npcId = cm.getNpc();
                String npcMode = cm.getScript();
                System.err.println("[NPC脚本] NPC脚本出错（ID : " + npcId + "）模式：" + npcMode + "  \r\n错误内容：" + e);
                dispose(c);
                FileoutputUtil.log("logs/异常/脚本异常.log", "NPC脚本出错（ID : " + npcId + "）模式：" + npcMode + ". \r\n错误信息：" + e);
            } finally {
                lock.unlock();
            }
        }
    }

    public final void startQuest(final MapleClient c, final int npc, final int quest) {
        if (!MapleQuest.getInstance(quest).canStart(c.getPlayer(), null)) {
            return;
        }
        final Lock lock = c.getNPCLock();
        lock.lock();
        try {
            if (!cms.containsKey(c) && c.canClickNPC()) {
                final Invocable iv = getInvocable("quest/" + quest + ".js", c, true);
                if (iv == null) {
                    if (c.getPlayer().isAdmin()) {
                        c.getPlayer().dropMessage(5, "开始任务脚本不存在 NPC：" + npc + " Quest：" + quest);
                    }
                    dispose(c);
                    FileoutputUtil.log("logs/异常/任务脚本异常.txt", "开始任务脚本不存在 NPC：" + npc + " Quest：" + quest);
                    return;
                }
                final ScriptEngine scriptengine = (ScriptEngine) iv;
                final NPCConversationManager cm = new NPCConversationManager(c, npc, quest, null, (byte) 0, iv, 0);
                cms.put(c, cm);
                scriptengine.put("qm", cm);

                c.getPlayer().setConversation(1);
                c.setClickedNPC();
                iv.invokeFunction("start", (byte) 1, (byte) 0, 0); // start it off as something
            }
        } catch (final ScriptException | NoSuchMethodException e) {
            System.err.println("Error executing Quest script. (" + quest + ")..NPCID: " + npc + ":" + e);
            FileoutputUtil.log("logs/异常/任务脚本异常.txt", "执行任务脚本失败 任务ID: (" + quest + ")..NPCID: " + npc + ". \r\n错误信息: " + e);
            dispose(c);
        } finally {
            lock.unlock();
        }
    }

    public final void startQuest(final MapleClient c, final byte mode, final byte type, final int selection) {
        final Lock lock = c.getNPCLock();
        final NPCConversationManager cm = cms.get(c);
        if (cm == null || cm.getLastMsg() > -1) {
            return;
        }
        lock.lock();
        try {
            if (cm.pendingDisposal) {
                dispose(c);
            } else {
                c.setClickedNPC();
                cm.getIv().invokeFunction("start", mode, type, selection);
            }
        } catch (ScriptException | NoSuchMethodException e) {
            System.err.println("Error executing Quest script. (" + cm.getQuest() + ")...NPC: " + cm.getNpc() + ":" + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing Quest script. (" + cm.getQuest() + ")..NPCID: " + cm.getNpc() + ":" + e);
            dispose(c);
        } finally {
            lock.unlock();
        }
    }

    public final void endQuest(final MapleClient c, final int npc, final int quest, final boolean customEnd) {
        if (!customEnd && !MapleQuest.getInstance(quest).canComplete(c.getPlayer(), null)) {
            return;
        }
        final Lock lock = c.getNPCLock();
        lock.lock();
        try {
            if (!cms.containsKey(c) && c.canClickNPC()) {
                final Invocable iv = getInvocable("quest/" + quest + ".js", c, true);
                if (iv == null) {
                    dispose(c);
                    return;
                }
                final ScriptEngine scriptengine = (ScriptEngine) iv;
                final NPCConversationManager cm = new NPCConversationManager(c, npc, quest, null, (byte) 1, iv, 0);
                cms.put(c, cm);
                scriptengine.put("qm", cm);

                c.getPlayer().setConversation(1);
                c.setClickedNPC();
                iv.invokeFunction("end", (byte) 1, (byte) 0, 0); // start it off as something
            }
        } catch (ScriptException | NoSuchMethodException e) {
            System.err.println("Error executing Quest script. (" + quest + ")..NPCID: " + npc + ":" + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing Quest script. (" + quest + ")..NPCID: " + npc + ":" + e);
            dispose(c);
        } finally {
            lock.unlock();
        }
    }

    public final void endQuest(final MapleClient c, final byte mode, final byte type, final int selection) {
        final Lock lock = c.getNPCLock();
        final NPCConversationManager cm = cms.get(c);
        if (cm == null || cm.getLastMsg() > -1) {
            return;
        }
        lock.lock();
        try {
            if (cm.pendingDisposal) {
                dispose(c);
            } else {
                c.setClickedNPC();
                cm.getIv().invokeFunction("end", mode, type, selection);
            }
        } catch (ScriptException | NoSuchMethodException e) {
            System.err.println("Error executing Quest script. (" + cm.getQuest() + ")...NPC: " + cm.getNpc() + ":" + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing Quest script. (" + cm.getQuest() + ")..NPCID: " + cm.getNpc() + ":" + e);
            dispose(c);
        } finally {
            lock.unlock();
        }
    }

    public final void startItemScript(final MapleClient c, final int npc, final String script) {
        final Lock lock = c.getNPCLock();
        lock.lock();
        try {
            if (!cms.containsKey(c) && c.canClickNPC()) {
                final Invocable iv = getInvocable("item/" + script + ".js", c, true);
                if (iv == null) {
                    System.out.println("New scripted item : " + script);
                    dispose(c);
                    return;
                }
                final ScriptEngine scriptengine = (ScriptEngine) iv;
                final NPCConversationManager cm = new NPCConversationManager(c, npc, -1, script, (byte) -1, iv, 0);
                cms.put(c, cm);
                scriptengine.put("im", cm);
                c.getPlayer().setConversation(1);
                c.setClickedNPC();
                iv.invokeFunction("use");
            }
        } catch (final ScriptException | NoSuchMethodException e) {
            System.err.println("Error executing Item script, SCRIPT : " + script + ". " + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing Item script, SCRIPT : " + script + ". " + e);
            dispose(c);
        } finally {
            lock.unlock();
        }
    }

    public final void dispose(final MapleClient c) {
        final NPCConversationManager npccm = cms.get(c);
        if (npccm != null) {
            cms.remove(c);
            if (npccm.getType() == -1) {
                c.removeScriptEngine("scripts/npc/" + npccm.getNpc() + ".js");
                c.removeScriptEngine("scripts/npc/" + npccm.getNpc() + "_" + npccm.getMod() + ".js");
                c.removeScriptEngine("scripts/npc/" + npccm.getScript() + ".js");
                c.removeScriptEngine("scripts/npc/notcoded.js");
            } else {
                c.removeScriptEngine("scripts/quest/" + npccm.getQuest() + ".js");
            }
        }
        if (c.getPlayer() != null && c.getPlayer().getConversation() == 1) {
            c.getPlayer().setConversation(0);
        }
    }

    public final NPCConversationManager getCM(final MapleClient c) {
        return cms.get(c);
    }
}
