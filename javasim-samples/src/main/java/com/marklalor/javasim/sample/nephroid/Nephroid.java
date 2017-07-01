package com.marklalor.javasim.sample.nephroid;

import com.marklalor.javasim.data.SimInput;
import com.marklalor.javasim.data.SimInterface;
import com.marklalor.javasim.data.SimOutput;
import com.marklalor.javasim.data.input.InputInt32;
import com.marklalor.javasim.data.output.OutputImage;

@SimInterface(group="com.marklalor")
public class Nephroid  
{
    public void draw(
            @SimInput(group="javasim", name="frame") InputInt32 frame,
            @SimOutput(name="nephriod") OutputImage image)
    {
        
    }
}
