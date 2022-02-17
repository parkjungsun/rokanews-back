package com.pjsun.MilCoevo.interceptor;

import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
import com.pjsun.MilCoevo.domain.user.User;
import com.pjsun.MilCoevo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class CheckMemberInterceptor implements HandlerInterceptor {

    @Autowired
    private MemberService memberService;

    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {

        // TODO SuppressWarnings
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if(pathVariables == null) {
            throw new RuntimeException();
        }

        Long groupId = Long.valueOf(pathVariables.get("groupId"));

        Member member = memberService.getMemberByUserAndGroup(groupId);

        return true;
    }
}
