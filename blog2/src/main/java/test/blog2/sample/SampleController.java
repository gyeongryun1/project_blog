package test.blog2.sample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

    @GetMapping("/info")
    public String getTest() {
        return "/info";
    }
}
