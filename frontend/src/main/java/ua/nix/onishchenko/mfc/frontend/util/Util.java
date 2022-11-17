package ua.nix.onishchenko.mfc.frontend.util;

import lombok.extern.apachecommons.CommonsLog;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@CommonsLog
public final class Util {

    public static boolean matchRegex(String str) {
        return str.matches("[a-zA-z0-9_. ]+");
    }

    public static String convertISOToString(String iso) {
        try {
            iso = iso.replace("Z", "").replace("Z", "");
            return convertLocalDateTimeToString(LocalDateTime.parse(iso, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } catch (DateTimeParseException timeParseException) {
            log.warn(timeParseException.getMessage());
            return iso;
        }
    }

    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(dateTimeFormatter);
    }

    public static Cookie getAccessTokenCookie(Cookie[] cookies) {
        Cookie accessTokenCookie = null;
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), "access_token")) {
                accessTokenCookie = cookie;
            }
        }
        return accessTokenCookie;
    }

    public static Cookie getRefreshTokenCookie(Cookie[] cookies) {
        Cookie refreshTokenCookie = null;
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), "refresh_token")) {
                refreshTokenCookie = cookie;
            }
        }
        return refreshTokenCookie;
    }

}
