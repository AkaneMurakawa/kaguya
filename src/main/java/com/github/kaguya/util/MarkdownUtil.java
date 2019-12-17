package com.github.kaguya.util;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class MarkdownUtil {

    public static String MarkdownToHtml(String markdown){
        MutableDataSet options = new MutableDataSet();

        // uncomment to set optional extensions
        //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // You can re-use parser and renderer instances
        Node document = parser.parse(markdown);
        // "<p>This is <em>Sparta</em></p>\n"
        String html = renderer.render(document);
        return html;
    }

    public static String HtmlToText(String html) {
        // 过滤html标签，转换为纯文本格式
        String regexHtml = "<[^>]+>";
        String text = "";
        text = html.replaceAll(regexHtml, "");
        return text;
    }
}
