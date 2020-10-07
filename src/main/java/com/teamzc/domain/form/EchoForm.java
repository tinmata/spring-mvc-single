package com.teamzc.domain.form;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EchoForm implements Serializable {

  private static final long serialVersionUID = -4247403134023646189L;

  @NotNull // (1)
  @Size(min = 1) // (2)
  private String url;

  @NotNull
  @Size(min = 1)
  private String code;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
