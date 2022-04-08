package dev.sonatype.jeopardy.ui;

import dev.sonatype.jeopardy.model.Team;
import io.quarkus.qute.TemplateExtension;

@TemplateExtension
public class TemplateExtensions {

    public static String md(Team in) {
        return MarkdownRenderer.render(in.description);
    }

}
