package com.example.cube.Controller;

import com.example.cube.Model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {


        @GetMapping("/signup")
        public String showSignup(Model model) {

            model.addAttribute("newUser", new User());
            model.addAttribute("error", "error");

            return "signup";
        }

        @PostMapping("/signup")
        public String postSignup(@ModelAttribute User newUser) {


            return "redirect:signup";

        }

        @GetMapping("/")
        public String showLogin(Model model) {

            model.addAttribute("logUser", new User());
            model.addAttribute("error", "error");

            return "login";
        }

        @PostMapping("/login")
        public String postLogin(@ModelAttribute User logUser, Model model) {

            model.addAttribute("error", "error");

            return "redirect:home";

        }
}
