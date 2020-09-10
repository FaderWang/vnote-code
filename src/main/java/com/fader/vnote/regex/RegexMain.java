package com.fader.vnote.regex;

import org.springframework.util.AntPathMatcher;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author FaderW
 * 2019/10/14
 */

public class RegexMain {



    public static void metaRegex() {
        String string = "The fat cat sat on the mat";
        Pattern pattern = Pattern.compile(".at");
        Matcher matcher = pattern.matcher(string);
        System.out.println(matcher.replaceFirst("dog"));

        while (matcher.find()) {
            System.out.println(matcher.group());
        }


        //匹配非f的
        pattern = Pattern.compile("[^f]at");
        matcher = pattern.matcher(string);
        while (matcher.find()) {
            System.out.print(matcher.group() + " ");
        }
        // cat sat mat

    }

    public static void groupRegex() {
        String string = "IOPS=2932, bw=329,and other number avg=399, savg=23029";
        // \b代表单词边界
        String regex = "^IOPS=(\\d+)\\b.*\\bavg=(\\d+)\\b.*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);

        if (matcher.find() && matcher.groupCount() == 2) {
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }

    }

    public static void replaceRegex() {
        String string = "I am a dog, a hot dog";
        System.out.println(string.replace("dog", "cat"));
        System.out.println(string.replaceFirst("dog", "cat"));
        System.out.println(string.replaceAll("dog", "cat"));
        /*
            I am a cat, a hot cat
            I am a cat, a hot dog
            I am a cat, a hot cat
         */

        String s = "18852951219";
        Pattern pattern = Pattern.compile("(\\d{3})\\d{4}(\\d{4})");
        Matcher matcher = pattern.matcher(s);
        // 使用find从字符串中找出下一个匹配的串
        if (matcher.find() && matcher.groupCount() == 2) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.replaceAll("$1****$2"));

        }
        System.out.println(matcher.replaceAll("{sss}"));

//        System.out.println(s.replaceFirst("(\\d{3})\\d{4}(\\d{3})", "$1****$2"));
    }

    public static void advanceReplace() {
        String s = "name4age3sex3";
        Pattern pattern = Pattern.compile("([a-z]+)(\\d)");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find() && matcher.groupCount() == 2) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }

        System.out.println(matcher.replaceAll("$1=$2"));
        // name=4age=3sex=3
    }

    public static void AntMatcher() {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        Map<String, String> map = antPathMatcher.extractUriTemplateVariables("{uid}", "123");
//        System.out.println(antPathMatcher.extractPathWithinPattern("/path/{uid}", "/path/123"));
        System.out.println(map);

//        Pattern pattern = Pattern.compile("\\?|\\*|\\{((?:[^/{}])+?)\\}");
//        String s = "/{name:[a-z-]+}-{version:\\d\\.\\d\\.\\d}{ext:\\.[a-z]+}";
//        Matcher matcher = pattern.matcher(s);
//        while (matcher.find()) {
//            System.out.println(matcher.groupCount());
//            for (int i = 0; i <= matcher.groupCount(); i++) {
//                System.out.println(matcher.group(i));
//            }
//        }


    }

    public static void main(String[] args) {

//        Pattern pattern = Pattern.compile("((\\d)+?)");
//        String s = "name:123";
//        Matcher matcher = pattern.matcher(s);
//        while (matcher.find()) {
//            System.out.println(matcher.groupCount());
//            System.out.println(matcher.group(0));
//            System.out.println(matcher.group(1));
////            System.out.println(matcher.group(1));
//        }
//        metaRegex();
//        groupRegex();
//        replaceRegex();
//        advanceReplace();
        AntMatcher();
//        Pattern pattern = Pattern.compile("/(([^:/]*):([^/]+))|(\\.\\*)");
//        String path = "api_test/:size";
//        Matcher matcher = pattern.matcher(path);
//        if (matcher.find()) {
//            System.out.println(matcher.groupCount());
//            System.out.println(matcher.group(0));
//            System.out.println(matcher.group(1));
//            System.out.println(matcher.group(2));
//            System.out.println(matcher.group(3));
//            System.out.println(matcher.group(4));
//        }
    }
}
