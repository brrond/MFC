package ua.nix.onishchenko.mfc.api;

import java.util.HashMap;
import java.util.Map;

public final class Util {

    public static Map<String, Object> error(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", msg);
        return map;
    }

}
