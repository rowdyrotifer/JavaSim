package com.marklalor.javasim.content.interfacing.ui.capabilities;

import com.google.common.collect.Range;

public interface Rewindable
{
    public void setFrame(int frame);
    
    public Range<Integer> getFrameRange();
}
