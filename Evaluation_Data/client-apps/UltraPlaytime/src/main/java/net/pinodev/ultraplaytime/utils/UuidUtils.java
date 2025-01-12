package net.pinodev.ultraplaytime.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

public class UuidUtils {

    public byte[] toBytes(UUID uuid){
        return ByteBuffer.wrap(new byte[16])
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits()).array();
    }

    public UUID fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(), buffer.getLong());
    }
}
