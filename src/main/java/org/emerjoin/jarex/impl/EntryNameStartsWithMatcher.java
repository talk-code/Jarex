package org.emerjoin.jarex.impl;

import org.emerjoin.jarex.MatchContext;
import org.emerjoin.jarex.Matcher;
import org.emerjoin.jarex.query.EntryNameEndsWithQuery;
import org.emerjoin.jarex.query.EntryNameStartsWithQuery;
import org.emerjoin.jarex.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author Mário Júnior
 */
public class EntryNameStartsWithMatcher implements Matcher {

    private static Logger _log = LoggerFactory.getLogger(EntryNameStartsWithMatcher.class);

    public boolean supports(Query query) {

        return query instanceof EntryNameStartsWithQuery;

    }

    @Override
    public boolean doMatch(MatchContext context, Query query, URL url) {

        EntryNameStartsWithQuery q = (EntryNameStartsWithQuery) query;
        JarFile jarFile = context.getJar(url);
        String part = q.getPart();

        List<JarEntry> entryList = jarFile.stream().filter(entry -> entry.getName().startsWith(part))
                .collect(Collectors.toList());

        entryList.forEach(entry -> {
            if (_log.isDebugEnabled())
                _log.debug(String.format("Entry name [%s] starts with [%s]", entry.getName(), part));
            context.publishMatch(q, context.createMatch(url, entry));
        });


        return entryList.size()>0;

    }

}
