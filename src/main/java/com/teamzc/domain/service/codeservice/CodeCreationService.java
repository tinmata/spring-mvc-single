package com.teamzc.domain.service.codeservice;

public interface CodeCreationService {

  /**
   * Base64でエンコードしたQRコードを作成します。
   *
   * @param contents
   * @return
   */
  String createQrCodeStream(String contents);

  /**
   * Base64でエンコードしたバーコードを作成します。
   *
   * @param contents
   * @return
   */
  String createBarcodeStream(String contents);
}
