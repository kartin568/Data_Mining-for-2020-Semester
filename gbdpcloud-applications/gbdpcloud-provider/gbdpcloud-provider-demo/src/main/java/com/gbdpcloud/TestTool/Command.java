package com.gbdpcloud.TestTool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Command {


    public static String exeCmd(String commandStr) {
        System.out.print("todo:"+commandStr);
        String[] cm = commandStr.split(" ");
        //commandStr.replaceAll(" ", "\" \"");
        //commandStr = "\""+commandStr+"\"";
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            Process p = Runtime.getRuntime().exec(cm);
            br = new BufferedReader(new InputStreamReader(p.getInputStream(),Charset.forName("GBK") ) );
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static String  CDCommand(String path){
        String cmd = "cd "+path;
        return Command.exeCmd(cmd);
    }

    public static void main(String[] args) {
        String commandStr = "ping www.baidu.com";
        //String commandStr = "ipconfig";
        Command.exeCmd(commandStr);
    }
}