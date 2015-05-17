package com.kenvifire.mina.timeserver;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Date;

/**
 * Created by hannahzhang on 15/5/16.
 */
public class TimeServerHandler extends IoHandlerAdapter{
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("IDLE " + session.getIdleCount(status));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String str = message.toString();

        if(str.trim().equalsIgnoreCase("quit")){
            session.close();
            return;
        }
        Date date = new Date();
        session.write(date.toString());
        System.out.println("Message written...");

    }
}
