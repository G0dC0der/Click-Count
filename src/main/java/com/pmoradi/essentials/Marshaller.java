package com.pmoradi.essentials;

import com.pmoradi.entities.Click;
import com.pmoradi.entities.Group;
import com.pmoradi.entities.URL;
import com.pmoradi.rest.entries.GroupEntry;
import com.pmoradi.rest.entries.UrlEntry;

import java.util.List;

public class Marshaller {

    public static UrlEntry marshall(URL url) {
        UrlEntry urlEntry = new UrlEntry();
        urlEntry.setUrlName(url.getUrl());
        urlEntry.setLink(url.getLink());
        urlEntry.setAddDate(url.getAddDate().getTime());

        List<Click> urlClicks = url.getClicks();
        long[] clicks = new long[urlClicks.size()];

        for(int i = 0; i < clicks.length; i++) {
            clicks[i] = urlClicks.get(i).getTime().getTime();
        }
        urlEntry.setClicks(clicks);

        return urlEntry;
    }

    public static GroupEntry marshall(Group group) {
        GroupEntry groupEntry = new GroupEntry();
        groupEntry.setGroupName(group.getGroupName());

        List<URL> urls = group.getUrls();
        UrlEntry[] urlEntries = new UrlEntry[urls.size()];

        for(int i = 0; i < urlEntries.length; i++) {
            urlEntries[i] = marshall(urls.get(i));
        }

        groupEntry.setUrls(urlEntries);

        return groupEntry;
    }
}
