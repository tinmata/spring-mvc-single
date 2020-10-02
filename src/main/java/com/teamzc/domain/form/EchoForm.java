package com.teamzc.domain.form;

import java.io.Serializable;

public class EchoForm implements Serializable {

  private static final long serialVersionUID = -4247403134023646189L;

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
