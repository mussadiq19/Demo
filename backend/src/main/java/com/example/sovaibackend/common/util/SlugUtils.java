package com.example.sovaibackend.common.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtils {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s_]");
    private static final Pattern MULTIHYPHEN = Pattern.compile("-+");

    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String slug = input.toLowerCase(Locale.ENGLISH);
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = NONLATIN.matcher(slug).replaceAll("");
        slug = WHITESPACE.matcher(slug).replaceAll("-");
        slug = MULTIHYPHEN.matcher(slug).replaceAll("-");
        slug = slug.replaceAll("-$", "");
        slug = slug.replaceAll("^-", "");

        return slug;
    }
}

