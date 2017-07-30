package org.emerjoin.jarex.impl;

import org.emerjoin.jarex.MatchContext;
import org.emerjoin.jarex.Matcher;
import org.emerjoin.jarex.query.FileEntryNameEndsWithQuery;
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
public class FileEntryNameEndsWithMatcher implements Matcher {

    private static Logger _log = LoggerFactory.getLogger(FileEntryNameEndsWithMatcher.class);

    public boolean supports(Query query) {

        return query instanceof FileEntryNameEndsWithQuery;

    }

    @Override
    public boolean doMatch(MatchContext context, Query query, URL url) {

        FileEntryNameEndsWithQuery q = (FileEntryNameEndsWithQuery) query;
        JarFile jarFile = context.getJar(url);
        String part = q.getPart();

        List<JarEntry> entryList = jarFile.stream().filter(entry -> entry.getName().endsWith(part) && !entry.isDirectory())
                .collect(Collectors.toList());

        entryList.forEach(entry -> {
            if (_log.isDebugEnabled())
                _log.debug(String.format("Hit: File Entry name [%s] ends with [%s]", entry.getName(), part));
            context.publishMatch(q, context.createMatch(url, entry));
        });


        return entryList.size()>0;

    }

}