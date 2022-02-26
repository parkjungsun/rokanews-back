package com.pjsun.MilCoevo.test.builder;

import com.pjsun.MilCoevo.domain.group.Group;

import java.util.ArrayList;

public class GroupBuilder {

    public static Group build(Long id) {
        return new Group(id, "inviteCode", "groupName",
                true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
}
