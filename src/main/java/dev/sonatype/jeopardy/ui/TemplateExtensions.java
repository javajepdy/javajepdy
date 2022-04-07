package dev.sonatype.jeopardy.ui;

import dev.sonatype.jeopardy.model.MyTeam;
import io.quarkus.qute.TemplateExtension;

@TemplateExtension
public class TemplateExtensions {

    public static String md(MyTeam in) {
        return MarkdownRenderer.render(in.description);
    }

}
