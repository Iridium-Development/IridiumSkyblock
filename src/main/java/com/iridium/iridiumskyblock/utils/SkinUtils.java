package com.iridium.iridiumskyblock.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

public class SkinUtils {

    private static HashMap<UUID, String> cache = new HashMap<>();

    public static String getHeadData(UUID uuid) {
        return cache.get(uuid);
    }

    public static String queryHeadData(UUID uuid) {
        try {
            Gson g = new Gson();
            String signature = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());
            JsonObject obj = g.fromJson(signature, JsonObject.class);
            String value = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
            String decoded = new String(Base64.getDecoder().decode(value));
            obj = g.fromJson(decoded, JsonObject.class);
            String skinURL = obj.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            byte[] skinByte = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}").getBytes();
            String data = new String(Base64.getEncoder().encode(skinByte));
            cache.put(uuid, data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getURLContent(String urlStr) {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = null;
        try {
            URL url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String str;
            while ((str = in.readLine()) != null) sb.append(str);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
