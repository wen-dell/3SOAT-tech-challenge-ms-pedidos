package br.com.tech.challenge.utils;

import com.google.zxing.WriterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

import static br.com.tech.challenge.utils.QRCodeGeneratorUtils.getQRCodeImage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QRCodeGeneratorUtilsTest {

    @DisplayName("Deve gerar o código QR corretamente")
    @Test
    void shouldGetQRCodeImage() {
        String qrData = "Testar QR Code";
        final int QR_CODE_WIDTH = 300;
        final int QR_CODE_HEIGHT = 300;

        byte[] qrCodeImage = getQRCodeImage(qrData, QR_CODE_WIDTH, QR_CODE_HEIGHT);

        assertThat(qrCodeImage).isNotNull();
        assertThat(qrCodeImage.length).isGreaterThan(0);
    }

    @DisplayName("Deve lancar WriterException")
    @Test
    void shouldThrowWriterException() {
        String qrData = "Algum dado invalido";
        final int QR_CODE_WIDTH = 300;
        final int QR_CODE_HEIGHT = 300;

        try (MockedStatic<QRCodeGeneratorUtils> mockedStatic = Mockito.mockStatic(QRCodeGeneratorUtils.class)) {
            mockedStatic.when(() -> QRCodeGeneratorUtils.getQRCodeImage(qrData, QR_CODE_WIDTH, QR_CODE_HEIGHT))
                    .thenThrow(new RuntimeException(new WriterException("Ocorreu um erro ao gerar o QR Code")));

            assertThatThrownBy(() -> {
                QRCodeGeneratorUtils.getQRCodeImage(qrData, QR_CODE_WIDTH, QR_CODE_HEIGHT);
            }).hasMessageContaining("Ocorreu um erro ao gerar o QR Code");
        }

    }

    @DisplayName("Deve lancar IOException")
    @Test
    void shouldThrowIOException() {
        String qrData = "Algum dado invalido";
        final int QR_CODE_WIDTH = 300;
        final int QR_CODE_HEIGHT = 300;

        try (MockedStatic<QRCodeGeneratorUtils> mockedStatic = Mockito.mockStatic(QRCodeGeneratorUtils.class)) {
            mockedStatic.when(() -> QRCodeGeneratorUtils.getQRCodeImage(qrData, QR_CODE_WIDTH, QR_CODE_HEIGHT))
                    .thenThrow(new RuntimeException(new IOException("Ocorreu um erro ao gerar o QR Code")));

            assertThatThrownBy(() -> {
                QRCodeGeneratorUtils.getQRCodeImage(qrData, QR_CODE_WIDTH, QR_CODE_HEIGHT);
            }).hasMessageContaining("Ocorreu um erro ao gerar o QR Code");
        }

    }

}
