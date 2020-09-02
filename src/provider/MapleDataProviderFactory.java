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
package provider;

import java.io.File;
import java.io.IOException;
import provider.nx.NXDataProvider;
import provider.wz.WZFile;
import provider.wz.XMLWZFile;

public class MapleDataProviderFactory {

    private final static String wzPath = System.getProperty("wzpath");

    private static MapleDataProvider getWZ(Object in) {
        return getWZ(in, false);
    }

    private static MapleDataProvider getWZ(Object in, boolean provideImages) {
        if (in instanceof File) {
            File wzFile = (File) in;
            if (wzFile.getName().toLowerCase().endsWith("nx") && !wzFile.isDirectory()) {
                try {
                    return new NXDataProvider(wzFile.getPath());
                } catch (IOException e) {
                    throw new RuntimeException("Loading NX File failed", e);
                }
            } else if (wzFile.getName().toLowerCase().endsWith("wz") && !wzFile.isDirectory()) {
                try {
                    return new WZFile(wzFile, provideImages);
                } catch (IOException e) {
                    throw new RuntimeException("Loading WZ File failed", e);
                }
            } else {
                return new XMLWZFile(wzFile);
            }
            //return new MapleDataProvider((File) in);
        }
        throw new IllegalArgumentException("Can't create data provider for input " + in);
    }

    public static MapleDataProvider getDataProvider(Object in) {
        return getWZ(in);
    }

    public static File fileInWZPath(String filename) {
        return new File(wzPath, filename);
    }
}
