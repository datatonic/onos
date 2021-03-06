/*
* Copyright 2016-present Open Networking Foundation
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.onosproject.isis.io.isispacket.tlv.subtlv;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.primitives.Bytes;
import org.jboss.netty.buffer.ChannelBuffer;
import org.onlab.packet.Ip4Address;
import org.onosproject.isis.io.isispacket.tlv.TlvHeader;
import org.onosproject.isis.io.util.IsisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of neighbor ip address TE value.
 */
public class NeighborIpAddress extends TlvHeader implements TrafficEngineeringSubTlv {
    private static final Logger log =
            LoggerFactory.getLogger(NeighborIpAddress.class);
    private Ip4Address neighborIPAddress;

    /**
     * Creates an instance of neighbor ip address.
     *
     * @param header tlv header instance
     */
    public NeighborIpAddress(TlvHeader header) {
        this.setTlvType(header.tlvType());
        this.setTlvLength(header.tlvLength());
    }

    /**
     * Sets the neighbor ip address.
     *
     * @param neighborIPAddress ip address
     */
    public void setIpAddress(Ip4Address neighborIPAddress) {
        this.neighborIPAddress = neighborIPAddress;
    }

    /**
     * Gets the neighbor ip address.
     *
     * @return neighbor ip address
     */
    public Ip4Address neighborIPAddress() {
        return neighborIPAddress;
    }

    /**
     * Reads bytes from channel buffer.
     *
     * @param channelBuffer channel buffer instance
     */
    public void readFrom(ChannelBuffer channelBuffer) {
        while (channelBuffer.readableBytes() >= IsisUtil.FOUR_BYTES) {
            byte[] tempByteArray = new byte[IsisUtil.FOUR_BYTES];
            channelBuffer.readBytes(tempByteArray, 0, IsisUtil.FOUR_BYTES);
            this.setIpAddress(Ip4Address.valueOf(tempByteArray));

        }
    }

    /**
     * Gets the neighbor ip address as byte array.
     *
     * @return neighbor ip address as byte array
     */
    public byte[] asBytes() {
        byte[] linkSubType = null;

        byte[] linkSubTlvHeader = tlvHeaderAsByteArray();
        byte[] linkSubTlvBody = tlvBodyAsBytes();
        linkSubType = Bytes.concat(linkSubTlvHeader, linkSubTlvBody);

        return linkSubType;
    }

    /**
     * Gets byte array of neighborIPAddress.
     *
     * @return byte array of neighborIPAddress
     */
    public byte[] tlvBodyAsBytes() {

        List<Byte> linkSubTypeBody = new ArrayList<>();

        linkSubTypeBody.addAll(Bytes.asList(this.neighborIPAddress.toOctets()));


        return Bytes.toArray(linkSubTypeBody);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NeighborIpAddress that = (NeighborIpAddress) o;
        return Objects.equal(neighborIPAddress, that.neighborIPAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(neighborIPAddress);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .omitNullValues()
                .add("localInterfaceIPAddress", neighborIPAddress)
                .toString();
    }
}