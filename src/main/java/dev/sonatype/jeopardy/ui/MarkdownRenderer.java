package dev.sonatype.jeopardy.ui;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class MarkdownRenderer {

    private static  HtmlRenderer renderer;
    private static Parser parser;

 static {
     MutableDataSet options = new MutableDataSet();
     parser = Parser.builder(options).build();
     renderer = HtmlRenderer.builder(options).build();
 }

    public static String render(String in) {

        Node n=parser.parse(in);
        return renderer.render(n);

    }
}
