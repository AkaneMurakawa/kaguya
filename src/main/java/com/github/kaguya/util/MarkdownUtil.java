package com.github.kaguya.util;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

/**
 * Markdown转换工具
 */
public class MarkdownUtil {

    public static String markdownToHtml(String markdown){
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        // You can re-use parser and renderer instances
        Node document = parser.parse(markdown);
        // "<p>This is <em>Sparta</em></p>\n"
        return renderer.render(document);
    }

    public static String htmlToText(String html) {
        // 过滤html标签，转换为纯文本格式
        String regexHtml = "<[^>]+>";
        return html.replaceAll(regexHtml, "");
    }
}
