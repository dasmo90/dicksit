package de.marmor.dicksit.game.remote;


import junit.framework.Assert;

import org.junit.Test;

public class ServerPwTest {

    @Test
    public void testLong() {

        String expectedIp = "10.53.132.66";
        String otherIp = "10.21.254.2";
        String pw = ServerPw.retrieveServerPw(expectedIp);
        String ip = ServerPw.retrieveServerIp(otherIp, pw);
        Assert.assertEquals(expectedIp, ip);
    }


    @Test
    public void testShort() {

        String expectedIp = "192.168.132.66";
        String otherIp = "192.168.254.2";
        String pw = ServerPw.retrieveServerPw(expectedIp);
        String ip = ServerPw.retrieveServerIp(otherIp, pw);
        Assert.assertEquals(expectedIp, ip);
    }
}
