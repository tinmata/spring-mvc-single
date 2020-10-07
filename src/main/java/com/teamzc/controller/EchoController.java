package com.teamzc.controller;

import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.teamzc.domain.form.EchoForm;
import com.teamzc.domain.service.codeservice.CodeCreationService;

@Controller
@RequestMapping("echo")
public class EchoController {

  @Inject private CodeCreationService codeCreationService;

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
  public String hello(@Validated EchoForm form, BindingResult result, Model model) {

    if (result.hasErrors()) {
      return "echo/index";
    }

    String qrCode = codeCreationService.createQrCodeStream(form.getUrl());

    String barcode = codeCreationService.createBarcodeStream(form.getCode());

    model.addAttribute("url", form.getUrl());
    model.addAttribute("qrCode", qrCode);
    model.addAttribute("barcode", barcode);

    return "echo/hello";
  }
}
