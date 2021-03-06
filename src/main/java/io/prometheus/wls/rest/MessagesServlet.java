// Copyright 2019, Oracle Corporation and/or its affiliates.  All rights reserved.
// Licensed under the Universal Permissive License v 1.0 as shown at
// http://oss.oracle.com/licenses/upl.

package io.prometheus.wls.rest;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static io.prometheus.wls.rest.ServletConstants.MESSAGES_PAGE;

/**
 * A collector of REST requests and replies, that can be viewed to diagnose problems.List<String
 */
@WebServlet("/" + MESSAGES_PAGE)
public class MessagesServlet extends HttpServlet {
    static final int MAX_EXCHANGES = 5;
    private static final String TEMPLATE = "REQUEST:%n%s%nREPLY:%n%s%n";

    private static Queue<String> messages = new ArrayDeque<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try(ServletOutputStream out = resp.getOutputStream()) {
            for (String message : messages)
                out.println(message);
        }
    }

    static void clear() {
        messages.clear();
    }

    static void addExchange(String request, String response) {
        messages.add(String.format(TEMPLATE, request, response));
        if (messages.size() > MAX_EXCHANGES) messages.remove();
    }

    static List<String> getMessages() {
        return new ArrayList<>(messages);
    }
}
