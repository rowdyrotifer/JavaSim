package com.marklalor.javasim.misc;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MiscUtil
{
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-M-d_H-m-s-S");
    
    public static String getTimestamp()
    {
        return timestampFormat.format(new Date());
    }
}
