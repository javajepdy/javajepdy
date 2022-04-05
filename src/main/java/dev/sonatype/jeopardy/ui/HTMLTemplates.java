package dev.sonatype.jeopardy.ui;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton

public class HTMLTemplates {

    @Location("pages/main.html")
    Template main;

    @Location("pages/added.html")
    Template added;

    @Location("pages/find.html")
    Template find;

    @Location("pages/view.html")
    Template view;


    @Location("pages/new_game.html")
    Template newgame;


    @Location("pages/host.html")
    Template host;


    @Location("includes/gamestate.html")
    Template gamestate;

    @Location("includes/clue_modal.html")
    Template clue_modal;

    @Location("includes/host_grid.html")
    Template host_grid;


    @Location("pages/new_team.html")
    Template new_team;


    @Location("includes/team_added.html")
    Template team_added;

    @Location("includes/team_form.html")
    Template team_form;
}
