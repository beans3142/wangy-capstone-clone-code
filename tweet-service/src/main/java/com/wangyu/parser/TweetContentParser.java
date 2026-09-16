package com.wangyu.parser;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 본문에서 해시태그(#)와 멘션(@)을 추출한다. 같은 태그가 여러 번 등장해도 중복 이벤트를 발행하지
 * 않도록 등장 순서를 유지하는 Set으로 중복을 제거한다(순수 파싱 로직이라 상태 없는 정적 유틸로 둔다).
 */
public final class TweetContentParser {

    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#([\\w가-힣]+)");
    private static final Pattern MENTION_PATTERN = Pattern.compile("@([\\w가-힣]+)");

    private TweetContentParser() {
    }

    public static List<String> extractHashtags(String content) {
        return extract(HASHTAG_PATTERN, content);
    }

    public static List<String> extractMentions(String content) {
        return extract(MENTION_PATTERN, content);
    }

    private static List<String> extract(Pattern pattern, String content) {
        if (content == null || content.isBlank()) {
            return List.of();
        }

        Set<String> matches = new LinkedHashSet<>();
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            matches.add(matcher.group(1));
        }
        return List.copyOf(matches);
    }
}
