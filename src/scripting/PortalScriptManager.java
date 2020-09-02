/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package scripting;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import client.MapleClient;
import java.io.*;
import java.util.stream.Collectors;
import server.MaplePortal;
import tools.EncodingDetect;
import tools.FileoutputUtil;

public class PortalScriptManager {

    private static final PortalScriptManager instance = new PortalScriptManager();
    private final Map<String, PortalScript> scripts = new HashMap<>();
    private final static ScriptEngineFactory sef = new ScriptEngineManager().getEngineByName("javascript").getFactory();

    public static PortalScriptManager getInstance() {
        return instance;
    }

    private PortalScript getPortalScript(final String scriptName) {
        if (scripts.containsKey(scriptName)) {
            return scripts.get(scriptName);
        }

        final File scriptFile = new File("scripts/portal/" + scriptName + ".js");
        if (!scriptFile.exists()) {
            return null;
        }

        InputStream in = null;
        final ScriptEngine portal = sef.getScriptEngine();
        try {
            in = new FileInputStream(scriptFile);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in, EncodingDetect.getJavaEncode(scriptFile)));
            String lines = "load('nashorn:mozilla_compat.js');" + bf.lines().collect(Collectors.joining(System.lineSeparator()));
            CompiledScript compiled = ((Compilable) portal).compile(lines);
            compiled.eval();
        } catch (final Exception e) {

            System.err.println("请检查传送点脚本名为:(" + scriptName + ".js)的文件." + e);
            FileoutputUtil.log("logs/传送点脚本异常.tzt", "请检查传送点脚本名为:(" + scriptName + ".js)的文件. " + e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final IOException e) {
                    System.err.println("ERROR CLOSING" + e);
                }
            }
        }
        final PortalScript script = ((Invocable) portal).getInterface(PortalScript.class);
        scripts.put(scriptName, script);
        return script;
    }

    public final void executePortalScript(final MaplePortal portal, final MapleClient c) {
        final PortalScript script = getPortalScript(portal.getScriptName());

        if (script != null) {
            try {
                if (c.getPlayer().isAdmin()) {
                    c.getPlayer().dropMessage("[系统提示]您已经建立与PortalScript:[" + portal.getScriptName() + ".js]的对话。" + (script != null ? "" : "(脚本不存在或异常)"));
                }
                script.enter(new PortalPlayerInteraction(c, portal));
            } catch (Exception e) {
                if (c.getPlayer().isAdmin()) {
                    c.getPlayer().dropMessage(5, "执行地图脚本过程中发生错误.请检查传送点脚本名为:( " + portal.getScriptName() + ".js)的文件，错误信息：" + e);
                }
                System.err.println("Error entering Portalscript: " + portal.getScriptName() + " : " + e);
                FileoutputUtil.log("logs/传送点脚本异常.txt", "执行地图脚本过程中发生错误.未找到传送点脚本名为:(" + portal.getScriptName() + ".js)的文件 在地图 " + c.getPlayer().getMapId() + " - " + c.getPlayer().getMap().getMapName());
            }
        } else {
            if (c.getPlayer().isAdmin()) {
                c.getPlayer().dropMessage(5, "未找到传送点脚本名为:(" + portal.getScriptName() + ".js)的文件 在地图 " + c.getPlayer().getMapId() + " - " + c.getPlayer().getMap().getMapName());
            }
            System.out.println("Unhandled portal script " + portal.getScriptName() + " on map " + c.getPlayer().getMapId());
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Unhandled portal script " + portal.getScriptName() + " on map " + c.getPlayer().getMapId());
        }
    }

    public final void clearScripts() {
        scripts.clear();
    }
}
