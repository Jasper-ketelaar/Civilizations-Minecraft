package org.macroprod.civilization.jobs.instincts;

import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.macroprod.civilization.jobs.Task;
import org.macroprod.civilization.resident.Resident;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ChatInstinct extends Task {

    private static long time = 0;
    private static long random = 5000;

    private static final List<String> INSULTS = Arrays.asList("Go fuck yourself %s", "Die %s", "You're a fucking idiot %s",
            "I fucking hate you %s", "Shut the fuck up %s you sand nigger", "Hi %s", "Suck a dick you motherfucking cocksucker, %s",
            "Trimming armour for 50k %s", "Your mom's cavity is bigger than this cave I am digging, %s", "Please send %s a death message",
            "Why the fuck do you exist %s?");

    public ChatInstinct(Resident resident) {
        super(resident);
    }

    @Override
    public void run() {
        List<CraftPlayer> players = resident.getWorld().getServer().getOnlinePlayers();
        int rnd = resident.getRandom().nextInt(players.size());
        CraftPlayer player = players.get(rnd);
        resident.getWorld().getServer().broadcastMessage("<" + resident.getCustomName() + "> Yo " + player.getName() +
                "! " + getInsult());
        time = System.currentTimeMillis();
        random = 5000 + resident.getRandom().nextInt(10000 * 60);

    }

    @Override
    public boolean validate() {
        return resident.getRandom().nextFloat() > 0.995f && (System.currentTimeMillis() - time) > time + random;
    }

    public static String getInsult() {
        try {
            URL url = new URL("http://www.insultgenerator.org/");
            URLConnection connection = url.openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                String next = scanner.nextLine();
                if (next.contains("<br>")) {
                    return next.replaceAll("<br>", "").replaceAll("</div>", "").replaceAll("&nbsp", " ");
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
