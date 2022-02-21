package com.pjsun.MilCoevo.domain.schedule.service;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.Rank;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
import com.pjsun.MilCoevo.domain.member.service.MemberServiceImpl;
import com.pjsun.MilCoevo.domain.schedule.Schedule;
import com.pjsun.MilCoevo.domain.schedule.WorkScope;
import com.pjsun.MilCoevo.domain.schedule.dto.ScheduleResponseDto;
import com.pjsun.MilCoevo.domain.schedule.dto.SearchScheduleDto;
import com.pjsun.MilCoevo.domain.schedule.repository.ScheduleRepository;
import com.pjsun.MilCoevo.exception.NoExistGroupException;
import com.pjsun.MilCoevo.exception.NoPermissionException;
import com.pjsun.MilCoevo.exception.NoScheduleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleServiceImpl implements ScheduleService {

    private final MemberService memberService;
    private final GroupRepository groupRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Long addSchedule(Long groupId, String title, String content,
                            WorkScope workScope, LocalDateTime workDate) {

        Member member = memberService.getMemberByUserAndGroup(groupId);
        Group group = groupRepository.findById(groupId).orElseThrow(NoExistGroupException::new);

        Schedule schedule = Schedule.createScheduleBuilder()
                .title(title).content(content)
                .workScope(workScope).workDate(workDate)
                .drafter(member.sign()).group(group)
                .build();

        scheduleRepository.save(schedule);

        return schedule.getId();
    }

    public ScheduleResponseDto getSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(NoScheduleException::new);

        return new ScheduleResponseDto(schedule);
    }

    public Page<ScheduleResponseDto> getSchedules(
            Long groupId, SearchScheduleDto searchCondition, Pageable pageable) {

        return scheduleRepository.searchSchedules(groupId, searchCondition, pageable);
    }

    @Transactional
    public void updateSchedule(Long groupId, Long scheduleId, ProcessStatus processStatus) {
        Member member = memberService.getMemberByUserAndGroup(groupId);

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(NoScheduleException::new);

        if(havePermission(member, schedule)) {
            schedule.decide(member.sign(), processStatus);
        } else {
            throw new NoPermissionException();
        }
    }

    private boolean havePermission(Member member, Schedule schedule) {
        if(member.getRank().equals(Rank.LEADER)) {
            return true;
        }
        return schedule.getDrafter().getEmail().equals(member.getInfo().getEmail());
    }
}
