package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {

    private final MemberService memberService;

    //spring container한테 등록하고 사용, 매번 new memberService 선언하지 않도록. 
    @Autowired  // -> spring container가 뜰 때 MemberController를 생성, 이 생성자를 호출.
    // @Autowired가 있으면 parameter의 memberService를 Spring이 SpringContainer에 있는 memberService를 가져다가 연결시켜줌.
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
