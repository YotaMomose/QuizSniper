package com.tonkatsuudon.quizsniper.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class QuizSniperController {
    @GetMapping("/")
    public ModelAndView showMainView(ModelAndView mv) {
        mv.setViewName("index");
        return mv;
    }
    
}
