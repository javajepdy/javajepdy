package dev.sonatype.jeopardy.model;

import java.util.Comparator;

public class CategoryEntryComparator implements Comparator<CategoryEntry>  {
    @Override
    public int compare(CategoryEntry o1, CategoryEntry o2) {

        if(o1==null && o2==null) return 0;
        if(o1==null) return 1;
        if(o2==null) return -1;

        int vcomp=Integer.compare(o1.value,o2.value);
        if(vcomp!=0) return vcomp;
        return o1.clue.compareTo(o2.clue);

    }
}
