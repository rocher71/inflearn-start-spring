package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    //localhost:8080 접속시 home() 실행 -> home.html 호출
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
