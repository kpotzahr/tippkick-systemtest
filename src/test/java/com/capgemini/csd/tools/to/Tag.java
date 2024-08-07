package com.capgemini.csd.tools.to;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    public static final Tag UI_TAG = new Tag("@ui");
    public static final Tag SKIP_TAG = new Tag("@skip");

    private String name;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tag) {
            return name.equalsIgnoreCase(((Tag) obj).name);
        } else {
            return false;
        }
    }
}
