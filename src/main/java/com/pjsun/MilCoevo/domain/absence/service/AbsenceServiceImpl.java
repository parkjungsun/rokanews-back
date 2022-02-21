package com.pjsun.MilCoevo.domain.absence.service;

import com.pjsun.MilCoevo.domain.ProcessStatus;
import com.pjsun.MilCoevo.domain.absence.Absence;
import com.pjsun.MilCoevo.domain.absence.Reason;
import com.pjsun.MilCoevo.domain.absence.dto.AbsenceResponseDto;
import com.pjsun.MilCoevo.domain.absence.dto.SearchAbsenceDto;
import com.pjsun.MilCoevo.domain.absence.repository.AbsenceRepository;
import com.pjsun.MilCoevo.domain.group.Group;
import com.pjsun.MilCoevo.domain.group.repository.GroupRepository;
import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.Rank;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
import com.pjsun.MilCoevo.domain.member.service.MemberServiceImpl;
import com.pjsun.MilCoevo.exception.NoAbsenceException;
import com.pjsun.MilCoevo.exception.NoExistGroupException;
import com.pjsun.MilCoevo.exception.NoPermissionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AbsenceServiceImpl implements AbsenceService {

    private final MemberService memberService;
    private final GroupRepository groupRepository;
    private final AbsenceRepository absenceRepository;

    @Transactional
    public Long addAbsence(Long groupId, String title, String content,
                           Reason reason, LocalDate startDate, LocalDate endDate) {

        Member member = memberService.getMemberByUserAndGroup(groupId);
        Group group = groupRepository.findById(groupId).orElseThrow(NoExistGroupException::new);

        Absence absence = Absence.createAbsenceBuilder()
                .title(title).content(content).reason(reason)
                .startDate(startDate).endDate(endDate)
                .drafter(member.sign()).group(group)
                .build();

        absenceRepository.save(absence);

        return absence.getId();
    }

    public AbsenceResponseDto getAbsence(Long absenceId) {
        Absence absence = absenceRepository.findById(absenceId)
                .orElseThrow(NoAbsenceException::new);

        return new AbsenceResponseDto(absence);
    }

    public Page<AbsenceResponseDto> getAbsences(
            Long groupId, SearchAbsenceDto searchCondition, Pageable pageable) {

        return absenceRepository.searchAbsences(groupId, searchCondition, pageable);
    }

    @Transactional
    public void updateAbsence(Long groupId, Long absenceId, ProcessStatus processStatus) {
        Member member = memberService.getMemberByUserAndGroup(groupId);

        Absence absence = absenceRepository.findById(absenceId)
                .orElseThrow(NoAbsenceException::new);

        if(havePermission(member, absence)) {
            absence.decide(member.sign(), processStatus);
        } else {
            throw new NoPermissionException();
        }
    }

    private boolean havePermission(Member member, Absence absence) {
        if(member.getRank().equals(Rank.LEADER)) {
            return true;
        }
        return absence.getDrafter().getEmail().equals(member.getInfo().getEmail());
    }
}
