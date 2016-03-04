package com.bookmypacket.bmpclub.ui.frags;

import com.bookmypacket.bmpclub.dto.BMPPacket;

import java.util.List;

/**
 * Created by Manish on 28-01-2016.
 */
public interface OnPacketListResult
{
    void packetsRecieved(final List<BMPPacket> packets, final int i);
}
