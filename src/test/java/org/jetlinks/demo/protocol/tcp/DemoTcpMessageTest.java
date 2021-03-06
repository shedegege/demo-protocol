package org.jetlinks.demo.protocol.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.apache.commons.codec.binary.Hex;
import org.jetlinks.core.message.codec.EncodedMessage;
import org.jetlinks.core.message.property.ReadPropertyMessage;
import org.jetlinks.demo.protocol.tcp.message.AuthRequest;
import org.jetlinks.demo.protocol.tcp.message.ReadProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class DemoTcpMessageTest {

    @Test
    void test() {
        DemoTcpMessage message = DemoTcpMessage.of(MessageType.AUTH_REQ, AuthRequest.of(12345, "admin"));

        byte[] data = message.toBytes();
        System.out.println(Hex.encodeHexString(data));

        DemoTcpMessage decode = DemoTcpMessage.of(data);

        System.out.println(decode);

        Assertions.assertEquals(message.getType(), decode.getType());
        Assertions.assertArrayEquals(message.getData().toBytes(), decode.getData().toBytes());


    }

    @Test
    void encodeTest() {
        DemoTcpMessageCodec demoTcpMessageCodec = new DemoTcpMessageCodec(null);
        ReadPropertyMessage readPropertyMessage = new ReadPropertyMessage();
        readPropertyMessage.setCode("10001");
        readPropertyMessage.setDeviceId("111111");
        readPropertyMessage.setMessageId("test");
        readPropertyMessage.setTimestamp(LocalDateTime.now().getNano());
        DemoTcpMessage of = DemoTcpMessage.of(MessageType.READ_PROPERTY, ReadProperty.of(readPropertyMessage));
        EncodedMessage simple = EncodedMessage.simple(of.toByteBuf());
        ByteBuf byteBuf = simple.getPayload();
        byte[] payload = ByteBufUtil.getBytes(byteBuf, 0, byteBuf.readableBytes(), false);
        DemoTcpMessage message = DemoTcpMessage.of(payload);
        System.out.println(message.getType().getText());
        ReadProperty data = (ReadProperty) message.getData();
        System.out.println(data.getReadPropertyMessage().toString());
    }
}