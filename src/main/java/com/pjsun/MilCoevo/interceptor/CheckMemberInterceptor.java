package com.pjsun.MilCoevo.interceptor;

import com.pjsun.MilCoevo.domain.member.Member;
import com.pjsun.MilCoevo.domain.member.service.MemberService;
import com.pjsun.MilCoevo.domain.member.service.MemberServiceImpl;
import com.pjsun.MilCoevo.exception.IllegalPathException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

@Slf4j
public class CheckMemberInterceptor implements HandlerInterceptor {

    @Autowired
    private MemberService memberService;

    // TODO SuppressWarnings
    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {


        Object pathVariables = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if(!(pathVariables instanceof LinkedHashMap)) {
            throw new IllegalPathException();
        }

        LinkedHashMap<String, Long> pathVariable = (LinkedHashMap<String, Long>) pathVariables;

        Long groupId = pathVariable.get("groupId");

        Member member = memberService.getMemberByUserAndGroup(groupId);

        return true;
    }
}
