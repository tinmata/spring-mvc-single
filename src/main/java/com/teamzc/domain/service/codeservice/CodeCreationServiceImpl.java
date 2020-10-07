package com.teamzc.domain.service.codeservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.ITFWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * コード作成サービスです。
 *
 * <pre>
 *  ZXing（ゼブラクロッシング）ライブラリで作成できる１次元・２次元画像は下記の通りです。
 *  [1D product]
 *  UPC-A: アメリカ、カナダで使用されている12桁の統一商品コードです。
 *  UPC-E: アメリカ、カナダで使用されている8桁の統一商品コードです。
 *  EAN-8: EAN-8 バーコードは、EAN-13 バーコードの短縮版で、
 *         ヨーロッパおよびその他の国で標準的な小売り製品の識別子として使われてます。
 *  EAN-13: 日本で最も普及している商品識別コードであり、日本ではJANコードと呼称されています。
 *          EAN(JAN)コードから生成されたバーコードシンボルは市販される多くの商品に印刷または貼付されており、
 *          POSシステムや在庫管理システムなどで価格や商品名を検索するためのキーとして使われています。
 *  [1D industrial]
 *  Code39: 産業用途。
 *          数字と文字の両方を使用したコードで、一度に最大43のアルファベット文字をエンコードできます。
 *          一般的には、軍隊や自動車産業で使われています。
 *  Code93: 産業用途。
 *          Code39をさらにコンパクトにしたコードです。
 *  Code128: 産業用途。
 *           アスキーコード128文字（数字、アルファベット大文字/小文字、記号、制御コード）全てをバーコード化することができます。
 *  Codabar: 産業用途。
 *           コードには自己チェック機能が付いているため、コード入力時のエラーを排除できます。
 *  ITF: 産業用途。
 *       ITF（Interleaved Two of Five）コードは、日本における物流統一シンボルで、
 *       ヨーロッパの物流統一シンボルEAN-DUNや米国の物流統一シンボルUCC-SCSと互換性を持って作成されたものです。
 *       標準は14桁の ITF-14 というもので、16桁に拡張したバージョンの ITF-16 というもの等があります。
 *  [2D]
 *  QR Code:
 *  Data Matrix:
 *  Aztec:
 *  PDF 417:
 *  MaxiCode:
 *  RSS-14:
 *  SS-Expanded:
 * </pre>
 *
 * @author YuChen
 */
@Service
@Transactional
public class CodeCreationServiceImpl implements CodeCreationService {

  private static final Logger logger = LoggerFactory.getLogger(CodeCreationService.class);

  private static final String IMG_PNG = "png";

  private static final String ENCODE_UTF = "UTF-8";

  @Override
  public String createQrCodeStream(String contents) {
    logger.debug("QRコード作成サービスを実行します。");

    // 生成するコードの高さ
    int height = 100;
    // 生成するコードの幅
    int width = 100;
    String encodeStr = "";

    // コード作成処理
    ConcurrentHashMap<EncodeHintType, Object> hints =
        new ConcurrentHashMap<EncodeHintType, Object>();
    // QRコード特有のオプション指定
    // 誤り訂正レベル指定
    // レベル「L」：約7%、あまり汚れない環境
    // レベル「M」：約15%、一般的な環境
    // レベル「Q」：約25%、工場など汚れやすい環境
    // レベル「H」：約30%、工場など汚れやすい環境
    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
    // エンコーディング指定
    hints.put(EncodeHintType.CHARACTER_SET, ENCODE_UTF);
    // マージン指定
    hints.put(EncodeHintType.MARGIN, 0);
    //    hints.put(EncodeHintType.QR_VERSION, 40);

    byte[] res = createCodeToByteArray(contents, BarcodeFormat.QR_CODE, height, width, hints);
    if (null != res) {
      encodeStr = Base64.encodeBase64String(res);
    }

    return encodeStr;
  }

  /**
   *
   *
   * <pre>
   *  標準でITF-14 のバーコードを出力しましたが、同様に ITF-16 のバーコードを出力することも可能です。
   *  指定する contents を16文字で指定すればOKです。
   *  尚、ITFWriterそのもののソースコードを覗いてみると80桁までであれば生成が可能のようです。
   *  但し2の倍数の桁数である必要があります。
   * </pre>
   */
  @Override
  public String createBarcodeStream(String contents) {
    logger.debug("バーコード作成サービスを実行します。");

    // 生成するコードの高さ
    int height = 200;
    // 生成するコードの幅
    int width = 50;
    String encodeStr = "";

    byte[] res = createCodeToByteArray(contents, BarcodeFormat.ITF, height, width, null);
    if (null != res) {
      encodeStr = Base64.encodeBase64String(res);
    }

    return encodeStr;
  }

  /**
   * コードを作成して画像ストリームに変換するメソッドです。
   *
   * @param contents
   * @param format
   * @param height
   * @param width
   * @param hints
   * @return
   */
  private byte[] createCodeToByteArray(
      String contents,
      BarcodeFormat format,
      int height,
      int width,
      ConcurrentHashMap<EncodeHintType, Object> hints) {

    // 画像ストリーム初期化
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    try {
      if (format.equals(BarcodeFormat.QR_CODE)) {
        logger.debug("QRコードを作成します。");
        QRCodeWriter writer = new QRCodeWriter();
        // 生成するコードフォーマットをQRコードに指定
        BitMatrix bitMatrix = writer.encode(contents, format, height, width, hints);
        MatrixToImageWriter.writeToStream(bitMatrix, IMG_PNG, output);
      } else if (format.equals(BarcodeFormat.ITF)) {
        logger.debug("バーコードを作成します。");
        ITFWriter writer = new ITFWriter();
        // 生成するコードフォーマットをバーコードに指定
        BitMatrix bitMatrix = writer.encode(contents, format, height, width);
        MatrixToImageWriter.writeToStream(bitMatrix, IMG_PNG, output);
      }
    } catch (WriterException e) {
      logger.error("コード作成でエラーが発生しました。", e.getMessage(), e.getStackTrace());
    } catch (IOException e) {
      logger.error("コード画像出力でエラーが発生しました。", e.getMessage(), e.getStackTrace());
    }

    return output.toByteArray();
  }
}
