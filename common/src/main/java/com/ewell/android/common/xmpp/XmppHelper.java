package com.ewell.android.common.xmpp;

import com.ewell.android.common.config.InitConfigModel;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.common.exception.ExceptionEnum;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * Created by Dongjg on 2016-7-12.
 */
public class XmppHelper {
    private XmppHelper() {
    }

    private XMPPConnection connection = null;
    private InitConfigModel _initConfigModel = null;
    private XmppMsgDelegate _msgDelegate = null;

    public XmppHelper(InitConfigModel initConfigModel, XmppMsgDelegate msgDelegate) {
        if (connection == null) {
            this._initConfigModel = initConfigModel;
            this._msgDelegate = msgDelegate;

            ConnectionConfiguration configuration = new ConnectionConfiguration(initConfigModel.getXmppServerIP(),
                    initConfigModel.getXmppServerPort(), initConfigModel.getXmppServerIP());
            configuration.setReconnectionAllowed(true);
            configuration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            configuration.setSendPresence(true);
            configuration.setCompressionEnabled(false);
            SASLAuthentication.supportSASLMechanism("PLAIN", 0);

            this.connection = new XMPPTCPConnection(configuration);

            PacketFilter filter = new AndFilter(new PacketTypeFilter(Message.class),
                    new FromMatchesFilter(this._initConfigModel.getXmppServerJID(), true));
            PacketListener myListener = new PacketListener() {
                public void processPacket(Packet packet) {
                    String receiveXml = packet.toXML().toString();
                    _msgDelegate.ReciveMessage(receiveXml);
                }
            };
            connection.addPacketListener(myListener, filter);
        }
    }

    //0：未知 1：成功 2：失败
    private int connectedFlag = 0;
    //0:未登录 1：已登录
    private int loginFlag = 0;

    /*
    * 连接xmpp服务器
    * */
    public void Connect() throws EwellException {
        try {
            if (!this.connection.isConnected()) {
                connectedFlag = 0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            connection.connect();
                            connectedFlag = 1;

                        } catch (Exception ex) {
                            connectedFlag = 2;
                        }

                    }
                }).start();
            } else {
                connectedFlag = 1;
            }
            while (connectedFlag == 0) {
                Thread.sleep(10);
            }

            if (connectedFlag == 2) {
                throw new EwellException(ExceptionEnum.NetError);
            }

            if (loginFlag == 0) {
                this.connection.login(this._initConfigModel.getXmppUserName() + "@" + this._initConfigModel.getXmppServerIP(),
                        this._initConfigModel.getXmppUserPWD(), "androidphone");
                loginFlag = 1;
                Presence presence = new Presence(Presence.Type.available);
                presence.setStatus("andorid user online");
                connection.sendPacket(presence);
            }


        } catch (Exception e) {
            throw new EwellException(ExceptionEnum.NetError);
        }

    }

    /*
    * 发送请求数据
    * */
    public void SendMessage(String subjectXml, String bodyXml) throws EwellException {
        if (!this.connection.isConnected()) {
            throw new EwellException(ExceptionEnum.NetError);
        }

        try {
            Chat service = ChatManager.getInstanceFor(connection).createChat(this._initConfigModel.getXmppServerJID(), null);
            Message m = new Message();
            m.setSubject(subjectXml);
            m.setBody(bodyXml);

            service.sendMessage(m);
        } catch (SmackException ex) {
            throw new EwellException(ExceptionEnum.NetError);
        }
    }

    /*
    * 是否已经连接xmpp服务器
    * */
    public boolean IsConnected() {
        return this.connection.isConnected();
    }

    /*
    * 关闭xmpp连接
    * */
    public void CloseConnection() {
        if (this.connection.isConnected()) {
            try {
                Presence presence = new Presence(Presence.Type.unavailable);
                presence.setStatus("andorid user unline");
                this.connection.sendPacket(presence);
                this.connection.disconnect();
                loginFlag = 0;
            } catch (SmackException ex) {
            }
        }
    }

}
