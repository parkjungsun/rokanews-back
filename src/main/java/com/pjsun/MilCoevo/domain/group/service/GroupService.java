package com.pjsun.MilCoevo.domain.group.service;

import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.dto.GroupInfoResponseDto;
import com.pjsun.MilCoevo.domain.group.dto.SearchGroupMemberDto;
import com.pjsun.MilCoevo.domain.member.dto.MemberGroupDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService {

    Long createGroup(String groupName, String position, String nickname);

    GroupInfoResponseDto getGroupInfo(Long groupId);

    Group getGroupByInviteCode(String inviteCode);

    Long registerGroup(String inviteCode, String position, String nickname);

    String updateGroupName(Long groupId, String groupName);

    String updateInviteCode(Long groupId);

    Page<MemberGroupDto> getMembers(Long groupId,
                                    SearchGroupMemberDto searchCondition,
                                    Pageable pageable);
}
