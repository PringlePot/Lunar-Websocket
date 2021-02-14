package me.moose.websocket.server.server.nethandler.object;

import lombok.SneakyThrows;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.impl.packetids.WSPacketCosmeticGive;
import me.moose.websocket.server.server.objects.NameTagColor;

import javax.naming.Name;

public class ChangeLoglThread extends Thread {

    @SneakyThrows
    @Override
    public void run() {
        int i = 0;
        while(true) {

                String[] values = NameTagColor.run(i);
                if(Double.parseDouble(values[0]) > 0.170) {
                    System.out.println("YES");

                    System.out.println("R: " + values[0] + " G: " + values[1] + " B: " + values[2] + " Input: " + i);
                    Thread.sleep(1000);
                }
            i++;
        }
    }
}
