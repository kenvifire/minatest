package com.kenvifire.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by hannahzhang on 15/5/20.
 */
public class NIOTimeServer {

    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.socket().bind(new InetSocketAddress(8000));

        Selector selector = Selector.open();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            selector.select();

            Iterator<SelectionKey> ite = selector.selectedKeys().iterator();

            while(ite.hasNext()){

                SelectionKey key = ite.next();

                ite.remove();

                if(key.isAcceptable()){
                    ServerSocketChannel server = (ServerSocketChannel)key.channel();
                    SocketChannel channel = server.accept();
                    channel.configureBlocking(false);

                    channel.register(selector,SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    SocketChannel channel = (SocketChannel)key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(10);
                    channel.read(buffer);
                    byte[] data = buffer.array();
                    String msg = new String(data).trim();

                    ByteBuffer outBuff = null;
                    if("date".equalsIgnoreCase(msg)){
                        outBuff = ByteBuffer.wrap(Calendar.getInstance().getTime().toString().getBytes());
                        channel.write(outBuff);
                    }else if("quit".equalsIgnoreCase(msg)){
                        channel.close();

                    }else {
                        outBuff = ByteBuffer.wrap("Invalid command".toString().getBytes());
                        channel.write(outBuff);
                    }

                }


            }
        }
    }


}
