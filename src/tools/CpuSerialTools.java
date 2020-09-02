/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 *
 * @author wubin
 */
public class CpuSerialTools {

    public static String getCPUSerial() {

        String result = "";

        try {

            File file = File.createTempFile("tmp", ".vbs");

            file.deleteOnExit();

            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_Processor\") \n"
                    + "For Each objItem in colItems \n"
                    + "    Wscript.Echo objItem.ProcessorId \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            // + "    exit for  \r\n" + "Next";
            fw.write(vbs);

            fw.close();

            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());

            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));

            String line;

            while ((line = input.readLine()) != null) {

                result += line;

            }

            input.close();

            file.delete();

        } catch (Exception e) {

            e.fillInStackTrace();

        }

        if (result.trim().length() < 1 || result == null) {

            result = "无CPU_ID被读取";

        }

        return result.trim();

    }
}
