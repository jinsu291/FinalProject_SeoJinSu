package com.example.finalproject.finalproject.app.member.service;

import com.example.finalproject.finalproject.app.base.dto.RsData;
import com.example.finalproject.finalproject.app.cash.entity.CashLog;
import com.example.finalproject.finalproject.app.cash.service.CashService;
import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.member.entity.emum.AuthLevel;
import com.example.finalproject.finalproject.app.member.exception.AlreadyJoinException;
import com.example.finalproject.finalproject.app.member.repository.MemberRepository;
import com.example.finalproject.finalproject.app.security.dto.MemberContext;
import com.example.finalproject.finalproject.util.Ut;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CashService cashService;

    @Transactional
    public Member join(String username, String password, String email, String nickname){
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new AlreadyJoinException();
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .authLevel(AuthLevel.NORMAL)
                .build();

        memberRepository.save(member);

        return member;
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public void modifyProfile(Member member,String email, String nickname) {
        member.setEmail(email);
        member.setNickname(nickname);
        memberRepository.save(member);
    }
    @Transactional(readOnly = true)
    public Optional<Member> findByUserId(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member;
    }
    @Transactional
    public RsData<AddCashRsDataBody> addCash(Member member, long price, String eventType) {
        CashLog cashLog = cashService.addCash(member, price, eventType);

        long newRestCash = member.getRestCash() + cashLog.getPrice();
        member.setRestCash(newRestCash);
        memberRepository.save(member);

        return RsData.of(
                "S-1",
                "성공",
                new AddCashRsDataBody(cashLog, newRestCash)
        );
    }

    @Transactional
    public RsData modifyPassword(Member member, String password, String oldPassword) {
        Optional<Member> opMember = memberRepository.findById(member.getId());

        if (passwordEncoder.matches(oldPassword, opMember.get().getPassword()) == false) {
            return RsData.of("F-1", "기존 비밀번호가 일치하지 않습니다.");
        }

        opMember.get().setPassword(passwordEncoder.encode(password));

        return RsData.of("S-1", "비밀번호가 변경되었습니다.");
    }

    @Transactional
    public RsData beAuthor(Member member, String nickname) {
        Optional<Member> opMember = memberRepository.findByNickname(nickname);

        if (opMember.isPresent()) {
            return RsData.of("F-1", "해당 필명은 이미 사용중입니다.");
        }

        opMember = memberRepository.findById(member.getId());

        opMember.get().setNickname(nickname);
        forceAuthentication(opMember.get());

        return RsData.of("S-1", "해당 필명으로 활동을 시작합니다.");
    }

    private void forceAuthentication(Member member) {
        MemberContext memberContext = new MemberContext(member, member.genAuthorities());

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        memberContext,
                        member.getPassword(),
                        memberContext.getAuthorities()
                );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Data
    @AllArgsConstructor
    public static class AddCashRsDataBody {
        CashLog cashLog;
        long newRestCash;
    }


    public long getRestCash(Member member) {
        Member foundMember = findByUsername(member.getUsername()).get();

        return foundMember.getRestCash();
    }

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }
}