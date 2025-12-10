package com.zs.common.core.utils;

import jakarta.validation.constraints.NotNull;
import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * @author zsadmin
 */
public class StrUtils {


    public static boolean isMatch(@NotNull List<String> patterns, @NotNull String path) {
        AntPathMatcher antMatcher = new AntPathMatcher();
        return patterns.stream().anyMatch(s -> antMatcher.match(s, path));
    }


}
