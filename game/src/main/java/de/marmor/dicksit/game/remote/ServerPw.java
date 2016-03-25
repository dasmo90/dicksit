package de.marmor.dicksit.game.remote;

public class ServerPw {

    private static char[] characters = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '?', '!'
    };

    private static int indexOf(char c) {
        for (int i = 0; i < characters.length; i++) {
            if (c == characters[i]) {
                return i;
            }
        }
        return -1;
    }

    private static int LENGTH = 2;

    public static String retrieveServerIp(String ip, String serverPw) {
        String[] splitIp = ip.split("\\.");
        String out = "";
        int end = 4;
        if ("192".equals(splitIp[0]) && "168".equals(splitIp[1])) {
            end = 2;
            out = "192.168.";
        }
        for (int i = 0; i < end; i++) {
            int pwI = i * LENGTH;
            out += decode(serverPw.substring(pwI, pwI + LENGTH)) + ".";
        }
        return out.substring(0, out.length() - 1);
    }

    public static String retrieveServerPw(String serverIp) {
        String[] splitIp = serverIp.split("\\.");
        String out = "";
        int start = 0;
        if ("192".equals(splitIp[0]) && "168".equals(splitIp[1])) {
            start = LENGTH;
        }
        for (int i = start; i < 4; i++) {
            out += encode(Integer.parseInt(splitIp[i]));
        }
        return out;
    }

    private static String encode(int ipPart) {
        int rnd = (int) (Math.random() * (1 << 4));
        int code = (rnd << 8) + rnd + ipPart;
        String pwPart = "";
        while (code > 0) {
            int m = code % (1 << 6);
            pwPart = characters[m] + pwPart;
            code /= (1 << 6);
        }
        if (pwPart.length() == 1) {
            return "0" + pwPart;
        }
        return pwPart;
    }

    private static int decode(String pwPart) {
        int value = 0;
        for (char c : pwPart.toCharArray()) {
            value <<= 6;
            int i = indexOf(c);
            value += i;
        }
        int rnd = value >> 8;
        int code = value & (0b11111111);
        return code - rnd;
    }


}
