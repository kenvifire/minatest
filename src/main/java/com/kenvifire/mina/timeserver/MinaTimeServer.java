package com.kenvifire.mina.timeserver;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by hannahzhang on 15/5/16.
 */
public class MinaTimeServer {
    private static final int PORT = 9123;

    public static void main(String[] args) throws Exception{
        if(args[0].equals("-MINA")) {
            IoAcceptor acceptor = new NioSocketAcceptor();
            acceptor.getFilterChain().addLast("logger", new LoggingFilter());
            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
            acceptor.setHandler(new TimeServerHandler());
            acceptor.getSessionConfig().setReadBufferSize(2048);
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
            acceptor.bind(new InetSocketAddress(PORT));
        }else if(args[0].equals("-CS")){
            ServerSocket server = new ServerSocket(PORT);
            Socket client = null;
            while(true){
                client = server.accept();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                while((line = br.readLine()) != null){
                    if(line.trim().equals("quit")) {
                        break;
                    }else{
                        Date date = new Date();
                        bw.write(date.toString());
                        bw.flush();
                        System.out.println("Message written...");
                    }
                }

                br.close();
                bw.close();
            }
        }else{
            throw new Exception("Unsupported mode");
        }
    }
}
