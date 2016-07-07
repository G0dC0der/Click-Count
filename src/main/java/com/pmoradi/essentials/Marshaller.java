package com.pmoradi.essentials;

import com.pmoradi.entities.Namespace;
import com.pmoradi.entities.URL;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.UrlEntry;

public class Marshaller {

    public static UrlEntry marshall(URL url) {
        UrlEntry urlEntry = new UrlEntry();
        urlEntry.setUrlName(url.getAlias());
        urlEntry.setLink(url.getLink());
        urlEntry.setAddDate(url.getAdded());
        urlEntry.setClicks(url.getClicks());

        return urlEntry;
    }

    public static GroupEntry marshall(Namespace namespace) {
        GroupEntry groupEntry = new GroupEntry();
        groupEntry.setGroupName(namespace.getName());
        groupEntry.setUrls(
                namespace.getUrls()
                .stream()
                .map(Marshaller::marshall)
                .sorted((e1, e2) -> Long.compare(e1.getAddDate(), e2.getAddDate()))
                .toArray(UrlEntry[]::new));

        return groupEntry;
    }
}
