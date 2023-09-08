package com.example.commonutil.tool;

import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wei.song
 * @since 2023/9/8 15:53
 */
public class LarkTool {

    private static final Logger log = LoggerFactory.getLogger(LarkTool.class);

    private static final Pattern PATTERN = Pattern.compile("(https?://[^#]*)#.*?(\\))");

    public static void main(String[] args) {
        traverseDirectory("/Users/anker/Downloads/markdown/", ".md");
    }

    public static void replaceTextInFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            content = content.replace("<br />", "\n\n");
            content = removeHashFromLinks(content);
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static String removeHashFromLinks(String text) {
        Matcher matcher = PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1) + matcher.group(2));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void traverseDirectory(String path, @Nonnull String fileExtension) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (ObjectUtil.isNull(files)) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                traverseDirectory(file.getAbsolutePath(), fileExtension);
            } else {
                if (file.getName().endsWith(fileExtension)) {
                    log.info("File: {}", file.getName());
                    replaceTextInFile(file.getAbsolutePath());
                }
            }
        }
    }

}
