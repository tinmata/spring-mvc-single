package com.teamzc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.teamzc.domain.form.EchoForm;

@Controller
@RequestMapping("echo")
public class EchoController {

  @ModelAttribute
  public EchoForm setUpEchoForm() {
    EchoForm form = new EchoForm();
    return form;
  }

  @RequestMapping
  public String index(Model model) {
    return "echo/index";
  }

  @RequestMapping(value = "hello", method = RequestMethod.POST)
  public String hello(EchoForm form, Model model) {
    model.addAttribute("name", form.getName());
    return "echo/hello";
  }
}
