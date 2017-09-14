package com.marklalor.javasim.model.session;

import com.google.common.collect.Range;
import com.marklalor.javasim.model.data.DataSeries;
import com.marklalor.javasim.proto.DataMessageProto.DataMessage;

public interface Session {
	
	public int getSessionID();
	
	public DataMessage getDataMessage(DataSeries series, Range<Integer> instanceRange);
	
	public DataSeries getSeries(int seriesID);
}
